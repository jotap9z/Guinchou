begin;

-- Todo chamado novo começa em CREATED.
alter table public.tow_requests
    alter column status
    set default 'CREATED'::public.tow_request_status;


-- Corrige o aviso de search_path mantendo a função como invoker.
create or replace function public.set_updated_at()
returns trigger
language plpgsql
security invoker
set search_path = ''
as $function$
begin
    new.updated_at := pg_catalog.now();

    return new;
end;
$function$;


-- Mantém os mesmos parâmetros utilizados pelo aplicativo.
create or replace function public.create_customer_tow_request(
    p_vehicle_type text,
    p_vehicle_brand text,
    p_vehicle_model text,
    p_vehicle_year integer,
    p_vehicle_plate text,
    p_origin_address text,
    p_origin_latitude double precision,
    p_origin_longitude double precision,
    p_destination_address text,
    p_destination_latitude double precision,
    p_destination_longitude double precision,
    p_problem_type text,
    p_problem_detail text,
    p_problem_description text
)
returns uuid
language plpgsql
security invoker
set search_path = ''
as $function$
declare
    v_customer_id uuid := auth.uid();
    v_vehicle public.customer_vehicles%rowtype;
    v_request_id uuid;
    v_plate text := upper(btrim(p_vehicle_plate));
begin
    -- Autenticação e autorização.
    if v_customer_id is null
       or not coalesce(private.is_customer(), false) then
        raise exception
            'Faça login com uma conta de cliente ativa.'
            using errcode = '42501';
    end if;


    -- Dados obrigatórios do veículo.
    if nullif(btrim(p_vehicle_type), '') is null
       or nullif(btrim(p_vehicle_brand), '') is null
       or nullif(btrim(p_vehicle_model), '') is null
       or v_plate is null
       or v_plate !~ '^[A-Z0-9]{7}$' then
        raise exception
            'Preencha os dados do veículo e informe uma placa válida, sem espaços ou hífen.'
            using errcode = '22023';
    end if;


    -- Ano opcional.
    if p_vehicle_year is not null
       and (
           p_vehicle_year < 1886
           or p_vehicle_year > 2100
       ) then
        raise exception
            'Ano do veículo inválido.'
            using errcode = '22023';
    end if;


    -- Endereços e coordenadas.
    if nullif(btrim(p_origin_address), '') is null
       or nullif(btrim(p_destination_address), '') is null
       or p_origin_latitude is null
       or p_origin_longitude is null
       or p_destination_latitude is null
       or p_destination_longitude is null
       or not (p_origin_latitude between -90 and 90)
       or not (p_destination_latitude between -90 and 90)
       or not (p_origin_longitude between -180 and 180)
       or not (p_destination_longitude between -180 and 180) then
        raise exception
            'Informe origem e destino com coordenadas válidas.'
            using errcode = '22023';
    end if;


    -- Problema informado pelo cliente.
    if p_problem_type is null
       or p_problem_type not in ('MECHANICAL', 'ACCIDENT')
       or nullif(btrim(p_problem_detail), '') is null then
        raise exception
            'Informe o tipo e o detalhe do problema.'
            using errcode = '22023';
    end if;


    -- Busca apenas o veículo do cliente autenticado.
    -- O bloqueio mantém o cadastro estável durante esta operação.
    select cv.*
      into v_vehicle
      from public.customer_vehicles cv
     where cv.customer_id = v_customer_id
       and cv.plate = v_plate
     for update;


    -- Cadastra o veículo apenas quando ainda não existir.
    if not found then
        insert into public.customer_vehicles (
            customer_id,
            vehicle_type,
            brand,
            model,
            model_year,
            plate
        )
        values (
            v_customer_id,
            btrim(p_vehicle_type),
            btrim(p_vehicle_brand),
            btrim(p_vehicle_model),
            p_vehicle_year::smallint,
            v_plate
        )
        on conflict (plate) do nothing
        returning * into v_vehicle;


        if not found then
            -- Outra operação pode ter cadastrado a placa
            -- enquanto a primeira consulta estava em execução.
            select cv.*
              into v_vehicle
              from public.customer_vehicles cv
             where cv.customer_id = v_customer_id
               and cv.plate = v_plate
             for update;


            if not found then
                raise exception
                    'Não foi possível utilizar essa placa no seu cadastro. Confira a placa ou solicite suporte.'
                    using errcode = '42501';
            end if;
        end if;
    end if;


    -- Não reativa automaticamente um veículo desativado.
    if not v_vehicle.is_active then
        raise exception
            'Este veículo está inativo. Reative o cadastro antes de solicitar um guincho.'
            using errcode = '22023';
    end if;


    -- Reutiliza o cadastro sem sobrescrever os dados existentes.
    -- Comparações de texto ignoram diferenças de maiúsculas
    -- e espaços nas extremidades.
    if lower(btrim(v_vehicle.vehicle_type))
           is distinct from lower(btrim(p_vehicle_type))

       or lower(btrim(v_vehicle.brand))
           is distinct from lower(btrim(p_vehicle_brand))

       or lower(btrim(v_vehicle.model))
           is distinct from lower(btrim(p_vehicle_model))

       or (
           p_vehicle_year is not null
           and v_vehicle.model_year
               is distinct from p_vehicle_year
       ) then
        raise exception
            'Os dados informados não correspondem ao veículo cadastrado nessa placa. Confira o tipo, a marca, o modelo e o ano.'
            using errcode = '22023';
    end if;


    -- Correção da permissão:
    -- status não é inserido explicitamente.
    -- O banco aplica o padrão CREATED.
    insert into public.tow_requests (
        customer_id,
        customer_vehicle_id
    )
    values (
        v_customer_id,
        v_vehicle.id
    )
    returning id into v_request_id;


    -- Registra a rota.
    insert into public.tow_request_routes (
        tow_request_id,
        origin_address,
        origin_latitude,
        origin_longitude,
        destination_address,
        destination_latitude,
        destination_longitude
    )
    values (
        v_request_id,
        btrim(p_origin_address),
        p_origin_latitude,
        p_origin_longitude,
        btrim(p_destination_address),
        p_destination_latitude,
        p_destination_longitude
    );


    -- Registra o problema.
    insert into public.tow_request_problems (
        tow_request_id,
        problem_type,
        detail,
        description
    )
    values (
        v_request_id,
        p_problem_type::public.problem_type,
        btrim(p_problem_detail),
        nullif(btrim(p_problem_description), '')
    );


    return v_request_id;
