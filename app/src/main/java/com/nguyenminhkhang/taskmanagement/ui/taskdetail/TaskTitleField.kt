package com.nguyenminhkhang.taskmanagement.ui.taskdetail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R

@Composable
fun TaskTitleField(
    title: String,
    isInEditMode: Boolean,
    onTitleChange: (String) -> Unit,
    onEnterEditMode: () -> Unit,
    onExitEditMode: () -> Unit,
    onSave: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        value = title,
        onValueChange = onTitleChange,
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    onExitEditMode()
                }
            },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent
        ),
        textStyle = MaterialTheme.typography.titleLarge.copy(
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        ),
        trailingIcon = {
            Icon(
                painter = painterResource(if (isInEditMode) {
                    R.drawable.baseline_done_all_24
                } else {
                    R.drawable.baseline_edit_24}),
                contentDescription = "Edit Title",
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable {
                        if (isInEditMode) {
                            onSave()
                        } else {
                            onEnterEditMode()
                        }
                    }
            )
        },
        maxLines = 1,
        enabled = isInEditMode
    )
}