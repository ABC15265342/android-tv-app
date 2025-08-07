package com.education.tvapp

import android.content.Intent
import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.core.content.ContextCompat
import com.education.tvapp.models.Video

class MainFragment : BrowseSupportFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUI()
        loadData()
    }

    private fun setupUI() {
        title = getString(R.string.app_name)
        brandColor = ContextCompat.getColor(requireContext(), R.color.primary_color)
        onItemViewClickedListener = ItemViewClickedListener()
    }

    private fun loadData() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        
        // 创建简单的测试数据
        val videos = createTestVideos()
        val listRowAdapter = ArrayObjectAdapter(createVideoPresenter())
        
        videos.forEach { video ->
            listRowAdapter.add(video)
        }
        
        val header = HeaderItem(1, "测试视频")
        rowsAdapter.add(ListRow(header, listRowAdapter))
        adapter = rowsAdapter
    }

    private fun createTestVideos(): List<Video> {
        return listOf(
            Video(
                id = "1",
                title = "测试视频1",
                description = "这是一个测试视频",
                videoUrl = "https://sample-videos.com/zip/10/mp4/720/mp4-sample-1.mp4",
                thumbnailUrl = "",
                duration = "10:00",
                subject = "数学",
                grade = 9,
                instructor = "测试老师"
            )
        )
    }

    private fun createVideoPresenter(): Presenter {
        return object : Presenter() {
            override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
                val cardView = androidx.leanback.widget.ImageCardView(parent.context)
                cardView.isFocusable = true
                cardView.isFocusableInTouchMode = true
                cardView.setMainImageDimensions(313, 176)
                return ViewHolder(cardView)
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
                val video = item as Video
                val cardView = viewHolder.view as androidx.leanback.widget.ImageCardView
                
                cardView.titleText = video.title
                cardView.contentText = video.description
                cardView.mainImage = ContextCompat.getDrawable(cardView.context, R.drawable.default_background)
            }

            override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
        }
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            if (item is Video) {
                val intent = Intent(activity, VideoDetailsActivity::class.java)
                intent.putExtra("video", item)
                startActivity(intent)
            }
        }
    }
}
