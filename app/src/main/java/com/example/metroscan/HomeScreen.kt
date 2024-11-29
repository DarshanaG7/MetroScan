package com.example.metroscan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metroscan.ui.theme.Blue
import com.example.metroscan.ui.theme.MetroScanTheme

class HomeScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            MetroScanTheme {
                HomeScreenMain()
            }
        }
    }

    @Composable
    fun HomeScreenMain() {
        Box (
            modifier = Modifier
                .fillMaxSize()
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                HeaderText()
            }
            Row (
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
            ) {
                HomeScreenText()
            }
        }

    }
    @Composable
    fun HomeScreenText() {
        Row (
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                fontSize = 27.sp,
                fontFamily = FontFamily(Font(R.font.cavolini)),
                text = "Find Where You\n\nWant To Go",
                modifier = Modifier
                    .padding(bottom = 200.dp),
                textAlign = TextAlign.Center
            )
        }

    }

    @Composable
    fun HeaderText(){
        Row (
            verticalAlignment = Alignment.Top,
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = Brush.linearGradient(
                        0.1f to Blue,
                        1.0f to Color.White,
                        start = Offset.Zero,
                        end = Offset.Infinite
                    )
                )
        ) {
            Text(
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.cavolini)),
                text = "MetroScan",
                modifier = Modifier
                    .padding(top = 60.dp, start = 30.dp),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MetroScanTheme {
            HomeScreenMain()
        }
    }
}