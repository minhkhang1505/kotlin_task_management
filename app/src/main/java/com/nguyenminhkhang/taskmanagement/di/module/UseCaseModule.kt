package com.nguyenminhkhang.taskmanagement.di.module

import com.nguyenminhkhang.taskmanagement.domain.usecase.AddTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.DeleteTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.GetTaskByIdUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.GetTasksUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.GetTaskGroupsUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.ToggleCompleteUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.ToggleTaskFavoriteUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.UpdateTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.UpdateTaskPropertiesUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.auth.ObserveAuthStateUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.auth.ProcessUserSignInUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.auth.RegisterUserUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.auth.SignOutUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.AddNewCollectionUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.DeleteTaskCollectionUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.GetTaskCollectionsUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.MoveTaskToCollectionUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.UpdateCollectionNameUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.collectionusecase.UpdateCollectionSortTypeUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.home.CombineDateAndTimeUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.repeat.GetTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.repeat.TrackRepeatScreenViewUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.repeat.UpdateRepeatTaskUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.search.GetTodayTasksUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.search.SearchTasksUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.settings.ObserveSettingsUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.settings.TrackSettingScreenViewUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.settings.UpdateColorThemeUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.settings.UpdateFontStyleUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.settings.UpdateLanguageUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.settings.UpdateThemeModeUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.syncusecase.SyncTasksUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.taskdetail.BuildRepeatSummaryTextUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.taskdetail.TrackTaskDetailScreenViewUseCase
import com.nguyenminhkhang.taskmanagement.ui.home.CollectionUseCases
import com.nguyenminhkhang.taskmanagement.ui.home.TaskUseCases
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::SearchTasksUseCase)
    factoryOf(::GetTodayTasksUseCase)
    factoryOf(::GetTasksUseCase)
    factoryOf(::ToggleTaskFavoriteUseCase)

    factoryOf(::GetTaskUseCase)
    factoryOf(::UpdateRepeatTaskUseCase)
    factoryOf(::TrackRepeatScreenViewUseCase)

    factoryOf(::ObserveAuthStateUseCase)
    factoryOf(::SignOutUseCase)
    factoryOf(::ObserveSettingsUseCase)
    factoryOf(::UpdateLanguageUseCase)
    factoryOf(::UpdateThemeModeUseCase)
    factoryOf(::UpdateFontStyleUseCase)
    factoryOf(::UpdateColorThemeUseCase)
    factoryOf(::TrackSettingScreenViewUseCase)

    factoryOf(::GetTaskByIdUseCase)
    factoryOf(::GetTaskCollectionsUseCase)
    factoryOf(::ToggleCompleteUseCase)
    factoryOf(::UpdateTaskUseCase)
    factoryOf(::UpdateTaskPropertiesUseCase)
    factoryOf(::MoveTaskToCollectionUseCase)
    factoryOf(::BuildRepeatSummaryTextUseCase)
    factoryOf(::TrackTaskDetailScreenViewUseCase)

    factoryOf(::AddTaskUseCase)
    factoryOf(::DeleteTaskUseCase)
    factoryOf(::SyncTasksUseCase)
    factoryOf(::CombineDateAndTimeUseCase)

    factoryOf(::GetTaskGroupsUseCase)
    factoryOf(::AddNewCollectionUseCase)
    factoryOf(::DeleteTaskCollectionUseCase)
    factoryOf(::UpdateCollectionSortTypeUseCase)
    factoryOf(::UpdateCollectionNameUseCase)

    factoryOf(::TaskUseCases)
    factoryOf(::CollectionUseCases)

    factoryOf(::RegisterUserUseCase)
    factoryOf(::ProcessUserSignInUseCase)
}