package com.github.kmachida12345.healthconnectplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.StepsRecord
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.github.kmachida12345.healthconnectplayground.ui.theme.HealthConnectPlaygroundTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // build a set of permissions for required data types
    val PERMISSIONS = setOf(
        HealthPermission.createReadPermission(StepsRecord::class),
        HealthPermission.createWritePermission(StepsRecord::class)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val healthConnectClient = if (HealthConnectClient.isAvailable(this)) {
            // Health Connect is available and installed.
            HealthConnectClient.getOrCreate(this)
        } else {
            // ...
            null
        }

        // Create the permissions launcher.
        val requestPermissionActivityContract =
            PermissionController.createRequestPermissionResultContract()

        val requestPermissions =
            registerForActivityResult(requestPermissionActivityContract) { granted ->
                if (granted.containsAll(PERMISSIONS)) {
                    // Permissions successfully granted
                } else {
                    // Lack of required permissions
                }
            }

        suspend fun checkPermissionsAndRun(healthConnectClient: HealthConnectClient) {
            val granted =
                healthConnectClient.permissionController.getGrantedPermissions(PERMISSIONS)
            if (granted.containsAll(PERMISSIONS)) {

            } else {
                requestPermissions.launch(PERMISSIONS)
            }
        }


        lifecycleScope.launch {
            checkPermissionsAndRun(healthConnectClient!!)
        }


        setContent {
            HealthConnectPlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background
                ) {

                    HcpNavHost(

                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, onClickButton: () -> Unit, navigateToAnnualScreen: () -> Unit) {
    Column {
        Button(onClick = { onClickButton() }) {
            Text(text = "Hello $name!")

        }
        Button(onClick = { navigateToAnnualScreen() }) {
            Text(text = "Hello $name! see annual records")

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HealthConnectPlaygroundTheme {
        Greeting("Android", {}, {})
    }
}
