package xyz.pcrab.smaniszk.ui.content

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.pcrab.smaniszk.ui.SmanisDestinations
import xyz.pcrab.smaniszk.ui.content.manage.ExamInfo
import xyz.pcrab.smaniszk.ui.content.manage.StudentInfo
import xyz.pcrab.smaniszk.ui.content.manage.StudentList
import xyz.pcrab.smaniszk.ui.data.SmanisViewModel
import xyz.pcrab.smaniszk.utils.state.SmanisContentType
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
            ManageCompactContent(modifier = modifier)
        }
    }
}

@Composable
fun ManageCompactContent(modifier: Modifier = Modifier) {
    val viewModel: SmanisViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var displayStudent by remember {
        mutableStateOf<String?>(null)
    }
//    val showStudentInfo = remember {
//        MutableTransitionState(false)
//    }
//    if (!showStudentInfo.currentState && !showStudentInfo.targetState) {
//        displayStudent = null
//    }

    var displayExam by remember {
        mutableStateOf<String?>(null)
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
            onClickStudent = {
                displayStudent = it.id
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
            studentId = displayStudent,
            onClickBack = {
                displayStudent = null
//                    showStudentInfo.targetState = false
            },
            onClickRecheck = {
//                    showExamInfo.targetState = true
                displayExam = it?.id
            },
            onClickExam = {
                viewModel.updateCurrentStudent(uiState.allStudents[displayStudent])
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
            examId = displayExam,
            studentId = displayStudent,
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
    ManageCompactContent()
}

@Composable
fun ManageExtendedContent(modifier: Modifier = Modifier, viewModel: SmanisViewModel) {
    StudentList(modifier = modifier)
    StudentInfo()
}

@Composable
@Preview
fun ManageExtendedContentPreview() {
    ManageExtendedContent(viewModel = SmanisViewModel())
}