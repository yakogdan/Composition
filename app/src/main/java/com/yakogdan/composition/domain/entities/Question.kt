package com.yakogdan.composition.domain.entities

data class Question(
    val sum: Int,
    val visibleNumber: Int,
    val options: List<Int>
)
