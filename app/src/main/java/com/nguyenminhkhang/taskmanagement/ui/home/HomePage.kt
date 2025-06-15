package com.nguyenminhkhang.taskmanagement.ui.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.TaskDelegate
import com.nguyenminhkhang.taskmanagement.ui.AppMenuItem
import com.nguyenminhkhang.taskmanagement.ui.RoundedOutlinedTextField
import com.nguyenminhkhang.taskmanagement.ui.datepicker.DatePickerModal
import com.nguyenminhkhang.taskmanagement.ui.datepicker.TimePickerModal
import com.nguyenminhkhang.taskmanagement.ui.floataction.AppFloatActionButton
import com.nguyenminhkhang.taskmanagement.ui.pagertab.PagerTabLayout
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TaskGroupUiState
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.toHourMinuteString
import com.nguyenminhkhang.taskmanagement.ui.topbar.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeLayout(
    listTabGroup: List<TaskGroupUiState>,
    taskDelegate: TaskDelegate,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    var isShowAddNoteButtonSheet by remember { mutableStateOf(false) }
    var isShowAddNewCollectionButton by remember { mutableStateOf(false) }
    var menuListButtonSheet by remember{mutableStateOf<List<AppMenuItem>?>(null) }
    var isShowDetailTextField by remember { mutableStateOf(false) }
    var inputTaskContent by remember { mutableStateOf("") }
    var inputTaskDetailContent by remember { mutableStateOf("") }
    var isShowDatePickerModel by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var isShowTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("") }
    var contentDateTime by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            AppFloatActionButton{
                isShowAddNoteButtonSheet = taskDelegate.currentCollectionId() > 0
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(taskDelegate)
            PagerTabLayout( listTabGroup, taskDelegate, navController)
        }

        if(isShowAddNoteButtonSheet) {

            ModalBottomSheet({
                isShowAddNoteButtonSheet = false
            }) {
                TextField(
                    value=inputTaskContent,
                    onValueChange = { inputTaskContent = it },
                    placeholder = { Text("What’s your next task?", style = TextStyle(color = Color.Gray.copy(0.5f))) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp)),
                    maxLines = 1,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                if(selectedDate.isNotEmpty() || selectedTime.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .fillMaxWidth(),
                    ) {
                        contentDateTime = "$selectedDate, $selectedTime"
                        RoundedOutlinedTextField(contentDateTime, onClick = {
                            selectedDate = ""
                            selectedTime = ""
                        })
                    }
                }
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        IconButton(
                            onClick = {isShowDetailTextField = !isShowDetailTextField}
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu Icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                        IconButton(
                            onClick = { isShowDatePickerModel = !isShowDatePickerModel }
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = "Date Icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                        IconButton(
                            onClick = { isShowTimePicker = !isShowTimePicker }
                        ) {
                            Icon(painter = painterResource(R.drawable.baseline_access_time_24), contentDescription = "Time Icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                        IconButton(
                            onClick = {
                                isFavorite = !isFavorite
                            }
                        ) {
                            Log.d("HomeLayout", "isFavorite: $isFavorite")
                            Icon(painter = if(isFavorite) {
                                painterResource(R.drawable.baseline_star_24)
                            } else {
                                painterResource(R.drawable.baseline_star_outline_24)
                            }, contentDescription = "Favorite Icon",
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        }
                    }
                    Button({
                        if(inputTaskContent.isNotEmpty()) {
                            taskDelegate.addNewTaskToCurrentCollection(inputTaskContent)
                            inputTaskContent = ""
                        }
                        isShowAddNoteButtonSheet = false
                    }) {
                        Text("Save")
                    }

                }

            }
        }

        if(isShowAddNewCollectionButton) {
            var inputTaskCollection by remember { mutableStateOf("") }

            ModalBottomSheet({
                isShowAddNewCollectionButton = false
            }) {
                Text("Input task Collection",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth())
                TextField(value=inputTaskCollection, onValueChange = { inputTaskCollection = it },
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth())

                Button({
                    if(inputTaskCollection.isNotEmpty()) {
                        taskDelegate.addNewCollection(inputTaskCollection)
                        inputTaskCollection = ""
                    }
                    isShowAddNewCollectionButton = false
                }, modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()) {
                    Text("Add collection")
                }
            }
        }

        if(menuListButtonSheet.isNullOrEmpty()== false) {
            ModalBottomSheet(
                onDismissRequest = {
                    menuListButtonSheet = null
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    menuListButtonSheet?.forEach { item ->
                        Text(item.title, modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                item.action.invoke()
                                menuListButtonSheet = null
                            }
                            .padding(12.dp))
                    }
                }
            }

        }

        if(isShowDetailTextField) {
            Text("Detail", modifier = Modifier.padding(horizontal = 16.dp))
            TextField(
                value=inputTaskDetailContent,
                onValueChange = { inputTaskDetailContent = it },
                placeholder = { Text("Add detail", style = TextStyle(fontSize = 12.sp, color = Color.Gray.copy(0.5f))) },
                modifier = Modifier
                    .padding(horizontal = 16.dp,)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(fontSize = 12.sp)
            )
        }

        if(isShowDatePickerModel) {
            DatePickerModal(
                onDismiss = { isShowDatePickerModel = false },
                onDateSelected = { date ->
                    // Handle the selected date here
                    selectedDate = date
                }
            )
        }

        if (isShowTimePicker) {
            TimePickerModal(onDismiss = {isShowTimePicker = false},
                onConfirm = { timePickerState ->
                    selectedTime = timePickerState.toHourMinuteString()
                } )
        }
    }
}