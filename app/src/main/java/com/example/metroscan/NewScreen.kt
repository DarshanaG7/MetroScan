import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.metroscan.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MetroScanApp()
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MetroScanApp() {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header
                TopAppBar(
                    title = { Text(text = "METROSCAN") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1565C0),
                        titleContentColor = Color.White
                    )
                )

                // Map Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        properties = MapProperties(isMyLocationEnabled = true)
                    )
                }

                // Text
                Text(
                    text = "Find Where You Want To Go",
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )

                // QR Code and Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Account Button
                    IconButton(onClick = { /* Navigate to Account */ }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.account),
                                contentDescription = "Account",
                                tint = Color.Black,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(text = "Account", fontSize = 12.sp)
                        }
                    }

                    // QR Code Button
                    IconButton(onClick = { /* QR Code Scanner */ }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.qr_code),
                                contentDescription = "Scan QR Code",
                                tint = Color.Black,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(text = "SCAN ME", fontSize = 12.sp)
                        }
                    }

                    // Emergency Button
                    IconButton(onClick = { /* Navigate to Emergency */ }) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(id = R.drawable.emergency),
                                contentDescription = "Emergency",
                                tint = Color.Red,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(text = "Emergency", fontSize = 12.sp, color = Color.Red)
                        }
                    }
                }
            }
        }

    }
    @Preview
    @Composable
    fun Greeting(){
        MetroScanApp()
    }
}
