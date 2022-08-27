package otus.homework.customview.firsttask

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import otus.homework.customview.Payload
import kotlin.random.Random

class PieChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var onCategoryClickListener: ((String) -> Unit)? = null

    private var isDown = false
    private var payloadHM = HashMap<String, ChartEntry>()
    private var totalAmount = 0F

    private val paint = Paint()
    private val path = Path()

    init {
        paint.flags = Paint.ANTI_ALIAS_FLAG
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

        val minSize = Math.min(width, height).toFloat()

        val halfHeight = (height / 2).toFloat()
        val halfWidth = (width / 2).toFloat()
        val halfMinSize = minSize / 2

        path.addCircle(halfWidth, halfHeight, halfMinSize / 2, Path.Direction.CW)
        canvas.clipPath(path, Region.Op.DIFFERENCE)


        payloadHM.forEach {
            val charEntry = it.value


            paint.color = charEntry.color
            canvas.drawArc(
                halfWidth - halfMinSize,
                halfHeight - halfMinSize,
                halfWidth + halfMinSize,
                halfHeight + halfMinSize,
                charEntry.startAngle,
                charEntry.endAngle - charEntry.startAngle,
                true,
                paint
            )
        }
    }

    fun setPayload(payload: List<Payload>) {
        totalAmount = 0F
        val random = Random(0)

        payload.forEach {
            val oldEntry = payloadHM[it.category]
            if (oldEntry != null) {
                payloadHM[it.category] = oldEntry.copy(amount = oldEntry.amount + it.amount)
            } else {
                val color =
                    Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
                payloadHM[it.category] = ChartEntry(amount = it.amount, color = color)
            }
            totalAmount += it.amount
        }


        var lastAngle = 0F

        payloadHM.forEach {
            val amount = it.value.amount.toFloat()
            val amountPercentage = amount / totalAmount
            val degreePercentage = amountPercentage * 360

            val oldEntry = payloadHM[it.key]
            oldEntry?.let { entity ->
                payloadHM[it.key] =
                    oldEntry.copy(startAngle = lastAngle, endAngle = lastAngle + degreePercentage)
            }

            lastAngle += degreePercentage
        }

        invalidate()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val halfHeight = (height / 2).toDouble()
        val halfWidth = (width / 2).toDouble()

        val minSize = Math.min(width, height).toFloat()
        val outerRadius = minSize / 2
        val innerRadius = outerRadius / 2

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isDown = true
                return true
            }
            MotionEvent.ACTION_CANCEL -> {
                isDown = false
            }
            MotionEvent.ACTION_UP -> {
                isDown = false

                val radius = Math.sqrt(
                    Math.pow(halfWidth - event.x, 2.0) +
                            Math.pow(halfHeight - event.y, 2.0)
                )

                val isInBorder = radius in innerRadius..outerRadius

                if (isInBorder) {
                    var degree = Math.toDegrees(Math.acos((event.x - halfWidth) / radius))
                    if (halfHeight >= event.y) {
                        degree = 360 - degree
                    }
                    val category = payloadHM.entries.find {
                        it.value.startAngle < degree &&  it.value.endAngle >= degree
                    }

                    if (category != null){
                        onCategoryClickListener?.invoke(category.key)
                    }
                    return true
                } else {
                    return false
                }
            }
        }

        return super.onTouchEvent(event)
    }
}

data class ChartEntry(
    val amount: Int,
    val color: Int,
    val startAngle: Float = 0f,
    val endAngle: Float = 0f,
)