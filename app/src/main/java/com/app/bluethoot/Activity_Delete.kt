package com.app.bluethoot


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.hp.bluetoothjhr.BluetoothJhr
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_delete.*
import kotlinx.android.synthetic.main.activity_delete.addresEditText
import kotlinx.android.synthetic.main.activity_delete.card_view
import kotlinx.android.synthetic.main.activity_delete.exit_button
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_registro.*


class Activity_Delete : AppCompatActivity() {


    var dataKey2=""
    var cont2=0
    var id2=""
    var A=0

    private lateinit var database: DatabaseReference

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete)

        database = Firebase.database.reference
        card_view.visibility = View.GONE
        update.visibility = View.GONE
        addresEditText3.visibility = View.GONE

        exit()

        btnBusqueda.setOnClickListener { 
            if(addresEditText.text.isNotEmpty()){

                println("Diste click en el boton")
                val cedula = addresEditText.text.toString()


                    val fer = FirebaseDatabase.getInstance().getReference("/Aforo/Cedulas")
                    val postListener = object : ValueEventListener {
                        @RequiresApi(Build.VERSION_CODES.N)
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            for (postSnapshot in  dataSnapshot.children){
                                val datos = postSnapshot.value //Toma los datos de firebase en el nodo seleccioado
                                if(cedula == datos.toString()){
                                    dataKey2 = postSnapshot.key.toString()
                                    Toast.makeText(this@Activity_Delete, dataKey2, Toast.LENGTH_SHORT).show()
                                    cont2= 1
                                }
                            }
                            if (cont2 == 1){

                                println("Esta en la base de datos")
                                Toast.makeText(this@Activity_Delete, "Se encuentra registrado", Toast.LENGTH_SHORT).show()

                                id2 = dataKey2[6].toString()

                                val UserDataRegister2 = object : ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        val post = dataSnapshot.getValue<Post>()
                                        println(post)
                                        var consola22 = findViewById<TextView>(R.id.consola22)
                                        var cedula33 = findViewById<TextView>(R.id.cedula33)
                                        var consola44 = findViewById<TextView>(R.id.consola44)
                                        var consola55 = findViewById<TextView>(R.id.consola55)
                                        if (post != null) {
                                            consola22.text = post.Nombre
                                            cedula33.text = post.Cedula
                                            consola44.text = post.Celular
                                            consola55.text = post.Curso

                                        }


                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                }
                                val ref1 = FirebaseDatabase.getInstance().getReference("/Aforo/Users/User$id2")
                                ref1.addValueEventListener(UserDataRegister2)
                                card_view.visibility = View.VISIBLE


                            }else{
                                Toast.makeText(this@Activity_Delete, "No se encuentra en la base de datos", Toast.LENGTH_LONG).show()
                            }

                        }
                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            Toast.makeText(this@Activity_Delete, "Error: " + databaseError.toException(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    fer.addValueEventListener(postListener)

            }else{
                Toast.makeText(this, "Accion no valida", Toast.LENGTH_SHORT).show()
            }
        }

        EditNombre.setOnClickListener {
            var consola22 = findViewById<TextView>(R.id.consola22)
            var Nombre = consola22.text.toString()
            var update = findViewById<TextView>(R.id.update)
            var addresEditText3 = findViewById<EditText>(R.id.addresEditText3)

            update.visibility = View.VISIBLE
            addresEditText3.visibility = View.VISIBLE
            addresEditText3.hint = Nombre
            A=1
        }

        editCedula.setOnClickListener {
            var cedula33 = findViewById<TextView>(R.id.cedula33)
            var cedula = cedula33.text.toString()
            var update = findViewById<TextView>(R.id.update)
            var addresEditText3 = findViewById<EditText>(R.id.addresEditText3)

            update.visibility = View.VISIBLE
            addresEditText3.visibility = View.VISIBLE
            addresEditText3.hint = cedula
            A=2
        }

        editCelular.setOnClickListener {
            var cedula44 = findViewById<TextView>(R.id.consola44)
            var celular = cedula44.text.toString()
            var update = findViewById<TextView>(R.id.update)
            var addresEditText3 = findViewById<EditText>(R.id.addresEditText3)

            update.visibility = View.VISIBLE
            addresEditText3.visibility = View.VISIBLE
            addresEditText3.hint = celular
            A=3
        }

        editCurso.setOnClickListener {
            var curso55= findViewById<TextView>(R.id.consola55)
            var curso = curso55.text.toString()
            var update = findViewById<TextView>(R.id.update)
            var addresEditText3 = findViewById<EditText>(R.id.addresEditText3)

            update.visibility = View.VISIBLE
            addresEditText3.visibility = View.VISIBLE
            addresEditText3.hint = curso
            A=4
        }

        update.setOnClickListener {
            var consola22 = findViewById<TextView>(R.id.consola22)
            var addresEditText3 = findViewById<EditText>(R.id.addresEditText3)
            var Nombre = consola22.text.toString()

            if(addresEditText3.text.isNotEmpty()){
                if (A==1){
                    val ADatabase = FirebaseDatabase.getInstance().getReference("/Aforo/Users/User$id2")
                    val childUpdates = hashMapOf<String, Any>(
                            "/nombre" to addresEditText3.text.toString(),
                    )
                    ADatabase.updateChildren(childUpdates)

                }else if(A==2){
                    val ADatabase = FirebaseDatabase.getInstance().getReference("/Aforo/Users/User$id2")
                    val childUpdates = hashMapOf<String, Any>(
                            "/cedula" to addresEditText3.text.toString(),
                    )
                    ADatabase.updateChildren(childUpdates)

                }else if(A==3){
                    val ADatabase = FirebaseDatabase.getInstance().getReference("/Aforo/Users/User$id2")
                    val childUpdates = hashMapOf<String, Any>(
                            "/celular" to addresEditText3.text.toString(),
                    )
                    ADatabase.updateChildren(childUpdates)

                }else if(A==4){
                    val ADatabase = FirebaseDatabase.getInstance().getReference("/Aforo/Users/User$id2")
                    val childUpdates = hashMapOf<String, Any>(
                            "/curso" to addresEditText3.text.toString(),
                    )
                    ADatabase.updateChildren(childUpdates)
                }

            }else{
                Toast.makeText(this, "Accion no valida", Toast.LENGTH_SHORT).show()
            }
            update.visibility = View.GONE
            addresEditText3.visibility = View.GONE
        }


        eliminar.setOnClickListener {
            if (cont2==1) {
                val ref5 = FirebaseDatabase.getInstance().getReference("Aforo/Acceso/User$id2")
                ref5.removeValue()

                val ref4 = FirebaseDatabase.getInstance().getReference("Aforo/Cards/Card$id2")
                ref4.removeValue()

                val ref3 = FirebaseDatabase.getInstance().getReference("Aforo/Cedulas/Cedula$id2")
                ref3.removeValue()

                val ref2 = FirebaseDatabase.getInstance().getReference("/Aforo/Users/User$id2")
                ref2.removeValue()

                var consola22 = findViewById<TextView>(R.id.consola22)
                var cedula33 = findViewById<TextView>(R.id.cedula33)
                var consola44 = findViewById<TextView>(R.id.consola44)
                var consola55 = findViewById<TextView>(R.id.consola55)

                consola22.text = ""
                cedula33.text = ""
                consola44.text = ""
                consola55.text = ""

                cont2=0
            }
            else{
                Toast.makeText(this@Activity_Delete, "Invalido", Toast.LENGTH_LONG).show()
            }
        }

    } // Fin del Oncreate


    private fun exit(){
           exit_button.setOnClickListener {
                val intent3 = Intent(this, Activity_maestro::class.java)
                startActivity(intent3)
             }

    }
}

