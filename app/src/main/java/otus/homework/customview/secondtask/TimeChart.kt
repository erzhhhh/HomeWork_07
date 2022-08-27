package otus.homework.customview.secondtask

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import otus.homework.customview.Payload
import java.util.*
import kotlin.collections.HashMap
import kotlin.random.Random

class TimeChart @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var minDate: Long = Long.MAX_VALUE
    private var maxDate: Long = Long.MIN_VALUE

    private var minAmount: Int = Int.MAX_VALUE
    private var maxAmount: Int = Int.MIN_VALUE

    private val hashMap = HashMap<String, MutableList<CategoryEntity>>()
    private val paint = Paint()
    private val path = Path()
    private val point = Point()

    init {
        paint.color = Color.RED
        if (isInEditMode) {
            setPayload(
                listOf(
                    Payload("", "", 2, "a", 0),
                    Payload("", "", 4, "a", 0),
                    Payload("", "", 5, "b", 0),
                    Payload("", "", 7, "c", 0),
                )
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)


//        val minSize = Math.min(width, height).toFloat()
//
//        val halfHeight = (height / 2).toFloat()
//        val halfWidth = (width / 2).toFloat()
//        val halfMinSize = minSize / 2
//
//
//        hashMap.forEach {
//            val category = it.key
//            val categoryEntries: MutableList<CategoryEntity> = it.value
//
//            categoryEntries.forEach {
//                val time = it.time
//                val amount = it.amount
//
//                point.x =
//
//                path.cubicTo()
//            }
//        }
    }

    fun setPayload(payload: List<Payload>) {
        payload.forEach {
            if (hashMap[it.category] != null) {
                val oldEntity: MutableList<CategoryEntity>? = hashMap[it.category]
                oldEntity?.add(CategoryEntity(it.time, it.amount))
                hashMap[it.category] = oldEntity!!
            } else {
                hashMap[it.category] = mutableListOf(CategoryEntity(it.time, it.amount))
            }

            if (it.amount < minAmount) {
                minAmount = it.amount
            }

            if (it.amount > maxAmount) {
                maxAmount = it.amount
            }

            if (it.time < minDate) {
                minDate = it.time
            }

            if (it.time > maxDate) {
                maxDate = it.time
            }
        }

        invalidate()
    }

}

private data class CategoryEntity(
    val time: Long,
    val amount: Int
)