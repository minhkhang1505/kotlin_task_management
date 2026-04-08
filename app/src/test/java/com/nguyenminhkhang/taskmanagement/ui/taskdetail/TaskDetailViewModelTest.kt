package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.lifecycle.SavedStateHandle
import com.nguyeminhkhang.shared.model.Task
import com.nguyenminhkhang.taskmanagement.domain.usecase.GetTaskByIdUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.ToggleCompleteUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.ToggleTaskFavoriteUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.UpdateTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.GetTaskCollectionsUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.MoveTaskToCollectionUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.taskdetail.BuildRepeatSummaryTextUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.taskdetail.TrackTaskDetailScreenViewUseCase
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.effects.TaskDetailEffect
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.events.NavigationEvent
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.events.TaskDetailEvent
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskDetailViewModelTest {

    private val getTaskByIdUseCase = mockk<GetTaskByIdUseCase>()
    private val getTaskCollectionsUseCase = mockk<GetTaskCollectionsUseCase>()
    private val toggleCompleteUseCase = mockk<ToggleCompleteUseCase>()
    private val toggleTaskFavoriteUseCase = mockk<ToggleTaskFavoriteUseCase>()
    private val updateTaskUseCase = mockk<UpdateTaskUseCase>()
    private val moveTaskToCollectionUseCase = mockk<MoveTaskToCollectionUseCase>()
    private val buildRepeatSummaryTextUseCase = mockk<BuildRepeatSummaryTextUseCase>()
    private val trackTaskDetailScreenViewUseCase = mockk<TrackTaskDetailScreenViewUseCase>()
    private val savedStateHandle = mockk<SavedStateHandle>()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private fun createViewModel() = TaskDetailViewModel(
        getTaskByIdUseCase = getTaskByIdUseCase,
        getTaskCollectionsUseCase = getTaskCollectionsUseCase,
        toggleCompleteUseCase = toggleCompleteUseCase,
        toggleTaskFavoriteUseCase = toggleTaskFavoriteUseCase,
        updateTaskUseCase = updateTaskUseCase,
        moveTaskToCollectionUseCase = moveTaskToCollectionUseCase,
        buildRepeatSummaryTextUseCase = buildRepeatSummaryTextUseCase,
        trackTaskDetailScreenViewUseCase = trackTaskDetailScreenViewUseCase,
        savedStateHandle = savedStateHandle
    )

    private fun createTask(
        id: Long = 1L,
        content: String = "Test Task",
        collectionId: Long = 1L,
        favorite: Boolean = false,
        completed: Boolean = false,
        startDate: Long? = null,
        startTime: Long? = null,
        reminderTimeMillis: Long? = null
    ) = Task(
        id = id,
        content = content,
        collectionId = collectionId,
        favorite = favorite,
        completed = completed,
        startDate = startDate,
        startTime = startTime,
        reminderTimeMillis = reminderTimeMillis
    )

    private fun createCollection(id: Long = 1L, content: String = "Default Collection") = Collection(
        id = id,
        content = content
    )

    @Test
    fun `init should fetch task and collections when taskId is present`() = runTest {
        // Arrange
        val taskId = 1L
        val task = createTask(id = taskId)
        val collections = listOf(createCollection(id = 1L, content = "Inbox"))
        
        every { savedStateHandle.get<Long>("taskId") } returns taskId
        every { getTaskByIdUseCase(taskId) } returns flowOf(task)
        every { getTaskCollectionsUseCase() } returns flowOf(collections)
        every { buildRepeatSummaryTextUseCase(task) } returns "No repeat"

        // Act
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        val uiState = viewModel.uiState.value
        assertNotNull(uiState.task)
        assertEquals(taskId, uiState.task?.id)
        assertEquals(collections, uiState.collection)
        assertEquals("Inbox", uiState.currentCollection)
        assertFalse(uiState.isLoading)
    }

    @Test
    fun `init should show error when taskId is missing`() = runTest {
        // Arrange
        every { savedStateHandle.get<Long>("taskId") } returns null
        
        // Act
        val viewModel = createViewModel()
        
        // Assert
        val snackbarEvent = viewModel.snackbarEvent.first()
        assertEquals("Unable to load task details", snackbarEvent.message)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `onScreenShow should track screen view`() {
        // Arrange
        every { savedStateHandle.get<Long>("taskId") } returns 1L
        every { getTaskByIdUseCase(any()) } returns flowOf(createTask())
        every { getTaskCollectionsUseCase() } returns flowOf(emptyList())
        every { buildRepeatSummaryTextUseCase(any()) } returns ""
        every { trackTaskDetailScreenViewUseCase() } just Runs
        
        val viewModel = createViewModel()

        // Act
        viewModel.onScreenShow()

        // Assert
        coVerify { trackTaskDetailScreenViewUseCase() }
    }

    @Test
    fun `onMarkAsDoneClicked should call complete use case and navigate back`() = runTest {
        // Arrange
        val taskId = 1L
        every { savedStateHandle.get<Long>("taskId") } returns taskId
        every { getTaskByIdUseCase(taskId) } returns flowOf(createTask(id = taskId))
        every { getTaskCollectionsUseCase() } returns flowOf(emptyList())
        every { buildRepeatSummaryTextUseCase(any()) } returns ""
        coEvery { toggleCompleteUseCase(taskId, true) } returns true
        
        val viewModel = createViewModel()
        
        // Act
        viewModel.onMarkAsDoneClicked()
        advanceUntilIdle()

        // Assert
        coVerify { toggleCompleteUseCase(taskId, true) }
        val navEvent = viewModel.navigationEvent.first()
        assertTrue(navEvent is NavigationEvent.NavigateBackWithResult)
        assertEquals(taskId, (navEvent as NavigationEvent.NavigateBackWithResult).taskId)
    }

    @Test
    fun `toggleFavorite should update status and show snackbar`() = runTest {
        // Arrange
        val taskId = 1L
        val task = createTask(id = taskId, favorite = false)
        every { savedStateHandle.get<Long>("taskId") } returns taskId
        every { getTaskByIdUseCase(taskId) } returns flowOf(task)
        every { getTaskCollectionsUseCase() } returns flowOf(emptyList())
        every { buildRepeatSummaryTextUseCase(any()) } returns ""
        coEvery { toggleTaskFavoriteUseCase(taskId, true) } returns true
        
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Act
        viewModel.toggleFavorite()
        advanceUntilIdle()

        // Assert
        coVerify { toggleTaskFavoriteUseCase(taskId, true) }
        val snackbarEvent = viewModel.snackbarEvent.first()
        assertTrue(snackbarEvent.message.contains("added to favorites"))
    }

    @Test
    fun `onTitleChange should update ui state`() = runTest {
        // Arrange
        val taskId = 1L
        every { savedStateHandle.get<Long>("taskId") } returns taskId
        every { getTaskByIdUseCase(taskId) } returns flowOf(createTask(id = taskId, content = "Old Title"))
        every { getTaskCollectionsUseCase() } returns flowOf(emptyList())
        every { buildRepeatSummaryTextUseCase(any()) } returns ""
        
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Act
        viewModel.onTitleChange("New Title")

        // Assert
        assertEquals("New Title", viewModel.uiState.value.task?.content)
    }

    @Test
    fun `saveTitle should call update use case`() = runTest {
        // Arrange
        val taskId = 1L
        val task = createTask(id = taskId, content = "Updated Title")
        every { savedStateHandle.get<Long>("taskId") } returns taskId
        every { getTaskByIdUseCase(taskId) } returns flowOf(task)
        every { getTaskCollectionsUseCase() } returns flowOf(emptyList())
        every { buildRepeatSummaryTextUseCase(any()) } returns ""
        coEvery { updateTaskUseCase(any()) } returns true
        
        val viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.onTitleChange("Updated Title")

        // Act
        viewModel.saveTitle()
        advanceUntilIdle()

        // Assert
        coVerify { updateTaskUseCase(match { it.content == "Updated Title" }) }
    }

    @Test
    fun `onExitEditMode should auto-save if in edit mode`() = runTest {
        // Arrange
        val taskId = 1L
        every { savedStateHandle.get<Long>("taskId") } returns taskId
        every { getTaskByIdUseCase(taskId) } returns flowOf(createTask(id = taskId))
        every { getTaskCollectionsUseCase() } returns flowOf(emptyList())
        every { buildRepeatSummaryTextUseCase(any()) } returns ""
        coEvery { updateTaskUseCase(any()) } returns true
        
        val viewModel = createViewModel()
        advanceUntilIdle()
        viewModel.onEnterEditMode()

        // Act
        viewModel.onExitEditMode()
        advanceUntilIdle()

        // Assert
        coVerify { updateTaskUseCase(any()) }
        assertFalse(viewModel.uiState.value.isInEditMode)
    }

    @Test
    fun `onDateSelected should update task and call update use case`() = runTest {
        // Arrange
        val taskId = 1L
        val dateMillis = 123456789L
        every { savedStateHandle.get<Long>("taskId") } returns taskId
        every { getTaskByIdUseCase(taskId) } returns flowOf(createTask(id = taskId))
        every { getTaskCollectionsUseCase() } returns flowOf(emptyList())
        every { buildRepeatSummaryTextUseCase(any()) } returns ""
        coEvery { updateTaskUseCase(any()) } returns true
        
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Act
        viewModel.onDateSelected(dateMillis)
        advanceUntilIdle()

        // Assert
        assertEquals(dateMillis, viewModel.uiState.value.task?.startDate)
        coVerify { updateTaskUseCase(match { it.startDate == dateMillis }) }
    }

    @Test
    fun `onClearDateSelected should set startDate to null and call update use case`() = runTest {
        // Arrange
        val taskId = 1L
        val task = createTask(id = taskId, startDate = 12345L)
        every { savedStateHandle.get<Long>("taskId") } returns taskId
        every { getTaskByIdUseCase(taskId) } returns flowOf(task)
        every { getTaskCollectionsUseCase() } returns flowOf(emptyList())
        every { buildRepeatSummaryTextUseCase(any()) } returns ""
        coEvery { updateTaskUseCase(any()) } returns true
        
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Act
        viewModel.onClearDateSelected()
        advanceUntilIdle()

        // Assert
        assertEquals(null, viewModel.uiState.value.task?.startDate)
        coVerify { updateTaskUseCase(match { it.startDate == null }) }
    }

    @Test
    fun `onTimeSelected should update task and call update use case`() = runTest {
        // Arrange
        val taskId = 1L
        val timeMillis = 3600000L
        every { savedStateHandle.get<Long>("taskId") } returns taskId
        every { getTaskByIdUseCase(taskId) } returns flowOf(createTask(id = taskId))
        every { getTaskCollectionsUseCase() } returns flowOf(emptyList())
        every { buildRepeatSummaryTextUseCase(any()) } returns ""
        coEvery { updateTaskUseCase(any()) } returns true
        
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Act
        viewModel.onTimeSelected(timeMillis)
        advanceUntilIdle()

        // Assert
        assertEquals(timeMillis, viewModel.uiState.value.task?.startTime)
        coVerify { updateTaskUseCase(match { it.startTime == timeMillis }) }
    }

    @Test
    fun `addTaskToCalendar should emit OpenCalendar effect when reminder is set`() = runTest {
        // Arrange
        val taskId = 1L
        val reminderMillis = 1000L
        val task = createTask(id = taskId, reminderTimeMillis = reminderMillis)
        every { savedStateHandle.get<Long>("taskId") } returns taskId
        every { getTaskByIdUseCase(taskId) } returns flowOf(task)
        every { getTaskCollectionsUseCase() } returns flowOf(emptyList())
        every { buildRepeatSummaryTextUseCase(any()) } returns ""
        
        val viewModel = createViewModel()
        advanceUntilIdle()
        
        val effects = mutableListOf<TaskDetailEffect>()
        val job = launch { viewModel.effect.collect { effects.add(it) } }

        // Act
        viewModel.onEvent(TaskDetailEvent.AddToCalendar)
        advanceUntilIdle()

        // Assert
        assertTrue(effects.any { it is TaskDetailEffect.OpenCalendar })
        val effect = effects.find { it is TaskDetailEffect.OpenCalendar } as TaskDetailEffect.OpenCalendar
        assertEquals(reminderMillis, effect.startTimeMillis)
        
        job.cancel()
    }

    @Test
    fun `CurrentCollectionChanged event should move task to new collection`() = runTest {
        // Arrange
        val taskId = 1L
        val newCollectionId = 2L
        val collections = listOf(
            createCollection(id = 1L, content = "Inbox"),
            createCollection(id = 2L, content = "Personal")
        )
        every { savedStateHandle.get<Long>("taskId") } returns taskId
        every { getTaskByIdUseCase(taskId) } returns flowOf(createTask(id = taskId, collectionId = 1L))
        every { getTaskCollectionsUseCase() } returns flowOf(collections)
        every { buildRepeatSummaryTextUseCase(any()) } returns ""
        coEvery { moveTaskToCollectionUseCase(taskId, newCollectionId) } returns true
        
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Act
        viewModel.onEvent(TaskDetailEvent.CurrentCollectionChanged(newCollectionId))
        advanceUntilIdle()

        // Assert
        coVerify { moveTaskToCollectionUseCase(taskId, newCollectionId) }
        assertEquals("Personal", viewModel.uiState.value.currentCollection)
    }
}
