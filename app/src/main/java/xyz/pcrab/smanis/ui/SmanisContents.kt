package xyz.pcrab.smanis.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SmanisCompactContent(selectedDestination: String = SmanisDestinations.MANAGE) {
    Text(selectedDestination)
}

@Composable
fun SmanisExtendedContent(selectedDestination: String = SmanisDestinations.MANAGE) {
    Text(selectedDestination)
}