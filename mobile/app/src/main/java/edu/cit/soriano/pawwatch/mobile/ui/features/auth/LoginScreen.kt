package edu.cit.soriano.pawwatch.mobile.ui.features.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.model.LoginRequest
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.components.LabeledTextField
import edu.cit.soriano.pawwatch.mobile.ui.components.PrimaryButton
import edu.cit.soriano.pawwatch.mobile.ui.components.loginFieldColors
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors
import edu.cit.soriano.pawwatch.mobile.util.SessionManager
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PawWatchColors.Background),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("🐾", fontSize = 26.sp)
                Text(
                    "PawWatch",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PawWatchColors.Primary
                )
                Text(
                    "Animal Adoption Management System",
                    fontSize = 12.sp,
                    color = PawWatchColors.TextGray
                )

                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = PawWatchColors.Divider)
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Welcome Back",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PawWatchColors.TextDark,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )
                Text(
                    "Log in to continue to your account",
                    fontSize = 13.sp,
                    color = PawWatchColors.TextGray,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(20.dp))

                LabeledTextField(
                    label = "Email Address",
                    value = email,
                    onValueChange = { email = it },
                    useFloatingLabel = false,
                    keyboardType = KeyboardType.Email,
                    colors = loginFieldColors()
                )

                Spacer(modifier = Modifier.height(14.dp))

                LabeledTextField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    useFloatingLabel = false,
                    isPassword = true,
                    keyboardType = KeyboardType.Password,
                    colors = loginFieldColors()
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = if (loading) "Logging in..." else "Log In",
                    enabled = !loading,
                    shape = RoundedCornerShape(10.dp),
                    buttonHeight = 48.dp,
                    fontWeight = FontWeight.Bold,
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                            return@PrimaryButton
                        }
                        loading = true
                        scope.launch {
                            try {
                                val response = RetrofitClient.authApi.login(LoginRequest(email, password))
                                if (response.isSuccessful && response.body() != null) {
                                    val body = response.body()!!
                                    sessionManager.saveSession(body.token, body.role, body.fullName)
                                    Toast.makeText(context, "Welcome back, ${body.fullName}!", Toast.LENGTH_SHORT).show()
                                    onLoginSuccess(body.role)
                                } else {
                                    Toast.makeText(context, "Invalid email or password.", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Connection error: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                loading = false
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Text("Don't have an account? ", fontSize = 13.sp, color = PawWatchColors.TextGray)
                    Text(
                        "Register here",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = PawWatchColors.Primary,
                        modifier = Modifier.clickable(onClick = onNavigateToRegister)
                    )
                }
            }
        }
    }
}
