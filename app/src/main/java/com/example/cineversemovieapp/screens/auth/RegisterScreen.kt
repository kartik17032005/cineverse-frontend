package com.example.cineversemovieapp.screens.auth

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
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.components.InputTextField
import com.example.cineversemovieapp.viewmodel.AuthViewModel
import com.example.cineversemovieapp.viewmodel.AuthViewModel.RegisterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController) {

    val usernameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    val confirmPasswordState = remember { mutableStateOf("") }
    val termsAccepted = remember { mutableStateOf(false) }

    val usernameError = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }
    val confirmPasswordError = remember { mutableStateOf("") }
    val termsError = remember { mutableStateOf("") }

    // States for Bottom Sheets
    var showTermsSheet by remember { mutableStateOf(false) }
    var showPrivacySheet by remember { mutableStateOf(false) }

    val viewModel: AuthViewModel = viewModel()
    val registerState by viewModel.registerState.collectAsState()

    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val gold = Color(0xFFCCA246)
    val muted = Color(0xFF5E5C68)

    // ── Reset state when screen opens ──
    LaunchedEffect(Unit) {
        viewModel.resetRegisterState()
    }

    // ── React to state changes ──
    LaunchedEffect(registerState) {
        when (registerState) {
            is RegisterState.Success -> {
                navController.navigate("login") {
                    popUpTo("register") { inclusive = true }
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
                text = "GET STARTED FREE",
                color = gold,
                fontFamily = bebasNeue,
                letterSpacing = 3.sp,
                fontSize = 14.sp
            )

            Text(
                text = "CREATE ACCOUNT",
                color = Color.White,
                fontFamily = bebasNeue,
                fontSize = 50.sp
            )

            // ── Sign in redirect ──
            Row {
                Text(text = "Already a member? ", color = muted, fontSize = 14.sp)
                Text(
                    text = "Sign in",
                    color = gold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            // ── Fields ──
            InputTextField(
                valueState = usernameState,
                labelId = "USERNAME",
                enabled = true,
                leadingIcon = Icons.Filled.Person
            )
            if (usernameError.value.isNotEmpty()) ErrorText(message = usernameError.value)

            InputTextField(
                valueState = emailState,
                labelId = "EMAIL",
                enabled = true,
                keyboardType = KeyboardType.Email,
                leadingIcon = Icons.Filled.Email
            )
            if (emailError.value.isNotEmpty()) ErrorText(message = emailError.value)

            InputTextField(
                valueState = passwordState,
                labelId = "PASSWORD",
                enabled = true,
                keyboardType = KeyboardType.Password,
                leadingIcon = Icons.Filled.Lock
            )
            if (passwordError.value.isNotEmpty()) ErrorText(message = passwordError.value)

            InputTextField(
                valueState = confirmPasswordState,
                labelId = "CONFIRM PASSWORD",
                enabled = true,
                keyboardType = KeyboardType.Password,
                leadingIcon = Icons.Filled.Lock
            )
            if (confirmPasswordError.value.isNotEmpty()) ErrorText(message = confirmPasswordError.value)

            Spacer(modifier = Modifier.height(12.dp))

            // ── Terms checkbox ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = termsAccepted.value,
                    onCheckedChange = {
                        termsAccepted.value = it
                        if (it) termsError.value = ""
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = gold,
                        uncheckedColor = if (termsError.value.isNotEmpty()) Color(0xFFE84B4B) else muted,
                        checkmarkColor = Color(0xFF101017)
                    )
                )

                val annotatedTermsString = buildAnnotatedString {
                    withStyle(SpanStyle(color = muted, fontSize = 13.sp)) {
                        append("I agree to Cineverse's ")
                    }
                    pushStringAnnotation(tag = "TERMS", annotation = "")
                    withStyle(
                        SpanStyle(
                            color = gold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Terms of Service")
                    }
                    pop()
                    withStyle(SpanStyle(color = muted, fontSize = 13.sp)) {
                        append(" and ")
                    }
                    pushStringAnnotation(tag = "POLICY", annotation = "")
                    withStyle(
                        SpanStyle(
                            color = gold,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Privacy Policy")
                    }
                    pop()
                }

                ClickableText(
                    text = annotatedTermsString,
                    style = TextStyle(fontFamily = FontFamily.Default),
                    onClick = { offset ->
                        annotatedTermsString.getStringAnnotations(
                            tag = "TERMS",
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let {
                                showTermsSheet = true
                            }
                        annotatedTermsString.getStringAnnotations(
                            tag = "POLICY",
                            start = offset,
                            end = offset
                        )
                            .firstOrNull()?.let {
                                showPrivacySheet = true
                            }
                    }
                )
            }

            if (termsError.value.isNotEmpty()) {
                ErrorText(message = termsError.value)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── CTA Button ──
            Button(
                onClick = {
                    if (validateRegisterForm(
                            username = usernameState.value,
                            email = emailState.value,
                            password = passwordState.value,
                            confirmPassword = confirmPasswordState.value,
                            termsAccepted = termsAccepted.value,
                            usernameError = usernameError,
                            emailError = emailError,
                            passwordError = passwordError,
                            confirmPasswordError = confirmPasswordError,
                            termsError = termsError
                        )
                    ) {
                        viewModel.registerUser(
                            username = usernameState.value,
                            email = emailState.value,
                            password = passwordState.value,
                            confirmPassword = confirmPasswordState.value
                        )
                    }
                },
                enabled = registerState !is RegisterState.Loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = gold,
                    contentColor = Color(0xFF101017),
                    disabledContainerColor = gold.copy(alpha = 0.5f)
                )
            ) {
                if (registerState is RegisterState.Loading) {
                    CircularProgressIndicator(
                        color = Color(0xFF101017),
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "CREATE MY ACCOUNT",
                        fontFamily = bebasNeue,
                        fontSize = 18.sp,
                        letterSpacing = 2.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        // ── Bottom Sheets for Terms & Privacy ──
        if (showTermsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showTermsSheet = false },
                containerColor = Color(0xFF1A1A24),
                contentColor = Color.White
            ) {
                TermsContent(bebasNeue, gold)
            }
        }

        if (showPrivacySheet) {
            ModalBottomSheet(
                onDismissRequest = { showPrivacySheet = false },
                containerColor = Color(0xFF1A1A24),
                contentColor = Color.White
            ) {
                PrivacyContent(bebasNeue, gold)
            }
        }
    }
}

@Composable
fun TermsContent(fontFamily: FontFamily, gold: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "TERMS OF SERVICE",
            fontFamily = fontFamily,
            fontSize = 24.sp,
            color = gold,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Welcome to Cineverse. By using our application, you agree to comply with and be bound by the following terms and conditions of use. Our service provides access to movie information, trailers, and personalized recommendations. You are responsible for maintaining the confidentiality of your account credentials. Cineverse reserves the right to terminate accounts that violate our community guidelines.",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun PrivacyContent(fontFamily: FontFamily, gold: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            text = "PRIVACY POLICY",
            fontFamily = fontFamily,
            fontSize = 24.sp,
            color = gold,
            letterSpacing = 2.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your privacy is important to us. Cineverse collects minimal personal data such as your email and username to provide a personalized experience. We do not sell your data to third parties. We use industry-standard encryption to protect your information. By using Cineverse, you consent to our data collection practices as outlined in this policy.",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun ErrorText(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        color = Color(0xFFE84B4B),
        fontSize = 12.sp,
        modifier = modifier.padding(start = 8.dp, top = 4.dp)
    )
}

fun validateRegisterForm(
    username: String,
    email: String,
    password: String,
    confirmPassword: String,
    termsAccepted: Boolean,
    usernameError: MutableState<String>,
    emailError: MutableState<String>,
    passwordError: MutableState<String>,
    confirmPasswordError: MutableState<String>,
    termsError: MutableState<String>
): Boolean {
    var isValid = true

    if (username.isBlank()) {
        usernameError.value = "Username is required"
        isValid = false
    } else {
        usernameError.value = ""
    }

    if (email.isBlank()) {
        emailError.value = "Email is required"
        isValid = false
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        emailError.value = "Invalid email address"
        isValid = false
    } else {
        emailError.value = ""
    }

    if (password.length < 6) {
        passwordError.value = "Password must be at least 6 characters"
        isValid = false
    } else {
        passwordError.value = ""
    }

    if (confirmPassword != password) {
        confirmPasswordError.value = "Passwords do not match"
        isValid = false
    } else {
        confirmPasswordError.value = ""
    }

    if (!termsAccepted) {
        termsError.value = "You must accept the terms"
        isValid = false
    } else {
        termsError.value = ""
    }

    return isValid
}
