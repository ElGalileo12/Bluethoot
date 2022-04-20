package com.app.bluethoot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.hp.bluetoothjhr.BluetoothJhr
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lista_dispositivos = findViewById<ListView>(R.id.lista_dispositivos)
        val bluetoothJhr = BluetoothJhr(this,lista_dispositivos)
        bluetoothJhr.EncenderBluetooth()

        lista_dispositivos.setOnItemClickListener { parent, view, position, id ->
            bluetoothJhr.Disp_Seleccionado(view,position, MainActivity2::class.java)
        }

        imv_Volver0.setOnClickListener {
            finish()
            val intent1 = Intent(this, Login::class.java)
            startActivity(intent1)
        }


    }//fin del oncreate
}