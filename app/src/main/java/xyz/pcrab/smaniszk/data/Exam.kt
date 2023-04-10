package xyz.pcrab.smaniszk.data

data class Exam(
    val id: String,
    val score: Int,
    val points: List<Pair<String, Int>>,
    val takenTime: String
)