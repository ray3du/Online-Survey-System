package com.ray3du.onlinesurvey

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Initialize mAuth firebase
        mAuth = FirebaseAuth.getInstance()

        //Initialize views
        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)
        val error = findViewById<TextView>(R.id.error)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginProgressBar = findViewById<ProgressBar>(R.id.loginBar)

        //Handle intents
        val bundle: Bundle? = intent.extras

        if (bundle != null){
            error.text = bundle.getString("message")
            error.isVisible = true
        }

        //handle register button
        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
            finish()
        }

        //handle login in
        loginButton.setOnClickListener {
            loginProgressBar.isVisible = true
            if(email.text.toString() == ""){
                Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show()
                loginProgressBar.isVisible = false
            }else if (password.text.toString() == ""){
                Toast.makeText(this, "password cannot be empty", Toast.LENGTH_SHORT).show()
                loginProgressBar.isVisible = false
            }else{
                authentication(email.text.toString(), password.text.toString(), loginProgressBar)
            }
        }
    }

    //Handle app persistence
    override fun onStart() {
        super.onStart()
        //Handle intents
        val bundle: Bundle? = intent.extras

        var errorText: String? = null

        if (bundle != null){
            errorText = bundle.getString("message")
        }
        val currentUser = mAuth.currentUser
        if (currentUser != null){
            if(errorText == null){
                if (currentUser.email?.toString() == "admin@gmail.com"){
                    startActivity(Intent(this, AdminActivity::class.java))
                }else{
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
        }
    }

    private fun authentication(email: String, password: String, loginProgressBar: ProgressBar){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            task ->
            if (task.isSuccessful){
                if(email == "admin@gmail.com"){
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this, MainActivity::class.java)
                    loginProgressBar.isVisible = false
                    startActivity(intent)
                    finish()
                }
            }else{
                loginProgressBar.isVisible = false
                Toast.makeText(this, "Login Failed..", Toast.LENGTH_SHORT).show()
            }
        }
    }
}