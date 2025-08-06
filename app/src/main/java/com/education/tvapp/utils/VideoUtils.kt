package com.education.tvapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import java.net.URL
import java.util.regex.Pattern

/**
 * 视频和网络相关的工具类
 */
object VideoUtils {

    /**
     * 验证视频URL是否有效
     */
    fun isValidVideoUrl(url: String): Boolean {
        if (url.isBlank()) return false
        
        return try {
            val urlObject = URL(url)
            val protocol = urlObject.protocol
            val extension = url.substringAfterLast('.', "").lowercase()
            
            // 检查协议
            val validProtocols = listOf("http", "https", "rtmp", "rtsp")
            if (protocol !in validProtocols) return false
            
            // 检查文件扩展名
            val validExtensions = listOf("mp4", "m3u8", "ts", "mov", "avi", "mkv")
            if (extension.isNotEmpty() && extension !in validExtensions) return false
            
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * 检查网络连接状态
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected == true
        }
    }

    /**
     * 格式化视频时长
     */
    fun formatDuration(durationMs: Long): String {
        val totalSeconds = durationMs / 1000
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60

        return if (hours > 0) {
            String.format("%d:%02d:%02d", hours, minutes, seconds)
        } else {
            String.format("%d:%02d", minutes, seconds)
        }
    }

    /**
     * 将时长字符串转换为毫秒
     */
    fun parseDurationToMs(duration: String): Long {
        return try {
            val parts = duration.split(":")
            when (parts.size) {
                2 -> {
                    // MM:SS 格式
                    val minutes = parts[0].toLong()
                    val seconds = parts[1].toLong()
                    (minutes * 60 + seconds) * 1000
                }
                3 -> {
                    // HH:MM:SS 格式
                    val hours = parts[0].toLong()
                    val minutes = parts[1].toLong()
                    val seconds = parts[2].toLong()
                    (hours * 3600 + minutes * 60 + seconds) * 1000
                }
                else -> 0L
            }
        } catch (e: Exception) {
            0L
        }
    }

    /**
     * 检查视频文件是否为MP4格式
     */
    fun isMp4Video(url: String): Boolean {
        val pattern = Pattern.compile(".*\\.(mp4|MP4)$")
        return pattern.matcher(url).matches()
    }

    /**
     * 获取视频缩略图URL（如果有规律的话）
     */
    fun getThumbnailUrl(videoUrl: String): String {
        // 这里可以根据您的视频服务器规律来生成缩略图URL
        // 例如：将.mp4替换为.jpg
        return if (videoUrl.endsWith(".mp4", ignoreCase = true)) {
            videoUrl.substringBeforeLast(".") + ".jpg"
        } else {
            ""
        }
    }

    /**
     * 生成视频ID（从URL中提取）
     */
    fun extractVideoId(url: String): String {
        return try {
            val urlObject = URL(url)
            val path = urlObject.path
            val fileName = path.substringAfterLast('/')
            fileName.substringBeforeLast('.')
        } catch (e: Exception) {
            url.hashCode().toString()
        }
    }
}

/**
 * 焦点处理工具类 - 优化Android TV的焦点导航
 */
object FocusUtils {
    
    /**
     * 为View添加焦点变化的缩放动画
     */
    fun addFocusScaleAnimation(view: android.view.View, scale: Float = 1.1f) {
        view.setOnFocusChangeListener { _, hasFocus ->
            val targetScale = if (hasFocus) scale else 1.0f
            view.animate()
                .scaleX(targetScale)
                .scaleY(targetScale)
                .setDuration(200)
                .start()
        }
    }
}

/**
 * 缓存管理工具类
 */
object CacheUtils {
    
    /**
     * 清理应用缓存
     */
    fun clearCache(context: Context) {
        try {
            val cacheDir = context.cacheDir
            deleteDir(cacheDir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun deleteDir(dir: java.io.File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            if (children != null) {
                for (child in children) {
                    val success = deleteDir(java.io.File(dir, child))
                    if (!success) {
                        return false
                    }
                }
            }
        }
        return dir?.delete() ?: false
    }
    
    /**
     * 获取缓存大小
     */
    fun getCacheSize(context: Context): Long {
        return getFolderSize(context.cacheDir)
    }

    private fun getFolderSize(file: java.io.File): Long {
        var size = 0L
        if (file.isDirectory) {
            file.listFiles()?.forEach { child ->
                size += getFolderSize(child)
            }
        } else {
            size = file.length()
        }
        return size
    }
}
