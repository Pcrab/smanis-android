package xyz.pcrab.smanis.ui.content.manage

import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
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
import xyz.pcrab.smanis.ui.data.SmanisViewModel
import java.io.File

@Composable
@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
fun ExamInfo(
    modifier: Modifier = Modifier,
    viewModel: SmanisViewModel = SmanisViewModel(),
    exam: Exam? = null,
    onClickBack: () -> Unit = {},
) {
    if (exam == null) return
    val interactionSource = MutableInteractionSource()
    val uiState = viewModel.uiState.collectAsState().value
    val context = LocalContext.current

    val localVideoFile = File(context.filesDir, exam.video)
    var displayUri = localVideoFile.toURI().toString()
    if (!localVideoFile.exists()) {
        displayUri = uiState.remoteUrl + exam.video
        viewModel.fetchVideoFile(videoUri = displayUri, fileName = exam.video, context = context)
    }
    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val dataSourceFactory = DefaultDataSource.Factory(context)
                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(displayUri))
                setMediaSource(source)
                prepare()
            }
    }
    exoPlayer.addListener(object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
//            super.onPlayerError(error)
            Log.e("Player", error.message.toString())
        }
    })
    exoPlayer.playWhenReady = true
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
    exoPlayer.repeatMode = ExoPlayer.REPEAT_MODE_ONE

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .clickable(interactionSource = interactionSource, indication = null) {}
            .focusable(true)
    ) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.clickable {
            onClickBack()
        })
        Text(text = exam.video)
        Text(text = "remoteUrl: ${uiState.remoteUrl}")
        Text(text = "displayUri: $displayUri")

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

        Column {}
    }
}