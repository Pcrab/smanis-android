package xyz.pcrab.smanis.ui.content

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import xyz.pcrab.smanis.ui.data.SmanisViewModel
import xyz.pcrab.smanis.utils.state.SmanisContentType

@Composable
fun ExamContent(viewModel: SmanisViewModel, contentType: SmanisContentType) {
    when (contentType) {
        SmanisContentType.EXTENDED -> {
            ExamExtendedContent(viewModel)
        }
        SmanisContentType.COMPACT -> {
            ExamCompactContent(viewModel)
        }
    }
}

@Composable
fun ExamCompactContent(viewModel: SmanisViewModel) {
    val uiModel = viewModel.uiState.collectAsState().value
    Text(text = "Exam compact")
}

@Composable
fun ExamExtendedContent(viewModel: SmanisViewModel) {
    val uiModel = viewModel.uiState.collectAsState().value
    Text(text = "Exam extended")
}