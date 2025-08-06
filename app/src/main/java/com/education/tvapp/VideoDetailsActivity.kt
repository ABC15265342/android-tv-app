package com.education.tvapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.leanback.app.DetailsSupportFragment
import androidx.leanback.widget.*
import androidx.core.content.ContextCompat
import com.education.tvapp.models.Video

/**
 * 视频详情Activity
 * 显示视频的详细信息，包括标题、描述、时长等
 */
class VideoDetailsActivity : FragmentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_details)
        
        val video = intent.getParcelableExtra<Video>("video")
        
        if (video != null) {
            val fragment = VideoDetailsFragment.newInstance(video)
            supportFragmentManager.beginTransaction()
                .replace(R.id.details_fragment, fragment)
                .commitNow()
        }
    }

    class VideoDetailsFragment : DetailsSupportFragment() {
        
        private lateinit var video: Video
        private lateinit var mDetailsRowBuilderAdapter: SparseArrayObjectAdapter

        companion object {
            fun newInstance(video: Video): VideoDetailsFragment {
                val fragment = VideoDetailsFragment()
                val args = Bundle()
                args.putParcelable("video", video)
                fragment.arguments = args
                return fragment
            }
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            
            video = arguments?.getParcelable("video") ?: return
            
            setupDetailsOverviewRow()
            setupEventListeners()
        }

        private fun setupDetailsOverviewRow() {
            val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
            
            mDetailsRowBuilderAdapter = SparseArrayObjectAdapter()

            // 创建详情行
            val detailsOverview = DetailsOverviewRow(video)
            detailsOverview.imageDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.default_background)

            // 添加动作按钮
            val actionAdapter = SparseArrayObjectAdapter()
            
            // 播放按钮
            actionAdapter.set(
                ACTION_PLAY,
                Action(ACTION_PLAY, getString(R.string.action_play), getString(R.string.action_play))
            )
            
            detailsOverview.actionsAdapter = actionAdapter

            mDetailsRowBuilderAdapter.set(DETAIL_THUMB_POSITION, detailsOverview)
            
            val rowsAdapter = ArrayObjectAdapter(ClassPresenterSelector().apply {
                addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
            })
            
            rowsAdapter.add(detailsOverview)
            adapter = rowsAdapter
        }

        private fun setupEventListeners() {
            onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
                if (item is Action) {
                    when (item.id) {
                        ACTION_PLAY -> {
                            val intent = Intent(activity, VideoPlayerActivity::class.java).apply {
                                putExtra("video_url", video.videoUrl)
                                putExtra("video_title", video.title)
                            }
                            startActivity(intent)
                        }
                    }
                }
            }
        }

        private inner class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {
            override fun onBindDescription(viewHolder: ViewHolder, item: Any) {
                val video = item as Video
                viewHolder.title.text = video.title
                viewHolder.subtitle.text = video.description
                viewHolder.body.text = "时长: ${video.duration} | ${video.grade}年级 ${video.subject} | 讲师: ${video.instructor}"
            }
        }

        companion object {
            private const val ACTION_PLAY = 1L
            private const val DETAIL_THUMB_POSITION = 0
        }
    }
}
