package com.app.bluethoot


import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.hp.bluetoothjhr.BluetoothJhr
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_registro.*
import kotlinx.android.synthetic.main.activity_registro.a_real
import kotlinx.android.synthetic.main.activity_registro.a_real2
import kotlinx.android.synthetic.main.activity_registro.addbutton
import kotlinx.android.synthetic.main.activity_registro.addresEditText
import kotlinx.android.synthetic.main.activity_registro.cedulaEditText
import kotlinx.android.synthetic.main.activity_registro.exit_button
import kotlinx.android.synthetic.main.activity_registro.id_tarjeta
import kotlinx.android.synthetic.main.activity_registro.phonEditText
import kotlin.concurrent.thread


class Activity_Register : AppCompatActivity() {
    var K=""
    var K1=""
    var ban1 = 1
    var cont1 = 0
    var dataKey = ""
    lateinit var bluetoothJhr: BluetoothJhr
    var booleano: Boolean = true

    val ref2 = FirebaseDatabase.getInstance().getReference("/Aforo/Cards")
    private val ref = FirebaseDatabase.getInstance().getReference("/Aforo")
    private lateinit var database: DatabaseReference

    private val timefinish = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {

                val id = snapshot.child("K").value.toString()
                a_real.text = id
                Log.e("FIREBASE", "id: $id")
                val persona = RFID(addresEditText.text.toString(), cedulaEditText.text.toString(), cursEditText2.text.toString(),phonEditText.text.toString(),
                        id_tarjeta.text.toString())
                val cards = id_tarjeta.text.toString()
                val identi = cedulaEditText.text.toString()
                val ide0 = acceso0.text.toString()

                database.child("Aforo").child("Users").child("User$id").setValue(persona)
                database.child("Aforo").child("Cards").child("Card$id").setValue(cards)
                database.child("Aforo").child("Cedulas").child("Cedula$id").setValue(identi)

                database.child("Aforo/Acceso").child("User$id").setValue(ide0)
            }
        }


        override fun onCancelled(error: DatabaseError) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        database = Firebase.database.reference
        funciona()
        exit()

        val id_tarjeta = findViewById<TextView>(R.id.id_tarjeta)
        bluetoothJhr = BluetoothJhr(Conecttion::class.java, this)


        thread(start = true) {
            while (booleano) {
                Thread.sleep(1000)
                runOnUiThread {
                id_tarjeta.text = bluetoothJhr.Rx()
                K = ""
                K = bluetoothJhr.Rx().toString()
                if (K != "") {
                    if (K[0] == '*') {
                        K1 = ""
                        for (posicion in K.indices) {
                            if (K.get(posicion) != '*') {
                                K1 += K.get(posicion)
                            }
                        }
                        ban1 = 0
                        cont1 = 0
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
                                            println("Está yes")
                                        }
                                    }
                                }

                                if (cont1 == 1 && ban1 == 0) {
                                    ban1 = 1
                                    showAlert3()
                                    bluetoothJhr.ResetearRx()
                                    K1=""

                                } else if (cont1 == 0 && ban1 == 0) {
                                    println("No esta en la base de datos")
                                    id_tarjeta.text = K1
                                    booleano = false
                                    ban1 = 1
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // Getting Post failed, log a message
                            }
                        }
                        ref2.addValueEventListener(postListener)

                    }
                    bluetoothJhr.Tx("N*")
                 }
                }
            }
        }
    }

    private fun funciona () {
            addbutton.setOnClickListener {
                if (id_tarjeta.text.isNotEmpty() && cont1 == 0){
                    ref.addValueEventListener(timefinish)
                    Toast.makeText(this, "¡Registro Exitoso!", Toast.LENGTH_SHORT).show()
                }else{
                    showAlert4()
                }
                }
        }
    
    private fun exit(){
         exit_button.setOnClickListener {
                val valoruno: Int = (a_real.text.toString()).toInt()
                val valordos: Int = (a_real2.text.toString()).toInt()
                val result: String = (valordos + valoruno).toString()
                database.child("Aforo").child("K").setValue(result)

                database.child("Aforo").removeEventListener(timefinish)
               val intent3 = Intent(this, Activity_maestro::class.java)
               startActivity(intent3)
               finish()
           }
       }

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

    private fun showAlert3(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Está en la base de datos.")
        builder.setMessage("Dirigirse con el administrador.")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
    private fun showAlert4(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("No se ha encontrado tarjeta.")
        builder.setMessage("Por favor pase una tarjeta.")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
