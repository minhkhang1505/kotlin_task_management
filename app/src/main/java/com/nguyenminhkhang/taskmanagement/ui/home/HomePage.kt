package com.nguyenminhkhang.taskmanagement.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nguyenminhkhang.taskmanagement.MainEvent
import com.nguyenminhkhang.taskmanagement.MainViewModel
import com.nguyenminhkhang.taskmanagement.ui.floataction.AppFloatActionButton
import com.nguyenminhkhang.taskmanagement.ui.pagertab.PagerTabLayout
import com.nguyenminhkhang.taskmanagement.ui.topbar.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeLayout(mainViewModel: MainViewModel = hiltViewModel()) {
    val listTabGroup by mainViewModel.listTabGroup.collectAsStateWithLifecycle(emptyList())
    val taskDelegate = remember { mainViewModel }
    var isShowAddNoteButtonSheet by remember { mutableStateOf(false) }
    var isShowAddNewCollectionButton by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mainViewModel.eventFlow.collect {
            when(it) {
                MainEvent.RequestAddNewCollection -> {
                    isShowAddNewCollectionButton = true
                }
                is MainEvent.RequestShowButtonSheetOption -> {
                    Log.d("HomeLayout", "RequestShowButtonSheetOption: ${it}")
                }
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
        AppFloatActionButton{
            isShowAddNoteButtonSheet = taskDelegate.currentCollectionId() > 0
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(taskDelegate)
            PagerTabLayout( listTabGroup, taskDelegate)
        }

        if(isShowAddNoteButtonSheet) {
            var inputTaskContent by remember { mutableStateOf("") }

            ModalBottomSheet({
                isShowAddNoteButtonSheet = false
            }) {
                Text("Input task Content",
                    modifier = Modifier.padding(16.dp).fillMaxWidth())
                TextField(value=inputTaskContent, onValueChange = { inputTaskContent = it },
                    modifier = Modifier.padding(16.dp).fillMaxWidth())
                Button({
                    if(inputTaskContent.isNotEmpty()) {
                        taskDelegate.addNewTaskToCurrentCollection(inputTaskContent)
                        inputTaskContent = ""
                    }
                    isShowAddNoteButtonSheet = false
                }, modifier =  Modifier.padding(16.dp).fillMaxWidth()) {
                    Text("Add Task")
                }
            }
        }

        if(isShowAddNewCollectionButton) {
            var inputTaskCollection by remember { mutableStateOf("") }

            ModalBottomSheet({
                isShowAddNewCollectionButton = false
            }) {
                Text("Input task Collection",
                    modifier = Modifier.padding(16.dp).fillMaxWidth())
                TextField(value=inputTaskCollection, onValueChange = { inputTaskCollection = it },
                    modifier = Modifier.padding(16.dp).fillMaxWidth())
                Button({
                    if(inputTaskCollection.isNotEmpty()) {
                        taskDelegate.addNewCollection(inputTaskCollection)
                        inputTaskCollection = ""
                    }
                    isShowAddNewCollectionButton = false
                }, modifier =  Modifier.padding(16.dp).fillMaxWidth()) {
                    Text("Add collection")
                }
            }
        }
    }
}