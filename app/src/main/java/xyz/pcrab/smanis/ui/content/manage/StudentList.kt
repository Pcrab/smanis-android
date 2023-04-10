package xyz.pcrab.smanis.ui.content.manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import xyz.pcrab.smanis.R
import xyz.pcrab.smanis.data.Student
import xyz.pcrab.smanis.ui.data.SmanisViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentList(
    modifier: Modifier = Modifier,
    onClickStudent: (student: Student) -> Unit = {}
) {
    val viewModel: SmanisViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var searchText by remember {
        mutableStateOf("")
    }
    var displayStudents by remember {
        mutableStateOf(uiState.allStudents)
    }
    val focusManager = LocalFocusManager.current
    val interactionSource = MutableInteractionSource()
    Column(
        modifier = modifier
            .clickable(interactionSource = interactionSource, indication = null) {
                focusManager.clearFocus()
            }
            .padding(20.dp),
    ) {
        val searchFontSize = 20.sp
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            textStyle = TextStyle.Default.copy(fontSize = searchFontSize),
            value = searchText,
            label = {
                Text(
                    text = stringResource(id = R.string.manage_search_label),
                    fontSize = searchFontSize
                )
            },
            onValueChange = {
                searchText = it
                displayStudents = uiState.allStudents.filter { student ->
                    student.value.username.contains(it, true) || student.value.id.contains(it, true)
                }
            },
        )
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            displayStudents.forEach { student ->
                StudentCard(focusManager = focusManager, student = student.value) {
                    onClickStudent(it)
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun StudentCard(
    focusManager: FocusManager, student: Student, onClick: (student: Student) -> Unit = {}
) {
    val fontSize = 20.sp

    ListItem(modifier = Modifier.clickable {
        focusManager.clearFocus()
        onClick(student)
    }, headlineText = {
        Text(text = student.username, fontSize = fontSize)
    }, supportingText = {
        Text(text = student.id, fontSize = fontSize * 0.8f)
    }, leadingContent = {
        Icon(
            modifier = Modifier.size(40.dp),
            imageVector = Icons.Default.Person,
            contentDescription = "Student icon"
        )
    }, trailingContent = {
        Icon(
            imageVector = Icons.Default.ArrowForward, contentDescription = "Go to student info"
        )
    })
}