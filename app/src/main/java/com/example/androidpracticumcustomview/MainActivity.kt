package com.example.androidpracticumcustomview

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.xmlActivity).setOnClickListener {
            startActivity(Intent(this, XmlActivity::class.java))
        }
        findViewById<Button>(R.id.composeActivity).setOnClickListener {
            startActivity(Intent(this, ComposeScreen::class.java))
        }
    }
}
