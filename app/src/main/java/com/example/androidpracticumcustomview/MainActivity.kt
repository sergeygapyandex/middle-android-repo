package com.example.androidpracticumcustomview

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.xmlActivity).setOnClickListener {
            showViewCountDialog()
        }
        findViewById<Button>(R.id.composeActivity).setOnClickListener {
            startActivity(Intent(this, ComposeScreen::class.java))
        }
    }

    private fun showViewCountDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.alert_dia_title)
            .setMessage(R.string.alert_dia_mess)
            .setPositiveButton(R.string.alert_dia_pos_btn) { _, _ ->
                startActivity(XmlActivity.newIntent(this))
            }
            .setNegativeButton(R.string.alert_dia_neg_btn) { _, _ ->
                startActivity(XmlActivity.newIntent(this, false))
            }
            .setCancelable(true)
            .show()
    }
}
