import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hingesensornew.hingesensor.database.HingeSensorItemEntity
import com.example.hingesensornew.measurement.Measurement
import com.example.hingesensornew.measurement.database.MeasurementAndHingeSensorItem
import com.example.hingesensornew.measurement.database.MeasurementEntity

@Composable
fun MeasurementScreen(
    measurementAndHingeSensorItems:List<MeasurementAndHingeSensorItem>,
    onMeasurementAddClick:()->Unit,
    onMeasurementDeleteClick:()->Unit,
    onHingeSensorDeleteClick:()->Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn() {
        items(measurementAndHingeSensorItems) { item ->
            Measurement(
                item,
                onMeasurementAddClick,
                onMeasurementDeleteClick,
                onHingeSensorDeleteClick
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun MeasurementScreenPreview() {
    val list = mutableListOf<HingeSensorItemEntity>()
    list.add(
        HingeSensorItemEntity(
            1,
            "Test 1",
            12.33f,
            2
        )
    )
    list.add(
        HingeSensorItemEntity(
            1,
            "Test 2",
            15.33f,
            2
        )
    )
    val measurement1 = MeasurementAndHingeSensorItem(
        MeasurementEntity(
            1,
            "Test 1"
        ),
        list
    )
    val measurement2 = MeasurementAndHingeSensorItem(
        MeasurementEntity(
            1,
            "Test 2"
        ),
        list
    )
    val measurementList = mutableListOf<MeasurementAndHingeSensorItem>()
    measurementList.add(measurement1)
    measurementList.add(measurement2)
   MeasurementScreen(
        measurementList,
       {},
       {},
       {}
   )
}