end;
$function$;


-- A função fica disponível apenas para usuários autenticados.
-- A autorização de cliente também é verificada dentro dela.
revoke all
on function public.create_customer_tow_request(
    text,
    text,
    text,
    integer,
    text,
    text,
    double precision,
    double precision,
    text,
    double precision,
    double precision,
    text,
    text,
    text
)
from public, anon;


grant execute
on function public.create_customer_tow_request(
    text,
    text,
    text,
    integer,
    text,
    text,
    double precision,
    double precision,
    text,
    double precision,
    double precision,
    text,
    text,
    text
)
to authenticated;


-- Índices apontados pela revisão de desempenho.
create index if not exists idx_complaints_reviewed_by
    on public.complaints (reviewed_by);


create index if not exists idx_tow_requests_vehicle_customer
    on public.tow_requests (
        customer_vehicle_id,
        customer_id
    );


-- Atualiza o cache da API.
notify pgrst, 'reload schema';

commit;


-- Verificação da configuração aplicada.
-- Todas as colunas deste resultado devem retornar true.
select
    not p.prosecdef
        as funcao_security_invoker,

    has_function_privilege(
        'authenticated',
        p.oid,
        'EXECUTE'
    ) as cliente_pode_executar,

    not has_function_privilege(
        'anon',
        p.oid,
        'EXECUTE'
    ) as anonimo_bloqueado,

    not has_column_privilege(
        'authenticated',
        'public.tow_requests',
        'status',
        'INSERT'
    ) as status_permanece_protegido,

    exists (
        select 1
        from pg_catalog.pg_proc f
        join pg_catalog.pg_namespace ns
            on ns.oid = f.pronamespace
        where ns.nspname = 'public'
          and f.proname = 'set_updated_at'
          and 'search_path=""' = any(f.proconfig)
    ) as updated_at_search_path_fixo,

    to_regclass(
        'public.idx_complaints_reviewed_by'
    ) is not null
        as indice_complaints_criado,

    to_regclass(
        'public.idx_tow_requests_vehicle_customer'
    ) is not null
        as indice_chamados_criado

from pg_catalog.pg_proc p
join pg_catalog.pg_namespace n
    on n.oid = p.pronamespace

where n.nspname = 'public'
  and p.proname = 'create_customer_tow_request';