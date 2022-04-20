package com.app.bluethoot

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login2.*
import kotlinx.android.synthetic.main.activity_main2.*

class Login2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
         setup()

      imv_Volver1.setOnClickListener {
            val intent1 = Intent(this, Login::class.java)
            startActivity(intent1)
        }
    }

    private fun setup() {
        button_login.setOnClickListener {
            if (editText_email_login.text.isNotEmpty() && editTextText_Password_login.text.isNotEmpty())
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    editText_email_login.text.toString(),
                    editTextText_Password_login.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
        }
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider:ProviderType){

        val homeIntent = Intent (this, Activity_maestro::class.java).apply {
            putExtra("Email", email)
            putExtra("Provaider", provider.name)
        }
        startActivity(homeIntent)
    }
}