package com.example.androidpracticumcustomview

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.view.setPadding
import androidx.lifecycle.lifecycleScope
import com.example.androidpracticumcustomview.customView.CustomContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class XmlActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startXmlPracticum()
    }

    private fun startXmlPracticum() {
        val customContainer = CustomContainer(this)
        setContentView(customContainer)
        customContainer.setOnClickListener { finish() }
        customContainer.addView(TextView(this).apply { custom() })
        lifecycleScope.launch {
            delay(DELAY_MILLIS)
            customContainer.addView(TextView(this@XmlActivity).apply { custom(false) })
        }
    }

    private fun TextView.custom(isTop: Boolean = true) {
        when (isTop) {
            true -> {
                text = getString(R.string.text_for_top_tv)
                setBackgroundColor(Color.BLUE)
            }

            false -> {
                text = getString(R.string.text_for_bottom_tv)
                setBackgroundColor(Color.RED)
            }
        }
        textSize = TEXT_SIZE
        setPadding((PADDING * resources.displayMetrics.density).toInt())
        setTextColor(Color.WHITE)
    }

    private companion object {
        const val DELAY_MILLIS = 2000L
        const val TEXT_SIZE = 22f
        const val PADDING = 16
    }
}
