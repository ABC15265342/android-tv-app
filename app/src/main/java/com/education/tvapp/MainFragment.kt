package com.education.tvapp

import android.content.Intent
import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.core.content.ContextCompat
import com.education.tvapp.models.Video
import com.education.tvapp.presenters.VideoPresenter

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
        
        // 创建测试数据
        val videos = createTestVideos()
        val listRowAdapter = ArrayObjectAdapter(VideoPresenter())
        
        videos.forEach { video ->
            listRowAdapter.add(video)
        }
        
        val header = HeaderItem(1, "教育视频")
        rowsAdapter.add(ListRow(header, listRowAdapter))
        adapter = rowsAdapter
    }

    private fun createTestVideos(): List<Video> {
        // 使用VideoService获取默认视频数据
        return com.education.tvapp.services.VideoService.getDefaultVideos()
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
