package `in`.opening.area.zustapp.utility

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CustomDividerItemDecoration @SuppressLint("UseCompatLoadingForDrawables") constructor(context: Context?, resId: Int) : ItemDecoration() {
    private val mDivider: Drawable?

    init {
        mDivider = ContextCompat.getDrawable(context!!, resId)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount-1) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val bottom = top + mDivider!!.intrinsicHeight
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(canvas)
        }
    }
}