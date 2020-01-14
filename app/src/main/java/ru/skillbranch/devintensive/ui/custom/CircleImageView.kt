package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import ru.skillbranch.devintensive.R
import kotlin.math.min

class CircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0

) : ImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BORDER_COLOR = Color.WHITE
        private const val DEFAULT_BORDER_WIDTH = 2.0F
    }

    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWidth = DEFAULT_BORDER_WIDTH
    private var circlePaint = Paint()
    private var avaPaint = Paint()
    private lateinit var avaBitmap: Bitmap

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getInt(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth =
                a.getDimension(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            a.recycle()
        }
        circlePaint.color = borderColor

        // Use default (don't-resized) shader
        avaBitmap = BitmapFactory.decodeResource(resources, R.drawable.ava_test)
        val shader = BitmapShader(avaBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        avaPaint.shader = shader
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Use resized shader if it possible
        if (measuredWidth > 0 && measuredHeight > 0) {
            val scaledBitmap =
                Bitmap.createScaledBitmap(avaBitmap, measuredWidth, measuredHeight, false)
            val newShader = BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            avaPaint.shader = newShader
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawCircle(
            width.toFloat() / 2.0F, height.toFloat() / 2.0F,
            min(width, height).toFloat() / 2.0F, circlePaint
        )

        canvas?.drawCircle(
            width.toFloat() / 2.0F, height.toFloat() / 2.0F,
            (min(width, height).toFloat() - borderWidth) / 2.0F, avaPaint
        )
    }

    @Dimension
    fun getBorderWidth() = borderWidth

    fun setBorderWidth(@Dimension dp: Int) {
        if (dp > 0) borderWidth = dp.toFloat()
    }

    fun getBorderColor() = borderColor

    fun setBorderColor(hex: String) {
        borderColor = Color.parseColor(hex)
    }

    fun setBorderColor(@ColorRes colorId: Int) {
        borderColor = colorId
    }

}