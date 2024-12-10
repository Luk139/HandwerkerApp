import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hingesensornew.measurement.Measurement
import com.example.hingesensornew.measurement.MeasurementScreenViewModel
import com.example.hingesensornew.measurement.database.MeasurementEntity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

@Composable
fun MeasurementScreen(
    measurementScreenViewModel: MeasurementScreenViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
            delay(1000)
            Log.d("Load Measurements","Lade all Measurements")
            Log.d("MeasurementScreen View Model",measurementScreenViewModel.toString())
        try {
            measurementScreenViewModel.loadMeasurements()
        } catch (e: Exception) {
            Log.e("Load Measurements", "Error in loadMeasurements: ${e.message}")
        }
        Log.d("Finished Loading","Finished loading Process")
    }

    val measurementAndDistanceItemAndHingeSensorItems by measurementScreenViewModel.measurements.collectAsState()
    LazyColumn() {
        items(measurementAndDistanceItemAndHingeSensorItems) { item ->
            Measurement(
                item,
                {
                    measurement-> measurementScreenViewModel.deleteMeasurement(measurement)
                },
                {
                    hingeSensorItemEntity -> measurementScreenViewModel.deleteHingeSensorEntity(hingeSensorItemEntity)
                },
                {
                    distanceItemEntity -> measurementScreenViewModel.deleteDistanceEntity(distanceItemEntity)
                }
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun MeasurementScreenPreview() {
}