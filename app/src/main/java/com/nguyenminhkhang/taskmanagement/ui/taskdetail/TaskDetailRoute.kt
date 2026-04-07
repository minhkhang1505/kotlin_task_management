package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.effects.TaskDetailEffect
import com.nguyenminhkhang.taskmanagement.ui.taskdetail.events.NavigationEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskDetailRoute(
    onNavigateToRepeat: (Long) -> Unit,
    onPopBackStack: () -> Unit,
) {
    val taskDetailViewModel: TaskDetailViewModel = koinViewModel()
    val uiState by taskDetailViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        taskDetailViewModel.navigationEvent.collect { event ->
            when (event) {
                is NavigationEvent.NavigateBackWithResult -> {
                    onPopBackStack()
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        taskDetailViewModel.effect.collect { effect ->
            when (effect) {
                is TaskDetailEffect.OpenCalendar -> {
                    val intent = Intent(Intent.ACTION_INSERT).apply {
                        data = CalendarContract.Events.CONTENT_URI
                        putExtra(CalendarContract.Events.TITLE, effect.task.content)
                        putExtra(CalendarContract.Events.DESCRIPTION, effect.task.taskDetail)
                        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, effect.startTimeMillis)
                        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, effect.endTimeMillis)
                    }

                    if (intent.resolveActivity(context.packageManager) != null) {
                        context.startActivity(intent)
                        Toast.makeText(context, "Task added to calendar", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "No calendar app found", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    if (uiState.isLoading) {
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
        }
    } else {
        TaskDetailScreen(
            uiState = uiState,
            onPopBackStack = onPopBackStack,
            onNavigateToRepeat = onNavigateToRepeat,
            onEvent = taskDetailViewModel::onEvent,
            onScreenShow = taskDetailViewModel::onScreenShow
        )
    }
}