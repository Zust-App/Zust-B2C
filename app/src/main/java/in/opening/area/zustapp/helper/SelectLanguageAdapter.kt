package `in`.opening.area.zustapp.helper

import `in`.opening.area.zustapp.R
import `in`.opening.area.zustapp.helper.model.LanguageDTO
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class SelectLanguageAdapter(
    selectedLanguagePosition: Int, languageList: List<LanguageDTO>?,
    listener: ItemClickListener?,
) : RecyclerView.Adapter<SelectLanguageAdapter.LanguageViewHolder>() {
    private val mListener: ItemClickListener?
    private var mIsSelected = -1
    private val mLanguageList: List<LanguageDTO>?
    // private val animation by lazy { AnimationUtils.loadAnimation(context, R.anim.scale_animation) }

    init {
        if (selectedLanguagePosition > -1) {
            mIsSelected = selectedLanguagePosition
        }
        mLanguageList = languageList
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.langauge_selection_item, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        if (mLanguageList != null) {
            holder.mLanguage.text = mLanguageList[position].languageTitle
            val value = getDrawableForSelectedLang(context = holder.itemView.context, (position == mIsSelected))
            holder.radioBtn.isChecked = position==mIsSelected
            if (value != null) {
                holder.itemView.background = value
            }

            holder.mLanguage.setTextColor(if (position == mIsSelected) ContextCompat.getColor(holder.itemView.context, R.color.app_black) else ContextCompat.getColor(holder.itemView.context, R.color.language_default))
        }
    }

    override fun getItemCount(): Int {
        return mLanguageList?.size ?: 0
    }

    inner class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var mLanguage: TextView
        var radioBtn: RadioButton

        init {
            mLanguage = itemView.findViewById(R.id.tv_language)
            radioBtn = itemView.findViewById(R.id.radioButton)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (!mLanguageList.isNullOrEmpty()) {
                val animator: ObjectAnimator = ObjectAnimator.ofFloat(mLanguage, "translationY", 0.0f, -20.0f, 0.0f)
                animator.duration = 500
                animator.interpolator = BounceInterpolator()
                animator.start()
                val pos = layoutPosition
                if (mListener != null && pos != -1 && pos < mLanguageList.size) {
                    mIsSelected = pos
                    mListener.onItemClick(pos, mLanguageList[pos].languageTitle)
                }
            }
        }
    }
}

private fun getDrawableForSelectedLang(context: Context, isSelected: Boolean): Drawable? {
    return if (isSelected) {
        ContextCompat.getDrawable(context, R.drawable.green_selection_color)
    } else {
        ContextCompat.getDrawable(context, R.drawable.light_rounded_background)
    }
}

interface ItemClickListener {
    fun onItemClick(position: Int, language: String?)
}