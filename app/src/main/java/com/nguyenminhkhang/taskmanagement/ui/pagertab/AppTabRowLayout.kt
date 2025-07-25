package com.nguyenminhkhang.taskmanagement.ui.pagertab

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TabUiState

@Composable
fun AppTabRowLayout(
    selectedTabIndex: PagerState,
    listTabs: List<TabUiState>,
    onTabSelected: (Int) -> Unit
) {
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        CustomIndicator(tabPositions, selectedTabIndex)
    }
    if (listTabs.isNotEmpty() && selectedTabIndex.currentPage in listTabs.indices) {
        if (listTabs.size > 3) {
            Log.d("AppTabRowLayout", "tabPositions: ${listTabs[selectedTabIndex.currentPage].id}")

            ScrollableTabRow(
                selectedTabIndex.currentPage,
                indicator = indicator,
                edgePadding = 0.dp,
                divider = {
                    Divider(
                        color = Color.Transparent,
                        thickness = 0.dp
                    )
                },
                modifier = Modifier.clip(RoundedCornerShape(50.dp)).fillMaxWidth().background(color = MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {
                repeat(listTabs.size) { tabIndex ->

                    TabItemLayout(
                        state = listTabs[tabIndex],
                        isSelected = (selectedTabIndex.currentPage == tabIndex),
                        onTabSelected = { onTabSelected(tabIndex) }
                    )
                }
            }
        } else {
            TabRow(
                selectedTabIndex.currentPage,
                indicator = indicator,
                divider = {
                    Divider(
                        color = Color.Transparent,
                        thickness = 0.dp
                    )
                },
                modifier = Modifier.clip(RoundedCornerShape(50.dp)).fillMaxWidth().background(color = MaterialTheme.colorScheme.surfaceContainerHigh),
            ) {
                repeat(listTabs.size) { tabIndex ->
                    TabItemLayout(
                        state = listTabs[tabIndex],
                        isSelected = (selectedTabIndex.currentPage == tabIndex),
                        onTabSelected = { onTabSelected(tabIndex) }
                    )
                }
            }
        }
    }
}