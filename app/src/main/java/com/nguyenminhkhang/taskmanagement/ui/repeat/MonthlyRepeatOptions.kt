package com.nguyenminhkhang.taskmanagement.ui.repeat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.common.DateDropDownMenu
import com.nguyenminhkhang.taskmanagement.ui.common.DayDropDownMenu
import com.nguyenminhkhang.taskmanagement.ui.repeat.state.RepeatUiState

@Composable
fun MonthlyRepeatOptions(
    uiState: RepeatUiState,
    onEvent: (RepeatEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().selectableGroup()
    ) {
        uiState.monthRepeatOptions.forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == uiState.selectedMonthRepeatOption),
                    onClick = { onEvent(RepeatEvent.MonthRepeatOptionChanged(option)) },
                    modifier = Modifier.padding(end = 8.dp)
                )
                when(option) {
                    "OnDate" -> {
                        Box(modifier = Modifier.fillMaxWidth()
                            .selectable(
                                selected = ("OnDate" == uiState.selectedMonthRepeatOption),
                                onClick = { onEvent(RepeatEvent.MonthRepeatOptionChanged("OnDate")) },
                                role = Role.RadioButton
                            )
                        ) {
                            DateDropDownMenu(
                                selectedDay = uiState.selectedDayInMonth,
                                onDaySelected = { day -> onEvent(RepeatEvent.DayInMonthChanged(day)) },
                            )
                        }
                    }
                    "OnDay" -> {
                        Row(
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            DayDropDownMenu(
                                selectedDay = uiState.selectedWeekOrder,
                                onDaySelected = { order -> onEvent(RepeatEvent.WeekOrderChanged(order)) },
                                data = uiState.weekOrderOptions
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DayDropDownMenu(
                                selectedDay = uiState.selectedWeekDay,
                                onDaySelected = { day -> onEvent(RepeatEvent.WeekDayChanged(day)) },
                                data = uiState.weekDayOptions
                            )
                        }
                    }
                }
            }
        }
    }
}