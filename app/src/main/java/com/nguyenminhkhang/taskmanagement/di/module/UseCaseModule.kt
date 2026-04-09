package com.nguyenminhkhang.taskmanagement.di.module

import com.nguyenminhkhang.shared.usecase.AddTaskUseCase
import com.nguyenminhkhang.shared.usecase.DeleteTaskUseCase
import com.nguyenminhkhang.shared.usecase.GetTaskByIdUseCase
import com.nguyenminhkhang.shared.usecase.GetTaskGroupsUseCase
import com.nguyenminhkhang.shared.usecase.GetTasksUseCase
import com.nguyenminhkhang.shared.usecase.ToggleCompleteUseCase
import com.nguyenminhkhang.shared.usecase.ToggleTaskFavoriteUseCase
import com.nguyenminhkhang.shared.usecase.UpdateTaskPropertiesUseCase
import com.nguyenminhkhang.shared.usecase.UpdateTaskUseCase
import com.nguyenminhkhang.shared.usecase.auth.ObserveAuthStateUseCase
import com.nguyenminhkhang.shared.usecase.auth.ProcessUserSignInUseCase
import com.nguyenminhkhang.shared.usecase.auth.RegisterUserUseCase
import com.nguyenminhkhang.shared.usecase.auth.SignOutUseCase
import com.nguyenminhkhang.shared.usecase.collectionusecase.AddNewCollectionUseCase
import com.nguyenminhkhang.shared.usecase.collectionusecase.DeleteTaskCollectionUseCase
import com.nguyenminhkhang.shared.usecase.collectionusecase.GetTaskCollectionsUseCase
import com.nguyenminhkhang.shared.usecase.collectionusecase.MoveTaskToCollectionUseCase
import com.nguyenminhkhang.shared.usecase.collectionusecase.UpdateCollectionNameUseCase
import com.nguyenminhkhang.shared.usecase.collectionusecase.UpdateCollectionSortTypeUseCase
import com.nguyenminhkhang.shared.usecase.home.CombineDateAndTimeUseCase
import com.nguyenminhkhang.shared.usecase.repeat.GetTaskUseCase
import com.nguyenminhkhang.shared.usecase.repeat.TrackRepeatScreenViewUseCase
import com.nguyenminhkhang.shared.usecase.repeat.UpdateRepeatTaskUseCase
import com.nguyenminhkhang.shared.usecase.search.GetTodayTasksUseCase
import com.nguyenminhkhang.shared.usecase.search.SearchTasksUseCase
import com.nguyenminhkhang.shared.usecase.settings.ObserveSettingsUseCase
import com.nguyenminhkhang.shared.usecase.settings.TrackSettingScreenViewUseCase
import com.nguyenminhkhang.shared.usecase.settings.UpdateFontStyleUseCase
import com.nguyenminhkhang.shared.usecase.settings.UpdateColorThemeUseCase
import com.nguyenminhkhang.shared.usecase.settings.UpdateLanguageUseCase
import com.nguyenminhkhang.shared.usecase.settings.UpdateThemeModeUseCase
import com.nguyenminhkhang.shared.usecase.syncusecase.SyncTasksUseCase
import com.nguyenminhkhang.shared.usecase.taskdetail.BuildRepeatSummaryTextUseCase
import com.nguyenminhkhang.shared.usecase.taskdetail.TrackTaskDetailScreenViewUseCase
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