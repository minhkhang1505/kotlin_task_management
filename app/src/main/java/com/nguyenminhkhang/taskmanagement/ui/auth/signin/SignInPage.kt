package com.nguyenminhkhang.taskmanagement.ui.auth.signin

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.nguyenminhkhang.taskmanagement.R
import com.nguyenminhkhang.taskmanagement.ui.NavScreen

@Composable
fun SignInPage(
    viewModel: SignInViewModel = hiltViewModel(),
    navController: NavController
) {
    val loginState by viewModel.signInState.collectAsState()
    val webClientId = stringResource(id = R.string.default_web_client_id)
    val context = LocalContext.current

    val googleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account.idToken != null) {
                    viewModel.signInWithGoogle(account.idToken!!)
                } else {
                    Log.e("SignInPage", "idToken is NULL! Check your default_web_client_id and SHA keys.")
                }
            } catch (e: ApiException) {
                Log.e("SignInPage", "Google sign in failed: ${e.statusCode}", e)
            }
        } else {
            Log.e("SignInPage", "Google Sign-In canceled or failed.")
        }
    }

    val onGoogleSignInClick = {
        googleSignInLauncher.launch(googleSignInClient.signInIntent)
    }

    LaunchedEffect(loginState) {
        if (loginState.isSuccess) {
            navController.navigate(NavScreen.HOME.route) {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    SignInLayout(
        loginState = loginState,
        onGoogleSignInClick = onGoogleSignInClick,
        navController = navController,
        onEvent = viewModel::onEvent
    )

    if (loginState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}
