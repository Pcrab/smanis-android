package xyz.pcrab.smaniszk.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import xyz.pcrab.smaniszk.ui.content.ExamContent
import xyz.pcrab.smaniszk.ui.content.ManageContent
import xyz.pcrab.smaniszk.ui.content.SettingsContent
import xyz.pcrab.smaniszk.ui.data.SmanisViewModel
import xyz.pcrab.smaniszk.utils.state.SmanisContentType


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
