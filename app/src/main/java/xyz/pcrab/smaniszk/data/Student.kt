package xyz.pcrab.smaniszk.data

data class Student(
    val id: String,
    val username: String,
    var exams: Map<String, Exam> = emptyMap(),
)

typealias Students = Map<String, Student>