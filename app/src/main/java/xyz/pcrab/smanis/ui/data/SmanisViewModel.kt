package xyz.pcrab.smanis.ui.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import xyz.pcrab.smanis.data.Student
import xyz.pcrab.smanis.data.Students
import xyz.pcrab.smanis.ui.SmanisDestinations

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


    init {
        updateAllStudents(
            listOf(
                Student(id = "1", username = "Student 1"),
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
)