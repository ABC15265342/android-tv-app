package com.education.tvapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.education.tvapp.models.Video
import com.education.tvapp.models.CourseCategory
import com.education.tvapp.models.Grade
import com.education.tvapp.viewmodels.MainViewModel
import com.education.tvapp.presenters.VideoPresenter

/**
 * 主Activity - 显示明诚教育的课程分类和视频列表
 * 适配机顶盒遥控器操作和Android TV界面
 */
class MainActivity : FragmentActivity() {
    
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        
        val fragment = MainFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_browse_fragment, fragment)
            .commitNow()
    }

    class MainFragment : BrowseSupportFragment() {
        
        private lateinit var viewModel: MainViewModel
        private lateinit var mRowsAdapter: ArrayObjectAdapter
        private lateinit var gradeHeaderAdapter: ArrayObjectAdapter

        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            
            viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
            
            setupUI()
            setupEventListeners()
            observeData()
        }

        private fun setupUI() {
            // 设置标题
            title = getString(R.string.app_name)
            headersState = HEADERS_ENABLED
            isHeadersTransitionOnBackEnabled = true

            // 设置品牌颜色
            brandColor = ContextCompat.getColor(requireContext(), R.color.primary)
            searchAffordanceColor = ContextCompat.getColor(requireContext(), R.color.accent)

            // 初始化适配器
            mRowsAdapter = ArrayObjectAdapter(ListRowPresenter())
            adapter = mRowsAdapter
        }

        private fun setupEventListeners() {
            // 视频点击监听器
            onItemViewClickedListener = ItemViewClickedListener()
            
            // 搜索监听器
            setOnSearchClickedListener {
                // TODO: 实现搜索界面
                android.widget.Toast.makeText(context, "搜索功能开发中", android.widget.Toast.LENGTH_SHORT).show()
            }

            // 行选择监听器 - 用于年级切换
            onItemViewSelectedListener = OnItemViewSelectedListener { itemViewHolder, item, rowViewHolder, row ->
                if (item is Grade) {
                    viewModel.setSelectedGrade(item.id)
                }
            }
        }

        private fun observeData() {
            // 观察课程分类数据
            viewModel.getCourseCategories().observe(viewLifecycleOwner) { categories ->
                setupCourseRows(categories)
            }

            // 观察选中年级
            viewModel.getSelectedGrade().observe(viewLifecycleOwner) { grade ->
                // 可以在这里更新UI显示当前选中年级
                title = "${getString(R.string.app_name)} - ${grade.name}"
            }

            // 观察加载状态
            viewModel.getLoading().observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    // 显示加载指示器
                    progressBarManager.show()
                } else {
                    progressBarManager.hide()
                }
            }

            // 观察错误信息
            viewModel.getError().observe(viewLifecycleOwner) { error ->
                if (error.isNotEmpty()) {
                    android.widget.Toast.makeText(context, error, android.widget.Toast.LENGTH_LONG).show()
                }
            }
        }

        private fun setupCourseRows(categories: List<CourseCategory>) {
            mRowsAdapter.clear()

            // 添加年级选择行
            addGradeSelectionRow()

            // 添加课程分类行
            categories.forEach { category ->
                if (category.videos.isNotEmpty()) {
                    val listRowAdapter = ArrayObjectAdapter(VideoPresenter())
                    
                    category.videos.forEach { video ->
                        listRowAdapter.add(video)
                    }

                    val header = HeaderItem(category.id.hashCode().toLong(), category.name)
                    mRowsAdapter.add(ListRow(header, listRowAdapter))
                }
            }

            // 如果没有课程数据，显示提示
            if (categories.isEmpty() || categories.all { it.videos.isEmpty() }) {
                addEmptyStateRow()
            }
        }

        private fun addGradeSelectionRow() {
            val gradeAdapter = ArrayObjectAdapter(GradePresenter())
            val allGrades = viewModel.getAllGrades()
            
            allGrades.forEach { grade ->
                gradeAdapter.add(grade)
            }

            val gradeHeader = HeaderItem(0, "选择年级")
            mRowsAdapter.add(ListRow(gradeHeader, gradeAdapter))
        }

        private fun addEmptyStateRow() {
            val emptyAdapter = ArrayObjectAdapter(EmptyStatePresenter())
            emptyAdapter.add("暂无课程数据，请检查网络连接或稍后重试")
            
            val emptyHeader = HeaderItem(-1, "提示")
            mRowsAdapter.add(ListRow(emptyHeader, emptyAdapter))
        }

        private inner class ItemViewClickedListener : OnItemViewClickedListener {
            override fun onItemClicked(
                itemViewHolder: Presenter.ViewHolder?,
                item: Any?,
                rowViewHolder: RowPresenter.ViewHolder?,
                row: Row?
            ) {
                when (item) {
                    is Video -> {
                        // 播放视频
                        val intent = Intent(activity, VideoDetailsActivity::class.java).apply {
                            putExtra("video", item)
                        }
                        startActivity(intent)
                    }
                    is Grade -> {
                        // 切换年级
                        viewModel.setSelectedGrade(item.id)
                    }
                    is String -> {
                        // 处理空状态点击 - 重新加载
                        viewModel.refreshCurrentGrade()
                    }
                }
            }
        }

        // 年级选择Presenter
        private inner class GradePresenter : Presenter() {
            override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
                val cardView = object : androidx.leanback.widget.ImageCardView(parent.context) {
                    override fun setSelected(selected: Boolean) {
                        updateCardBackgroundColor(this, selected)
                        super.setSelected(selected)
                    }
                }
                cardView.isFocusable = true
                cardView.isFocusableInTouchMode = true
                cardView.setMainImageDimensions(200, 120)
                return ViewHolder(cardView)
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
                val grade = item as Grade
                val cardView = viewHolder.view as androidx.leanback.widget.ImageCardView
                
                cardView.titleText = grade.name
                cardView.contentText = grade.stage
                cardView.setMainImageDimensions(200, 120)
                
                // 设置年级图标
                val drawable = when (grade.stage) {
                    "小学" -> ContextCompat.getDrawable(cardView.context, R.drawable.default_background)
                    "初中" -> ContextCompat.getDrawable(cardView.context, R.drawable.default_background)
                    "高中" -> ContextCompat.getDrawable(cardView.context, R.drawable.default_background)
                    else -> ContextCompat.getDrawable(cardView.context, R.drawable.default_background)
                }
                cardView.mainImage = drawable
            }

            override fun onUnbindViewHolder(viewHolder: ViewHolder) {
                val cardView = viewHolder.view as androidx.leanback.widget.ImageCardView
                cardView.badgeImage = null
                cardView.mainImage = null
            }

            private fun updateCardBackgroundColor(view: androidx.leanback.widget.ImageCardView, selected: Boolean) {
                val color = if (selected) {
                    ContextCompat.getColor(view.context, R.color.selected_background)
                } else {
                    ContextCompat.getColor(view.context, R.color.default_background)
                }
                view.setBackgroundColor(color)
            }
        }

        // 空状态Presenter
        private inner class EmptyStatePresenter : Presenter() {
            override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
                val textView = android.widget.TextView(parent.context).apply {
                    setTextColor(ContextCompat.getColor(context, R.color.light_gray))
                    textSize = 18f
                    setPadding(48, 48, 48, 48)
                    gravity = android.view.Gravity.CENTER
                }
                return ViewHolder(textView)
            }

            override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
                val textView = viewHolder.view as android.widget.TextView
                textView.text = item as String
            }

            override fun onUnbindViewHolder(viewHolder: ViewHolder) {
                // Nothing to unbind
            }
        }
    }
}
