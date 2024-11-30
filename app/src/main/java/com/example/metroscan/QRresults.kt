package com.example.metroscan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metroscan.HomeScreen.Companion.QR_CODE_REQUEST
import com.example.metroscan.ui.theme.Blue
import com.example.metroscan.ui.theme.DarkBlue
import com.example.metroscan.ui.theme.MetroScanTheme
import com.google.firebase.database.*
import com.journeyapps.barcodescanner.CaptureActivity

class QRresults : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val qrCodeData = intent.getStringExtra(HomeScreen.QR_CODE_DATA)
            var scrollState = rememberScrollState()
            if (qrCodeData != null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        HeaderText()
                    }
                    Row(modifier = Modifier.align(Alignment.BottomCenter)) {
                        Footer()
                    }
                    Box(modifier = Modifier.align(Alignment.Center).padding(bottom = 70.dp).height(550.dp)){
                        Row() {
                            QRResultScreen(scrollState = scrollState, qrCodeData)
                        }
                    }

                }
            } else {
                Text(text = "No QR Code data found.")
            }
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeScreen::class.java) // Replace HomeScreen::class.java with your actual HomeScreen class
        startActivity(intent)
        finish() // Optionally call finish() to remove QRresults from the back stack
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
                        val intent = Intent(this@QRresults, CaptureActivity::class.java)
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

    @Composable
    fun QRResultScreen(scrollState: ScrollState, id: String) {
        var stopDetails: Map<String, Any>? by remember { mutableStateOf(null) }
        var matchingStops: List<Map<String, Any>> by remember { mutableStateOf(emptyList()) }
        var errorMessage: String? by remember { mutableStateOf(null) }

        LaunchedEffect(id) {
            fetchDataById(id) { details, error ->
                stopDetails = details
                errorMessage = error

                // If data exists, try to fetch matching stops
                if (details != null) {
                    val fromField = details["From"] as? String
                    if (fromField != null) {
                        fetchMatchingStops(fromField) { stops ->
                            matchingStops = stops
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(scrollState)) {
            if (errorMessage != null) {
                Text(text = "Error: $errorMessage")
            } else if (stopDetails != null) {
                val from = stopDetails?.get("From") as? String ?: "N/A"
                val to = stopDetails?.get("To") as? String ?: "N/A"
                val routes = stopDetails?.get("Routes") as? List<*> ?: emptyList<String>()
                // Display the first stop details
                SectionHeader(title = "From: $from")
                Text(text = "To: $to")
                Text(text = "Routes: ${routes.joinToString(", ")}")
                Divider()

                // Display matching stops
                if (matchingStops.isNotEmpty()) {
                    matchingStops.forEach { stop ->
                        val matchTo = stop["To"] as? String ?: "N/A"
                        val matchRoutes = stop["Routes"] as? List<*> ?: emptyList<String>()
                        SectionHeader(title = "To: $matchTo")
                        Text(text = "Routes: ${matchRoutes.joinToString(", ")}")
                        Divider()
                    }
                }
            } else {
                Text(text = "Loading...")
            }
        }
    }

    @Composable
    fun SectionHeader(title: String) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }

    @Composable
    fun Divider() {
        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.Gray)
                .padding(vertical = 8.dp)
        )
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

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        MetroScanTheme {
        }
    }

    private fun fetchDataById(id: String, onComplete: (Map<String, Any>?, String?) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("bus_stops").child(id)

        ref.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                val details = mapOf(
                    "From" to dataSnapshot.child("From").value.toString(),
                    "To" to dataSnapshot.child("To").value.toString(),
                    "Routes" to dataSnapshot.child("routes").children.map { it.value.toString() }
                )
                onComplete(details, null)
            } else {
                onComplete(null, "No data found for ID: $id")
            }
        }.addOnFailureListener { e ->
            Log.e("Firebase", "Error retrieving data", e)
            onComplete(null, e.message)
        }
    }

    private fun fetchMatchingStops(from: String, onComplete: (List<Map<String, Any>>) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("bus_stops")

        ref.orderByChild("From").equalTo(from).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val stops = mutableListOf<Map<String, Any>>()
                for (stopSnapshot in dataSnapshot.children) {
                    val stop = mapOf(
                        "From" to stopSnapshot.child("From").value.toString(),
                        "To" to stopSnapshot.child("To").value.toString(),
                        "Routes" to stopSnapshot.child("routes").children.map { it.value.toString() }
                    )
                    stops.add(stop)
                }
                Log.d("Firebase", "Matching Stops: $stops")
                onComplete(stops)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Firebase", "Error fetching matching stops: ${databaseError.message}")
                onComplete(emptyList())
            }
        })
    }
}
