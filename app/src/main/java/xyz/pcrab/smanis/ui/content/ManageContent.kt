package xyz.pcrab.smanis.ui.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import xyz.pcrab.smanis.data.Exam
import xyz.pcrab.smanis.data.Student
import xyz.pcrab.smanis.ui.SmanisDestinations
import xyz.pcrab.smanis.ui.content.manage.ExamInfo
import xyz.pcrab.smanis.ui.content.manage.StudentInfo
import xyz.pcrab.smanis.ui.content.manage.StudentList
import xyz.pcrab.smanis.ui.data.SmanisViewModel
import xyz.pcrab.smanis.utils.state.SmanisContentType
import kotlin.system.exitProcess

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
            ManageCompactContent(modifier = modifier.padding(20.dp), viewModel = viewModel)
        }
    }
}

@Composable
fun ManageCompactContent(modifier: Modifier = Modifier, viewModel: SmanisViewModel) {
//    val uiState = viewModel.uiState.collectAsState().value
    var displayStudent by remember {
        mutableStateOf<Student?>(null)
    }
//    val showStudentInfo = remember {
//        MutableTransitionState(false)
//    }
//    if (!showStudentInfo.currentState && !showStudentInfo.targetState) {
//        displayStudent = null
//    }

    var displayExam by remember {
        mutableStateOf<Exam?>(null)
    }
//    val showExamInfo = remember {
//        MutableTransitionState(false)
//    }
//    if (!showExamInfo.currentState && !showExamInfo.targetState) {
//        displayExam = null
//    }

    Box(modifier = modifier) {
        StudentList(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
            onClickStudent = {
                displayStudent = it
//                showStudentInfo.targetState = true
            })
//        AnimatedVisibility(
//            visibleState = showStudentInfo,
//            enter = slideInHorizontally(
//                initialOffsetX = { it }
//            ),
//            exit = slideOutHorizontally(targetOffsetX = { it })
//        ) {
        StudentInfo(
            modifier = Modifier.fillMaxSize(),
            student = displayStudent,
            onClickBack = {
                displayStudent = null
//                    showStudentInfo.targetState = false
            },
            onClickRecheck = {
//                    showExamInfo.targetState = true
                displayExam = it
            },
            onClickExam = {
                viewModel.updateCurrentStudent(displayStudent)
                viewModel.updateCurrentDestination(SmanisDestinations.EXAM)
            })
//        }
//        AnimatedVisibility(visibleState = showExamInfo,
//            enter = slideInHorizontally(
//                initialOffsetX = { it }
//            ),
//            exit = slideOutHorizontally(targetOffsetX = { it })
//        ) {
        ExamInfo(
            modifier = Modifier.fillMaxSize(),
            viewModel = viewModel,
            exam = displayExam,
            studentId = displayStudent?.id,
            onClickBack = {
                displayExam = null
//                    showExamInfo.targetState = false
            }
        )
//        }
    }
    BackHandler {
        if (displayExam != null) {
            displayExam = null
        } else if (displayStudent != null) {
            displayStudent = null
//        if (showExamInfo.currentState) {
//            showExamInfo.targetState = false
//        } else if (showStudentInfo.currentState) {
//            showStudentInfo.targetState = false
        } else {
            // exit app
            exitProcess(0)
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