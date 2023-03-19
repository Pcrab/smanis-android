package xyz.pcrab.smanis.ui.content

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.camera.view.PreviewView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.util.Consumer
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import xyz.pcrab.smanis.R
import xyz.pcrab.smanis.ui.data.SmanisViewModel
import xyz.pcrab.smanis.utils.state.SmanisContentType
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun ExamContent(
    modifier: Modifier = Modifier,
    viewModel: SmanisViewModel,
    contentType: SmanisContentType
) {
    when (contentType) {
        SmanisContentType.EXTENDED -> {
            ExamExtendedContent(viewModel)
        }
        SmanisContentType.COMPACT -> {
            ExamCompactContent(viewModel)
        }
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener(
            {
                continuation.resume(future.get())
            },
            mainExecutor
        )
    }
}

suspend fun Context.createVideoCaptureUseCase(
    lifecycleOwner: LifecycleOwner,
    cameraSelector: CameraSelector,
    previewView: PreviewView
): VideoCapture<Recorder> {
    val preview = Preview.Builder()
        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
        .build()
        .apply { setSurfaceProvider(previewView.surfaceProvider) }

    val qualitySelector = QualitySelector.from(
        Quality.FHD,
        FallbackStrategy.lowerQualityOrHigherThan(Quality.FHD)
    )
    val recorder = Recorder.Builder()
        .setExecutor(mainExecutor)
        .setQualitySelector(qualitySelector)
        .build()
    val videoCapture = VideoCapture.withOutput(recorder)

    val cameraProvider = getCameraProvider()
    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(
        lifecycleOwner,
        cameraSelector,
        preview,
        videoCapture
    )

    return videoCapture
}

@SuppressLint("MissingPermission")
fun startRecordingVideo(
    context: Context,
    filenameFormat: String,
    videoCapture: VideoCapture<Recorder>,
    outputDirectory: File,
    executor: Executor,
    audioEnabled: Boolean,
    consumer: Consumer<VideoRecordEvent>
): Recording {
    val videoFile = File(
        outputDirectory,
        SimpleDateFormat(filenameFormat, Locale.US).format(System.currentTimeMillis()) + ".mp4"
    )

    val outputOptions = FileOutputOptions.Builder(videoFile).build()

    return videoCapture.output
        .prepareRecording(context, outputOptions)
        .apply { if (audioEnabled) withAudioEnabled() }
        .start(executor, consumer)
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ExamCompactContent(viewModel: SmanisViewModel) {
    val uiModel = viewModel.uiState.collectAsState().value
    var dis = remember { "" }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var recording: Recording? = remember { null }
    val previewView: PreviewView = remember { PreviewView(context) }
    val videoCapture: MutableState<VideoCapture<Recorder>?> = remember { mutableStateOf(null) }
    val recordingStarted: MutableState<Boolean> = remember { mutableStateOf(false) }

    val audioEnabled: MutableState<Boolean> = remember { mutableStateOf(false) }
    val cameraSelector: MutableState<CameraSelector> = remember {
        mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA)
    }

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }

    LaunchedEffect(previewView) {
        videoCapture.value = context.createVideoCaptureUseCase(
            lifecycleOwner = lifecycleOwner,
            cameraSelector = cameraSelector.value,
            previewView = previewView
        )
    }

    PermissionsRequired(
        multiplePermissionsState = permissionState,
        permissionsNotGrantedContent = { /* ... */ },
        permissionsNotAvailableContent = { /* ... */ }
    ) {

        Column {
            Spacer(modifier = Modifier.height(40.dp))
            AndroidView(
                factory = { previewView },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
            ) {
                Text(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                        .clickable {
                            if (!recordingStarted.value) {
                                videoCapture.value?.let { videoCapture ->
                                    recordingStarted.value = true
                                    val mediaDir = context.externalCacheDirs
                                        .firstOrNull()
                                        ?.let {
                                            File(
                                                it,
                                                context.getString(R.string.app_name)
                                            ).apply { mkdirs() }
                                        }

                                    recording = startRecordingVideo(
                                        context = context,
                                        filenameFormat = "yyyy-MM-dd-HH-mm-ss-SSS",
                                        videoCapture = videoCapture,
                                        outputDirectory = if (mediaDir != null && mediaDir.exists()) mediaDir else context.filesDir,
                                        executor = context.mainExecutor,
                                        audioEnabled = audioEnabled.value
                                    ) { event ->
                                        if (event is VideoRecordEvent.Finalize) {
                                            val uri = event.outputResults.outputUri
                                            if (uri != Uri.EMPTY) {
                                                val uriEncoded = URLEncoder.encode(
                                                    uri.toString(),
                                                    StandardCharsets.UTF_8.toString()
                                                )
                                                dis = uriEncoded
                                            }
                                        }
                                    }
                                }
                            } else {
                                recordingStarted.value = false
                                recording?.stop()
                            }
                        },
                    text = (if (!recordingStarted.value) "Start" else "Stop") + " recording",
                )
            }
        }
    }
}

@Composable
fun ExamExtendedContent(viewModel: SmanisViewModel) {
    val uiModel = viewModel.uiState.collectAsState().value
    Text(text = "Exam extended")
}