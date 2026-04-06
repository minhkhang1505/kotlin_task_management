package com.nguyenminhkhang.taskmanagement.ui.search

import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsEvent
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsTracker
import com.nguyenminhkhang.taskmanagement.core.time.TimeProvider
import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.domain.model.Task
import com.nguyenminhkhang.taskmanagement.data.mapper.toEntity
import com.nguyenminhkhang.taskmanagement.domain.repository.TaskRepository
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private val taskRepository = mockk<TaskRepository>()
    private val analyticsTracker = mockk<AnalyticsTracker>()
    private val timeProvider = mockk<TimeProvider>()

    private val testTimeMillis = 1700000000000L // arbitrary fixed time for tests

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())

        every { timeProvider.getCurrentTimeMillis() } returns testTimeMillis
        every { analyticsTracker.trackEvent(any()) } just Runs
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `init should fetch today tasks`() = runTest {
        // Arrange
        val domainTask = createDomainTask(id = 1L)
        val expectedTasks = listOf(domainTask)
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(expectedTasks)

        // Act
        val viewModel = createViewModel()
        advanceUntilIdle()

        // Assert
        val now = Instant.ofEpochMilli(testTimeMillis)
        val today = now.atZone(ZoneId.systemDefault()).toLocalDate()
        val startOfDay = today.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000
        val endOfDay = today.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000 - 1

        coVerify { taskRepository.getTodayTasks(startOfDay, endOfDay) }
        assertEquals(expectedTasks.map { it.toEntity() }, viewModel.searchUiState.value.todayTaskResult)
    }

    @Test
    fun `onScreenShow should track screen view event`() {
        // Arrange
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(emptyList())
        val viewModel = createViewModel()

        // Act
        viewModel.onScreenShow()

        // Assert
        verify { analyticsTracker.trackEvent(AnalyticsEvent.ScreenView("SearchScreen")) }
    }

    @Test
    fun `onSearchQueryChange should update search query state`() = runTest {
        // Arrange
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(emptyList())
        val viewModel = createViewModel()

        // Act
        viewModel.onEvent(SearchEvent.OnSearchQueryChange("test query"))

        // Assert
        assertEquals("test query", viewModel.searchUiState.value.searchQuery)
    }

    @Test
    fun `onExpandedChange true should update expanded state to true`() = runTest {
        // Arrange
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(emptyList())
        val viewModel = createViewModel()

        // Act
        viewModel.onEvent(SearchEvent.OnExpandedChange(true))

        // Assert
        assertTrue(viewModel.searchUiState.value.expanded)
    }

    @Test
    fun `onExpandedChange false should clear search query`() = runTest {
        // Arrange
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(emptyList())
        val viewModel = createViewModel()
        viewModel.onEvent(SearchEvent.OnSearchQueryChange("previous query"))

        // Act
        viewModel.onEvent(SearchEvent.OnExpandedChange(false))

        // Assert
        assertFalse(viewModel.searchUiState.value.expanded)
        assertEquals("", viewModel.searchUiState.value.searchQuery)
    }

    @Test
    fun `clearSearchQuery should reset search query to empty string`() = runTest {
        // Arrange
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(emptyList())
        val viewModel = createViewModel()
        viewModel.onEvent(SearchEvent.OnSearchQueryChange("query"))

        // Act
        viewModel.onEvent(SearchEvent.ClearSearchQuery)

        // Assert
        assertEquals("", viewModel.searchUiState.value.searchQuery)
    }

    @Test
    fun `toggleSearchBarVisibility should invert isSearchBarVisible`() = runTest {
        // Arrange
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(emptyList())
        val viewModel = createViewModel()
        val initialVisibility = viewModel.searchUiState.value.isSearchBarVisible

        // Act
        viewModel.onEvent(SearchEvent.ToggleSearchBarVisibility)

        // Assert
        assertEquals(!initialVisibility, viewModel.searchUiState.value.isSearchBarVisible)
    }

    @Test
    fun `hideSearchBar should set isSearchBarVisible to false`() = runTest {
        // Arrange
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(emptyList())
        val viewModel = createViewModel()
        if (!viewModel.searchUiState.value.isSearchBarVisible) {
            viewModel.onEvent(SearchEvent.ToggleSearchBarVisibility) // Make it visible first if it isn't
        }

        // Act
        viewModel.onEvent(SearchEvent.HideSearchBar)

        // Assert
        assertFalse(viewModel.searchUiState.value.isSearchBarVisible)
    }

    @Test
    fun `expandSearchBarChanged should toggle expanded state`() = runTest {
        // Arrange
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(emptyList())
        val viewModel = createViewModel()
        val initialState = viewModel.searchUiState.value.expanded

        // Act
        viewModel.onEvent(SearchEvent.ExpandSearchBarChanged)

        // Assert
        assertEquals(!initialState, viewModel.searchUiState.value.expanded)
    }

    @Test
    fun `collapseSearchBar should set expanded to false`() = runTest {
        // Arrange
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(emptyList())
        val viewModel = createViewModel()
        viewModel.onEvent(SearchEvent.OnExpandedChange(true))

        // Act
        viewModel.onEvent(SearchEvent.CollapseSearchBar)

        // Assert
        assertFalse(viewModel.searchUiState.value.expanded)
    }

    @Test
    fun `onToggleFavoriteClick should call repository to update favorite status`() = runTest {
        // Arrange
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(emptyList())
        coEvery { taskRepository.updateTaskFavoriteById(any(), any()) } returns true
        val viewModel = createViewModel()

        // Act
        viewModel.onEvent(SearchEvent.OnToggleFavoriteClick(taskId = 5L, isFavorite = true))
        advanceUntilIdle()

        // Assert
        coVerify { taskRepository.updateTaskFavoriteById(taskId = 5L, isFavorite = true) }
    }

    @Test
    fun `searchResults should emit from repository when query length is at least 2`() = runTest {
        // Arrange
        val domainTask = createDomainTask(id = 10L, content = "Meeting")
        val expectedResults = listOf(domainTask)
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(emptyList())
        coEvery { taskRepository.SearchTasks("Meeting") } returns flowOf(expectedResults)
        
        val viewModel = createViewModel()
        val job = launch { viewModel.searchResults.collect {} }
        
        // Act
        viewModel.onEvent(SearchEvent.OnSearchQueryChange("Meeting"))
        advanceUntilIdle()
        
        // Assert
        assertEquals(expectedResults.map { it.toEntity() }, viewModel.searchResults.value)
        job.cancel()
    }

    @Test
    fun `searchResults should emit empty list when query length is less than 2`() = runTest {
        // Arrange
        coEvery { taskRepository.getTodayTasks(any(), any()) } returns flowOf(emptyList())
        coEvery { taskRepository.SearchTasks(any()) } returns flowOf(listOf(createDomainTask(10L))) // Should not be called
        
        val viewModel = createViewModel()
        val job = launch { viewModel.searchResults.collect {} }
        
        // Act
        viewModel.onEvent(SearchEvent.OnSearchQueryChange("A"))
        advanceUntilIdle()
        
        // Assert
        assertEquals(emptyList<TaskEntity>(), viewModel.searchResults.value)
        coVerify(exactly = 0) { taskRepository.SearchTasks(any()) }
        job.cancel()
    }

    private fun createViewModel() = SearchViewModel(
        taskRepository = taskRepository,
        analyticsTracker = analyticsTracker,
        timeProvider = timeProvider
    )

    private fun createDomainTask(
        id: Long,
        content: String = "Task $id",
    ): Task {
        return Task(
            id = id,
            userId = "user1",
            content = content,
            taskDetail = "",
            favorite = false,
            completed = false,
            collectionId = 1L,
            updatedAt = System.currentTimeMillis()
        )
    }

    private fun createTask(
        id: Long,
        content: String = "Task $id",
    ): TaskEntity {
        return TaskEntity(
            id = id,
            userId = "user1",
            content = content,
            taskDetail = "",
            favorite = false,
            completed = false,
            collectionId = 1L,
            updatedAt = System.currentTimeMillis()
        )
    }
}
