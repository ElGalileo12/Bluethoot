package com.app.bluethoot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //para splashscreen
        setTheme(R.style.SplashTheme)

        //Boton para drijirse a la parte de usuario
        user.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //Boton para drijirse a la parte de administrador
        admi.setOnClickListener {
            val intent = Intent(this, Login2::class.java)
            startActivity(intent)
        }

    }
}