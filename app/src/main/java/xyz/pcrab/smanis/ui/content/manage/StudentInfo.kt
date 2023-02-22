package xyz.pcrab.smanis.ui.content.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import xyz.pcrab.smanis.data.Student

@Composable
fun StudentInfo(
    modifier: Modifier = Modifier,
    student: Student? = null,
    onClickBack: () -> Unit = {},
    onClickExam: () -> Unit = {}
) {
    if (student == null) return
    Column(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        Text(modifier = Modifier.clickable {
            onClickBack()
        }, text = student.username)
    }
}