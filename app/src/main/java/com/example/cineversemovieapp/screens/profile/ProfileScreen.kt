package com.example.cineversemovieapp.screens.profile

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.cineversemovieapp.R
import com.example.cineversemovieapp.navigation.Routes
import com.example.cineversemovieapp.ui.theme.gold
import com.example.cineversemovieapp.viewmodel.AuthViewModel
import com.example.cineversemovieapp.viewmodel.BadgeViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel(),
    badgeViewModel: BadgeViewModel = viewModel()
) {
    val gold = gold
    val bg = Color(0xFF0A0A0F)
    val surface = Color(0xFF1A1A24)
    val muted = Color(0xFF5E5C68)
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    val userProfileState by authViewModel.userProfile.collectAsState()
    val watchlistIds by authViewModel.watchlistIds.collectAsState()
    val badges by badgeViewModel.badges.collectAsState()
    val unlockedCount = badges.count { it.isUnlocked }
    
    val notificationsEnabled = remember { mutableStateOf(true) }
    val subtitlesEnabled = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authViewModel.getUserProfile()
        authViewModel.getWatchlist()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .verticalScroll(rememberScrollState())
    ) {

        // ── HEADER ──
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF1a1a24), Color(0xFF0a0a0f))
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(gold.copy(alpha = 0.05f))
                    .align(Alignment.TopEnd)
            )

            Box(
                modifier = Modifier
                    .padding(top = 52.dp, start = 18.dp)
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .border(1.dp, Color.White.copy(alpha = 0.12f), CircleShape)
                    .align(Alignment.TopStart)
                    .clickable { navController.popBackStack() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }

            when (val state = userProfileState) {
                is AuthViewModel.UserProfileState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = gold)
                    }
                }
                is AuthViewModel.UserProfileState.Success -> {
                    val user = state.user
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 40.dp)
                    ) {
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(
                                        Brush.linearGradient(
                                            colors = listOf(gold, Color(0xFFa87830))
                                        )
                                    )
                                    .border(3.dp, bg, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = (user.userName.firstOrNull() ?: 'U').uppercase().toString(),
                                    fontFamily = bebasNeue,
                                    fontSize = 36.sp,
                                    color = bg
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(gold)
                                    .border(2.dp, bg, CircleShape)
                                    .align(Alignment.BottomEnd)
                                    .clickable { navController.navigate(Routes.EditProfile.route) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit",
                                    tint = bg,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }

                        Text(
                            text = user.userName.uppercase(),
                            fontFamily = bebasNeue,
                            fontSize = 22.sp,
                            letterSpacing = 3.sp,
                            color = Color.White
                        )

                        Text(
                            text = user.email,
                            fontSize = 12.sp,
                            color = muted
                        )
                    }
                }
                is AuthViewModel.UserProfileState.Error -> {
                    Text(
                        text = "Error loading profile",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {}
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ── STATS ROW (Updated with Badges) ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(surface)
                .border(1.dp, Color.White.copy(alpha = 0.07f), RoundedCornerShape(16.dp))
        ) {
            StatItem(value = watchlistIds.size.toString(), label = "Watchlist", modifier = Modifier.weight(1f))
            StatDivider()
            StatItem(value = unlockedCount.toString(), label = "Badges", modifier = Modifier.weight(1f))
            StatDivider()
            StatItem(value = "0", label = "Reviews", modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        SectionLabel(title = "ACCOUNT", bebasNeue = bebasNeue, muted = muted)

        MenuCard {
            MenuItem(
                icon = Icons.Filled.Person,
                iconBg = Color(0xFF1a1a24),
                iconTint = gold,
                label = "Edit Profile",
                sub = "Update your info",
                onClick = {
                    navController.navigate(Routes.EditProfile.route)
                }
            )
            MenuDivider()
            MenuItem(
                icon = Icons.Filled.Stars,
                iconBg = Color(0xFF2E1A0A),
                iconTint = gold,
                label = "My Badges",
                sub = "View your film achievements",
                onClick = {
                    navController.navigate(Routes.Badges.route)
                }
            )
            MenuDivider()
            SwitchMenuItem(
                icon = Icons.Filled.Notifications,
                iconBg = Color(0xFF0d1a2e),
                iconTint = Color(0xFF378ADD),
                label = "Notifications",
                sub = "New releases & updates",
                checked = notificationsEnabled.value,
                onCheckedChange = { notificationsEnabled.value = it }
            )
            MenuDivider()
            MenuItem(
                icon = Icons.Filled.Lock,
                iconBg = Color(0xFF0d1a12),
                iconTint = Color(0xFF639922),
                label = "Change Password",
                sub = "Last changed 30 days ago",
                onClick = {}
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        SectionLabel(title = "PREFERENCES", bebasNeue = bebasNeue, muted = muted)

        MenuCard {
            BadgeMenuItem(
                icon = Icons.Filled.Wifi,
                iconBg = Color(0xFF1a0a2e),
                iconTint = Color(0xFFa882e6),
                label = "Streaming Quality",
                sub = "Currently set to HD",
                badge = "HD"
            )
            MenuDivider()
            MenuItem(
                icon = Icons.Filled.Language,
                iconBg = Color(0xFF1a1a0d),
                iconTint = gold,
                label = "Language",
                sub = "English",
                onClick = {}
            )
            MenuDivider()
            SwitchMenuItem(
                icon = Icons.Filled.Subtitles,
                iconBg = Color(0xFF0d1a2e),
                iconTint = Color(0xFF378ADD),
                label = "Subtitles",
                sub = "Auto detect",
                checked = subtitlesEnabled.value,
                onCheckedChange = { subtitlesEnabled.value = it }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        SectionLabel(title = "SUPPORT", bebasNeue = bebasNeue, muted = muted)

        MenuCard {
            MenuItem(
                icon = Icons.Filled.QuestionMark,
                iconBg = Color(0xFF1a1a0d),
                iconTint = gold,
                label = "Help & Support",
                sub = "FAQs and contact",
                onClick = {}
            )
            MenuDivider()
            MenuItem(
                icon = Icons.Filled.Policy,
                iconBg = Color(0xFF0d1a12),
                iconTint = Color(0xFF639922),
                label = "Privacy Policy",
                sub = "Terms & conditions",
                onClick = {}
            )
            MenuDivider()
            BadgeMenuItem(
                icon = Icons.Filled.Info,
                iconBg = Color(0xFF0d1a2e),
                iconTint = Color(0xFF378ADD),
                label = "App Version",
                sub = "v1.0.0",
                badge = "Latest"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ── SIGN OUT (Fixed with logout logic) ──
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFE24B4A).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .background(Color(0xFFE24B4A).copy(alpha = 0.06f))
                .clickable {
                    authViewModel.logout()
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = "Sign Out",
                tint = Color(0xFFE24B4A),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Sign Out",
                fontSize = 14.sp,
                color = Color(0xFFE24B4A),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun StatItem(value: String, label: String, modifier: Modifier = Modifier) {
    val gold = gold
    val bebasNeue = FontFamily(Font(R.font.bebas_neue_regular))

    Column(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            fontFamily = bebasNeue,
            fontSize = 22.sp,
            color = gold,
            letterSpacing = 1.sp
        )
        Text(text = label, fontSize = 10.sp, color = Color(0xFF5E5C68), letterSpacing = 0.5.sp)
    }
}

@Composable
fun StatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(Color.White.copy(alpha = 0.07f))
    )
}

@Composable
fun SectionLabel(title: String, bebasNeue: FontFamily, muted: Color) {
    Text(
        text = title,
        fontFamily = bebasNeue,
        fontSize = 13.sp,
        letterSpacing = 2.sp,
        color = muted,
        modifier = Modifier.padding(start = 22.dp, bottom = 10.dp)
    )
}

@Composable
fun MenuCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1A1A24))
            .border(1.dp, Color.White.copy(alpha = 0.07f), RoundedCornerShape(16.dp))
    ) {
        content()
    }
}

@Composable
fun MenuDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 66.dp, end = 16.dp)
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.07f))
    )
}

