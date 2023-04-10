package xyz.pcrab.smaniszk.ui.content.manage

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import io.ktor.client.request.*
import xyz.pcrab.smaniszk.ui.data.SmanisViewModel
import java.io.File


@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun buildPlayer(context: Context, uri: String): ExoPlayer {
    val player = ExoPlayer.Builder(context)
        .build()
        .apply {
            val dataSourceFactory = DefaultDataSource.Factory(context)
            val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri))
            setMediaSource(source)
            prepare()
        }
    player.addListener(object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Log.e("Player", error.message.toString())
        }
    })
    player.playWhenReady = false
    player.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    player.repeatMode = ExoPlayer.REPEAT_MODE_OFF
    return player
}

data class ExamPlayerInfo(
    val player: ExoPlayer,
    val score: Int,
)

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun ExamInfo(
    modifier: Modifier = Modifier,
    studentId: String? = null,
    examId: String? = null,
    onClickBack: () -> Unit = {},
) {
    val viewModel: SmanisViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    val exam = uiState.allStudents[studentId]?.exams?.get(examId) ?: return
    val interactionSource = MutableInteractionSource()
    val context = LocalContext.current
    val filePath = "${studentId}/${exam.id}/"

    val displayImage = remember {
        mutableStateOf("")
    }

    fun parseDisplayUri(
        type: String,
        ext: String,
        path: String,
        frame: String
    ): String {
        val fileName = "${type}-${frame}.${ext}"
        val localFile = File(context.externalCacheDir, "$path${fileName}")
        Log.d("SmanisViewModel", "checkVideoPath: ${localFile.absolutePath}")
        var displayUri = localFile.toURI().toString()
        if (!localFile.exists()) {
            println("File not exist, downloading...")
            displayUri = "${uiState.remoteUrl}okGetOneFile/${fileName}"
            viewModel.fetchVideoFile(
                videoUri = displayUri,
                path = path,
                fileName = fileName,
                context = context
            )
        }
        return displayUri
    }

    val players = remember {
        mutableMapOf<Int, ExamPlayerInfo>()
    }
    exam.points.forEachIndexed { index, it ->
        players[index] =
            ExamPlayerInfo(
                player = buildPlayer(
                    context = context,
                    uri = parseDisplayUri(
                        type = "video",
                        ext = "mp4",
                        path = filePath,
                        frame = it.first
                    )
                ),
                score = it.second
            )
    }

    Box {

        Column(
            modifier = modifier
                .padding(20.dp)
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .clickable(interactionSource = interactionSource, indication = null) {}
                .verticalScroll(rememberScrollState())
                .focusable(true)
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable {
                    onClickBack()
                })

            Column {
                players.forEach { (index, playerInfo) ->
                    Text(
                        text = "第 ${index + 1} 球: ${playerInfo.score} 分",
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    DisplayVideoView(context = context, exoPlayer = playerInfo.player)
                    Row {
                        ElevatedButton(onClick = {
                            val url = parseDisplayUri(
                                type = "pic",
                                ext = "jpg",
                                path = filePath,
                                frame = exam.points[index].first
                            )
                            displayImage.value = url
                        }) {
                            Text(text = "实景图")
                        }
                        ElevatedButton(onClick = {
                            val url = parseDisplayUri(
                                type = "Transfer",
                                ext = "jpg",
                                path = filePath,
                                frame = exam.points[index].first
                            )
                            displayImage.value = url
                        }) {
                            Text(text = "平面图")
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .height(1.dp)
                            .fillMaxWidth()
                            .background(Color.Gray)
                    )
                }
            }
        }
        if (displayImage.value != "") {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxSize()
                    .clickable { displayImage.value = "" }
                    .background(color = Color.Black.copy(alpha = 0.8f))) {
                AsyncImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    model = ImageRequest.Builder(context)
                        .data(displayImage.value)
                        .build(),
                    contentDescription = ""
                )
            }
        }

    }

    BackHandler(enabled = displayImage.value != "") {
        displayImage.value = ""
    }
}

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun DisplayVideoView(context: Context, exoPlayer: ExoPlayer) {
    DisposableEffect(
        AndroidView(factory = {
            PlayerView(context).apply {
                hideController()
                useController = true
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

            }
        })
    ) {
        onDispose {
            exoPlayer.release()
        }
    }
    return
}