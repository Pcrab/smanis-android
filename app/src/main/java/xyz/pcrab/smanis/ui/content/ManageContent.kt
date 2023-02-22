package xyz.pcrab.smanis.ui.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import xyz.pcrab.smanis.data.Student
import xyz.pcrab.smanis.ui.content.manage.StudentInfo
import xyz.pcrab.smanis.ui.content.manage.StudentList
import xyz.pcrab.smanis.ui.data.SmanisViewModel
import xyz.pcrab.smanis.utils.state.SmanisContentType

@Composable
fun ManageContent(viewModel: SmanisViewModel, contentType: SmanisContentType) {
    when (contentType) {
        SmanisContentType.EXTENDED -> {
            ManageExtendedContent(viewModel)
        }
        SmanisContentType.COMPACT -> {
            ManageCompactContent(viewModel)
        }
    }
}

@Composable
fun ManageCompactContent(viewModel: SmanisViewModel) {
    val uiState = viewModel.uiState.collectAsState().value
    var displayStudent by remember {
        mutableStateOf<Student?>(null)
    }
    var showStudentInfo by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        StudentList(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
            onClickStudent = {
                displayStudent = it
                showStudentInfo = true
            })
        AnimatedVisibility(
            visible = showStudentInfo,
            enter = slideInHorizontally(
                initialOffsetX = { it }
            ),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            StudentInfo(
                modifier = Modifier.fillMaxSize(),
                student = displayStudent,
                onClickBack = {
                    showStudentInfo = false
                },
                onClickExam = {
                    viewModel.updateCurrentStudent(displayStudent)
                })
        }
    }
}

@Composable
@Preview
fun ManageCompactContentPreview() {
    ManageCompactContent(viewModel = SmanisViewModel())
}

@Composable
fun ManageExtendedContent(viewModel: SmanisViewModel) {
    val uiState = viewModel.uiState.collectAsState().value
    StudentList(viewModel = viewModel)
    StudentInfo()
}

@Composable
@Preview
fun ManageExtendedContentPreview() {
    ManageExtendedContent(viewModel = SmanisViewModel())
}