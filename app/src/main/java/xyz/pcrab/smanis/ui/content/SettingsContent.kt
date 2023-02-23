package xyz.pcrab.smanis.ui.content

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import xyz.pcrab.smanis.ui.data.SmanisViewModel
import xyz.pcrab.smanis.utils.state.SmanisContentType

@Composable
fun SettingsContent(
    modifier: Modifier = Modifier,
    viewModel: SmanisViewModel,
    contentType: SmanisContentType
) {
    when (contentType) {
        SmanisContentType.EXTENDED -> {
            SettingsExtendedContent(viewModel)
        }
        SmanisContentType.COMPACT -> {
            SettingsCompactContent(viewModel)
        }
    }
}

@Composable
fun SettingsCompactContent(viewModel: SmanisViewModel) {
    Text(text = "Settings compact")
}

@Composable
fun SettingsExtendedContent(viewModel: SmanisViewModel) {
    Text(text = "Settings extended")
}