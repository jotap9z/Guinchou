# Login e Home do cliente

Esta etapa usa Supabase Auth e lê `profiles`, `customers` e `tow_requests` pelo token do próprio usuário. Não usa chave administrativa no Android e não altera o banco.

## Preparação local

O `SupabaseProvider` existente depende de `com.guinchou.app.config.SupabaseConfig`. Esse arquivo está ignorado pelo Git. Mantenha nele somente a URL base do projeto e a chave publishable/anon. Nunca inclua `service_role` no aplicativo.

Crie uma conta de cliente de teste em Authentication → Users do Supabase. Confirme que `auth.users.id = profiles.id = customers.id`, `profiles.role = CUSTOMER` e `profiles.status = ACTIVE`. Se `profiles.full_name` estiver vazio, a Home mostra “Cliente”.

## Teste manual no Android Studio

1. Abra o projeto local que contém `SupabaseConfig.kt` e execute a versão desta branch.
2. Entre com o e-mail e a senha da conta confirmada. O antigo login `admin@guinchou.com / 123456` não é mais especial.
3. Confira nome, quantidade de serviços concluídos e estado do atendimento na Home. Sem chamado ativo, o botão de solicitação deve estar habilitado.
4. Encerre a sessão no perfil. Ao abrir novamente o aplicativo, a sessão deve ser restaurada quando ainda válida; após logout, deve pedir login.
5. Se o banco negar leitura por RLS, a Home mostra erro e bloqueia o início do fluxo.

## Validações pendentes no Supabase

- Revisar o código de `private.can_view_request(uuid)` e `private.is_customer()`; o export das policies demonstra seu uso, mas não demonstra suas implementações.
- Testar as leituras com o token do cliente, pois o SQL Editor administrativo pode contornar RLS.
- Impor no banco a regra de um único atendimento ativo por cliente. O botão desabilitado evita apenas a ação pela interface e não substitui a proteção transacional.
- Confirmar que as funções de criação automática de `profiles` e `customers` recebem os dados do cadastro. A tela de criação de conta ainda depende desse trabalho e não faz parte deste teste de login.
- Validar o build no Android Studio; o ambiente desta revisão não dispõe do Android SDK nem do `SupabaseConfig.kt` local.
