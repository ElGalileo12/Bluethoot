package com.app.bluethoot

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    //var id: String? = "",
    var Cedula: String = "",
    var Celular: String = "",
    var Nombre: String = "",
    var Temperatura: String = "",
    var Registro: String = "",
    var Curso: String = ""
    //var stars: MutableMap<String, Boolean> = HashMap()
) {

    // [START post_to_map]
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            //"id" to id,
            "Cedula" to Cedula,
            "Celular" to  Celular,
            "Nombre" to Nombre,
            "Temperatura" to Temperatura,
            "Registro" to Registro,
            "Curso" to Curso
            //"stars" to stars
        )
    }
    // [END post_to_map]
}