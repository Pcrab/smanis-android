package xyz.pcrab.smanis.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import xyz.pcrab.smanis.ui.content.ExamContent
import xyz.pcrab.smanis.ui.content.ManageContent
import xyz.pcrab.smanis.ui.content.SettingsContent
import xyz.pcrab.smanis.ui.data.SmanisViewModel
import xyz.pcrab.smanis.utils.state.SmanisContentType


@Composable
fun SmanisAppContent(
    modifier: Modifier = Modifier,
    contentType: SmanisContentType,
    viewModel: SmanisViewModel,
) {
    val uiState = viewModel.uiState.collectAsState().value
    when (uiState.currentDestination) {
        SmanisDestinations.EXAM -> {
            ExamContent(modifier = modifier, viewModel = viewModel, contentType = contentType)
        }
        SmanisDestinations.MANAGE -> {
            ManageContent(modifier = modifier, viewModel = viewModel, contentType = contentType)
        }
        SmanisDestinations.SETTINGS -> {
            SettingsContent(modifier = modifier, viewModel = viewModel, contentType = contentType)
        }
        else -> {
            Text(
                modifier = modifier,
                text = "Unknown selected destination: ${uiState.currentDestination}"
            )
        }
    }
}
