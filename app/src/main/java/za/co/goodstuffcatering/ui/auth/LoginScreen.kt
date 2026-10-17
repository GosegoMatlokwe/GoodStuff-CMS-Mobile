package za.co.goodstuffcatering.ui.auth

import android.content.Context
import android.credentials.CredentialManager
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import za.co.goodstuffcatering.ui.navigation.Screen

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = CredentialManager.create(context)

    // TODO: Replace this with your Web Client ID from Firebase Console
    val webClientId = "918135390608-5ulabsaqhetsdlbmes1h60i4ua7it630.apps.googleusercontent.com"

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            navController.navigate(Screen.Dashboard.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Good Stuff Catering",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        // 1. Configure Google Sign-In options
                        val googleIdOption = GetGoogleIdOption.Builder()
                            .setFilterByAuthorizedAccounts(false)
                            .setServerClientId(webClientId)
                            .setAutoSelectEnabled(true)
                            .build()

                        val request = GetCredentialRequest.Builder()
                            .addCredentialOption(googleIdOption)
                            .build()

                        // 2. Launch the Google account picker
                        val result: GetCredentialResponse = credentialManager.getCredential(
                            context = context,
                            request = request,
                        )

                        // 3. Extract the ID Token
                        val credential = result.credential
                        if (credential is CustomCredential &&
                            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                        ) {
                            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                            // 4. Send the ID Token to your ViewModel for Firebase Auth
                            viewModel.signInWithGoogle(googleIdTokenCredential.idToken)
                        } else {
                            Log.e("LoginScreen", "Unexpected credential type")
                        }

                    } catch (e: GetCredentialException) {
                        Log.e("LoginScreen", "Credential Manager error: ${e.message}")
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "Unexpected error: ${e.message}")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text("Sign in with Google")
            }
        }

        if (authState is AuthState.Error) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}