package com.ray3du.onlinesurvey

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import com.google.firebase.database.*

class EvaluationActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference
    private lateinit var textView1: TextView
    private lateinit var editText: EditText
    private lateinit var editText1: EditText
    private lateinit var button1: Button
    private var titleKey: String = ""
    private var quizNum: Int = 1


    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluation)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val evaluationLayout = findViewById<WebView>(R.id.evaluationLayout)
        val title = findViewById<EditText>(R.id.titleAdmin)
        var evaluationTitle = findViewById<TextView>(R.id.evaluationTitle)


        //Initialize database
        database = FirebaseDatabase.getInstance()
        databaseRef = database.reference

        //Initialize webView
        val settings = evaluationLayout.settings
        settings.domStorageEnabled = true
        settings.allowContentAccess = true
        settings.javaScriptEnabled = true

        evaluationLayout.webViewClient = WebViewClient()

        val bundle: Bundle? = intent.extras

        if (bundle != null){
            val titleKey = bundle.getString("keys")
            if (titleKey != ""){
                databaseRef.child("Data/$titleKey").addValueEventListener(
                        object : ValueEventListener {
                            @SuppressLint("SetTextI18n")
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val value = snapshot.getValue(Question::class.java)
                                evaluationTitle.text = value?.quizNumber
                                evaluationLayout.loadUrl("${value?.quiz}")

                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@EvaluationActivity, "Failed to load data from database", Toast.LENGTH_SHORT).show()
                            }
                        })
            }
        }


        backButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}