package edu.cit.soriano.pawwatch.mobile.ui.features.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import edu.cit.soriano.pawwatch.mobile.model.RegisterRequest
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.components.LabeledTextField
import edu.cit.soriano.pawwatch.mobile.ui.components.PrimaryButton
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        LabeledTextField(
            label = "Full Name",
            value = fullName,
            onValueChange = { fullName = it }
        )
        Spacer(modifier = Modifier.height(12.dp))

        LabeledTextField(
            label = "Email",
            value = email,
            onValueChange = { email = it },
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(12.dp))

        LabeledTextField(
            label = "Contact Number",
            value = contactNumber,
            onValueChange = { contactNumber = it },
            keyboardType = KeyboardType.Phone
        )
        Spacer(modifier = Modifier.height(12.dp))

        LabeledTextField(
            label = "Password",
            value = password,
            onValueChange = { password = it },
            isPassword = true,
            keyboardType = KeyboardType.Password
        )
        Spacer(modifier = Modifier.height(12.dp))

        LabeledTextField(
            label = "Confirm Password",
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            isPassword = true,
            keyboardType = KeyboardType.Password
        )
        Spacer(modifier = Modifier.height(20.dp))

        PrimaryButton(
            text = if (loading) "Creating account..." else "Register",
            enabled = !loading,
            containerColor = MaterialTheme.colorScheme.primary,
            onClick = {
                if (fullName.isBlank() || email.isBlank() || contactNumber.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                    return@PrimaryButton
                }
                if (password != confirmPassword) {
                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@PrimaryButton
                }
                loading = true
                scope.launch {
                    try {
                        val response = RetrofitClient.authApi.register(
                            RegisterRequest(fullName, email, contactNumber, password)
                        )
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Account created successfully! Please log in.", Toast.LENGTH_LONG).show()
                            onRegisterSuccess()
                        } else {
                            Toast.makeText(context, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
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

        TextButton(onClick = onNavigateToLogin) {
            Text("Already have an account? Log in")
        }
    }
}
