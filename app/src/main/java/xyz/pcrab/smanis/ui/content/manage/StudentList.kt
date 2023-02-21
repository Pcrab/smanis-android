package xyz.pcrab.smanis.ui.content.manage

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import xyz.pcrab.smanis.data.Students

@Composable
fun StudentList(students: Students) {
    Text(text = "SearchBar")
    LazyColumn {
        items(students) {student ->
            Text(text = student.username)
        }
    }
}