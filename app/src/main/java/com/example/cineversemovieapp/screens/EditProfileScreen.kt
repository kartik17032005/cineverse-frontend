package com.example.cineversemovieapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.viewmodel.AuthViewModel

private val EditGold = Color(0xFFCCA246)
private val EditGold2 = Color(0xFFA87830)
private val EditBg = Color(0xFF0A0A0F)
private val EditSurface = Color(0xFF111118)
private val EditSurface2 = Color(0xFF191927)
private val EditTextColor = Color(0xFFF0EDE8)
private val EditMuted = Color(0xFF5E5C68)
private val EditBorderColor = Color.White.copy(alpha = 0.06f)
private val EditErrorRed = Color(0xFFE05C5C)

@Composable
fun EditProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    val userProfileState by authViewModel.userProfile.collectAsState()
    val updateState by authViewModel.updateState.collectAsState()

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // ── Load profile on open ──
    LaunchedEffect(Unit) {
        authViewModel.getUserProfile()
    }

    // ── Pre-fill fields when profile loads ──
    LaunchedEffect(userProfileState) {
        if (userProfileState is AuthViewModel.UserProfileState.Success) {
            val user = (userProfileState as AuthViewModel.UserProfileState.Success).user
            name = user.userName
            email = user.email
        }
    }

    // ── Navigate back on success ──
    LaunchedEffect(updateState) {
        if (updateState is AuthViewModel.UpdateState.Success) {
            authViewModel.resetUpdateState()
            navController.popBackStack()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EditBg)
    ) {
        when (userProfileState) {

            // ── Loading ──
            is AuthViewModel.UserProfileState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = EditGold)
                }
            }

            // ── Error loading profile ──
            is AuthViewModel.UserProfileState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚠ Failed to load profile",
                        color = EditErrorRed,
                        fontSize = 13.sp
                    )
                }
            }

            // ── Content ──
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 40.dp)
                ) {

                    // ── Header ──
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 18.dp,
                                end = 18.dp,
                                top = 52.dp,
                                bottom = 10.dp
                            )
                    ) {
                        // Back button
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(38.dp)
                                .background(EditSurface2, CircleShape)
                                .border(0.5.dp, EditBorderColor, CircleShape)
                                .clickable { navController.popBackStack() }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ArrowBack,
                                contentDescription = "Back",
                                tint = EditTextColor,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = "EDIT PROFILE",
                            fontFamily = bebasNeue,
                            fontSize = 20.sp,
                            letterSpacing = 3.sp,
                            color = EditTextColor
                        )

                        // Save button top right
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(EditGold, EditGold2)
                                    ),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .padding(horizontal = 18.dp, vertical = 7.dp)
                                .clickable {
                                    authViewModel.updateUser(
                                        userName = name,
                                        email = email
                                    )
                                }
                        ) {
                            if (updateState is AuthViewModel.UpdateState.Loading) {
                                CircularProgressIndicator(
                                    color = EditBg,
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "SAVE",
                                    fontFamily = bebasNeue,
                                    fontSize = 14.sp,
                                    letterSpacing = 1.5.sp,
                                    color = EditBg
                                )
                            }
                        }
                    }

                    // ── Update Error Message ──
                    if (updateState is AuthViewModel.UpdateState.Error) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 6.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(EditErrorRed.copy(alpha = 0.08f))
                                .border(
                                    0.5.dp,
                                    EditErrorRed.copy(alpha = 0.3f),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "⚠ ${(updateState as AuthViewModel.UpdateState.Error).message}",
                                color = EditErrorRed,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // ── Avatar ──
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, bottom = 28.dp)
                    ) {
                        // gold circle with initial
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(88.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        listOf(EditGold, EditGold2)
                                    ),
                                    shape = CircleShape
                                )
                                .border(3.dp, EditBg, CircleShape)
                        ) {
                            Text(
                                text = name.firstOrNull()
                                    ?.uppercaseChar()
                                    ?.toString() ?: "K",
                                fontFamily = bebasNeue,
                                fontSize = 36.sp,
                                color = EditBg
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        Text(
                            text = name.uppercase().ifEmpty { "USER" },
                            fontFamily = bebasNeue,
                            fontSize = 22.sp,
                            letterSpacing = 3.sp,
                            color = EditTextColor
                        )

                        Spacer(Modifier.height(4.dp))

                        Text(
                            text = email.ifEmpty { "email@example.com" },
                            fontSize = 12.sp,
                            color = EditMuted
                        )

                        Spacer(Modifier.height(12.dp))

                        // joined date badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(100.dp))
                                .background(EditGold.copy(alpha = 0.08f))
                                .border(
                                    0.5.dp,
                                    EditGold.copy(alpha = 0.25f),
                                    RoundedCornerShape(100.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "🎬  Cineverse Member",
                                fontSize = 11.sp,
                                color = EditGold,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    HorizontalDivider(
                        color = EditBorderColor,
                        modifier = Modifier.padding(horizontal = 18.dp)
                    )

                    Spacer(Modifier.height(26.dp))

                    // ── Section Label ──
                    Text(
                        text = "ACCOUNT INFO",
                        fontFamily = bebasNeue,
                        fontSize = 13.sp,
                        letterSpacing = 2.5.sp,
                        color = EditMuted,
                        modifier = Modifier.padding(start = 18.dp, bottom = 12.dp)
                    )

                    // ── Fields ──
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(horizontal = 14.dp)
                    ) {

                        // Username
                        ProfileTextField(
                            label = "Username",
                            value = name,
                            onValueChange = { name = it },
                            icon = Icons.Outlined.Person,
                            hint = "Enter your username"
                        )

                        // Email
                        ProfileTextField(
                            label = "Email Address",
                            value = email,
                            onValueChange = { email = it },
                            icon = Icons.Outlined.Email,
                            keyboardType = KeyboardType.Email,
                            hint = "Enter your email"
                        )
                    }

                    Spacer(Modifier.height(32.dp))

                    // ── Save Button ──
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp)
                            .height(52.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    listOf(EditGold, EditGold2)
                                ),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .clickable {
                                authViewModel.updateUser(
                                    userName = name,
                                    email = email
                                )
                            }
                    ) {
                        if (updateState is AuthViewModel.UpdateState.Loading) {
                            CircularProgressIndicator(
                                color = EditBg,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Outlined.Check,
                                    null,
                                    tint = EditBg,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "SAVE CHANGES",
                                    fontFamily = bebasNeue,
                                    fontSize = 17.sp,
                                    letterSpacing = 2.5.sp,
                                    color = EditBg
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // ── Info note ──
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Changes will be reflected immediately after saving",
                            fontSize = 11.sp,
                            color = EditMuted.copy(alpha = 0.6f),
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        }
    }
}

// ── Reusable Profile Text Field ──
@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector,
    hint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(EditSurface2, RoundedCornerShape(12.dp))
            .border(
                width = if (isFocused) 1.dp else 0.5.dp,
                color = if (isFocused) EditGold.copy(alpha = 0.4f) else EditBorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 18.dp, vertical = 14.dp)
    ) {
        // Label row
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        EditGold.copy(alpha = if (isFocused) 0.2f else 0.12f),
                        RoundedCornerShape(6.dp)
                    )
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = EditGold,
                    modifier = Modifier.size(11.dp)
                )
            }
            Spacer(Modifier.width(7.dp))
            Text(
                text = label.uppercase(),
                fontSize = 10.sp,
                letterSpacing = 1.8.sp,
                color = if (isFocused) EditGold else EditMuted,
                fontWeight = FontWeight.Medium
            )
        }

        // Input
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = EditTextColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            cursorBrush = SolidColor(EditGold),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = hint,
                        fontSize = 15.sp,
                        color = EditMuted.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Light
                    )
                }
                innerTextField()
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { isFocused = it.isFocused }
        )

        // Underline
        HorizontalDivider(
            color = EditGold.copy(alpha = if (isFocused) 0.6f else 0.15f),
            thickness = 1.dp,
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}