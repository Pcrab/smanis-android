package xyz.pcrab.smanis.ui.content.manage

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.pcrab.smanis.R
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

    Column(modifier = modifier
        .background(MaterialTheme.colorScheme.inverseOnSurface)
        .padding(horizontal = 10.dp)
        .clickable(interactionSource = interactionSource, indication = null) {}
        .focusable(true)) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.clickable {
            onClickBack()
        })
        Text(text = student.username)
        Row {
            Text(text = "Go to test")
            Icon(Icons.Default.ArrowForward,
                contentDescription = "Go to test",
                modifier = Modifier.clickable {
                    onClickExam()
                })
        }
        Text(text = "Previous exam scores")

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp)
        ) {
            student.exams.forEach { exam ->
                ExamCard(exam = exam, onClick = { onClickRecheck(exam) })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun ExamCard(
    exam: Exam, onClick: (exam: Exam) -> Unit = {}
) {
    val fontSize = 20.sp
    val scoreUnit = if (exam.score > 1) {
        stringResource(id = R.string.score_unit_plural)
    } else {
        stringResource(id = R.string.score_unit_single)
    }


    ListItem(modifier = Modifier
        .clickable {
            onClick(exam)
        }
        .height(500.dp), headlineText = {
        Text(text = exam.id, fontSize = fontSize)
    }, supportingText = {
        Text(
            text = "${exam.score} $scoreUnit", fontSize = fontSize * 0.8f
        )
    }, leadingContent = {
//        Spacer(modifier = Modifier.width(15.dp))
    }, trailingContent = {
        Row {
            Text(text = "${exam.takenTime}")
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Go to student info"
            )
        }

    })
}