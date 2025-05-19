package com.nguyenminhkhang.taskmanagement

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.theme.TaskManagementTheme
import com.nguyenminhkhang.taskmanagement.ui.theme.floataction.CustomFloatActionButton
import com.nguyenminhkhang.taskmanagement.ui.theme.topbar.TopBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainViewModel", "onCreate MainViewModel: $mainViewModel")
        enableEdgeToEdge()
        setContent {
            TaskManagementTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
                    CustomFloatActionButton(
                        modifier = Modifier
                            .background(
                                color = Color.Black,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .size(58.dp)
                            .clip(RoundedCornerShape(16.dp))
                    ) {
                        Log.d("CustomFloatActionButton", "CustomFloatActionButton clicked")
                    }
                }) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TopBar(
                            Modifier
                                .fillMaxWidth()
                                .height(58.dp)
                                .padding(horizontal = 12.dp))
                        var selectedTabIndex by remember { mutableStateOf(0)}
                        var pageCount by remember { mutableStateOf(3)}
                        val pagerState = rememberPagerState(
                            pageCount = { pageCount }
                        )
                        val scope = rememberCoroutineScope()
                        ScrollableTabRow(
                            pagerState.currentPage,
                            indicator = { tabPositions ->
                                TabRowDefaults.PrimaryIndicator(
                                    Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
                                    width = Dp.Unspecified,
                                )
                            },
                            edgePadding = 12.dp,

                        ) {
                            repeat(pageCount + 1) {tabIndex ->

                                Tab(
                                    text = { if(tabIndex < pageCount) Text("Tab $tabIndex") else Text("+ New list") },
                                    selected = (pagerState.currentPage == tabIndex),
                                    onClick = {
                                        Log.d("MainActivity", "Tab $tabIndex clicked")
                                        if(tabIndex == pageCount) {
                                            pageCount++
                                        }
                                        scope.launch {
                                            pagerState.scrollToPage(tabIndex)
                                        }
                                    }
                                )
                            }
                        }
                        HorizontalPager( pagerState
                        ) { pageIndex ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Page: $pageIndex",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
                }
            }
        }
    }
}