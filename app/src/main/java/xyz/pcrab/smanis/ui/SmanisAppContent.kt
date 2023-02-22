package xyz.pcrab.smanis.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import xyz.pcrab.smanis.ui.content.*
import xyz.pcrab.smanis.ui.data.SmanisViewModel
import xyz.pcrab.smanis.utils.state.SmanisContentType


@Composable
fun SmanisAppContent(
    contentType: SmanisContentType,
    viewModel: SmanisViewModel,
) {
    val uiState = viewModel.uiState.collectAsState().value
    when (uiState.currentDestination) {
        SmanisDestinations.EXAM -> {
            ExamContent(viewModel, contentType)
        }
        SmanisDestinations.MANAGE -> {
            ManageContent(viewModel, contentType)
        }
        SmanisDestinations.SETTINGS -> {
            SettingsContent(viewModel, contentType)
        }
        else -> {
            Text(text = "Unknown selected destination: ${uiState.currentDestination}")
        }
    }
}
