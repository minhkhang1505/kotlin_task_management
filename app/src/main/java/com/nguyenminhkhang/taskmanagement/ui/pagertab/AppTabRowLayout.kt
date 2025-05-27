package com.nguyenminhkhang.taskmanagement.ui.pagertab

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.ui.pagertab.state.TabUiState

@Composable
fun AppTabRowLayout(
    selectedTabIndex: Int,
    listTabs: List<TabUiState>,
    onTabSelected: (Int) -> Unit
) {
    if (listTabs.isNotEmpty() && selectedTabIndex in listTabs.indices) {
        if (listTabs.size > 3) {
            ScrollableTabRow(
                selectedTabIndex,
                indicator = { tabPositions ->
                    TabRowDefaults.PrimaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        width = Dp.Unspecified,
                    )
                },
                edgePadding = 12.dp,
            ) {
                repeat(listTabs.size) { tabIndex ->

                    TabItemLayout(
                        state = listTabs[tabIndex],
                        isSelected = (selectedTabIndex == tabIndex),
                        onTabSelected = { onTabSelected(tabIndex) }
                    )
                }
            }
        } else {
            TabRow(
                selectedTabIndex,
                modifier = Modifier.fillMaxWidth(),
                indicator = { tabPositions ->
                    TabRowDefaults.PrimaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        width = Dp.Unspecified
                    )
                }
            ) {
                repeat(listTabs.size) { tabIndex ->
                    TabItemLayout(
                        state = listTabs[tabIndex],
                        isSelected = (selectedTabIndex == tabIndex),
                        onTabSelected = { onTabSelected(tabIndex) }
                    )
                }
            }
        }
    }
}
