package xyz.pcrab.smanis.data

import kotlinx.datetime.Instant

data class Exam(
    val id: String,
    val video: String,
    val score: Int,
    val points: Map<String, Int>,
    val takenTime: Instant
)