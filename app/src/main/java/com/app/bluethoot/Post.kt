package com.app.bluethoot

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.HashMap

// [START post_class]
@IgnoreExtraProperties
data class Post(
    //var id: String? = "",
    var Cedula: String = "",
    var Celular: String = "",
    var Curso: String = "",
    var Nombre: String = "",
    var Registro: String = "",
    var Temperatura: String = ""
    //var stars: MutableMap<String, Boolean> = HashMap()
) {

    // [START post_to_map]
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            //"id" to id,
            "Cedula"  to Cedula,
            "Celular" to  Celular,
            "Curso"   to  Curso,
            "Nombre"  to Nombre,
            "Registro"  to Registro,
            "Temperatura" to  Temperatura,
            //"stars" to stars
        )
    }
    // [END post_to_map]
}