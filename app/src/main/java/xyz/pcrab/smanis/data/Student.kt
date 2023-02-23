package xyz.pcrab.smanis.data

data class Student(
    val id: String,
    val username: String,
    val exams: List<Exam> = emptyList(),
)

typealias Students = List<Student>