@Composable
fun MenuItem(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    sub: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 14.sp, color = Color.White)
            Text(
                text = sub,
                fontSize = 11.sp,
                color = Color(0xFF5E5C68),
                fontWeight = FontWeight.Light
            )
        }

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = Color(0xFF5E5C68),
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
fun SwitchMenuItem(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    sub: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val gold = gold

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 14.sp, color = Color.White)
            Text(
                text = sub,
                fontSize = 11.sp,
                color = Color(0xFF5E5C68),
                fontWeight = FontWeight.Light
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF0A0A0F),
                checkedTrackColor = gold,
                uncheckedThumbColor = Color(0xFF5E5C68),
                uncheckedTrackColor = Color(0xFF2A2A34)
            )
        )
    }
}

@Composable
fun BadgeMenuItem(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    label: String,
    sub: String,
    badge: String
) {
    val gold = gold

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 14.sp, color = Color.White)
            Text(
                text = sub,
                fontSize = 11.sp,
                color = Color(0xFF5E5C68),
                fontWeight = FontWeight.Light
            )
        }

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(100.dp))
                .background(gold)
                .padding(horizontal = 10.dp, vertical = 3.dp)
        ) {
            Text(
                text = badge,
                fontSize = 10.sp,
                color = Color(0xFF0A0A0F),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
