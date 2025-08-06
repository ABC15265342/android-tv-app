package com.education.tvapp.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import java.util.regex.Pattern

/**
 * 网络和URL工具类
 * 专门处理明诚教育网站的视频URL验证和网络状态检测
 */
object NetworkUtils {
    
    private const val MINGCHENG_BASE_URL = "http://43.138.218.45:3000"
    
    /**
     * 检查网络连接状态
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
    
    /**
     * 验证视频URL是否有效
     */
    fun isValidVideoUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        
        return try {
            val uri = Uri.parse(url)
            // 检查是否为有效的URI
            uri.scheme != null && uri.host != null &&
            // 检查是否为视频文件
            (url.endsWith(".mp4", ignoreCase = true) ||
             url.endsWith(".mkv", ignoreCase = true) ||
             url.endsWith(".avi", ignoreCase = true) ||
             url.endsWith(".mov", ignoreCase = true) ||
             url.contains("video", ignoreCase = true))
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * 构建明诚教育视频URL
     */
    fun buildMingChengVideoUrl(relativePath: String): String {
        return if (relativePath.startsWith("http")) {
            relativePath
        } else {
            "$MINGCHENG_BASE_URL/videos/$relativePath"
        }
    }
    
    /**
     * 构建明诚教育缩略图URL
     */
    fun buildMingChengThumbnailUrl(relativePath: String): String {
        return if (relativePath.startsWith("http")) {
            relativePath
        } else {
            "$MINGCHENG_BASE_URL/thumbnails/$relativePath"
        }
    }
    
    /**
     * 获取网络连接类型
     */
    fun getNetworkType(context: Context): String {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return "无网络"
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return "无网络"
        
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "WiFi"
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "移动网络"
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "以太网"
            else -> "未知网络"
        }
    }
}
