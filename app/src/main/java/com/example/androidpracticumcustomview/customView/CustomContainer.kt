package com.example.androidpracticumcustomview.customView

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.example.androidpracticumcustomview.R
import androidx.core.view.isEmpty

@Suppress("TooGenericExceptionCaught")
class CustomContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    val alphaAnimationDuration: Long = 2000L,
    val translationAnimationDuration: Long = 5000L,
) : FrameLayout(context, attrs) {

    private val gap by lazy { (MIDDLE_GAP * resources.displayMetrics.density).toInt() }
    private val interpolator = DecelerateInterpolator()

    init {
        setWillNotDraw(false)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        val childWidthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST)
        val childHeightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST)

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            try {
                child.measure(childWidthSpec, childHeightSpec)
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка в onMeasure, дочерний элемент $i", e)
                child.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.EXACTLY)
                )
            }
        }

        setMeasuredDimension(width, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (isEmpty()) return
        try {
            val centerY = (bottom - top) / 2

            getChildAt(0).apply {
                val childLeft = (right - left - this.measuredWidth) / 2
                layout(
                    childLeft,
                    centerY - gap / 2 - this.measuredHeight,
                    childLeft + this.measuredWidth,
                    centerY - gap / 2
                )
            }

            if (childCount < 2) return
            getChildAt(1).apply {
                val childLeft = (right - left - this.measuredWidth) / 2
                layout(
                    childLeft,
                    centerY + gap / 2,
                    childLeft + this.measuredWidth,
                    centerY + gap / 2 + this.measuredHeight
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка при выполнении onLayout", e)
        }
    }

    /**
     * Добавляет дочерний view. Максимум 2 дочерних view
     * @throws IllegalStateException если больше 2 view
     */
    override fun addView(child: View) {
        if (childCount >= 2) throw IllegalStateException(resources.getString(R.string.ill_ex))
        try {
            super.addView(child)
            child.alpha = 0f
            child.post {
                try {
                    startAnimations(child)
                } catch (e: Exception) {
                    Log.e(TAG, "Ошибка в анимации", e)
                    child.alpha = 1f
                    child.translationY = 0f
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Ошибка в добавлении дочернего view", e)
            throw e
        }
    }

    private fun CustomContainer.startAnimations(child: View) {
        val alphaAnimation = child.animate()
            .alpha(1f)
            .setDuration(alphaAnimationDuration)
            .setInterpolator(interpolator)


        val translationYAnimation = when (indexOfChild(child) == 0) {
            true -> {
                val currentTop = child.top
                val targetTranslationY = -currentTop.toFloat()
                child.animate()
                    .translationY(targetTranslationY)
                    .setDuration(translationAnimationDuration)
                    .setInterpolator(interpolator)
            }

            false -> {
                val targetTranslationY = (height - child.bottom).toFloat()
                child.animate()
                    .translationY(targetTranslationY)
                    .setDuration(translationAnimationDuration)
                    .setInterpolator(interpolator)
            }
        }
        alphaAnimation.start()
        translationYAnimation.start()
    }

    private companion object {
        const val MIDDLE_GAP = 10
        const val TAG = "CustomContainerError"
    }
}
