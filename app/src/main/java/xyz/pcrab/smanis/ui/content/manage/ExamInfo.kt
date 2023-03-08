package xyz.pcrab.smanis.ui.content.manage

import android.content.Context
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import io.ktor.client.request.*
import xyz.pcrab.smanis.data.Exam
import xyz.pcrab.smanis.ui.data.SmanisUIState
import xyz.pcrab.smanis.ui.data.SmanisViewModel
import java.io.File

fun parseDisplayUri(
    context: Context,
    viewModel: SmanisViewModel = SmanisViewModel(),
    path: String,
    uiState: SmanisUIState,
    uri: String
): String {
    val localVideoFile = File(context.cacheDir, "$path$uri")
    println(localVideoFile.absolutePath)
    var displayUri = localVideoFile.toURI().toString()
    if (!localVideoFile.exists()) {
        println("File not exist, downloading...")
        displayUri = "${uiState.remoteUrl}${path}${uri}"
        viewModel.fetchVideoFile(
            videoUri = displayUri,
            path = path,
            fileName = uri,
            context = context
        )
    }
    return displayUri
}

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
    viewModel: SmanisViewModel = SmanisViewModel(),
    studentId: String? = null,
    exam: Exam? = null,
    onClickBack: () -> Unit = {},
) {
    if (exam == null || studentId == null) return
    val interactionSource = MutableInteractionSource()
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current
    val videoPath = "${studentId}/${exam.id}/"

    val players = remember {
        mutableMapOf<String, ExamPlayerInfo>()
    }
    exam.points.forEach {
        players[it.key] = ExamPlayerInfo(
            player = buildPlayer(
                context = context,
                uri = parseDisplayUri(
                    context = context,
                    uiState = uiState,
                    viewModel = viewModel,
                    path = videoPath,
                    uri = "${it.key}.mp4"
                )
            ),
            score = it.value
        )
    }
    val fullPlayer =
        buildPlayer(
            context = context,
            uri = parseDisplayUri(
                context = context,
                uiState = uiState,
                viewModel = viewModel,
                path = videoPath,
                uri = "full.mp4"
            )
        )

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .clickable(interactionSource = interactionSource, indication = null) {}
            .verticalScroll(rememberScrollState())
            .focusable(true)
    ) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.clickable {
            onClickBack()
        })
        Text(text = exam.video)
        Text(text = "remoteUrl: ${uiState.remoteUrl}")

        DisplayVideoView(context = context, exoPlayer = fullPlayer)

        Column {
            players.forEach { (key, value) ->
                Text(text = "$key: ${value.score}")
                DisplayVideoView(context = context, exoPlayer = value.player)
            }
        }
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