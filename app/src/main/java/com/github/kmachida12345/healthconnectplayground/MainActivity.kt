package com.github.kmachida12345.healthconnectplayground

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.lifecycle.lifecycleScope
import com.github.kmachida12345.healthconnectplayground.ui.theme.HealthConnectPlaygroundTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime

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
                // Permissions already granted, proceed with inserting or reading data.
                val now = LocalDateTime.now()
                val response = healthConnectClient.readRecords(
                    ReadRecordsRequest(
                        StepsRecord::class,
                        timeRangeFilter = TimeRangeFilter.between(now.minusDays(1), now)
                    )
                )
                for (stepRecord in response.records) {
                    // Process each step record
                    Log.d(
                        "machida",
                        "checkPermissionsAndRun: ${stepRecord.startTime}, ${stepRecord.endTime} ${stepRecord.count}"
                    )
                }

                val res = healthConnectClient.aggregate(
                    AggregateRequest(
                        metrics = setOf(StepsRecord.COUNT_TOTAL),
                        timeRangeFilter = TimeRangeFilter.between(now.minusYears(1), now)
                    )
                )
                // The result may be null if no data is available in the time range.
                val stepsTotal = res[StepsRecord.COUNT_TOTAL]
                Log.d("machida", "checkPermissionsAndRun: hoge $stepsTotal")

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
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    HealthConnectPlaygroundTheme {
        Greeting("Android")
    }
}
