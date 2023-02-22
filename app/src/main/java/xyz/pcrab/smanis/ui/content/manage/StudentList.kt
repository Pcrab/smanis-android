package xyz.pcrab.smanis.ui.content.manage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.pcrab.smanis.R
import xyz.pcrab.smanis.data.Student
import xyz.pcrab.smanis.ui.data.SmanisViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentList(
    modifier: Modifier = Modifier,
    viewModel: SmanisViewModel = SmanisViewModel(),
    onClickStudent: (student: Student) -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsState().value
    var searchText by remember {
        mutableStateOf("")
    }
    var displayStudents by remember {
        mutableStateOf(uiState.allStudents)
    }
    val focusManager = LocalFocusManager.current
    val interactionSource = MutableInteractionSource()
    Column(
        modifier = modifier.clickable(interactionSource = interactionSource, indication = null) {
            focusManager.clearFocus()
        },
    ) {
        val searchFontSize = 20.sp
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
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
                    student.username.contains(it, true) || student.id.contains(it, true)
                }
            },
        )
        LazyColumn(
            modifier = Modifier.padding(
                PaddingValues(
                    horizontal = 20.dp,
                    vertical = 10.dp
                )
            )
        ) {
            items(displayStudents) { student ->
                StudentCard(interactionSource = interactionSource, student = student) {
                    onClickStudent(it)
                }
            }
        }
    }
}

@Composable
fun StudentCard(
    interactionSource: MutableInteractionSource,
    student: Student,
    onClick: (student: Student) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    Text(modifier = Modifier.clickable {
        onClick(student)
    }, text = student.username)
}