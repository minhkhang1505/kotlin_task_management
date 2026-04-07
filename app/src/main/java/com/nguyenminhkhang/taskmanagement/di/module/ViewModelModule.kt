package com.nguyenminhkhang.taskmanagement.di.module

import com.nguyenminhkhang.taskmanagement.domain.usecase.auth.ObserveAuthStateUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.auth.SignOutUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.settings.ObserveSettingsUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.settings.TrackSettingScreenViewUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.settings.UpdateColorThemeUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.settings.UpdateFontStyleUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.settings.UpdateLanguageUseCase
import com.nguyenminhkhang.taskmanagement.domain.usecase.settings.UpdateThemeModeUseCase
import com.nguyenminhkhang.taskmanagement.ui.auth.register.RegisterViewModel
import com.nguyenminhkhang.taskmanagement.ui.auth.signin.SignInViewModel
import com.nguyenminhkhang.taskmanagement.ui.home.HomeViewModel
import com.nguyenminhkhang.taskmanagement.ui.repeat.RepeatViewModel
import com.nguyenminhkhang.taskmanagement.ui.search.SearchViewModel
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.SettingViewModel
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.TaskDetailViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(
            searchTasksUseCase = get(),
            getTodayTasksUseCase = get(),
            toggleTaskFavoriteUseCase = get(),
            analyticsTracker = get()
        )
    }

    viewModel {
        RepeatViewModel(
            getTaskUseCase = get(),
            updateRepeatTaskUseCase = get(),
            trackRepeatScreenViewUseCase = get(),
            savedStateHandle = get()
        )
    }

    viewModel {
        SettingViewModel(
            observeAuthStateUseCase = get(),
            signOutUseCase= get(),
            observeSettingsUseCase= get(),
            updateLanguageUseCase= get(),
            updateThemeModeUseCase= get(),
            updateFontStyleUseCase= get(),
            updateColorThemeUseCase= get(),
            trackSettingScreenViewUseCase= get()
        )
    }

    viewModel {
        TaskDetailViewModel(
            getTaskByIdUseCase = get(),
            getTaskCollectionsUseCase = get(),
            toggleCompleteUseCase = get(),
            toggleTaskFavoriteUseCase = get(),
            updateTaskUseCase = get(),
            moveTaskToCollectionUseCase = get(),
            buildRepeatSummaryTextUseCase = get(),
            trackTaskDetailScreenViewUseCase = get(),
            savedStateHandle = get()
        )
    }

    viewModelOf(::HomeViewModel)

    viewModel {
        RegisterViewModel(
            registerUserUseCase = get()
        )
    }

    viewModel {
        SignInViewModel(
            processUserSignInUseCase = get()
        )
    }
}