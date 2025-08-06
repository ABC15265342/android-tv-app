package com.education.tvapp.presenters

import android.graphics.Color
import android.view.ViewGroup
import android.widget.TextView
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.education.tvapp.R
import com.education.tvapp.models.Video

/**
 * 视频卡片Presenter - 用于在Android TV界面中显示视频卡片
 */
class VideoPresenter : Presenter() {

    companion object {
        private const val CARD_WIDTH = 313
        private const val CARD_HEIGHT = 176
        private const val SELECTED_CARD_WIDTH = 343
        private const val SELECTED_CARD_HEIGHT = 194
    }

    override fun onCreateViewHolder(parent: ViewGroup): Presenter.ViewHolder {
        val cardView = object : ImageCardView(parent.context) {
            override fun setSelected(selected: Boolean) {
                updateCardBackgroundColor(this, selected)
                super.setSelected(selected)
            }
        }

        cardView.isFocusable = true
        cardView.isFocusableInTouchMode = true
        updateCardBackgroundColor(cardView, false)
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: Presenter.ViewHolder, item: Any) {
        val video = item as Video
        val cardView = viewHolder.view as ImageCardView

        cardView.titleText = video.title
        cardView.contentText = "${video.grade}年级 ${video.subject} | ${video.duration}"
        cardView.setMainImageDimensions(CARD_WIDTH, CARD_HEIGHT)

        // 使用Glide加载缩略图
        if (video.thumbnailUrl.isNotEmpty()) {
            Glide.with(viewHolder.view.context)
                .load(video.thumbnailUrl)
                .centerCrop()
                .error(R.drawable.default_background)
                .into(cardView.mainImageView)
        } else {
            // 根据学科设置不同的默认图片
            val defaultImage = when (video.subject) {
                "数学" -> R.drawable.default_background
                "语文" -> R.drawable.default_background
                "英语" -> R.drawable.default_background
                "物理" -> R.drawable.default_background
                "化学" -> R.drawable.default_background
                else -> R.drawable.default_background
            }
            cardView.mainImage = ContextCompat.getDrawable(cardView.context, defaultImage)
        }
    }

    override fun onUnbindViewHolder(viewHolder: Presenter.ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        
        // 清除图片资源，避免内存泄漏
        cardView.badgeImage = null
        cardView.mainImage = null
    }

    private fun updateCardBackgroundColor(view: ImageCardView, selected: Boolean) {
        val color = if (selected) {
            ContextCompat.getColor(view.context, R.color.selected_background)
        } else {
            ContextCompat.getColor(view.context, R.color.default_background)
        }
        
        view.setBackgroundColor(color)
        view.findViewById<TextView>(R.id.title_text)?.setTextColor(
            if (selected) Color.WHITE else Color.LTGRAY
        )
    }
}
