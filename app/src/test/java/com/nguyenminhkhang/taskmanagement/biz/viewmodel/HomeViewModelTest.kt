package com.nguyenminhkhang.taskmanagement.biz.viewmodel

import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsEvent
import com.nguyenminhkhang.taskmanagement.core.analytics.AnalyticsTracker
import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskCollection
import com.nguyenminhkhang.taskmanagement.data.local.database.entity.TaskEntity
import com.nguyenminhkhang.taskmanagement.domain.model.SortedType
import com.nguyenminhkhang.taskmanagement.domain.usecase.AddTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.DeleteTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.GetTaskGroupsUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.ToggleCompleteUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.ToggleTaskFavoriteUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.AddNewCollectionUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.DeleteTaskCollectionUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.GetTaskCollectionsUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.UpdateCollectionNameUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.UpdateCollectionSortTypeUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.syncusecase.SyncTasksUseCase
import com.nguyenminhkhang.taskmanagement.notification.TaskScheduler
import com.nguyenminhkhang.taskmanagement.ui.common.pagertab.state.TabUiState
import com.nguyenminhkhang.taskmanagement.ui.common.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.common.pagertab.state.TaskPageUiState
import com.nguyenminhkhang.taskmanagement.ui.common.pagertab.state.TaskUiState
import com.nguyenminhkhang.taskmanagement.ui.common.snackbar.SnackbarActionType
import com.nguyenminhkhang.taskmanagement.ui.common.stringprovider.StringProvider
import com.nguyenminhkhang.taskmanagement.ui.home.CollectionUseCases
import com.nguyenminhkhang.taskmanagement.ui.home.HomeViewModel
import com.nguyenminhkhang.taskmanagement.ui.home.TaskUseCases
import com.nguyenminhkhang.taskmanagement.ui.home.event.CollectionEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.MenuEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.TaskEvent
import com.nguyenminhkhang.taskmanagement.ui.home.event.UiEvent
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

	private val addTaskUseCase = mockk<AddTaskUseCase>()
	private val toggleTaskFavoriteUseCase = mockk<ToggleTaskFavoriteUseCase>()
	private val toggleCompleteUseCase = mockk<ToggleCompleteUseCase>()
	private val deleteTaskUseCase = mockk<DeleteTaskUseCase>()
	private val syncTasksUseCase = mockk<SyncTasksUseCase>()

	private val getTaskGroupsUseCase = mockk<GetTaskGroupsUseCase>()
	private val getTaskCollectionsUseCase = mockk<GetTaskCollectionsUseCase>()
	private val addNewCollectionUseCase = mockk<AddNewCollectionUseCase>()
	private val deleteTaskCollectionUseCase = mockk<DeleteTaskCollectionUseCase>()
	private val updateCollectionSortTypeUseCase = mockk<UpdateCollectionSortTypeUseCase>()
	private val updateCollectionNameUseCase = mockk<UpdateCollectionNameUseCase>()

	private val scheduler = mockk<TaskScheduler>()
	private val strings = mockk<StringProvider>()
	private val analyticsTracker = mockk<AnalyticsTracker>()

	private lateinit var listGroupsFlow: MutableStateFlow<List<TaskGroupUiState>>

	@Before
	fun setUp() {
		Dispatchers.setMain(UnconfinedTestDispatcher())

		listGroupsFlow = MutableStateFlow(emptyList())

		every { strings.getString(R.string.favorite_tab_name) } returns "Favorite"
		every { strings.getString(R.string.add_new_collecion) } returns "+ New"
		every { strings.getString(R.string.collection_one) } returns "Collection 1"
		every { strings.getString(R.string.collection_two) } returns "Collection 2"

		every { getTaskGroupsUseCase.invoke(any(), any()) } returns listGroupsFlow
		every { getTaskCollectionsUseCase.invoke() } returns flowOf(listOf(TaskCollection(id = 1L, content = "Inbox")))

		coEvery { addNewCollectionUseCase.invoke(any()) } returns TaskCollection(id = 10L, content = "New")
		coEvery { deleteTaskCollectionUseCase.invoke(any()) } returns true
		coEvery { updateCollectionSortTypeUseCase.invoke(any(), any()) } returns true
		coEvery { updateCollectionNameUseCase.invoke(any(), any()) } returns true

		coEvery {
			addTaskUseCase.invoke(
				content = any(),
				collectionId = any(),
				taskDetail = any(),
				isFavorite = any(),
				startDate = any(),
				startTime = any(),
				reminderTimeMillis = any()
			)
		} returns TaskEntity(id = 777L, content = "Saved")

		coEvery { toggleTaskFavoriteUseCase.invoke(any(), any()) } returns true
		coEvery { toggleCompleteUseCase.invoke(any(), any()) } returns true
		coEvery { deleteTaskUseCase.invoke(any()) } returns true

		every { syncTasksUseCase.invoke() } just Runs
		every { scheduler.schedule(any()) } just Runs
		every { scheduler.cancel(any()) } just Runs
		every { scheduler.scheduleDailyCheckIn(any()) } just Runs
		every { scheduler.scheduleInactiveReminder(any()) } just Runs
		every { scheduler.scheduleMissedTask(any(), any()) } just Runs
		every { analyticsTracker.trackEvent(any()) } just Runs
	}

	@After
	fun tearDown() {
		Dispatchers.resetMain()
		clearAllMocks()
	}

	@Test
	fun `homeViewModel should expose tab groups when groups flow emits`() = runTest {
		// Arrange
		val viewModel = createViewModel()
		val expectedGroups = listOf(createGroup(groupId = 5L))

		// Act
		listGroupsFlow.value = expectedGroups
		advanceUntilIdle()

		// Assert
		assertEquals(expectedGroups, viewModel.uiState.value.listTabGroup)
	}

	@Test
	fun `onScreenShown should track screen view event when called`() {
		// Arrange
		val viewModel = createViewModel()

		// Act
		viewModel.onScreenShown()

		// Assert
		verify { analyticsTracker.trackEvent(AnalyticsEvent.ScreenView("HomeScreen")) }
	}

	@Test
	fun `showAddTaskSheet should set sheet visible when current collection id is valid`() {
		// Arrange
		val viewModel = createViewModel()
		viewModel.onEvent(CollectionEvent.CurrentCollectionId(collectionId = 1L))

		// Act
		viewModel.onEvent(UiEvent.ShowAddTaskSheet)

		// Assert
		assertTrue(viewModel.uiState.value.isAddTaskSheetVisible)
	}

	@Test
	fun `showAddTaskSheet should keep sheet hidden when current collection id is invalid`() {
		// Arrange
		val viewModel = createViewModel()

		// Act
		viewModel.onEvent(UiEvent.ShowAddTaskSheet)

		// Assert
		assertFalse(viewModel.uiState.value.isAddTaskSheetVisible)
	}

	@Test
	fun `taskContentChanged should update new task content when event is dispatched`() {
		// Arrange
		val viewModel = createViewModel()

		// Act
		viewModel.onEvent(TaskEvent.TaskContentChanged(content = "Read a book"))

		// Assert
		assertEquals("Read a book", viewModel.uiState.value.newTask?.content)
	}

	@Test
	fun `saveNewTask should call add task and schedule reminder when reminder exists`() = runTest {
		// Arrange
		val viewModel = createViewModel()
		viewModel.onEvent(CollectionEvent.CurrentCollectionId(collectionId = 11L))
		viewModel.onEvent(TaskEvent.TaskContentChanged(content = "  Finish report  "))
		viewModel.onEvent(TaskEvent.TaskDetailChanged(detail = "monthly KPI"))
		viewModel.onEvent(TaskEvent.UpdateReminderTimeMillis(reminder = 1_900_000_000_000L))

		// Act
		viewModel.onEvent(TaskEvent.SaveNewTask)
		advanceUntilIdle()

		// Assert
		coVerify(timeout = 2_000) {
			addTaskUseCase.invoke(
				content = "Finish report",
				collectionId = 11L,
				taskDetail = "monthly KPI",
				isFavorite = false,
				startDate = null,
				startTime = null,
				reminderTimeMillis = 1_900_000_000_000L
			)
		}
		verify(timeout = 2_000) { scheduler.schedule(any()) }
		verify(timeout = 2_000) { analyticsTracker.trackEvent(AnalyticsEvent.AddTask(777L)) }
	}

	@Test
	fun `saveNewTask should not call add task when task content is blank`() = runTest {
		// Arrange
		val viewModel = createViewModel()
		viewModel.onEvent(CollectionEvent.CurrentCollectionId(collectionId = 11L))
		viewModel.onEvent(TaskEvent.TaskContentChanged(content = "   "))

		// Act
		viewModel.onEvent(TaskEvent.SaveNewTask)
		advanceUntilIdle()

		// Assert
		coVerify(exactly = 0) {
			addTaskUseCase.invoke(any(), any(), any(), any(), any(), any(), any())
		}
	}

	@Test
	fun `toggleFavorite should call toggle favorite use case with inverse value when event is dispatched`() = runTest {
		// Arrange
		val viewModel = createViewModel()
		val task = createTask(id = 3L, isFavorite = false, completed = false, collectionId = 1L)

		// Act
		viewModel.onEvent(TaskEvent.ToggleFavorite(task))
		advanceUntilIdle()

		// Assert
		coVerify(timeout = 2_000) { toggleTaskFavoriteUseCase.invoke(taskId = 3L, isFavorite = true) }
	}

	@Test
	fun `toggleComplete should move task to completed list and emit undo action when task is active`() = runTest {
		// Arrange
		val activeTask = createTask(id = 9L, completed = false, collectionId = 1L)
		val initialGroup = createGroup(
			groupId = 1L,
			active = listOf(activeTask),
			completed = emptyList()
		)
		listGroupsFlow.value = listOf(initialGroup)
		val viewModel = createViewModel()
		viewModel.onEvent(CollectionEvent.CurrentCollectionId(collectionId = 1L))

		// Act
		viewModel.onEvent(TaskEvent.ToggleComplete(activeTask))
		val snackBarEvent = viewModel.snackBarEvent.first()

		// Assert
		val updatedGroup = viewModel.uiState.value.listTabGroup.first()
		assertTrue(updatedGroup.page.activeTaskList.none { it.id == 9L })
		assertTrue(updatedGroup.page.completedTaskList.any { it.id == 9L })
		assertEquals(SnackbarActionType.UNDO_TOGGLE_COMPLETE, snackBarEvent.actionType)
	}

	@Test
	fun `undoToggleComplete should restore active list when undo event is dispatched`() = runTest {
		// Arrange
		val activeTask = createTask(id = 12L, completed = false, collectionId = 1L)
		listGroupsFlow.value = listOf(
			createGroup(
				groupId = 1L,
				active = listOf(activeTask),
				completed = emptyList()
			)
		)
		val viewModel = createViewModel()
		viewModel.onEvent(CollectionEvent.CurrentCollectionId(collectionId = 1L))
		viewModel.onEvent(TaskEvent.ToggleComplete(activeTask))

		// Act
		viewModel.onEvent(TaskEvent.UndoToggleComplete)

		// Assert
		val updatedGroup = viewModel.uiState.value.listTabGroup.first()
		assertTrue(updatedGroup.page.activeTaskList.any { it.id == 12L })
		assertTrue(updatedGroup.page.completedTaskList.none { it.id == 12L })
	}

	@Test
	fun `deleteTask should call delete use case and track analytics when event is dispatched`() = runTest {
		// Arrange
		val viewModel = createViewModel()

		// Act
		viewModel.onEvent(TaskEvent.DeleteTask(taskId = 15L))
		advanceUntilIdle()

		// Assert
		coVerify(timeout = 2_000) { deleteTaskUseCase.invoke(15L) }
		verify(timeout = 2_000) { analyticsTracker.trackEvent(AnalyticsEvent.DeleteTask(15L)) }
	}

	@Test
	fun `sortCollection should call update sort type use case when menu sort event is dispatched`() = runTest {
		// Arrange
		val viewModel = createViewModel()

		// Act
		viewModel.onEvent(MenuEvent.SortCollection(collectionId = 4L, sortedType = SortedType.SORTED_BY_FAVORITE))
		advanceUntilIdle()

		// Assert
		coVerify(timeout = 2_000) {
			updateCollectionSortTypeUseCase.invoke(4L, SortedType.SORTED_BY_FAVORITE)
		}
	}

	@Test
	fun `addNewCollectionRequested should call add collection and track analytics when name is valid`() = runTest {
		// Arrange
		val viewModel = createViewModel()

		// Act
		viewModel.onEvent(CollectionEvent.AddNewCollectionRequested(name = "Work"))
		advanceUntilIdle()

		// Assert
		coVerify(timeout = 2_000) { addNewCollectionUseCase.invoke("Work") }
		verify(timeout = 2_000) { analyticsTracker.trackEvent(AnalyticsEvent.CreateCollection("Work")) }
	}

	@Test
	fun `addNewCollectionRequested should not call add collection when name is blank`() = runTest {
		// Arrange
		val viewModel = createViewModel()

		// Act
		viewModel.onEvent(CollectionEvent.AddNewCollectionRequested(name = "  "))
		advanceUntilIdle()

		// Assert
		coVerify(exactly = 0) { addNewCollectionUseCase.invoke(any()) }
	}

	@Test
	fun `renameCollection should call update collection name with current id when rename event is dispatched`() = runTest {
		// Arrange
		val viewModel = createViewModel()
		viewModel.onEvent(CollectionEvent.CurrentCollectionId(collectionId = 20L))

		// Act
		viewModel.onEvent(CollectionEvent.RenameCollection(newCollectionName = "Personal"))
		advanceUntilIdle()

		// Assert
		coVerify(timeout = 2_000) { updateCollectionNameUseCase.invoke(20L, "Personal") }
		verify(timeout = 2_000) { analyticsTracker.trackEvent(AnalyticsEvent.RenameCollection(20L)) }
	}

	@Test
	fun `resetMenuListButtonSheet should clear menu list when reset event is dispatched`() {
		// Arrange
		val viewModel = createViewModel()

		// Act
		viewModel.onEvent(MenuEvent.ResetMenuListButtonSheet)

		// Assert
		assertEquals(null, viewModel.uiState.value.sortMenuButtonSheet)
	}

	private fun createViewModel(): HomeViewModel {
		val taskUseCases = TaskUseCases(
			addTask = addTaskUseCase,
			toggleFavorite = toggleTaskFavoriteUseCase,
			toggleComplete = toggleCompleteUseCase,
			deleteTask = deleteTaskUseCase,
			syncTasks = syncTasksUseCase
		)

		val collectionUseCases = CollectionUseCases(
			getGroups = getTaskGroupsUseCase,
			getCollections = getTaskCollectionsUseCase,
			addCollection = addNewCollectionUseCase,
			deleteCollection = deleteTaskCollectionUseCase,
			updateSortType = updateCollectionSortTypeUseCase,
			updateName = updateCollectionNameUseCase
		)

		return HomeViewModel(
			taskUseCases = taskUseCases,
			collectionUseCases = collectionUseCases,
			scheduler = scheduler,
			strings = strings,
			analyticsTracker = analyticsTracker
		)
	}

	private fun createGroup(
		groupId: Long,
		active: List<TaskUiState> = emptyList(),
		completed: List<TaskUiState> = emptyList()
	): TaskGroupUiState {
		return TaskGroupUiState(
			tab = TabUiState(
				id = groupId,
				title = "Group $groupId",
				sortedType = SortedType.SORTED_BY_DATE
			),
			page = TaskPageUiState(
				activeTaskList = active,
				completedTaskList = completed
			)
		)
	}

	private fun createTask(
		id: Long,
		isFavorite: Boolean = false,
		completed: Boolean,
		collectionId: Long
	): TaskUiState {
		return TaskUiState(
			id = id,
			content = "Task $id",
			isFavorite = isFavorite,
			completed = completed,
			collectionId = collectionId,
			createdAt = 1_700_000_000_000L,
			updatedAt = 1_700_000_000_001L,
			stringUpdateAt = "date",
			repeatEvery = 1L,
			repeatDaysOfWeek = null,
			startDate = null,
			repeatInterval = null,
			repeatEndType = null,
			repeatEndDate = null,
			repeatEndCount = 0,
			startTime = null,
			taskDetail = "",
			reminderTimeMillis = null
		)
	}
}

