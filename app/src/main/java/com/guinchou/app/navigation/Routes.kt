package com.guinchou.app.navigation

/**
 * Centraliza todas as rotas utilizadas pelo Guinchou.
 *
 * As rotas são separadas conceitualmente em:
 *
 * - cliente;
 * - parceiro;
 * - operação;
 * - administração.
 */
object Routes {

    /*
     * =========================================
     * AUTENTICAÇÃO
     * =========================================
     */

    // Tela inicial.
    const val SPLASH =
        "splash"

    // Login principal.
    const val LOGIN =
        "login"


    /*
     * =========================================
     * CLIENTE
     * =========================================
     */

    // Home do cliente.
    const val HOME =
        "home"

    // Etapa 1 - origem.
    const val PICKUP =
        "pickup"

    // Etapa 2 - destino.
    const val DESTINATION =
        "destination"

    // Etapa 3 - veículo.
    const val VEHICLE =
        "vehicle"

    // Etapa 4 - problema.
    const val PROBLEM =
        "problem"

    // Etapa 5 - estimativa.
    const val ESTIMATE =
        "estimate"

    // Pagamento.
    const val PAYMENT =
        "payment"

    // Procurando parceiro.
    const val SEARCHING =
        "searching"

    // Acompanhamento da corrida.
    const val TRACKING =
        "tracking"


    /*
     * =========================================
     * PARCEIROS
     * =========================================
     */

    /**
     * Entrada principal do módulo parceiro.
     *
     * Aqui o usuário escolhe:
     *
     * - motorista independente;
     * - empresa de guinchos.
     */
    const val PARTNER =
        "partner"


    /**
     * Cadastro de motorista independente.
     *
     * Será desenvolvido na próxima etapa.
     */
    const val DRIVER_REGISTER =
        "driver_register"


    /**
     * Cadastro de empresa de guinchos.
     *
     * Será desenvolvido posteriormente.
     */
    const val COMPANY_REGISTER =
        "company_register"


    /**
     * Cadastro de frota.
     */
    const val FLEET_REGISTER =
        "fleet_register"


    /**
     * Importação de frota por planilha.
     */
    const val FLEET_IMPORT =
        "fleet_import"


    /**
     * Documentos do parceiro,
     * motorista e veículo.
     */
    const val PARTNER_DOCUMENTS =
        "partner_documents"


    /**
     * Pagamento da taxa de cadastro
     * dos guinchos.
     */
    const val PARTNER_REGISTRATION_PAYMENT =
        "partner_registration_payment"


    /**
     * Aguardando análise administrativa.
     */
    const val PARTNER_REVIEW =
        "partner_review"


    /**
     * Painel operacional do parceiro aprovado.
     */
    const val PARTNER_HOME =
        "partner_home"
}