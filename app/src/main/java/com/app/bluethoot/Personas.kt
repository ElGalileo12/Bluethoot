package com.app.bluethoot

class RFID(Nombre: String, Cedula: String,  Phone: String, Curso: String, Id: String?) {
    var Nombre: String = ""
    var Cedula: String = ""
    var Celular: String = ""
    var Curso: String = ""

    var Id: String = ""

        get() = field
        set(value) {
            field = value
        }

    init {
        this.Nombre = Nombre
        this.Cedula = Cedula
        this.Celular = Phone
        this.Curso = Curso

        if (Id != null) {
            this.Id = Id
        }
    }

}

/*
fun getId(Id: String): String {

    return Id
}

fun main(args: ArrayList<String>){

}
 */