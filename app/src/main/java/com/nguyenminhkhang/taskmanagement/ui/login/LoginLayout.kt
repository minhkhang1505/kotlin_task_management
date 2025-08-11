package com.nguyenminhkhang.taskmanagement.ui.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.TaskApp
import com.nguyenminhkhang.taskmanagement.ui.theme.TaskManagementTheme

@Composable
fun LoginLayout(
    loginState: LoginState
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text(
                text = "Login",
                fontSize = 52.sp,
                style = androidx.compose.material3.MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(60.dp))
            OutlinedTextField(
                value = loginState.email,
                onValueChange = {  },
                label = { Text("Email or Username") },
                placeholder = { Text("Enter your email or username") },
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = loginState.password,
                onValueChange = {},
                label = { Text("Password") },
                placeholder = { Text("Enter your password") },
                trailingIcon = {
                    Icon(
                        painter = if (!loginState.isPasswordVisible)
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
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth(0.9f),
                content = {
                    Text(text = "Login", fontSize = 18.sp)
                }
            )
            Spacer(modifier = Modifier.height(50.dp))
            OrDivider()
            Spacer(modifier = Modifier.height(50.dp))
            LoginWith()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginLayoutPreview() {
    TaskManagementTheme {
        val loginState = LoginState(
            email = "mkhang040506@gmail.com",
            password = "password123",
            isLoading = false,
            isPasswordVisible = true
        )
        LoginLayout(loginState = loginState)
    }
}