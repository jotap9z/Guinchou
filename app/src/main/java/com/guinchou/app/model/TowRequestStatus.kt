package com.guinchou.app.model

/**
 * Representa o estado atual
 * de uma solicitação de guincho.
 *
 * Esses estados serão usados:
 *
 * - no aplicativo do cliente;
 * - no aplicativo/parceiro;
 * - no backend;
 * - no painel administrativo.
 */
enum class TowRequestStatus {

    /**
     * Solicitação criada,
     * mas ainda não iniciou busca.
     */
    CREATED,

    /**
     * Sistema procurando
     * um parceiro disponível.
     */
    SEARCHING,

    /**
     * Um parceiro aceitou
     * o chamado.
     */
    ACCEPTED,

    /**
     * Guincheiro está indo
     * até o cliente.
     */
    DRIVER_ON_THE_WAY,

    /**
     * Guincheiro chegou
     * ao local.
     */
    ARRIVED,

    /**
     * Veículo já foi colocado
     * no guincho.
     */
    VEHICLE_LOADED,

    /**
     * Transporte até o destino
     * foi iniciado.
     */
    IN_TRANSIT,

    /**
     * Atendimento finalizado.
     */
    COMPLETED,

    /**
     * Solicitação cancelada.
     */
    CANCELLED
}