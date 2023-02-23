package xyz.pcrab.smanis.ui.content

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import xyz.pcrab.smanis.data.Exam
import xyz.pcrab.smanis.data.Student
import xyz.pcrab.smanis.ui.SmanisDestinations
import xyz.pcrab.smanis.ui.content.manage.ExamInfo
import xyz.pcrab.smanis.ui.content.manage.StudentInfo
import xyz.pcrab.smanis.ui.content.manage.StudentList
import xyz.pcrab.smanis.ui.data.SmanisViewModel
import xyz.pcrab.smanis.utils.state.SmanisContentType

@Composable
fun ManageContent(
    modifier: Modifier = Modifier,
    viewModel: SmanisViewModel,
    contentType: SmanisContentType
) {
    when (contentType) {
        SmanisContentType.EXTENDED -> {
            ManageExtendedContent(modifier = modifier, viewModel = viewModel)
        }
        SmanisContentType.COMPACT -> {
            ManageCompactContent(modifier = modifier, viewModel = viewModel)
        }
    }
}

@Composable
fun ManageCompactContent(modifier: Modifier = Modifier, viewModel: SmanisViewModel) {
    val activity = LocalContext.current as Activity
//    val uiState = viewModel.uiState.collectAsState().value
    var displayStudent by remember {
        mutableStateOf<Student?>(null)
    }
    var showStudentInfo by remember {
        mutableStateOf(false)
    }
    var displayExam by remember {
        mutableStateOf<Exam?>(null)
    }
    var showExamInfo by remember {
        mutableStateOf(false)
    }
    Box(modifier = modifier) {
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
                onClickRecheck = {
                    displayExam = it
                    showExamInfo = true
                },
                onClickExam = {
                    viewModel.updateCurrentStudent(displayStudent)
                    viewModel.updateCurrentDestination(SmanisDestinations.EXAM)
                })
        }
        AnimatedVisibility(visible = showExamInfo,
            enter = slideInHorizontally(
                initialOffsetX = { it }
            ),
            exit = slideOutHorizontally(targetOffsetX = { it })
        ) {
            ExamInfo(
                modifier = Modifier.fillMaxSize(),
                exam = displayExam,
                onClickBack = {
                    showExamInfo = false
                }
            )
        }
    }
    BackHandler {
        if (showExamInfo) {
            showExamInfo = false
        } else if (showStudentInfo) {
            showStudentInfo = false
        } else {
            // exit app
            activity.finish()
        }
    }
}

@Composable
@Preview
fun ManageCompactContentPreview() {
    ManageCompactContent(viewModel = SmanisViewModel())
}

@Composable
fun ManageExtendedContent(modifier: Modifier = Modifier, viewModel: SmanisViewModel) {
//    val uiState = viewModel.uiState.collectAsState().value
    StudentList(modifier = modifier, viewModel = viewModel)
    StudentInfo()
}

@Composable
@Preview
fun ManageExtendedContentPreview() {
    ManageExtendedContent(viewModel = SmanisViewModel())
}