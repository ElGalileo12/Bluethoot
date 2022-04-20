package com.app.bluethoot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.hp.bluetoothjhr.BluetoothJhr
import kotlinx.android.synthetic.main.activity_conecttion.*

class Conecttion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conecttion)

        val second_list = findViewById<ListView>(R.id.second_list)
        val bluetoothJhr = BluetoothJhr(this,second_list)
        bluetoothJhr.EncenderBluetooth()

        second_list.setOnItemClickListener { parent, view, position, id ->
            bluetoothJhr.Disp_Seleccionado(view,position, Activity_Register::class.java)
        }

        imv_Volver4.setOnClickListener {
            val intent1 = Intent(this, Activity_maestro::class.java)
            startActivity(intent1)
        }
    }
}