package com.education.tvapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.Log
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

/**
 * 视频播放Activity - 明诚教育专用
 * 使用ExoPlayer播放MP4视频，支持机顶盒遥控器操作
 * 适配明诚教育网站的视频格式和网络环境
 */
class VideoPlayerActivity : AppCompatActivity() {
    
    private lateinit var player: ExoPlayer
    private lateinit var playerView: StyledPlayerView
    private var videoUrl: String? = null
    private var videoTitle: String? = null
    private var playbackPosition = 0L
    private var currentWindow = 0
    private var playWhenReady = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        
        videoUrl = intent.getStringExtra("video_url")
        videoTitle = intent.getStringExtra("video_title")
        
        if (videoUrl.isNullOrEmpty()) {
            Toast.makeText(this, "视频地址错误", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        
        // 检查网络连接
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "请检查网络连接", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        
        playerView = findViewById(R.id.player_view)
        
        // 恢复播放状态
        savedInstanceState?.let {
            playbackPosition = it.getLong("playback_position")
            currentWindow = it.getInt("current_window")
            playWhenReady = it.getBoolean("play_when_ready")
        }
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onResume() {
        super.onResume()
        if (!::player.isInitialized) {
            initializePlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::player.isInitialized) {
            updatePlaybackState()
            player.pause()
        }
    }

    override fun onStop() {
        super.onStop()
        if (::player.isInitialized) {
            updatePlaybackState()
            player.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun initializePlayer() {
        if (::player.isInitialized) return
        
        // 创建ExoPlayer实例
        player = ExoPlayer.Builder(this)
            .build()
            .also { exoPlayer ->
                playerView.player = exoPlayer
                
                // 创建自定义数据源工厂，设置适当的超时时间
                val httpDataSourceFactory = DefaultHttpDataSource.Factory()
                    .setUserAgent("MingChengEducationTV/1.0")
                    .setConnectTimeoutMs(30000)
                    .setReadTimeoutMs(30000)
                    .setAllowCrossProtocolRedirects(true)

                val dataSourceFactory = DefaultDataSourceFactory(this, httpDataSourceFactory)
                
                // 创建媒体源
                val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(videoUrl!!))
                
                // 设置媒体源
                exoPlayer.setMediaSource(mediaSource)
                exoPlayer.playWhenReady = playWhenReady
                exoPlayer.seekTo(currentWindow, playbackPosition)
                exoPlayer.prepare()
            }
        
        // 设置播放监听器
        player.addListener(object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                super.onPlayerError(error)
                Log.e("VideoPlayer", "播放错误: ${error.message}")
                handlePlayerError(error)
            }
            
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        Log.d("VideoPlayer", "缓冲中...")
                    }
                    Player.STATE_READY -> {
                        Log.d("VideoPlayer", "准备就绪")
                        hideSystemUI()
                    }
                    Player.STATE_ENDED -> {
                        Log.d("VideoPlayer", "播放结束")
                        Toast.makeText(this@VideoPlayerActivity, "视频播放完成", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    Player.STATE_IDLE -> {
                        Log.d("VideoPlayer", "播放器空闲")
                    }
                }
            }

            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (isPlaying) {
                    hideSystemUI()
                }
            }
        })

        // 设置控制器显示时间
        playerView.controllerShowTimeoutMs = 3000
        playerView.useController = true
    }

    private fun handlePlayerError(error: PlaybackException) {
        val errorMessage = when (error.errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> "网络连接失败，请检查网络设置"
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> "网络连接超时，请稍后重试"
            PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND -> "视频文件不存在"
            PlaybackException.ERROR_CODE_PARSING_CONTAINER_MALFORMED -> "视频格式不支持"
            PlaybackException.ERROR_CODE_DECODER_INIT_FAILED -> "视频解码失败"
            else -> "视频播放失败: ${error.message}"
        }
        
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
        
        // 延迟关闭Activity，给用户时间阅读错误信息
        playerView.postDelayed({
            finish()
        }, 3000)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 处理遥控器按键
        when (keyCode) {
            KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 
            KeyEvent.KEYCODE_SPACE,
            KeyEvent.KEYCODE_DPAD_CENTER -> {
                togglePlayPause()
                return true
            }
            KeyEvent.KEYCODE_MEDIA_PLAY -> {
                if (::player.isInitialized) {
                    player.play()
                }
                return true
            }
            KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                if (::player.isInitialized) {
                    player.pause()
                }
                return true
            }
            KeyEvent.KEYCODE_MEDIA_FAST_FORWARD, 
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                seekForward()
                return true
            }
            KeyEvent.KEYCODE_MEDIA_REWIND, 
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                seekBackward()
                return true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                seekForward(60000) // 快进1分钟
                return true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                seekBackward(60000) // 快退1分钟
                return true
            }
            KeyEvent.KEYCODE_BACK, 
            KeyEvent.KEYCODE_ESCAPE -> {
                finish()
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun togglePlayPause() {
        if (::player.isInitialized) {
            if (player.isPlaying) {
                player.pause()
                Toast.makeText(this, "暂停", Toast.LENGTH_SHORT).show()
            } else {
                player.play()
                Toast.makeText(this, "播放", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun seekForward(seekTime: Long = 10000) {
        if (::player.isInitialized) {
            val currentPosition = player.currentPosition
            val duration = player.duration
            val seekToPosition = (currentPosition + seekTime).coerceAtMost(duration)
            player.seekTo(seekToPosition)
            
            val seekSeconds = seekTime / 1000
            Toast.makeText(this, "快进 ${seekSeconds}秒", Toast.LENGTH_SHORT).show()
        }
    }

    private fun seekBackward(seekTime: Long = 10000) {
        if (::player.isInitialized) {
            val currentPosition = player.currentPosition
            val seekToPosition = (currentPosition - seekTime).coerceAtLeast(0)
            player.seekTo(seekToPosition)
            
            val seekSeconds = seekTime / 1000
            Toast.makeText(this, "快退 ${seekSeconds}秒", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updatePlaybackState() {
        if (::player.isInitialized) {
            playbackPosition = player.currentPosition
            currentWindow = player.currentMediaItemIndex
            playWhenReady = player.playWhenReady
        }
    }

    private fun releasePlayer() {
        if (::player.isInitialized) {
            updatePlaybackState()
            player.release()
        }
    }

    private fun hideSystemUI() {
        playerView.systemUiVisibility = (
            android.view.View.SYSTEM_UI_FLAG_LOW_PROFILE
            or android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        )
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        updatePlaybackState()
        outState.putLong("playback_position", playbackPosition)
        outState.putInt("current_window", currentWindow)
        outState.putBoolean("play_when_ready", playWhenReady)
    }
}
