package xyz.pcrab.smanis.data

data class Exam(
    val id: String,
    val score: Int,
    val points: List<Pair<String, Int>>,
    val takenTime: String
)