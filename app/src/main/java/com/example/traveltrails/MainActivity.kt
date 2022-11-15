package com.example.traveltrails

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.doAsync

class MainActivity : AppCompatActivity() {

    private lateinit var login_user_field: EditText
    private lateinit var login_pass_field: EditText
    private lateinit var login_button: Button
    private lateinit var register_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_user_field = findViewById(R.id.login_user_field)
        login_pass_field = findViewById(R.id.login_pass_field)
        login_button = findViewById(R.id.login_button)
        register_button = findViewById(R.id.register_button)

        login_button.setOnClickListener { v: View ->
            doAsync {
                val s: String = ServerManager().test_server();
                runOnUiThread {
                    val toast =
                            Toast.makeText(this@MainActivity, s, Toast.LENGTH_LONG)
                    toast.show()
                }
            }

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        register_button.setOnClickListener { v: View ->
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}