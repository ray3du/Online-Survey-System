package com.ray3du.onlinesurvey

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AdminActivity : AppCompatActivity() {
    private var count: Int = 0
    private var quizId: Int = 1
    private lateinit var textView: TextView
    private lateinit var textView1: TextView
    private lateinit var editText: EditText
    private lateinit var editText1: EditText
    private lateinit var button: Button
    private lateinit var button1: Button
    private lateinit var mAuth: FirebaseAuth
    private lateinit var list: MutableList<EditText>
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var title2: String

    @SuppressLint("SetTextI18n", "SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        //Handle views
        val questionNumber = findViewById<EditText>(R.id.questionNumber)
        val viewLayout = findViewById<WebView>(R.id.viewLayout)
        val generateButton = findViewById<Button>(R.id.generateView)
        val preview = findViewById<TextView>(R.id.preview)
        val title = findViewById<EditText>(R.id.titleAdmin)
        val logout = findViewById<ImageView>(R.id.logout)

        //Initialize webView settings
        val settings = viewLayout.settings
        settings.javaScriptEnabled = true
        settings.allowContentAccess = true
        settings.domStorageEnabled = true

        viewLayout.webViewClient = WebViewClient()

        list = mutableListOf()
        mAuth = FirebaseAuth.getInstance()

        //Initialize firebase
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference

        generateButton.setOnClickListener {
            generateButton.isEnabled = false
           if(questionNumber.text.toString() != ""){
               writeNewUser(title.text.toString(), questionNumber.text.toString())
               preview.isVisible = true
               viewLayout.isClickable = false
               Toast.makeText(this, "Loading a preview of the questionnaire", Toast.LENGTH_LONG).show()
               viewLayout.loadUrl("${questionNumber.text}")
           }else{
               Toast.makeText(this, "Form link cannot be empty", Toast.LENGTH_SHORT).show()
           }
        }

        logout.setOnClickListener {
            mAuth = FirebaseAuth.getInstance()
            mAuth.signOut()
            startActivity((Intent(this, LoginActivity::class.java)))
            finish()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, AdminActivity::class.java))
        finish()
    }

    private fun writeNewUser(quizId: String, text: String) {
        val user = Question(quizId, text)
        databaseReference.child("Data/$quizId").setValue(user)
    }

}