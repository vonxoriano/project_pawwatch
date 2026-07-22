package edu.cit.soriano.pawwatch.mobile.ui.features.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.cit.soriano.pawwatch.mobile.model.ChangePasswordRequest
import edu.cit.soriano.pawwatch.mobile.model.UpdateProfileRequest
import edu.cit.soriano.pawwatch.mobile.network.RetrofitClient
import edu.cit.soriano.pawwatch.mobile.ui.components.FormCard
import edu.cit.soriano.pawwatch.mobile.ui.components.LabeledTextField
import edu.cit.soriano.pawwatch.mobile.ui.components.PrimaryButton
import edu.cit.soriano.pawwatch.mobile.ui.theme.PawWatchColors
import edu.cit.soriano.pawwatch.mobile.util.SessionManager
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val scope = rememberCoroutineScope()
    val token = "Bearer ${sessionManager.getToken()}"

    var email by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(true) }
    var saving by remember { mutableStateOf(false) }

    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmNewPassword by remember { mutableStateOf("") }
    var changingPassword by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.apiService.getProfile(token)
            if (response.isSuccessful && response.body() != null) {
                val profile = response.body()!!
                email = profile.email
                fullName = profile.fullName
                contactNumber = profile.contactNumber
            } else {
                Toast.makeText(context, "Failed to load profile.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Connection error: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            loading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PawWatchColors.Background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Back")
        }

        Text(
            "👤 My Profile",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PawWatchColors.TextDark
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        } else {
            FormCard(title = "Profile Information") {
                LabeledTextField(
                    label = "Email Address",
                    value = email,
                    onValueChange = {},
                    enabled = false
                )
                Spacer(modifier = Modifier.height(12.dp))

                LabeledTextField(
                    label = "Full Name",
                    value = fullName,
                    onValueChange = { fullName = it }
                )
                Spacer(modifier = Modifier.height(12.dp))

                LabeledTextField(
                    label = "Contact Number",
                    value = contactNumber,
                    onValueChange = { contactNumber = it },
                    keyboardType = KeyboardType.Phone
                )
                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton(
                    text = if (saving) "Saving..." else "Save Changes",
                    enabled = !saving,
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        if (fullName.isBlank() || contactNumber.isBlank()) {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                            return@PrimaryButton
                        }
                        saving = true
                        scope.launch {
                            try {
                                val response = RetrofitClient.apiService.updateProfile(
                                    token, UpdateProfileRequest(fullName, contactNumber)
                                )
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Failed to update profile.", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Connection error: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                saving = false
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            FormCard(title = "Change Password") {
                LabeledTextField(
                    label = "Current Password",
                    value = currentPassword,
                    onValueChange = { currentPassword = it },
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )
                Spacer(modifier = Modifier.height(12.dp))

                LabeledTextField(
                    label = "New Password",
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )
                Spacer(modifier = Modifier.height(12.dp))

                LabeledTextField(
                    label = "Confirm New Password",
                    value = confirmNewPassword,
                    onValueChange = { confirmNewPassword = it },
                    isPassword = true,
                    keyboardType = KeyboardType.Password
                )
                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton(
                    text = if (changingPassword) "Changing..." else "Change Password",
                    enabled = !changingPassword,
                    onClick = {
                        if (currentPassword.isBlank() || newPassword.isBlank() || confirmNewPassword.isBlank()) {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                            return@PrimaryButton
                        }
                        if (newPassword != confirmNewPassword) {
                            Toast.makeText(context, "New passwords do not match", Toast.LENGTH_SHORT).show()
                            return@PrimaryButton
                        }
                        changingPassword = true
                        scope.launch {
                            try {
                                val response = RetrofitClient.apiService.changePassword(
                                    token,
                                    ChangePasswordRequest(currentPassword, newPassword, confirmNewPassword)
                                )
                                if (response.isSuccessful) {
                                    Toast.makeText(context, "Password changed successfully!", Toast.LENGTH_SHORT).show()
                                    currentPassword = ""
                                    newPassword = ""
                                    confirmNewPassword = ""
                                } else {
                                    Toast.makeText(context, "Failed to change password. Check your current password.", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Connection error: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                changingPassword = false
                            }
                        }
                    }
                )
            }
        }
    }
}
