import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hingesensornew.distance.database.DistanceItemEntity
import com.example.hingesensornew.hingesensor.database.HingeSensorItemEntity
import com.example.hingesensornew.measurement.Measurement
import com.example.hingesensornew.measurement.database.MeasurementAndDistanceItemAndHingeSensorItem
import com.example.hingesensornew.measurement.database.MeasurementEntity

@Composable
fun MeasurementScreen(
    measurementAndDistanceItemAndHingeSensorItems: List<MeasurementAndDistanceItemAndHingeSensorItem>,
    onMeasurementAddClick:()->Unit,
    onMeasurementDeleteClick:()->Unit,
    onHingeSensorDeleteClick:()->Unit,
    onDistanceDeleteClick:()->Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn() {
        items(measurementAndDistanceItemAndHingeSensorItems) { item ->
            Measurement(
                item,
                onMeasurementAddClick,
                onMeasurementDeleteClick,
                onHingeSensorDeleteClick,
                onDistanceDeleteClick
            )
        }
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun MeasurementScreenPreview() {
    val hingeSensorList = mutableListOf<HingeSensorItemEntity>()
    val distanceSensorList = mutableListOf<DistanceItemEntity>()
    hingeSensorList.add(
        HingeSensorItemEntity(
            1,
            "Test 1",
            12.33f,
            1
        )
    )
    hingeSensorList.add(
        HingeSensorItemEntity(
            2,
            "Test 2",
            15.33f,
            1
        )
    )
    distanceSensorList.add(
        DistanceItemEntity(
            1,
            "Test 1",
            15.33f,
            1
        )
    )
    distanceSensorList.add(
        DistanceItemEntity(
            2,
            "Test 2",
            15.33f,
            1
        )
    )
    val measurementEntity1 = MeasurementEntity(
        1,
        "Test 1"
    )
    val measurementEntity2 = MeasurementEntity(
        2,
        "Test 2"
    )
    val measurementList = mutableListOf<MeasurementAndDistanceItemAndHingeSensorItem>()

    measurementList.add(
        MeasurementAndDistanceItemAndHingeSensorItem(
        measurementEntity1,
        distanceSensorList,
        hingeSensorList
        )
    )

    measurementList.add(MeasurementAndDistanceItemAndHingeSensorItem(
        measurementEntity2,
        distanceSensorList,
        hingeSensorList
    ))

   MeasurementScreen(
       measurementList,
       {},
       {},
       {},
       {}
   )
}