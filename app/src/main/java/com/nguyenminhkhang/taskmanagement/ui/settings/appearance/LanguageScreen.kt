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
import com.nguyenminhkhang.taskmanagement.ui.settings.LanguageOption
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.AccountEvent
import com.nguyenminhkhang.taskmanagement.ui.settings.settings.state.SettingUiState
import timber.log.Timber

@Composable
fun LanguageScreen(
    uiState : SettingUiState,
    onEvent: (AccountEvent) -> Unit,
    onPopBackStack: () -> Unit
) {
    val languageOptions = listOf(
        LanguageOption.ENGLISH,
        LanguageOption.VIETNAMESE
    )

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
            Text(stringResource(R.string.account_language), style = MaterialTheme.typography.titleLarge)
        }
        languageOptions.forEach { languageItem ->
            val languageLabel = when (languageItem) {
                LanguageOption.ENGLISH -> stringResource(R.string.language_english)
                LanguageOption.VIETNAMESE -> stringResource(R.string.language_vietnamese)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(start = 16.dp, end = 2.dp).clickable{
                        Timber.tag("LanguageScreen").d("Row clicked - language=%s", languageItem.code)
                        onEvent(AccountEvent.LanguageChanged(languageItem))
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = languageLabel, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(start = 4.dp))
                RadioButton(
                    selected =( uiState.languageRadioOption == languageItem.code),
                    onClick = {
                        Timber.tag("LanguageScreen").d("Radio clicked - language=%s", languageItem.code)
                        onEvent(AccountEvent.LanguageChanged(languageItem))
                    },
                )
            }
        }
    }
}