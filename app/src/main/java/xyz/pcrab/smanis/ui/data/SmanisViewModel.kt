package xyz.pcrab.smanis.ui.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import xyz.pcrab.smanis.data.Exam
import xyz.pcrab.smanis.data.Student
import xyz.pcrab.smanis.data.Students
import xyz.pcrab.smanis.ui.SmanisDestinations
import xyz.pcrab.smanis.utils.request.Client
import java.io.File

class SmanisViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SmanisUIState())
    val uiState: StateFlow<SmanisUIState> = _uiState.asStateFlow()

    fun updateCurrentDestination(newDestination: String) {
        _uiState.value = _uiState.value.copy(currentDestination = newDestination)
    }

    fun updateCurrentStudent(newStudent: Student? = null) {
        _uiState.value = _uiState.value.copy(currentStudent = newStudent)
    }

    private fun updateAllStudents(newStudents: Students) {
        _uiState.value = _uiState.value.copy(allStudents = newStudents)
    }

    fun initRemoteUrl(context: Context) {
        val defaultUrl = "http://localhost:20080/"
        val configFile = File(context.filesDir, "url.txt")
        if (!configFile.exists()) {
            configFile.writeText(defaultUrl)
        }
        _uiState.value = _uiState.value.copy(remoteUrl = configFile.readText())
    }

    fun updateRemoteUrl(context: Context, newRemoteUrl: String) {
        val configFile = File(context.filesDir, "url.txt")
        val realNewRemoteUrl = if (newRemoteUrl.endsWith("/")) newRemoteUrl else "$newRemoteUrl/"
        configFile.writeText(realNewRemoteUrl)
        _uiState.value = _uiState.value.copy(remoteUrl = realNewRemoteUrl)
    }

    var videoDownloading = false
    fun fetchVideoFile(videoUri: String, fileName: String, context: Context) {
        if (videoDownloading) return
        videoDownloading = true
        viewModelScope.launch {
            try {
                val response: ByteArray = Client.get(videoUri).body()
                val file = File(context.filesDir, fileName)
                val tmpFile = File(context.filesDir, "$fileName.tmp")
                tmpFile.writeBytes(response)
                tmpFile.copyTo(file, overwrite = true)
            } finally {
                videoDownloading = false
            }
        }

    }

    init {
        updateAllStudents(
            listOf(
                Student(
                    id = "1", username = "Student 1", exams = listOf(
                        Exam(
                            id = "111112",
                            video = "111112.mp4",
                            score = 100,
                            points = mapOf(),
                            takenTime = Instant.DISTANT_PAST
                        ),
                        Exam(
                            id = "212233",
                            video = "221133",
                            score = 100,
                            points = mapOf(),
                            takenTime = Instant.DISTANT_PAST
                        ),
                        Exam(
                            id = "323124",
                            video = "321321",
                            score = 100,
                            points = mapOf(),
                            takenTime = Instant.DISTANT_PAST
                        ),
                    )
                ),
                Student(id = "2", username = "Student 2"),
                Student(id = "3", username = "Student 3"),
                Student(id = "4", username = "Student 4"),
            )
        )
        updateCurrentStudent(null)
        updateCurrentDestination(SmanisDestinations.MANAGE)
    }
}

data class SmanisUIState(
    val currentStudent: Student? = null,
    val currentDestination: String = SmanisDestinations.MANAGE,
    val allStudents: Students = emptyList(),
    val remoteUrl: String = "",
)