package com.nguyenminhkhang.taskmanagement.ui.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.account.state.ThemeModeUiState

@Composable
fun ThemeModeLayout(
    themeModeUiState: ThemeModeUiState,
    onEvent: (AccountEvent) -> Unit,
    navController: NavController
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .size(56.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Back", modifier = Modifier.size(32.dp))
            }
            Text(
                text = stringResource(R.string.theme_page_title),
                style = MaterialTheme.typography.titleLarge,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.selectableGroup().padding(start = 16.dp, end = 2.dp)
        ) {
            Text(text = stringResource(R.string.appearance), style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            themeModeUiState.radioOptions.forEach { option ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = stringResource(option), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 4.dp))
                    RadioButton(
                        selected = (option == themeModeUiState.selectedOptionRes),
                        onClick = {
                            onEvent(AccountEvent.ThemeModeChanged(option))
                            onEvent(AccountEvent.SaveThemeMode(option))
                        }
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}