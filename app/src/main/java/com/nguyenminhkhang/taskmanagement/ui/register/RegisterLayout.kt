package com.nguyenminhkhang.taskmanagement.ui.register

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.register.state.RegisterState

@Composable
fun RegisterLayout(
    registerState: RegisterState,
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
                modifier = Modifier.size(250.dp),
                tint = Color.Unspecified
            )
            Text(text = "Register now",
                style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Text(
                text = "Create an account to start managing your tasks efficiently.",
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
            )
            Spacer(modifier = Modifier.height(60.dp))
            OutlinedTextField(
                value = registerState.email,
                onValueChange = {  },
                label = { Text("Email or Username") },
                placeholder = { Text("Enter your email or username") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = registerState.password,
                onValueChange = {},
                label = { Text("Password") },
                placeholder = { Text("Enter your password") },
                trailingIcon = {
                    Icon(
                        painter = if (!registerState.isPasswordVisible)
                            painterResource(R.drawable.baseline_visibility_24)
                        else
                            painterResource(R.drawable.baseline_visibility_off_24),
                        contentDescription = "Visible Password Icon",
                    )
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = registerState.confirmPassword,
                onValueChange = {},
                label = { Text("Confirm Password") },
                placeholder = { Text("Confirm your entered password") },
                trailingIcon = {
                    Icon(
                        painter = if (!registerState.isConfirmPasswordVisible)
                            painterResource(R.drawable.baseline_visibility_24)
                        else
                            painterResource(R.drawable.baseline_visibility_off_24),
                        contentDescription = "Visible Password Icon",
                    )
                },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(0.9f).height(60.dp),
                shape = RoundedCornerShape(12.dp),
                content = {
                    Text(
                        text = "Register",
                        style = androidx.compose.material3.MaterialTheme.typography.bodyLarge,
                    )
                }
            )
            Spacer(modifier = Modifier.height(150.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(text = "You had an account. ")
                Text(
                    text = "Login now",
                    color = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyLarge.copy(
                        textDecoration = TextDecoration.Underline
                    ),
                )
            }
        }
    }
}