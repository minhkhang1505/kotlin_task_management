package com.nguyenminhkhang.taskmanagement.ui.settings.account

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.settings.account.state.AccountUiState

@Composable
fun AccountLayout(
    accountUiState: AccountUiState,
    onEvent: (AccountEvent) -> Unit,
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.bottom_bar_ic_account),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            Avatar(
                imageUrl = accountUiState.userAvatarUrl,
                initials = accountUiState.userEmail ?: ""
            )
            Text(
                text = accountUiState.userEmail ?: "No email",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp)
                    .clickable {
                        navController.navigate("ThemeMode")
                    }
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(10.dp)
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_image_aspect_ratio_24),
                    contentDescription = stringResource(R.string.account_theme),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(0.1f),
                )
                Text(
                    text = stringResource(R.string.account_theme),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(0.9f).padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp)
                    .clickable {
                        navController.navigate("Language")
                    }
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(10.dp)
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_language_24),
                    contentDescription = stringResource(R.string.account_language),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(0.1f),
                )
                Text(
                    text = stringResource(R.string.account_language),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(0.9f).padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp)
                    .clickable {}
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(10.dp)
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_delete_outline_24),
                    contentDescription = stringResource(R.string.account_delete_account),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(0.1f),
                )
                Text(
                    text = stringResource(R.string.account_delete_account),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(0.9f).padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp)
                    .clickable { }
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(10.dp)
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_info_outline_24),
                    contentDescription = stringResource(R.string.account_about_app),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(0.1f),
                )
                Text(
                    text = stringResource(R.string.account_about_app),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(0.9f).padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .height(56.dp)
                    .clickable { onEvent(AccountEvent.ShowLogoutDialog) }
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        shape = RoundedCornerShape(10.dp)
                    ),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_logout_24),
                    contentDescription = stringResource(R.string.account_logout),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(0.1f),
                )
                Text(
                    text = stringResource(R.string.account_logout),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(0.9f).padding(8.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.account_version),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "1.0.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                )
            }
            if (accountUiState.isLogoutDialogVisible) {
                Dialog(
                    onDismissRequest = { onEvent(AccountEvent.DismissLogoutDialog) },
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(
                                MaterialTheme.colorScheme.surface,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Text(
                                text = stringResource(R.string.logout_warning_desc),
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Box(
                                    modifier = Modifier.clickable { onEvent(AccountEvent.DismissLogoutDialog) }
                                        .padding(8.dp).weight(0.5f),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = stringResource(R.string.cancel_button),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.Blue.copy(alpha = 0.8f)
                                    )
                                }
                                Box(
                                    modifier = Modifier.clickable { onEvent(AccountEvent.SignOut) }
                                        .padding(8.dp).weight(0.5f),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = stringResource(R.string.logout_button),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}