package com.ray3du.onlinesurvey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        //Initialize mAuth
        mAuth = FirebaseAuth.getInstance()

        //Capture views
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val confirmPassword = findViewById<EditText>(R.id.confirmPassword)
        val registerBtn = findViewById<Button>(R.id.registerButton)
        val loginBtn = findViewById<Button>(R.id.loginButton)
        val registerBar = findViewById<ProgressBar>(R.id.registerBar)

        //Login button listener
        loginBtn.setOnClickListener {
            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        //Handle registration
        registerBtn.setOnClickListener {
            registerBar.isVisible = true

            if (email.text.toString() == ""){
                Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                registerBar.isVisible = false
            }else if (password.text.toString() == ""){
                Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show()
                registerBar.isVisible = false
            }else if(confirmPassword.text.toString() == ""){
                Toast.makeText(this, "Confirm password cannot be empty", Toast.LENGTH_SHORT).show()
                registerBar.isVisible = false
            }else{
                if (confirmPassword.text.toString() != password.text.toString()){
                    Toast.makeText(this, "passwords do not match", Toast.LENGTH_SHORT).show()
                    password.setText("")
                    confirmPassword.setText("")
                    registerBar.isVisible = false
                }else{
                    authentication(email.text.toString(), password.text.toString(), registerBar)
                }
            }
        }
    }

    private fun authentication(email: String, password: String, registerBar: ProgressBar){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            task ->
            if (task.isSuccessful){
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra("message", "Successfully registered")
                registerBar.isVisible = false
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Registration failed, Retry", Toast.LENGTH_SHORT).show()
                registerBar.isVisible = false
            }
        }
    }
}