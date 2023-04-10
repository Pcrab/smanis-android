package xyz.pcrab.smanis.ui.content

import android.Manifest
import android.annotation.SuppressLint
import android.hardware.Camera
import android.media.MediaRecorder
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceView
import androidx.camera.video.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionsRequired
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import xyz.pcrab.smanis.R
import xyz.pcrab.smanis.ui.data.SmanisViewModel
import xyz.pcrab.smanis.utils.state.SmanisContentType
import java.io.File
import java.util.*

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

@SuppressLint("RestrictedApi", "InflateParams")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ExamCompactContent(viewModel: SmanisViewModel) {
    val uiModel = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    var surface: SurfaceView? = null

    val buttonText = remember {
        mutableStateOf("开始录制")
    }
    val enablePreviewBtn = remember {
        mutableStateOf(true)
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

    var mediaRecorder = MediaRecorder()
    var camera = Camera.open()
    fun startRecord(isPreview: Boolean = false) {
        mediaRecorder = MediaRecorder()
        mediaRecorder.reset()
        camera = Camera.open()
        camera.setDisplayOrientation(90)
        camera.setDisplayOrientation(90)
        camera.unlock()
        mediaRecorder.setCamera(camera)
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264)
        mediaRecorder.setOrientationHint(0)
        mediaRecorder.setVideoFrameRate(30)
        if (isPreview) {
            mediaRecorder.setVideoEncodingBitRate(16)
        } else {
            mediaRecorder.setVideoEncodingBitRate(125 * 1024 * 1024)
        }

        Log.i("Smanis Record", uiModel.resolution)

        if (isPreview || uiModel.resolution !== "1280x720") {
            mediaRecorder.setVideoSize(640, 480)
        } else {
            mediaRecorder.setVideoSize(1280, 720)
        }

        val finalDirFile = File(
            (context.externalCacheDir?.absolutePath
                ?: "/storage/emulated/0/datafile/tennis") + "/Smanis/" + (if (isPreview) "temp/" else "")
        )
        if (!finalDirFile.exists()) {
            finalDirFile.mkdirs()
        }
        Log.i("Smanis Record", "Preview: $isPreview, ${finalDirFile.absolutePath}")
        val finalFilePath =
            finalDirFile.absolutePath + "/" + System.currentTimeMillis() + ".mp4"
        Log.i("Smanis Record", finalFilePath)
        mediaRecorder.setOutputFile(finalFilePath)

        mediaRecorder.setPreviewDisplay(surface?.holder?.surface)

        try {
            mediaRecorder.prepare()
            mediaRecorder.start()
            camera.autoFocus { _, _ -> }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopRecord() {
        mediaRecorder.stop()
        mediaRecorder.release()
        camera.release()
        startRecord(true)
    }


    PermissionsRequired(
        multiplePermissionsState = permissionState,
        permissionsNotGrantedContent = { /* ... */ },
        permissionsNotAvailableContent = { /* ... */ }
    ) {

        Column {
            AndroidView(
                factory = { context ->
                    val view = LayoutInflater.from(context).inflate(R.layout.layout, null, false)
                    surface = view.findViewById(R.id.CameraPreview)
                    view
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ElevatedButton(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color(0xFF000000),
                        containerColor = if (enablePreviewBtn.value) {
                            Color(0xFFDDF8C7)
                        } else if (buttonText.value == "开始录制") {
                            Color(0xFFDDF8C7)
                        } else {
                            Color(0xFFF8CDC7)
                        }
                    ),
                    onClick = { ->
                        if (enablePreviewBtn.value) {
                            enablePreviewBtn.value = false
                            startRecord(true)
                            return@ElevatedButton
                        }
                        if (buttonText.value == "开始录制") {
                            buttonText.value = "停止录制"
                            startRecord()
                        } else {
                            buttonText.value = "开始录制"
                            stopRecord()
                        }
                    },
                ) {
                    Text(
                        text = if (enablePreviewBtn.value) {
                            "打开摄像头"
                        } else {
                            buttonText.value
                        }, fontWeight = FontWeight.Bold, fontSize = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ExamExtendedContent(viewModel: SmanisViewModel) {
    val uiModel = viewModel.uiState.collectAsState().value
    Text(text = "Exam extended")
}