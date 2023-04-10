package xyz.pcrab.smanis.ui.content

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Color
import android.hardware.Camera
import android.media.MediaRecorder
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceView
import android.widget.Button
import android.widget.Spinner
import androidx.camera.video.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
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

                    val surface = view.findViewById<SurfaceView>(R.id.CameraPreview)

                    val previewBtn = view.findViewById<Button>(R.id.PreviewBtn)

                    val recordBtn = view.findViewById<Button>(R.id.RecordBtn)
                    recordBtn.setBackgroundColor(Color.parseColor("#DDF8C7"))

                    val spinner = view.findViewById<Spinner>(R.id.ResolutionSpinner)
                    spinner.setSelection(1)


                    var mediaRecorder = MediaRecorder()
                    var camera = Camera.open()

                    fun startRecord(isPreview: Boolean = false) {
                        if (isPreview) {
                            previewBtn.isEnabled = false
                        }
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

                        Log.i("Smanis Record", spinner.selectedItem.toString())

                        if (isPreview || spinner.selectedItem.toString() !== "1280x720") {
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

                        mediaRecorder.setPreviewDisplay(surface.holder.surface)

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



                    previewBtn.setOnClickListener { startRecord(true) }

                    recordBtn.setOnClickListener {
                        if (recordBtn.text == "开始录制") {
                            recordBtn.text = "停止录制"
                            recordBtn.setBackgroundColor(Color.parseColor("#F8CDC7"))
                            startRecord()
                        } else {
                            recordBtn.text = "开始录制"
                            recordBtn.setBackgroundColor(Color.parseColor("#DDF8C7"))
                            stopRecord()
                        }
                    }


                    // do whatever you want...
                    view
                },
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun ExamExtendedContent(viewModel: SmanisViewModel) {
    val uiModel = viewModel.uiState.collectAsState().value
    Text(text = "Exam extended")
}