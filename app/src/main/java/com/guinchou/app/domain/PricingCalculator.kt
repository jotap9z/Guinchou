package com.guinchou.app.domain

import kotlin.math.ceil

/**
 * Centraliza todas as regras financeiras
 * utilizadas na solicitação de guincho.
 *
 * Regra inicial definida para o Guinchou:
 *
 * 0 até 10 km   = R$ 150,00
 * >10 até 20 km = R$ 200,00
 * >20 até 25 km = R$ 250,00
 * >25 até 30 km = R$ 300,00
 *
 * Acima de 30 km:
 *
 * + R$ 50,00 para cada bloco adicional
 * iniciado de até 5 km.
 *
 * Exemplos:
 *
 * 31 km = R$ 350
 * 35 km = R$ 350
 * 36 km = R$ 400
 * 40 km = R$ 400
 */
object PricingCalculator {

    /**
     * Calcula o preço total do serviço
     * com base na distância da rota.
     */
    fun calculateServicePrice(
        distanceKm: Double,
    ): Double {

        /*
         * Distâncias inválidas retornam zero.
         */
        if (distanceKm <= 0.0) {
            return 0.0
        }

        return when {

            /*
             * 0 até 10 km.
             */
            distanceKm <= 10.0 ->
                150.0

            /*
             * Acima de 10 até 20 km.
             */
            distanceKm <= 20.0 ->
                200.0

            /*
             * Acima de 20 até 25 km.
             */
            distanceKm <= 25.0 ->
                250.0

            /*
             * Acima de 25 até 30 km.
             */
            distanceKm <= 30.0 ->
                300.0

            /*
             * Acima de 30 km.
             */
            else -> {

                /*
                 * Descobre quantos quilômetros
                 * passaram dos primeiros 30.
                 */
                val extraDistance =
                    distanceKm - 30.0

                /*
                 * Divide a distância excedente
                 * em blocos de 5 km.
                 *
                 * ceil() arredonda para cima.
                 *
                 * Exemplo:
                 *
                 * 31 km
                 * extra = 1
                 * 1 / 5 = 0,2
                 * ceil(0,2) = 1 bloco
                 *
                 * Portanto:
                 * R$ 300 + R$ 50.
                 */
                val extraBlocks =
                    ceil(
                        extraDistance / 5.0,
                    )

                /*
                 * Cada bloco custa R$ 50.
                 */
                300.0 +
                        (
                                extraBlocks *
                                        50.0
                                )
            }
        }
    }


    /**
     * Calcula a porcentagem pertencente
     * à plataforma.
     *
     * Até R$ 300:
     * 10%
     *
     * Acima de R$ 300:
     * 6%
     */
    fun calculatePlatformFeePercentage(
        servicePrice: Double,
    ): Double {

        return if (
            servicePrice <= 300.0
        ) {

            0.10

        } else {

            0.06
        }
    }


    /**
     * Calcula quanto pertence
     * ao administrador do aplicativo.
     */
    fun calculatePlatformFee(
        servicePrice: Double,
    ): Double {

        if (servicePrice <= 0.0) {
            return 0.0
        }

        val percentage =
            calculatePlatformFeePercentage(
                servicePrice,
            )

        return servicePrice *
                percentage
    }


    /**
     * Calcula o valor destinado
     * ao motorista ou empresa.
     */
    fun calculatePartnerAmount(
        servicePrice: Double,
    ): Double {

        val platformFee =
            calculatePlatformFee(
                servicePrice,
            )

        return servicePrice -
                platformFee
    }
}