package com.ray3du.onlinesurvey

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
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

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        //Handle views
        val questionNumber = findViewById<EditText>(R.id.questionNumber)
        val viewLayout = findViewById<LinearLayout>(R.id.viewLayout)
        val generateButton = findViewById<Button>(R.id.generateView)
        val evaluationLayout = findViewById<LinearLayout>(R.id.evaluationLayout)
        val title = findViewById<EditText>(R.id.titleAdmin)
        val titleEvaluation = findViewById<TextView>(R.id.evaluationTitle)
        val logout = findViewById<ImageView>(R.id.logout)


        list = mutableListOf()
        mAuth = FirebaseAuth.getInstance()

        //Initialize firebase
        database = FirebaseDatabase.getInstance()
        databaseReference = database.reference

        generateButton.setOnClickListener {
            generateButton.isEnabled = false
           if(questionNumber.text.toString() != ""){
                while (count < questionNumber.text.toString().toInt()){
                textView = TextView(this)
                textView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                textView.text = "Quiz $count"
                editText = EditText(this)
                editText.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                editText.hint = "Write question $count"
                editText.inputType = InputType.TYPE_CLASS_TEXT
                editText.tag ="quiz$count"
                editText.id = count

                //Add textView and editText to layout
                viewLayout?.addView(textView)
                viewLayout?.addView(editText)
                list.add(editText)

                count++
            }
           }else{
               Toast.makeText(this, "Question number cannot be empty", Toast.LENGTH_SHORT).show()
           }

            button = Button(this)
            button.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            button.text = "Process Form"
            viewLayout?.addView(button)
            generateButton.isEnabled = true

            button.setOnClickListener {
                Toast.makeText(this, "Sent to database", Toast.LENGTH_SHORT).show()
                println("click.......")
                title2 = title.text.toString()
                for (et in list){
                    textView1 = TextView(this)
                    textView1.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    textView1.text = "$quizId. ${et.text}"
                    textView1.setTextColor(resources.getColor(R.color.black))

                    writeNewUser(quizId, et.text.toString())

                    editText1 = EditText(this)
                    editText1.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    editText1.hint = "Answer here"
                    editText1.inputType = InputType.TYPE_CLASS_TEXT

                    evaluationLayout?.addView(textView1)
                    evaluationLayout?.addView(editText1)

                    quizId++
                }

                Toast.makeText(this, "Sent to database", Toast.LENGTH_SHORT).show()
                button1 = Button(this)
                button1.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                button1.text = "Submit Response"

                evaluationLayout?.addView(button1)
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

    private fun writeNewUser(quizId: Int, text: String) {
        val user = Question(quizId, text)
        val uuid = UUID.randomUUID()
        databaseReference.child("Data/$title2").setValue(user)
    }

}