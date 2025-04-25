package com.example.unmute.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.unmute.R
import com.example.unmute.navigation.Screen

@Composable
fun SignInScreen(navController: NavHostController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color(0xFFFFFFFF)),
        // soft background, white
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier =
                Modifier
                    .padding(24.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .padding(24.dp)
                    .fillMaxWidth(0.9f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.hope_logo),
                contentDescription = "App Logo",
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(CircleShape),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider(color = Color.LightGray, thickness = 1.dp)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome to Hope",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
            Text(
                text = "Hope gives voice to silence",
                color = Color.Gray,
                fontSize = 14.sp,
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = { Text("Enter your username") },
                label = { Text("Username") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Enter your password") },
                label = { Text("Password") },
                leadingIcon = {
                    Icon(Icons.Default.Lock, contentDescription = null)
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // TODO: handle login later
                    Log.e("SignInScreen", "Login button clicked")
                    navController.navigate(Screen.Greeting.route)
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp),
            ) {
                Text("Login →")
            }

            Spacer(modifier = Modifier.height(16.dp))

            SignInFooter(
                onForgotPasswordClick = {
                    // Navigate to Forgot Password
                    Log.e("SignInScreen", "Forgot Password button clicked")
                },
                onSignUpClick = {
                    // Navigate to Sign Up
                    Log.e("SignInScreen", "Sign Up button clicked")
                },
            )
        }
    }
}

@Composable
fun SignInFooter(
    onForgotPasswordClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Forgot password button
        TextButton(onClick = onForgotPasswordClick) {
            Text(
                text = "Forgot password?",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
            )
        }
        SignUpPrompt(onSignUpClick = onSignUpClick)
    }
}

@Composable
fun SignUpPrompt(onSignUpClick: () -> Unit) {
    val annotatedText =
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Gray, fontSize = 14.sp)) {
                append("Don't have an account? ")
            }
            pushStringAnnotation(tag = "SIGN_UP", annotation = "sign_up")
            withStyle(
                style =
                    SpanStyle(
                        color = Color(0xFF1A237E), // Darker blue
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        textDecoration = TextDecoration.None,
                    ),
            ) {
                append("Sign up")
            }
            pop()
        }

    ClickableText(
        text = annotatedText,
        onClick = { offset ->
            annotatedText.getStringAnnotations("SIGN_UP", start = offset, end = offset)
                .firstOrNull()?.let { onSignUpClick() }
        },
        modifier = Modifier,
    )
}
