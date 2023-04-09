package xyz.pcrab.smanis.ui.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
        _uiState.update {
            it.copy(allStudents = newStudents)
        }
    }

    private fun updateStudent(newStudent: Student) {
        val newAllStudents = mutableMapOf<String, Student>()
//        val newAllStudents = _uiState.value.allStudents.toMutableMap()
        newAllStudents.putAll(_uiState.value.allStudents)
        newAllStudents[newStudent.id] = newStudent
        Log.d("UpdateStudent", "updateStudent: $newAllStudents")
        updateAllStudents(newAllStudents.toMap())
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
        val httpUrl =
            if (newRemoteUrl.startsWith("http://")) newRemoteUrl else "http://$newRemoteUrl"
        val realNewRemoteUrl = if (httpUrl.endsWith("/")) httpUrl else "$httpUrl/"
        configFile.writeText(realNewRemoteUrl)
        _uiState.value = _uiState.value.copy(remoteUrl = realNewRemoteUrl)
    }

    fun fetchExams(uri: String, studentId: String) {
        viewModelScope.launch {
            try {
                val student: Student =
                    _uiState.value.allStudents[studentId]?.copy() ?: return@launch
                Log.d(
                    "SmanisFetchExams",
                    "Fetching exams for ${uri}okGetFileList/$studentId"
                )
                val response: List<String> =
                    Client.get(uri + "okGetFileList/$studentId").body<String>()
                        .lines()
                Log.d("SmanisFetchExams", "Response: $response")
                val exams = response.slice(1..response.lastIndex).associate { rawExam ->
                    val examData = rawExam.split(",")
                    val examMetadata = examData[0].split("-")
                    val id = "$studentId-${examMetadata[0]}"
                    val score = examMetadata[1].toInt()
                    val points = examData.slice(1..examData.lastIndex).map {
                        it.split("-")
                    }.associate {
                        it[0] to it[1].toInt()
                    }
                    id to Exam(id, score, points, "2023-04-09T15:34:44.426Z")
                }
                student.exams = exams
                Log.d("SmanisFetchExams", "Exams: $exams")
                updateStudent(student)
            } catch (e: Exception) {
                Log.d("SmanisFetchExams", "fetchExams: $e")
            }
        }
    }

    fun fetchVideoFile(videoUri: String, path: String = "", fileName: String, context: Context) {
        viewModelScope.launch {
            try {
                val finalPath =
                    if (path.isEmpty()) "" else if (path.endsWith("/")) path else "$path/"
                val finalDirFile = File(context.externalCacheDir, finalPath)
                if (!finalDirFile.exists()) {
                    finalDirFile.mkdirs()
                }
                Log.d("SmanisViewModel", "fetchVideoFile: $videoUri")
                val response: ByteArray = Client.get(videoUri).body()
                val file = File(finalDirFile, fileName)
                val tmpFile = File(finalDirFile, "${fileName}.tmp")
                tmpFile.writeBytes(response)
                tmpFile.copyTo(file, overwrite = true)
                tmpFile.delete()
                Log.d("SmanisViewModel", "storeVideoPath: ${file.absolutePath}")
            } catch (e: ClientRequestException) {
                e.printStackTrace()
            }
        }

    }

    init {
        viewModelScope.launch {
            updateAllStudents(
                mapOf(
                    "110219" to Student(id = "110219", username = "Student 1"),
                    "291729" to Student(id = "291729", username = "Student 2"),
                )
            )
            updateCurrentStudent(null)
            updateCurrentDestination(SmanisDestinations.MANAGE)
        }
    }
}

data class SmanisUIState(
    val allStudents: Students = mapOf(),
    val currentStudent: Student? = null,
    val currentDestination: String = SmanisDestinations.MANAGE,
    val remoteUrl: String = "",
)