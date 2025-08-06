package com.education.tvapp

import android.content.Intent
import android.os.Bundle
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.core.content.ContextCompat
import com.education.tvapp.models.Video
import com.education.tvapp.models.CourseCategory
import com.education.tvapp.models.Grade
import com.education.tvapp.presenters.VideoPresenter

/**
 * 主Fragment - 显示课程分类和视频列表
 */
class MainFragment : BrowseSupportFragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        
        setupUI()
        loadData()
    }

    private fun setupUI() {
        // 设置品牌颜色
        brandColor = ContextCompat.getColor(requireContext(), R.color.primary_color)
        
        // 设置标题
        title = getString(R.string.app_name)
        
        // 设置头部图标
        badgeDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_foreground)
        
        // 禁用搜索
        isHeadersTransitionOnBackEnabled = true
        
        // 设置点击监听器
        onItemViewClickedListener = ItemViewClickedListener()
        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private fun loadData() {
        val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        
        // 创建示例数据
        val categories = createSampleData()
        
        for (category in categories) {
            val listRowAdapter = ArrayObjectAdapter(VideoPresenter())
            for (video in category.videos) {
                listRowAdapter.add(video)
            }
            
            val header = HeaderItem(category.id.toLong(), category.name)
            rowsAdapter.add(ListRow(header, listRowAdapter))
        }
        
        adapter = rowsAdapter
    }

    private fun createSampleData(): List<CourseCategory> {
        return listOf(
            CourseCategory(
                id = "math",
                name = "数学课程",
                videos = listOf(
                    Video(
                        id = "1",
                        title = "高等数学 - 微积分基础",
                        description = "学习微积分的基本概念和应用",
                        videoUrl = "https://sample-videos.com/zip/10/mp4/720/mp4-sample-1.mp4",
                        thumbnailUrl = "https://via.placeholder.com/320x180/4CAF50/FFFFFF?text=数学",
                        duration = "45:30",
                        category = "数学",
                        instructor = "张教授"
                    )
                )
            ),
            CourseCategory(
                id = "physics",
                name = "物理课程", 
                videos = listOf(
                    Video(
                        id = "2",
                        title = "大学物理 - 力学基础",
                        description = "物理力学的基本原理和定律",
                        videoUrl = "https://sample-videos.com/zip/10/mp4/720/mp4-sample-2.mp4",
                        thumbnailUrl = "https://via.placeholder.com/320x180/2196F3/FFFFFF?text=物理",
                        duration = "50:15",
                        category = "物理",
                        instructor = "李教授"
                    )
                )
            ),
            CourseCategory(
                id = "chemistry",
                name = "化学课程",
                videos = listOf(
                    Video(
                        id = "3", 
                        title = "有机化学 - 基础理论",
                        description = "有机化学的基本概念和反应机理",
                        videoUrl = "https://sample-videos.com/zip/10/mp4/720/mp4-sample-3.mp4",
                        thumbnailUrl = "https://via.placeholder.com/320x180/FF9800/FFFFFF?text=化学",
                        duration = "42:20",
                        category = "化学", 
                        instructor = "王教授"
                    )
                )
            )
        )
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            if (item is Video) {
                // 跳转到视频详情页面
                val intent = Intent(activity, VideoDetailsActivity::class.java)
                intent.putExtra("video", item)
                startActivity(intent)
            }
        }
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?,
            item: Any?,
            rowViewHolder: RowPresenter.ViewHolder?,
            row: Row?
        ) {
            // 可以在这里处理选中事件
        }
    }
}
