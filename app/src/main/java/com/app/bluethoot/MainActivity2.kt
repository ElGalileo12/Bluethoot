package com.app.bluethoot

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.hp.bluetoothjhr.BluetoothJhr
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_delete.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.cedula33
import kotlinx.android.synthetic.main.activity_main2.consola22
import kotlinx.android.synthetic.main.activity_main2.consola44
import kotlinx.android.synthetic.main.activity_main2.consola55
import java.util.*
import kotlin.concurrent.thread

class MainActivity2 : AppCompatActivity() {

    private var running = false
    private var pauseOffset: Long = 0

    lateinit var bluetoothJhr: BluetoothJhr
    var booleano: Boolean = true
    val database = Firebase.database
    var K = ""
    var K2 = ""
    var K1 = ""
    var ban1 = 0
    var ban2 = 0
    var ban3 = 10
    var cont2 = 0
    var cont1 = 0
    var dataKey = ""
    var dataKey2 = ""
    var Q=0
    var id2 =""


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        progressBar.visibility = View.GONE
        barra.visibility = View.GONE


        ingresar()

        imv_Volver.setOnClickListener {
            val intent1 = Intent(this, MainActivity::class.java)
            startActivity(intent1)
        }
        bluetoothJhr = BluetoothJhr(MainActivity::class.java, this)

        //Base de datos
        val ref = FirebaseDatabase.getInstance().getReference("/Aforo/Cards")

        //----------------------------------------------------------------------- user

