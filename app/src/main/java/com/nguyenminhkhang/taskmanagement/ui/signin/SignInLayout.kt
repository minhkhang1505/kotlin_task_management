package com.nguyenminhkhang.taskmanagement.ui.signin

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.NavScreen
import com.nguyenminhkhang.taskmanagement.ui.signin.state.SignInState

@Composable
fun SignInLayout(
    loginState: SignInState,
    onGoogleSignInClick: () -> Unit,
    navController: NavController
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
                text = stringResource(R.string.login_button),
                fontSize = 52.sp,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(150.dp))
            OutlinedTextField(
                value = loginState.email,
                onValueChange = {  },
                label = { Text(stringResource(R.string.email_or_username)) },
                placeholder = { Text("Enter your email or username") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.baseline_person_24),
                        contentDescription = "User Icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.9f),
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = loginState.password,
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
                        painter = if (loginState.isPasswordVisible)
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
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(0.9f),
                horizontalArrangement = Arrangement.End,
            ) {
                Text(
                    text = stringResource(R.string.forget_password),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = {
                    navController.navigate(NavScreen.HOME.route)
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth(0.9f).height(60.dp),
                content = {
                    Text(
                        text = stringResource(R.string.login_button),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                    )
                }
            )
            Spacer(modifier = Modifier.height(45.dp))
            OrDivider()
            Spacer(modifier = Modifier.height(45.dp))
            LoginWith(icon = painterResource(R.drawable.ic_google), title = stringResource(R.string.login_with_google_button),onGoogleSignInClick = onGoogleSignInClick)
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(R.string.dont_have_account),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                )
                Text(
                    text = stringResource(R.string.register_button),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 4.dp)
                        .clickable { navController.navigate(NavScreen.REGISTER.route) }
                )
            }
        }
    }
}