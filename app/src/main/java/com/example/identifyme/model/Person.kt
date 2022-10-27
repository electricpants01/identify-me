package com.example.identifyme.model

data class Person(
    val `data`: Data?
) {
    data class Data(
        val buscaPorDocumento: List<BuscaPorDocumento?>?
    ) {
        data class BuscaPorDocumento(
            val apmaterno: String?,
            val appaterno: String?,
            val fnacimiento: String?,
            val foto: String?,
            val nombres: String?,
            val numdoc: String?,
            val respuesta: String?
        )
    }
}