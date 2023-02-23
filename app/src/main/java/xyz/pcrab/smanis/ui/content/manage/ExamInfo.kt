package xyz.pcrab.smanis.ui.content.manage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import xyz.pcrab.smanis.data.Exam

@Composable
fun ExamInfo(
    modifier: Modifier = Modifier,
    exam: Exam? = null,
    onClickBack: () -> Unit = {},
) {
    if (exam == null) return
    val interactionSource = MutableInteractionSource()

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .clickable(interactionSource = interactionSource, indication = null) {}
            .focusable(true)
    ) {
        Icon(Icons.Default.ArrowBack, contentDescription = "Back", modifier = Modifier.clickable {
            onClickBack()
        })
        Text(text = exam.video)
    }
}