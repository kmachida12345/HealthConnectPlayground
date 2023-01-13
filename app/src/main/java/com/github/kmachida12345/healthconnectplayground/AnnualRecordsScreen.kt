package com.github.kmachida12345.healthconnectplayground

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime

private val flow: MutableStateFlow<Long> = MutableStateFlow(0)

@Composable
fun AnnualRecordsScreen() {

    val healthConnectClient = HealthConnectClient.getOrCreate(LocalContext.current)

    val now = LocalDateTime.now()


    LaunchedEffect(Unit) {
        val res = healthConnectClient.aggregate(
            AggregateRequest(
                metrics = setOf(StepsRecord.COUNT_TOTAL),
                timeRangeFilter = TimeRangeFilter.between(now.minusYears(1), now)
            )
        )
        // The result may be null if no data is available in the time range.
        val stepsTotal = res[StepsRecord.COUNT_TOTAL]
        Log.d("machida", "checkPermissionsAndRun: hoge $stepsTotal")
        flow.value = stepsTotal ?: 0
    }

    val data by flow.collectAsState()
    
    Text(text = "1年間の合計歩数は、、 $data")

}
