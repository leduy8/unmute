package com.example.unmute.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.unmute.R
import com.example.unmute.navigation.Screen

@Composable
fun GreetingScreen(navController: NavHostController) {
    val imagePainter = painterResource(id = R.drawable.hope_logo)

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .statusBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Logo + Divider
            Image(
                painter = imagePainter,
                contentDescription = "Hope Logo",
                modifier =
                    Modifier
                        .size(56.dp)
                        .clip(CircleShape),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Feature Cards
            FeatureCard(
                iconPainter = painterResource(id = R.drawable.sign_to_speech),
                title = "Sign to Speech",
                subtitle = "Translate sign gestures into natural-sounding speech",
            )
            FeatureCard(
                iconPainter = painterResource(id = R.drawable.speech_to_text),
                title = "Speech to Text",
                subtitle = "Convert speech into readable text",
            )
            FeatureCard(
                iconPainter = painterResource(id = R.drawable.isolation),
                title = "Speech Isolation",
                subtitle = "Filter background noise for clearer communication",
            )
            FeatureCard(
                iconPainter = painterResource(id = R.drawable.emoji_smile),
                title = "Facial Expression Recognition",
                subtitle = "Capture emotional context for enhanced understanding",
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Push button to bottom if there's space
            Spacer(modifier = Modifier.weight(1f, fill = true))

            Button(
                onClick = { navController.navigate(Screen.Camera.route) },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text(text = "Start Signing →", fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun FeatureCard(
    iconPainter: Painter,
    title: String,
    subtitle: String,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F9FF)),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = iconPainter,
                contentDescription = null,
                modifier = Modifier.size(36.dp),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = Color.Gray,
                )
            }
        }
    }
}
