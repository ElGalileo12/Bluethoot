package com.app.bluethoot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login2.*
import kotlinx.android.synthetic.main.activity_maestro.*



enum class ProviderType{
    BASIC
}

class Activity_maestro : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maestro)

        user_registro.setOnClickListener {
            val intent1 = Intent(this, Conecttion::class.java)
            startActivity(intent1)
        }

        user_delete.setOnClickListener {
            val intent2 = Intent(this, Activity_Delete::class.java)
            startActivity(intent2)
        }
       imv_Volver3.setOnClickListener {
            val intent1 = Intent(this, Login::class.java)
            startActivity(intent1)
        }
       excel.setOnClickListener {
            val intent1 = Intent(this, Excel::class.java)
            startActivity(intent1)
        }

    }
}
