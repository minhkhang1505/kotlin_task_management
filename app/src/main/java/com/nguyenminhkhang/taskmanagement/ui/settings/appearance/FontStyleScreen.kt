package com.nguyenminhkhang.taskmanagement.ui.settings.appearance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.settings.FontStyleOption
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.AccountEvent
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.state.SettingUiState
import timber.log.Timber

@Composable
fun FontStyleScreen(
    uiState: SettingUiState,
    onEvent: (AccountEvent) -> Unit,
    onPopBackStack: () -> Unit
) {
    val fontOptions = FontStyleOption.entries

    Column(
        modifier = Modifier
            .fillMaxSize()
            .selectableGroup(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onPopBackStack) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Back", modifier = Modifier.size(32.dp))
            }
            Text(stringResource(R.string.account_font_style), style = MaterialTheme.typography.titleLarge)
        }
        fontOptions.forEach { fontItem ->
            val fontLabel = stringResource(fontItem.labelRes)

            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(start = 16.dp, end = 2.dp).clickable {
                        Timber.tag("FontStyleScreen").d("Row clicked - fontStyle=%s", fontItem.key)
                        onEvent(AccountEvent.FontStyleChanged(fontItem))
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = fontLabel, 
                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = fontItem.fontFamily), 
                    modifier = Modifier.padding(start = 4.dp)
                )
                RadioButton(
                    selected = (uiState.fontStyleOption == fontItem.key),
                    onClick = {
                        Timber.tag("FontStyleScreen").d("Radio clicked - fontStyle=%s", fontItem.key)
                        onEvent(AccountEvent.FontStyleChanged(fontItem))
                    },
                )
            }
        }
    }
}
