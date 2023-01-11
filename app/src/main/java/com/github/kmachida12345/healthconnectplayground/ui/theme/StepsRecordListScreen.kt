package com.github.kmachida12345.healthconnectplayground.ui.theme

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.LocalDateTime

@Composable
fun StepsRecordListScreen() {
    val healthConnectClient = HealthConnectClient.getOrCreate(LocalContext.current)


    // Permissions already granted, proceed with inserting or reading data.
    val now = LocalDateTime.now()

    LaunchedEffect(Unit) {
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
    }


//    LazyColumn {
//        items(response.records) {
//
//        }
//
//    }

}
