package xyz.pcrab.smanis.ui

import androidx.compose.runtime.Composable
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

