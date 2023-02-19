package xyz.pcrab.smanis.data

data class Student(
    val id: String,
    val username: String,
)

typealias Students = Array<Student>