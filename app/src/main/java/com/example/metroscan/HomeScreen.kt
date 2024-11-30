package com.example.metroscan
import android.Manifest
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.android.gms.maps.model.LatLng

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.metroscan.ui.theme.Blue
import com.example.metroscan.ui.theme.DarkBlue
import com.example.metroscan.ui.theme.MetroScanTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.journeyapps.barcodescanner.CaptureActivity

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
        Box(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                HeaderText()
            }
            Row(modifier = Modifier.align(Alignment.BottomCenter)) {
                HomeScreenText()
            }
            Row(modifier = Modifier.align(Alignment.BottomCenter)) {
                Footer()
            }
            Box(modifier = Modifier.align(Alignment.Center).padding(bottom = 70.dp).height(450.dp)){
                Row() {
                    MapView()
                }
            }

        }
    }

    @Composable
    fun RequestLocationPermission(onPermissionGranted: () -> Unit) {
        val context = LocalContext.current
        val activity = context as? ComponentActivity

        // Remember the ActivityResultLauncher
        val requestPermissionLauncher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    onPermissionGranted()
                } else {
                    Toast.makeText(context, "Location permission is required to use this feature", Toast.LENGTH_SHORT).show()
                }
            }

        // Check if the permission is already granted
        val hasPermission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            onPermissionGranted()
        } else {
            // Trigger the permission request
            LaunchedEffect(Unit) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }


    @Composable
    fun MapView(){
        Box(modifier = Modifier
            .padding(horizontal = 30.dp)
            .padding(bottom = 60.dp, top = 0.dp)
            .height(1200.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.map),
                contentDescription = "Live Location",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

    }

    @Composable
    fun MapBox(){
        val cameraPositionState = rememberCameraPositionState {
            position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(
                LatLng(12.9716, 77.5946), // Bengaluru
                12f // Zoom level
            )
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true)
        )

    }

    @Composable
    fun Footer() {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.linearGradient(
                            0.1f to DarkBlue,
                            1.0f to Blue,
                            start = androidx.compose.ui.geometry.Offset.Zero,
                            end = androidx.compose.ui.geometry.Offset.Infinite
                        )
                    )
                    .align(Alignment.BottomCenter)
            ) {}

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .padding(bottom = 77.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = {
                        val intent = Intent(this@HomeScreen, CaptureActivity::class.java)
                        startActivityForResult(intent, QR_CODE_REQUEST)
                    },
                    modifier = Modifier.size(90.dp) // Define the size of the button
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.qr_code),
                        contentDescription = "Scan QR Code",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(5.dp))
                            .background(Color.White)
                    )
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp)
                    .padding(bottom = 50.dp, top = 60.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { /* Navigate to Account */ }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.account),
                            contentDescription = "Account",
                            tint = Color.Black,
                            modifier = Modifier
                                .size(100.dp)
                                .scale(1.07f)
                        )
                        Text(text = "Account", fontSize = 12.sp)
                    }
                }

                IconButton(onClick = { /* Navigate to Emergency */ }) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.emergency),
                            contentDescription = "Emergency",
                            tint = Color.Red,
                            modifier = Modifier
                                .size(50.dp)
                                .scale(2.0f)
                        )
                        Text(text = "Emergency", fontSize = 12.sp, color = Color.Red)
                    }
                }
                }

        }
    }

    @Composable
    fun HomeScreenText() {
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Text(
                fontSize = 27.sp,
                text = "Find Where You\n\nWant To Go",
                fontFamily = FontFamily(Font(R.font.madimione)),
                modifier = Modifier.padding(bottom = 200.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }

    @Composable
    fun HeaderText() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    brush = Brush.linearGradient(
                        0.1f to Blue,
                        1.0f to DarkBlue,
                        start = androidx.compose.ui.geometry.Offset.Zero,
                        end = androidx.compose.ui.geometry.Offset.Infinite
                    )
                )
        ) {
            Text(
                fontSize = 30.sp,
                fontFamily = FontFamily(Font(R.font.cavolini)),
                text = "MetroScan",
                modifier = Modifier.padding(top = 50.dp, start = 30.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = Color.White
            )
        }
    }

    private var scannedQRCode: String? = null

    companion object {
        const val QR_CODE_REQUEST = 1
        const val QR_CODE_DATA = "QR_CODE_DATA"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == QR_CODE_REQUEST && resultCode == RESULT_OK) {
            val scannedCode = data?.getStringExtra("SCAN_RESULT")
            if (scannedCode != null) {
                val intent = Intent(this, QRresults::class.java)
                intent.putExtra(QR_CODE_DATA, scannedCode)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to scan QR Code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleScannedQRCode(qrCode: String) {
        Toast.makeText(this, "Scanned QR Code: $qrCode", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MetroScanTheme {
            HomeScreenMain()
        }
    }
}
