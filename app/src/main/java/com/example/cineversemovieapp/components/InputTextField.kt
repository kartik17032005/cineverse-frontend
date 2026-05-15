package com.example.cineversemovieapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InputTextField(
    modifier: Modifier = Modifier,
    valueState: MutableState<String>,
    labelId: String,
    enabled: Boolean,
    isSingleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    onAction: KeyboardActions = KeyboardActions.Default,
    leadingIcon: ImageVector = Icons.Filled.Person
) {
    val gold = Color(0xFFCCA246)
    val muted = Color(0xFF5E5C68)
    val surfaceColor = Color(0xFF1A1A24)

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(bottom = 8.dp),        // 👈 spacing between fields
        value = valueState.value,
        label = {
            Text(
                text = labelId,
                color = muted,              // 👈 styled label
                fontSize = 12.sp
            )
        },
        onValueChange = { newVal ->
            valueState.value = newVal
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = muted               // 👈 icon color
            )
        },
        textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
        enabled = enabled,
        singleLine = isSingleLine,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = onAction,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(  // 👈 theme the field
            focusedBorderColor = gold,
            unfocusedBorderColor = muted.copy(alpha = 0.3f),
            focusedLabelColor = gold,
            unfocusedLabelColor = muted,
            cursorColor = gold,
            focusedLeadingIconColor = gold,
            unfocusedLeadingIconColor = muted,
            focusedContainerColor = surfaceColor,
            unfocusedContainerColor = surfaceColor,
        )
    )
}