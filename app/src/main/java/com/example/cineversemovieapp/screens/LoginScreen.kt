package com.example.cineversemovieapp.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.components.InputTextField
import com.example.cineversemovieapp.components.bannerItems.FeatureItem
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {

    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val rememberMeState = remember { mutableStateOf(false) }

    val emailError = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }

    val loginState by authViewModel.loginState.collectAsState()

    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val gold = Color(0xFFCCA246)
    val muted = Color(0xFF5E5C68)

    // ── Reset state when screen opens ──
    LaunchedEffect(Unit) {
        authViewModel.resetLoginState()
    }

    // ── React to state changes ──
    LaunchedEffect(loginState) {
        when (loginState) {
            is AuthViewModel.LoginState.Success -> {
                navController.navigate(Routes.Home.route) {
                    popUpTo("login") { inclusive = true }
                }
            }

            else -> {}
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFF101017)
    ) {
        Column(
            modifier = Modifier
                .padding(25.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start
        ) {

            // ── Logo ──
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 55.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(gold),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Movie,
                        contentDescription = null,
                        tint = Color(0xFF101017),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    text = "CINEVERSE",
                    fontWeight = FontWeight.Bold,
                    color = gold,
                    fontSize = 40.sp,
                    fontFamily = bebasNeue,
                    letterSpacing = 4.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Heading ──
            Text(
                text = "WELCOME BACK",
                color = gold,
                fontFamily = bebasNeue,
                letterSpacing = 3.sp,
                fontSize = 14.sp
            )

            Text(
                text = "SIGN IN",
                color = Color.White,
                fontFamily = bebasNeue,
                fontSize = 50.sp
            )

            // ── Register redirect ──
            Row {
                Text(text = "Don't have an account? ", color = muted, fontSize = 14.sp)
                Text(
                    text = "Create one",
                    color = gold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { navController.navigate("register") }
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // ── Email ──
            InputTextField(
                valueState = emailState,
                labelId = "EMAIL",
                enabled = true,
                keyboardType = KeyboardType.Email,
                leadingIcon = Icons.Filled.Email
            )
            if (emailError.value.isNotEmpty()) {
                ErrorText(message = emailError.value)
            }

            // ── Password ──
            InputTextField(
                valueState = passwordState,
                labelId = "PASSWORD",
                enabled = true,
                keyboardType = KeyboardType.Password,
                leadingIcon = Icons.Filled.Lock
            )
            if (passwordError.value.isNotEmpty()) {
                ErrorText(message = passwordError.value)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Remember me ──
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMeState.value,
                    onCheckedChange = { rememberMeState.value = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = gold,
                        uncheckedColor = muted,
                        checkmarkColor = Color(0xFF101017)
                    )
                )
                Text(
                    text = "Remember me",
                    color = muted,
                    fontSize = 13.sp
                )

                // ── Forgot password ──
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Forgot password?",
                    color = gold,
                    fontSize = 13.sp,
                    modifier = Modifier.clickable { /* navigate to forgot password */ }
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── API error message ──
            if (loginState is AuthViewModel.LoginState.Error) {
                ErrorText(
                    message = (loginState as AuthViewModel.LoginState.Error).message,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // ── CTA Button ──
            Button(
                onClick = {
                    if (validateLoginForm(
                            email = emailState.value,
                            password = passwordState.value,
                            emailError = emailError,
                            passwordError = passwordError
                        )
                    ) {
                        authViewModel.login(
                            email = emailState.value,
                            password = passwordState.value
                        )
                    }
                },
                enabled = loginState !is AuthViewModel.LoginState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = gold,
                    contentColor = Color(0xFF101017),
                    disabledContainerColor = gold.copy(alpha = 0.5f),
                    disabledContentColor = Color(0xFF101017)
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 2.dp,
                    hoveredElevation = 12.dp
                )
            ) {
                if (loginState is AuthViewModel.LoginState.Loading) {
                    Text(
                        text = "SIGNING IN...",
                        fontFamily = bebasNeue,
                        fontSize = 18.sp,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    )
                } else {
                    Text(
                        text = "SIGN IN TO CINEVERSE",
                        fontFamily = bebasNeue,
                        fontSize = 18.sp,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Spacer(modifier = Modifier.height(32.dp))

// ── Divider ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(muted.copy(alpha = 0.2f))
                )
                Text(
                    text = "  WHY CINEVERSE  ",
                    color = muted,
                    fontFamily = bebasNeue,
                    fontSize = 11.sp,
                    letterSpacing = 3.sp
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(muted.copy(alpha = 0.2f))
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Feature Highlights ──
            val features = listOf(
                Pair(Icons.Filled.Movie, "Discover Movies & Series"),
                Pair(Icons.Filled.Star, "Personalized Recommendations"),
                Pair(Icons.Filled.Tv, "Stream Anytime, Anywhere"),
                Pair(Icons.Filled.ThumbUp, "Zero Ads. Ever.")
            )

            features.forEach { (icon, label) ->
                FeatureItem(
                    icon = icon,
                    label = label,
                    gold = gold,
                    muted = muted,
                    bebasNeue = bebasNeue
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// ── Validation Function ──
fun validateLoginForm(
    email: String,
    password: String,
    emailError: MutableState<String>,
    passwordError: MutableState<String>
): Boolean {
    var isValid = true

    // Email
    if (email.isBlank()) {
        emailError.value = "Email cannot be empty"
        isValid = false
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        emailError.value = "Invalid email format"
        isValid = false
    } else {
        emailError.value = ""
    }

    // Password
    if (password.isBlank()) {
        passwordError.value = "Password cannot be empty"
        isValid = false
    } else if (password.length < 6) {
        passwordError.value = "Password must be at least 6 characters"
        isValid = false
    } else {
        passwordError.value = ""
    }

    return isValid
}