package xyz.pcrab.smanis.ui.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 200.dp)
    ) {
        Text(
            text = "当前远程地址: ${uiState.remoteUrl}",
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
        )
        OutlinedTextField(
            value = newRemoteUrl,
            onValueChange = {
                newRemoteUrl = it
//            viewModel.updateRemoteUrl(context, it)
            },
            label = { Text(text = "新远程地址") },
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = 20.dp)
        )
        Button(
            onClick = {
                if (!newRemoteUrl.isEmpty()) {
                    viewModel.updateRemoteUrl(context, newRemoteUrl)
                }
            }, modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
        ) {
            Text(text = "确认修改")
        }
    }
}

@Composable
fun SettingsExtendedContent(viewModel: SmanisViewModel) {
    Text(text = "Settings extended")
}