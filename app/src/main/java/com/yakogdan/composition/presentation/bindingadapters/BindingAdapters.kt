package com.yakogdan.composition.presentation.bindingadapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.yakogdan.composition.R

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