package com.yakogdan.composition.presentation.bindingadapters

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.yakogdan.composition.R

interface OnOptionClickListener {
    fun onOptionClick(option: Int)

}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_score),
        count
    )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, scoreAnswers: Int) {
    textView.text =
        String.format(
            textView.context.getString(R.string.score_answers),
            scoreAnswers
        )
}

@BindingAdapter("requirePercentage")
fun bindRequiredPercentage(textView: TextView, requirePercentage: Int) {
    textView.text =
        String.format(
            textView.context.getString(R.string.required_percentage),
            requirePercentage
        )
}

@BindingAdapter("countOfRightAnswers", "countOfQuestions")
fun bindScorePercentage(textView: TextView, countOfRightAnswers: Int, countOfQuestions: Int) {
    textView.text =
        String.format(
            textView.context.getString(R.string.score_percentage),
            getPercentOfRightAnswers(
                countOfRightAnswers,
                countOfQuestions
            )
        )
}

private fun getPercentOfRightAnswers(countOfRightAnswers: Int, countOfQuestions: Int): Int =
    if (countOfQuestions == 0) {
        0
    } else {
        (countOfRightAnswers / countOfQuestions.toDouble() * 100).toInt()
    }

@BindingAdapter("winner")
fun bindImageResource(imageView: ImageView, winner: Boolean) {
    imageView.setImageResource(getSmileResId(winner))
}

private fun getSmileResId(winner: Boolean): Int {
    return if (winner) R.drawable.ic_smile else R.drawable.ic_sad
}

@BindingAdapter("enoughCount")
fun bindEnoughCount(textView: TextView, enough: Boolean) {
    textView.setTextColor(getColorByState(textView.context, enough))
}

@BindingAdapter("enoughPercent")
fun bindEnoughPercent(progressBar: ProgressBar, enough: Boolean) {
    progressBar.progressTintList =
        ColorStateList.valueOf(getColorByState(progressBar.context, enough))
}

private fun getColorByState(context: Context, goodState: Boolean): Int {
    val colorResId = if (goodState) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(context, colorResId)
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionClickListener) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}
