package com.example.cineversemovieapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.cineversemovieapp.viewmodel.RegisterState

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
                    withStyle(SpanStyle(color = gold, fontSize = 13.sp, fontWeight = FontWeight.Bold)) {
                        append("Terms of Service")
                    }
                    pop()
                    withStyle(SpanStyle(color = muted, fontSize = 13.sp)) {
                        append(" and ")
                    }
                    pushStringAnnotation(tag = "POLICY", annotation = "")
                    withStyle(SpanStyle(color = gold, fontSize = 13.sp, fontWeight = FontWeight.Bold)) {
                        append("Privacy Policy")
                    }
                    pop()
                }

                ClickableText(
                    text = annotatedTermsString,
                    style = TextStyle(fontFamily = FontFamily.Default),
                    onClick = { offset ->
                        annotatedTermsString.getStringAnnotations(tag = "TERMS", start = offset, end = offset)
                            .firstOrNull()?.let {
                                showTermsSheet = true
                            }
                        annotatedTermsString.getStringAnnotations(tag = "POLICY", start = offset, end = offset)
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
                    CircularProgressIndicator(color = Color(0xFF101017), modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
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
    }

    // ── Bottom Sheets for Legal Text ──
    if (showTermsSheet) {
        LegalBottomSheet(
            title = "TERMS AND CONDITIONS",
            onDismiss = { showTermsSheet = false }
        ) {
            TermsContent()
        }
    }

    if (showPrivacySheet) {
        LegalBottomSheet(
            title = "PRIVACY POLICY",
            onDismiss = { showPrivacySheet = false }
        ) {
            PrivacyContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegalBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))
    val gold = Color(0xFFCCA246)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1A1A24),
        contentColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle(color = gold.copy(alpha = 0.5f)) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 48.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = title,
                fontFamily = bebasNeue,
                fontSize = 28.sp,
                color = gold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun TermsContent() {
    val muted = Color(0xFFF0EDE8).copy(alpha = 0.7f)
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Last Updated: April 2026", color = muted, fontSize = 12.sp)
        Text("Welcome to Cineverse. By accessing or using our application, you agree to comply with and be bound by the following Terms and Conditions.", color = Color.White, fontSize = 14.sp)
        
        LegalSection("1. Use of the App", "Cineverse provides movie browsing, recommendations, and AI-powered suggestions for entertainment purposes only. You agree to use the app only for lawful purposes.")
        LegalSection("2. User Accounts", "• You may be required to create an account.\n• You are responsible for maintaining the confidentiality of your login credentials.\n• Any activity under your account is your responsibility.")
        LegalSection("3. AI Recommendations", "Cineverse uses AI to provide movie suggestions based on user input.\n\n• Recommendations are generated automatically and may not always be accurate.\n• Cineverse is not responsible for decisions made based on AI suggestions.")
        LegalSection("4. Content & Data", "• Movie data is sourced from third-party APIs (e.g., TMDB).\n• Cineverse does not own movie content, posters, or metadata.\n• All trademarks and copyrights belong to their respective owners.")
        LegalSection("5. Prohibited Activities", "You agree NOT to:\n• Use the app for illegal purposes\n• Attempt to hack, reverse-engineer, or disrupt the app\n• Misuse AI features or spam requests")
        LegalSection("6. Service Availability", "We do not guarantee uninterrupted or error-free service. Features may be modified or discontinued at any time.")
        LegalSection("7. Limitation of Liability", "Cineverse is provided “as is” without warranties. We are not liable for:\n• Data loss\n• Incorrect recommendations\n• Service interruptions")
        LegalSection("8. Termination", "We may suspend or terminate your access if you violate these terms.")
        LegalSection("9. Changes to Terms", "We may update these Terms at any time. Continued use of the app means you accept the updated terms.")
        LegalSection("10. Contact", "For questions, contact us at: support@cineverse.app")
    }
}

@Composable
fun PrivacyContent() {
    val muted = Color(0xFFF0EDE8).copy(alpha = 0.7f)
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Last Updated: April 2026", color = muted, fontSize = 12.sp)
        Text("Your privacy is important to us. This Privacy Policy explains how Cineverse collects, uses, and protects your information.", color = Color.White, fontSize = 14.sp)

        LegalSection("1. Information We Collect", "a) Personal Information\n• Name (if provided)\n• Email address (for login/authentication)\n\nb) Usage Data\n• App interactions (movies viewed, searches)\n• AI queries for recommendations")
        LegalSection("2. How We Use Your Information", "We use your data to:\n• Provide personalized movie recommendations\n• Improve app performance and features\n• Maintain user accounts and authentication")
        LegalSection("3. AI Data Usage", "User queries entered in the AI feature may be processed by third-party AI services (e.g., Groq) to generate recommendations.")
        LegalSection("4. Third-Party Services", "Cineverse uses:\n• TMDB API (movie data)\n• AI services (for recommendations)\n\nThese services may process limited data as required.")
        LegalSection("5. Data Security", "We implement standard security practices to protect your data. However, no system is completely secure.")
        LegalSection("6. Data Sharing", "We do NOT sell your personal data. We only share data with:\n• Required third-party services (API providers)")
        LegalSection("7. User Control", "You can:\n• Update your account information\n• Request account deletion")
        LegalSection("8. Children’s Privacy", "Cineverse is not intended for children under 13.")
        LegalSection("9. Changes to Policy", "We may update this Privacy Policy. Changes will be reflected with a new “Last Updated” date.")
        LegalSection("10. Contact Us", "For privacy concerns: support@cineverse.app")
    }
}

@Composable
fun LegalSection(title: String, body: String) {
    Column {
        Text(text = title, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = body, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp, lineHeight = 20.sp)
    }
}

@Composable
fun ErrorText(message: String, modifier: Modifier = Modifier) {
    Text(
        text = "⚠ $message",
        color = Color(0xFFE84B4B),
        fontSize = 11.sp,
        modifier = modifier.padding(start = 8.dp, bottom = 4.dp)
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
        usernameError.value = "Username cannot be empty"; isValid = false
    } else {
        usernameError.value = ""
    }

    if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        emailError.value = "Invalid email format"; isValid = false
    } else {
        emailError.value = ""
    }

    if (password.length < 6) {
        passwordError.value = "Password must be at least 6 characters"; isValid = false
    } else {
        passwordError.value = ""
    }

    if (confirmPassword != password) {
        confirmPasswordError.value = "Passwords do not match"; isValid = false
    } else {
        confirmPasswordError.value = ""
    }

    if (!termsAccepted) {
        termsError.value = "Please accept the terms to continue"; isValid = false
    } else {
        termsError.value = ""
    }

    return isValid
}
