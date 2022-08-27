package otus.homework.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import otus.homework.customview.firsttask.PieChartView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val collection = Gson().fromJson<List<Payload>>(
            resources.openRawResource(R.raw.payload).reader(),
            object : TypeToken<List<Payload>>() {}.type
        )

        // first task
        findViewById<PieChartView>(R.id.pieChart).apply {
            onCategoryClickListener =
                { Toast.makeText(this@MainActivity, it, Toast.LENGTH_SHORT).show() }
            setPayload(collection)
        }
    }
}

data class Payload(
    val id: String,
    val name: String,
    val amount: Int,
    val category: String,
    val time: Long
)