package xyz.pcrab.smaniszk.ui.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import xyz.pcrab.smaniszk.ui.data.SmanisViewModel
import xyz.pcrab.smaniszk.utils.state.SmanisContentType

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
            .padding(top = 40.dp, start = 10.dp)
    ) {
        Text(text = "服务器设置", modifier = Modifier.padding(bottom = 10.dp), fontSize = 22.sp)
        Text(
            text = "当前服务器地址: ${uiState.remoteUrl}",
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
            label = { Text(text = "新服务器地址") },
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = 20.dp)
        )
        Button(
            onClick = {
                if (newRemoteUrl.isNotEmpty()) {
                    viewModel.updateRemoteUrl(context, newRemoteUrl)
                }
            }, modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(bottom = 10.dp)
        ) {
            Text(text = "确认修改")
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Color.Black)
        )
        Text(text = "相机分辨率设置", modifier = Modifier.padding(vertical = 10.dp), fontSize = 22.sp)
        Text(
            text = "当前分辨率: ${uiState.resolution}",
            modifier = Modifier
                .padding(bottom = 10.dp, start = 10.dp)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = uiState.resolution == "620x480", onClick = {
                viewModel.updateResolution(context, "620x480")
            },
                modifier = Modifier.semantics { contentDescription = "620x480" }
            )
            Text(text = "620x480", modifier = Modifier.padding(start = 10.dp))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = uiState.resolution == "1280x720", onClick = {
                viewModel.updateResolution(context, "1280x720")
            },
                modifier = Modifier.semantics { contentDescription = "1280x720" }
            )
            Text(text = "1280x720", modifier = Modifier.padding(start = 10.dp))
        }
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(color = Color.Black)
        )
    }
}

@Composable
fun SettingsExtendedContent(viewModel: SmanisViewModel) {
    Text(text = "Settings extended")
}