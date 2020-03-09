package ru.skillbranch.devintensive.ui.custom

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
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
    private var isBitmap = false //

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)
            borderColor = a.getInt(R.styleable.CircleImageView_cv_borderColor, DEFAULT_BORDER_COLOR)
            borderWidth =
                a.getDimension(R.styleable.CircleImageView_cv_borderWidth, DEFAULT_BORDER_WIDTH)
            a.recycle()

            circlePaint.color = borderColor

            // Checking for a property "src"
            val srcId = attrs.getAttributeResourceValue(
                "http://schemas.android.com/apk/res/android", "src", -1
            )
            if (srcId != -1) {
                // Checking for available the picture
                val drawable = AppCompatResources.getDrawable(context, srcId)
                if (drawable != null) {
                    avaBitmap = drawable.toBitmap()
                    // Here we don't know the actual size of the view,
                    // so we use a non-scalable image
                    avaPaint.shader =
                        BitmapShader(avaBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
                    isBitmap = true
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Use resized shader if it possible
        if (isBitmap && measuredWidth > 0 && measuredHeight > 0) {
            val scaledBitmap =
                Bitmap.createScaledBitmap(avaBitmap, measuredWidth, measuredHeight, false)
            val newShader = BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            avaPaint.shader = newShader
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (isBitmap) {
            canvas?.drawCircle(
                width.toFloat() / 2.0F, height.toFloat() / 2.0F,
                min(width, height).toFloat() / 2.0F, circlePaint
            )
            canvas?.drawCircle(
                width.toFloat() / 2.0F, height.toFloat() / 2.0F,
                (min(width, height).toFloat() - borderWidth) / 2.0F, avaPaint
            )
        } else {
            super.onDraw(canvas)
        }
    }

    @Dimension
    fun getBorderWidth() = borderWidth.toInt()

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