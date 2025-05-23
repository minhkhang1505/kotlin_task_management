package com.nguyenminhkhang.taskmanagement

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nguyenminhkhang.taskmanagement.ui.theme.TaskManagementTheme
import com.nguyenminhkhang.taskmanagement.ui.floataction.CustomFloatActionButton
import com.nguyenminhkhang.taskmanagement.ui.pagertab.PagerTabLayout
import com.nguyenminhkhang.taskmanagement.ui.topbar.TopBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel : MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainViewModel", "onCreate MainViewModel: $mainViewModel")
        enableEdgeToEdge()
        setContent {
            TaskManagementTheme {
                val listTabGroup by mainViewModel.listTabGroup.collectAsStateWithLifecycle()
                val taskDelegate = remember { mainViewModel }
                var isShowAddNoteButtonSheet by remember { mutableStateOf(false) }
                Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
                    CustomFloatActionButton {
                        isShowAddNoteButtonSheet = true
                    }
                }) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TopBar()
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
                }
            }
        }
    }
}