        thread(start = true) {
            while (booleano) {
                Thread.sleep(500)
                //if (bluetoothJhr.Rx() != null){
                //consola.text = bluetoothJhr.Rx()
                // K=bluetoothJhr.Rx().toString()
                //}

                runOnUiThread {
                    K = ""

                    K = bluetoothJhr.Rx().toString()

                    if (K != "") {

                        if (K[0] == '*') {
                            K1 = ""
                            for (posicion in K.indices) {
                                if (K[posicion] != '*') {
                                    K1 += K[posicion]
                                }
                            }



                            ban1 = 0
                        } else if (K[0] == '#') {
                            K2 = ""
                            for (posicion in K.indices) {
                                if (K[posicion] != '#') {
                                    K2 += K[posicion]
                                }
                            }
                            Log.i("RX", "Recibido del pic: $K2")
                            ban2 = 1

                        }


                        if (K1 != "" && ban1 == 0) {
                            println("Card: $K1")

                            val postListener = object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (ban1 == 0) {
                                        for (postSnapshot in dataSnapshot.children) {
                                            val datos = postSnapshot.value //Toma los datos de firebase en el nodo seleccioado
                                            println("Datos: $datos")
                                            println("Valor de k1: $K1")
                                            if (K1 == datos) {
                                                dataKey = postSnapshot.key.toString()
                                                cont1 = 1
                                            }
                                        }
                                    }

                                    if (cont1 == 1 && ban1 == 0) {
                                        val id = dataKey[4]
                                        val acceso = object : ValueEventListener {
                                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                                val ES = dataSnapshot.value.toString().toInt() //Toma los datos de firebase en el nodo seleccioado
                                                if (ES!=null){
                                                    if (ES == 0) {
                                                        ban3 = 0
                                                    } else if (ES == 1) {
                                                        ban3 = 1
                                                    }
                                                }else{
                                                    showAlert3()
                                                }

                                            }

                                            override fun onCancelled(databaseError: DatabaseError) {
                                                // Getting Post failed, log a message
                                                Log.w("Error", "loadPost:onCancelled", databaseError.toException())
                                                showAlert3()
                                            }
                                        }

                                        FirebaseDatabase.getInstance().getReference("/Aforo/Acceso/User${id}").addValueEventListener(acceso)
                                        //println("Esta en la base de datos")
                                        //println("Necesito la temperatura")
                                        bluetoothJhr.Tx("T*")




                                        //Toast.makeText(this@MainActivity2, "Se temará la temperatura. ", Toast.LENGTH_SHORT).show()
                                        progressBar.visibility = View.VISIBLE
                                        barra.visibility = View.VISIBLE
                                        progressBar.max = 100
                                        val currentProgress = 100
                                        ObjectAnimator.ofInt(progressBar,"progress", currentProgress)
                                                .setDuration(4200)
                                                .start()

                                        ban1 = 1

                                    } else if (cont1 == 0 && ban1 == 0) {
                                        ban1 = 1
                                        println("No esta en la base de datos")
                                        showAlert()
                                        bluetoothJhr.Tx("N*")
                                        bluetoothJhr.ResetearRx()
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Getting Post failed, log a message
                                    Toast.makeText(this@MainActivity2, "Error: " + databaseError.toException(), Toast.LENGTH_SHORT).show()
                                }
                            }

                            ref.addValueEventListener(postListener)

                        }


                        if (K1 != "" && K2 != "" && cont1 == 1 && ban2 == 1) {
                            var id = dataKey[4]
                            Log.e("UserData", "User:${K1} - Temp:${K2} - DataKey: ${dataKey}")
                            val date = Date();
                            val dateFormatWithZone = SimpleDateFormat("yyyy-MM-dd'-'HH:mm:ss", Locale.getDefault())
                            val s: String = dateFormatWithZone.format(date)
                            Log.e("Fecha", "Date: $s")

                            val UserDataRegister = object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    progressBar.visibility = View.GONE
                                    barra.visibility = View.GONE
                                    progressBar.max = 0
                                    val currentProgress = 0
                                    ObjectAnimator.ofInt(progressBar,"progress", currentProgress).setDuration(100).start()

                                    val post = dataSnapshot.getValue<Post>()
                                    println(post)
                                    val persona = post?.let { User(post.Cedula, post.Celular, post.Nombre,K2,s,post.Curso) }
                                    Log.e("Acceso", "Ban3: ${ban3}")
                                    adentroAforo(ban3)
                                    if (ban3 == 0) {
                                        database.getReference("/Aforo/Registro-Entrada").child(s).setValue(persona)
                                        database.getReference("/Aforo/Acceso/User$id").setValue("1")
                                        ban3 = 10
                                    } else if (ban3 == 1) {
                                        database.getReference("/Aforo/Registro-Salida").child(s).setValue(persona)
                                        database.getReference("/Aforo/Acceso/User$id").setValue("0")
                                        ban3 = 10
                                    }

                                    consola22.text = post!!.Nombre
                                    cedula33.text = post.Cedula
                                    consola44.text = post.Celular
                                    consola55.text = post.Curso
                                    viewTemp.text = K2

                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            }
                            val ref1 = FirebaseDatabase.getInstance().getReference("/Aforo/Users/User$id")
                            ref1.addValueEventListener(UserDataRegister)
                            ban2 = 0
                            cont1 = 0
                        }

                        if(cont2==1 && K2 != "" && ban2 == 1){
                            val date = Date();
                            val dateFormatWithZone = SimpleDateFormat("yyyy-MM-dd'-'HH:mm:ss", Locale.getDefault())
                            val s: String = dateFormatWithZone.format(date)
                            Log.e("Fecha", "Date: $s")

                            val acceso2 = object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {

                                    val ES = dataSnapshot.value.toString().toInt() //Toma los datos de firebase en el nodo seleccioado
                                    if (ES!=null){
                                        if (ES == 0) {
                                            ban3 = 0
                                        } else if (ES == 1) {
                                            ban3 = 1
                                        }
                                    }else{
                                        showAlert3()
                                    }

                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    // Getting Post failed, log a message
                                    Log.w("Error", "loadPost:onCancelled", databaseError.toException())
                                    showAlert3()
                                }
                            }
                            FirebaseDatabase.getInstance().getReference("/Aforo/Acceso/User${id2}").addValueEventListener(acceso2)

                            val UserDataRegister2 = object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    val post = dataSnapshot.getValue<Post>()
                                    println(post)
                                    val persona = post?.let { User(post.Cedula, post.Celular, post.Nombre,K2,s,post.Curso) }
                                    Log.e("Acceso", "Ban3: ${ban3}")
                                    adentroAforo(ban3)
                                    if (ban3 == 0) {
                                        database.getReference("/Aforo/Registro-Entrada").child(s).setValue(persona)
                                        database.getReference("/Aforo/Acceso/User$id2").setValue("1")
                                        ban3 = 10
                                    } else if (ban3 == 1) {
                                        database.getReference("/Aforo/Registro-Salida").child(s).setValue(persona)
                                        database.getReference("/Aforo/Acceso/User$id2").setValue("0")
                                        ban3 = 10
                                    }

                                    consola22.text = post!!.Nombre
                                    cedula33.text = post.Cedula
                                    consola44.text = post.Celular
                                    consola55.text = post.Curso
                                    viewTemp.text = K2

                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            }
                            val ref1 = FirebaseDatabase.getInstance().getReference("/Aforo/Users/User$id2")
                            ref1.addValueEventListener(UserDataRegister2)
                            ban2 = 0
                            cont2=0
                        }

                    }
                    bluetoothJhr.ResetearRx()

                }
            }
        }//fin del hilo

        //----------------------------------------------------------------------- toolbar

        var adentro = 0
        val ADatabase = FirebaseDatabase.getInstance().getReference("/Aforo/Adentro")

        if(Q==0){

            ADatabase.get().addOnSuccessListener {
                adentro = it.value.toString().toInt()
                database.getReference("/Aforo/Adentro").setValue(adentro.toString())
                contadorAforo.text = adentro.toString()
                Q=1
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }

    }//fin del oncreate



    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Usuario")
        builder.setMessage("No está en la base de datos, comuniquese, con el administrador.")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert2(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Atencion")
        builder.setMessage("El numero de identificacion no se encuentra en la base de datos.")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert3(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Usuario")
        builder.setMessage("Esta tarjeta aun no presenta datos disponibles.")
        builder.setMessage("Dirigirse con el administrador.")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlert4(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¡Atencion!")
        builder.setMessage("Para el ingreso o salida este campo no puede estar vacio.")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun adentroAforo(U:Int){
        var adentro = 0
        var ADatabase = FirebaseDatabase.getInstance().getReference("/Aforo/Adentro")

        if(U==0){

            ADatabase.get().addOnSuccessListener {
                adentro = it.value.toString().toInt() + 1
                database.getReference("/Aforo/Adentro").setValue(adentro.toString())
                contadorAforo.text = adentro.toString()
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }

        }else if(U==1){

            ADatabase.get().addOnSuccessListener {
                adentro = it.value.toString().toInt() - 1
                if(adentro <= 0 ){
                    adentro=0
                }
                database.getReference("/Aforo/Adentro").setValue(adentro.toString())
                contadorAforo.text = adentro.toString()
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }


        }

    }//fin de la funcion adentro

    override fun onResume() {
        super.onResume()
        bluetoothJhr.ConectaBluetooth()
        bluetoothJhr.ResetearRx()
    }

    override fun onPause() {
        super.onPause()
        bluetoothJhr.CierraConexion()
        booleano = false
    }

    private fun ingresar(){
        ingresar.setOnClickListener {
            println("Diste click en el boton")
            val cedula = cedulaEdt.text.toString()

            if(cedula!=""){
                val fer = FirebaseDatabase.getInstance().getReference("/Aforo/Cedulas")
                val postListener = object : ValueEventListener {
                    @RequiresApi(Build.VERSION_CODES.N)
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (postSnapshot in  dataSnapshot.children){
                            val datos = postSnapshot.value //Toma los datos de firebase en el nodo seleccioado
                            if(cedula == datos.toString()){
                                dataKey2 = postSnapshot.key.toString()
                                cont2= 1
                            }
                        }
                        if (cont2 == 1){
                            bluetoothJhr.ResetearRx()
                            println("Esta en la base de datos")
                            Toast.makeText(this@MainActivity2, "Se encuentra registrado.", Toast.LENGTH_SHORT).show()
                            bluetoothJhr.Tx("CT*")

                            id2 = dataKey2[6].toString()
                            println("Id cedula: ${id2}")
                            //adentroAforo(ban3)

                        }else{
                            showAlert2()
                        }
                        bluetoothJhr.ResetearRx()

                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting Post failed, log a message
                        Toast.makeText(this@MainActivity2, "Error: " + databaseError.toException(), Toast.LENGTH_SHORT).show()
                    }
                }
                fer.addValueEventListener(postListener)

            }else{
                showAlert4()
            }

        }//fin del onclick

    }
}