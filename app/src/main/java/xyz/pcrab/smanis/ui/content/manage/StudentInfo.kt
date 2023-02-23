package xyz.pcrab.smanis.ui.content.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import xyz.pcrab.smanis.data.Exam
import xyz.pcrab.smanis.data.Student

@Composable
fun StudentInfo(
    modifier: Modifier = Modifier,
    student: Student? = null,
    onClickBack: () -> Unit = {},
    onClickRecheck: (Exam?) -> Unit = {},
    onClickExam: () -> Unit = {}
) {
    if (student == null) return
    val interactionSource = MutableInteractionSource()

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .clickable(interactionSource = interactionSource, indication = null) {}
            .focusable(true)
    ) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.clickable {
            onClickBack()
        })
        Text(text = student.username)
        Row {
            Text(text = "Go to test")
            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "Go to test",
                modifier = Modifier.clickable {
                    onClickExam()
                })
        }
        Text(text = "Previous exam scores")
        Column {
            student.exams.forEach { exam ->
                Row {
                    Text(text = exam.video)
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Recheck",
                        modifier = Modifier.clickable {
                            onClickRecheck(exam)
                        })
                }
            }
        }
    }
}