package com.nguyenminhkhang.taskmanagement.ui.register

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.register.state.RegisterState

@Composable
fun RegisterLayout(
    registerState: RegisterState,
    navController: NavController
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_bloger),
                contentDescription = "Logout Icon",
                modifier = Modifier.size(200.dp),
                tint = Color.Unspecified
            )
            Text(text = stringResource(R.string.tilte_register_page),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = stringResource(R.string.desc_register_page),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                modifier = Modifier.padding(horizontal = 16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(80.dp))
            OutlinedTextField(
                value = registerState.email,
                onValueChange = {  },
                label = { Text(stringResource(R.string.email_or_username)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_person_24),
                        contentDescription = "User Icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                placeholder = { Text("Enter your email or username") },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(12.dp),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = registerState.password,
                onValueChange = {},
                label = { Text(stringResource(R.string.password)) },
                placeholder = { Text("Enter your password") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_key_24),
                        contentDescription = "Lock Icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = if (registerState.isPasswordVisible)
                            painterResource(R.drawable.baseline_visibility_24)
                        else
                            painterResource(R.drawable.baseline_visibility_off_24),
                        contentDescription = "Visible Password Icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.9f),
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = registerState.confirmPassword,
                onValueChange = {},
                label = { Text(stringResource(R.string.confirm_password)) },
                placeholder = { Text("Confirm your entered password") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_key_24),
                        contentDescription = "Lock Icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = if (registerState.isConfirmPasswordVisible)
                            painterResource(R.drawable.baseline_visibility_24)
                        else
                            painterResource(R.drawable.baseline_visibility_off_24),
                        contentDescription = "Visible Password Icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(12.dp),
                maxLines = 1,
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(0.9f).height(60.dp),
                shape = RoundedCornerShape(12.dp),
                content = {
                    Text(
                        text = stringResource(R.string.register_button),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }
            )
            Spacer(modifier = Modifier.height(90.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(R.string.had_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = stringResource(R.string.login_now_button),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier
                            .clickable { navController.navigate("Login") }
                    )
                }
            }
        }
    }
}