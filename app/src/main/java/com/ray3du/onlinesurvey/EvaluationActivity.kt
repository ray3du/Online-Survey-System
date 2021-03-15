package com.ray3du.onlinesurvey

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.ViewGroup
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluation)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val evaluationLayout = findViewById<LinearLayout>(R.id.evaluationLayout)
        val title = findViewById<EditText>(R.id.titleAdmin)
        val evaluationTitle = findViewById<TextView>(R.id.evaluationTitle)


        //Initialize database
        database = FirebaseDatabase.getInstance()
        databaseRef = database.reference


        databaseRef.child("Data").addValueEventListener(
            object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.children

                    value.forEach{
                        println("uiz: $it")
                        println("Value ${it.value}")
                        titleKey = it.key.toString()
                        evaluationTitle.setText(titleKey)
                        val value1  = it.getValue(Question::class.java)
                        println("Value quiz: $titleKey")
                        println("Value quiz number: ${value1?.quizNumber}")
                        textView1 = TextView(this@EvaluationActivity)
                        textView1.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        textView1.text = "$quizNum ${value1?.quiz} "
                        textView1.setTextColor(resources.getColor(R.color.black))

                        editText1 = EditText(this@EvaluationActivity)
                        editText1.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                        editText1.hint = "Answer here"
                        editText1.inputType = InputType.TYPE_CLASS_TEXT

                        evaluationLayout?.addView(textView1)
                        evaluationLayout?.addView(editText1)
                        quizNum++
                    }

                    //title.setText(titleKey)

                    button1 = Button(this@EvaluationActivity)
                    button1.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    button1.text = "Submit Response"

                    evaluationLayout?.addView(button1)
                    //titleEvaluation?.text = title?.text

                }

                override fun onCancelled(error: DatabaseError) {
                    //Toast.makeText(this@AdminActivity, "Failed to load data from database", Toast.LENGTH_SHORT).show()
                }
            }
        )


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