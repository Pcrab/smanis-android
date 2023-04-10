package xyz.pcrab.smaniszk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.window.layout.FoldingFeature
import androidx.window.layout.WindowInfoTracker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import xyz.pcrab.smaniszk.ui.SmanisApp
import xyz.pcrab.smaniszk.ui.data.SmanisViewModel
import xyz.pcrab.smaniszk.ui.theme.SmanisTheme
import xyz.pcrab.smaniszk.utils.state.DevicePosture
import xyz.pcrab.smaniszk.utils.state.isBookPosture
import xyz.pcrab.smaniszk.utils.state.isSeparating

class MainActivity : ComponentActivity() {

    private val viewModel: SmanisViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initRemoteUrl(applicationContext)
        viewModel.initResolution(applicationContext)
        println(viewModel.uiState.value.remoteUrl)

        val devicePostureFlow = WindowInfoTracker.getOrCreate(this).windowLayoutInfo(this)
            .flowWithLifecycle(this.lifecycle)
            .map { layoutInfo ->
                val foldingFeature =
                    layoutInfo.displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
                when {
                    isBookPosture(foldingFeature) -> DevicePosture.BookPosture(foldingFeature.bounds)
                    isSeparating(foldingFeature) -> DevicePosture.Separating(
                        foldingFeature.bounds,
                        foldingFeature.orientation
                    )
                    else -> DevicePosture.NormalPosture
                }
            }
            .stateIn(
                scope = lifecycleScope,
                started = SharingStarted.Eagerly,
                initialValue = DevicePosture.NormalPosture,
            )

        setContent {
            SmanisTheme {
                val windowSize = calculateWindowSizeClass(this)
                val devicePosture = devicePostureFlow.collectAsState().value
                SmanisApp(windowSize.widthSizeClass, devicePosture, viewModel)
            }
        }
    }
}

@Preview(showBackground = true, name = "Compact")
@Composable
fun SmanisAppCompactPreview() {
    SmanisTheme {
        SmanisApp(
            windowSize = WindowWidthSizeClass.Compact,
            foldingDevicePosture = DevicePosture.NormalPosture,
            viewModel = SmanisViewModel()
        )
    }
}

@Preview(showBackground = true, widthDp = 700, name = "Medium")
@Composable
fun SmanisAppMediumPreview() {
    SmanisTheme {
        SmanisApp(
            windowSize = WindowWidthSizeClass.Medium,
            foldingDevicePosture = DevicePosture.NormalPosture,
            viewModel = SmanisViewModel()
        )
    }
}

@Preview(showBackground = true, widthDp = 1000, name = "Expanded")
@Composable
fun SmanisAppExpandedPreview() {
    SmanisTheme {
        SmanisApp(
            windowSize = WindowWidthSizeClass.Expanded,
            foldingDevicePosture = DevicePosture.NormalPosture,
            viewModel = SmanisViewModel()
        )
    }
}