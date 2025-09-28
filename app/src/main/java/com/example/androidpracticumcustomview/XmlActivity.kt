package com.example.androidpracticumcustomview

import android.content.Context
import android.content.Intent
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
        val isOneChildView = intent.getBooleanExtra(EXTRA_CHILD_VIEW_STATE, true)
        startXmlPracticum(isOneChildView)
    }

    private fun startXmlPracticum(isOneChildView: Boolean) {
        val customContainer = CustomContainer(this)
        setContentView(customContainer)
        customContainer.setOnClickListener { finish() }
        when (isOneChildView) {
            true -> customContainer.addView(TextView(this).apply { custom() })
            false -> {
                customContainer.addView(TextView(this).apply { custom() })
                lifecycleScope.launch {
                    delay(DELAY_MILLIS)
                    customContainer.addView(TextView(this@XmlActivity).apply { custom(false) })
                }
            }
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

    companion object {
        private const val DELAY_MILLIS = 2000L
        private const val TEXT_SIZE = 22f
        private const val PADDING = 16
        private const val EXTRA_CHILD_VIEW_STATE = "EXTRA_CHILD_VIEW_STATE"

        fun newIntent(context: Context, isOneChildView: Boolean = true) =
            Intent(context, XmlActivity::class.java).apply {
                putExtra(EXTRA_CHILD_VIEW_STATE, isOneChildView)
            }
    }
}
