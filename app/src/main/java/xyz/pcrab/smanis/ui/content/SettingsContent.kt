package xyz.pcrab.smanis.ui.content

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsCompactContent(viewModel: SmanisViewModel) {

    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState().value

    var newRemoteUrl by remember {
        mutableStateOf("")
    }

    Text(text = "Settings compact")
    TextField(
        value = newRemoteUrl,
        onValueChange = {
            newRemoteUrl = it
            viewModel.updateRemoteUrl(context, it)
        },
        placeholder = { Text(text = uiState.remoteUrl) },
        label = { Text(text = "New remote url") }
    )
}

@Composable
fun SettingsExtendedContent(viewModel: SmanisViewModel) {
    Text(text = "Settings extended")
}