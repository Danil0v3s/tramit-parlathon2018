package br.com.firstsoft.parlathon.view.components

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

/**
 * Created by danilolemes on 26/03/2018.
 */
class SquareImageView : ImageView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val height = measuredHeight
        setMeasuredDimension(height, height)
    }

}