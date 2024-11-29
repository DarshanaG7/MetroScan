package com.example.metroscan

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metroscan.ui.theme.MetroScanTheme
import com.example.metroscan.ui.theme.Pink
import com.example.metroscan.ui.theme.Purple

class MainActivity : ComponentActivity() {
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SplashScreen()
                }
            }
        }
        Handler().postDelayed({
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
            finish()
        }, 3500)
    }


    @Composable
    fun SplashScreen() {
        Box(
            modifier = Modifier
                .fillMaxSize()

        ){
            Text(
                text = "MetroScan",
                fontSize = 40.sp,
                fontFamily = FontFamily(Font(R.font.montserratalternates_black)),
                modifier = Modifier
                    .align(alignment = Alignment.TopCenter)
                    .padding(top = 240.dp)
            )
            Image(painter = painterResource(id = R.drawable.bus_icon), contentDescription = "Mypic",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .sizeIn(400.dp)
                    .align(alignment = Alignment.Center)
                    .padding(start = 50.dp, end = 50.dp, top = 50.dp)
            )
            Text(
                text = "A Scan Step Away",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.protestguerrilla)),
                modifier = Modifier
                    .align(alignment = Alignment.BottomCenter)
                    .padding(bottom = 225.dp)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MetroScanTheme{
            SplashScreen()
        }
    }
}