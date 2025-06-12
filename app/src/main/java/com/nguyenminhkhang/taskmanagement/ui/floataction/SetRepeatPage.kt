package com.nguyenminhkhang.taskmanagement.ui.floataction

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetRepeatPage() {
    val dayItems = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Repeat") },
                navigationIcon = {
                    Icon(Icons.Default.Clear, contentDescription = "Back",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { /* Handle back action */ }
                    )
                },
                actions = {
                    Text("Save",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { /* Handle save action */ }
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Every")
            Row (
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = "1",
                    onValueChange = { /* Handle text change */ },
                    keyboardOptions = KeyboardOptions(keyboardType =  KeyboardType.Number),
                    modifier = Modifier
                        .weight(0.2f)
                        .padding(end = 8.dp),
                )
                OutlinedTextField(
                    value = "Daily",
                    onValueChange = { /* Handle text change */ },
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(0.8f),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                dayItems.forEach { day ->
                    DayItem(day, isSelected = false, onClick = {
                        // Handle day selection
                    })
                }
            }
            OutlinedTextField(
                value = "12:00",
                onValueChange = { /* Handle text change */ },
                modifier = Modifier
                    .padding(end = 8.dp).fillMaxWidth(),
                trailingIcon = {
                    Icon(Icons.Default.Clear, contentDescription = "Clear",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { /* Handle clear action */ }
                    )
                },
            )
            Text(text = "Start Date",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            OutlinedTextField(
                value = "MM/DD/YYYY",
                onValueChange = { /* Handle text change */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Text(text = "Other Options",
                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
            )
            Column (
                modifier = Modifier.selectableGroup()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = true,
                        onClick = { /* Handle radio button click */ },
                        modifier = Modifier.padding(end = 8.dp).semantics { contentDescription = "Never" },
                    )
                    Text(text = "Never",
                        modifier = Modifier.padding(end = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = true,
                        onClick = { /* Handle radio button click */ },
                        modifier = Modifier.padding(end = 8.dp).semantics { contentDescription = "Never" },
                    )
                    Text(text = "On",
                        modifier = Modifier.padding(end = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    OutlinedTextField(
                        value = "MM/DD/YYYY",
                        onValueChange = { /* Handle text change */ },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = true,
                        onClick = { /* Handle radio button click */ },
                        modifier = Modifier.padding(end = 8.dp).semantics { contentDescription = "Never" },
                    )
                    Text(text = "After",
                        modifier = Modifier.padding(end = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    OutlinedTextField(
                        value = "1",
                        onValueChange = { /* Handle text change */ },
                        modifier = Modifier
                            .weight(0.2f)
                            .padding(end = 8.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Text(text = "occurrence",
                        modifier = Modifier.padding(end = 8.dp).weight(0.6f),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun DayItem(day: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.border(
            width = 1.dp,
            shape = RoundedCornerShape(50.dp),
            color = MaterialTheme.colorScheme.primary,
        ).size(45.dp)
            .clickable { onClick() }
            .then(if (isSelected) Modifier.background(shape = RoundedCornerShape(50.dp), color = MaterialTheme.colorScheme.primary) else Modifier),
        contentAlignment = Alignment.Center
    ) {
        Text(text = day,
            modifier = Modifier
                .padding(8.dp),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
fun SetRepeatPagePreview() {
    SetRepeatPage()
}