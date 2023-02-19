package xyz.pcrab.smanis.data

import kotlinx.datetime.Instant

data class Exam (
    val video: String,
    val score: Int,
    val points: Map<String, Int>,
    val takenTime: Instant
)