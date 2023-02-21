package xyz.pcrab.smanis.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import xyz.pcrab.smanis.ui.content.*
import xyz.pcrab.smanis.utils.state.SmanisContentType


@Composable
fun SmanisAppContent(
    contentType: SmanisContentType,
    selectedDestination: String = SmanisDestinations.MANAGE,
) {
    if (contentType == SmanisContentType.EXTENDED) {
        SmanisExtendedContent(selectedDestination = selectedDestination)
    } else {
        SmanisCompactContent(selectedDestination = selectedDestination)
    }
}

@Composable
fun SmanisCompactContent(selectedDestination: String = SmanisDestinations.MANAGE) {
    when (selectedDestination) {
        SmanisDestinations.EXAM -> {
            ExamCompactContent()
        }
        SmanisDestinations.MANAGE -> {
            ManageCompactContent()
        }
        SmanisDestinations.SETTINGS -> {
            SettingsCompactContent()
        }
        else -> {
            Text(text = "Unknown selected destination: $selectedDestination")
        }
    }
}

@Composable
fun SmanisExtendedContent(selectedDestination: String = SmanisDestinations.MANAGE) {
    when (selectedDestination) {
        SmanisDestinations.EXAM -> {
            ExamExtendedContent()
        }
        SmanisDestinations.MANAGE -> {
            ManageExtendedContent()
        }
        SmanisDestinations.SETTINGS -> {
            SettingsExtendedContent()
        }
        else -> {
            Text(text = "Unknown selected destination: $selectedDestination")
        }
    }
}