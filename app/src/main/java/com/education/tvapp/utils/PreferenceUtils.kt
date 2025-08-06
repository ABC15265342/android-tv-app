package com.education.tvapp.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * 偏好设置工具类
 * 用于保存用户设置和播放进度
 */
object PreferenceUtils {
    
    private const val PREF_NAME = "mingcheng_education_tv"
    private const val KEY_SELECTED_GRADE = "selected_grade"
    private const val KEY_LAST_PLAYED_VIDEO = "last_played_video"
    private const val KEY_PLAYBACK_POSITION = "playback_position_"
    private const val KEY_FIRST_LAUNCH = "first_launch"
    
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    /**
     * 保存选中的年级
     */
    fun saveSelectedGrade(context: Context, grade: Int) {
        getPreferences(context).edit()
            .putInt(KEY_SELECTED_GRADE, grade)
            .apply()
    }
    
    /**
     * 获取选中的年级
     */
    fun getSelectedGrade(context: Context, defaultGrade: Int = 7): Int {
        return getPreferences(context).getInt(KEY_SELECTED_GRADE, defaultGrade)
    }
    
    /**
     * 保存最后播放的视频ID
     */
    fun saveLastPlayedVideo(context: Context, videoId: String) {
        getPreferences(context).edit()
            .putString(KEY_LAST_PLAYED_VIDEO, videoId)
            .apply()
    }
    
    /**
     * 获取最后播放的视频ID
     */
    fun getLastPlayedVideo(context: Context): String? {
        return getPreferences(context).getString(KEY_LAST_PLAYED_VIDEO, null)
    }
    
    /**
     * 保存视频播放进度
     */
    fun savePlaybackPosition(context: Context, videoId: String, position: Long) {
        getPreferences(context).edit()
            .putLong(KEY_PLAYBACK_POSITION + videoId, position)
            .apply()
    }
    
    /**
     * 获取视频播放进度
     */
    fun getPlaybackPosition(context: Context, videoId: String): Long {
        return getPreferences(context).getLong(KEY_PLAYBACK_POSITION + videoId, 0L)
    }
    
    /**
     * 清除视频播放进度
     */
    fun clearPlaybackPosition(context: Context, videoId: String) {
        getPreferences(context).edit()
            .remove(KEY_PLAYBACK_POSITION + videoId)
            .apply()
    }
    
    /**
     * 检查是否为首次启动
     */
    fun isFirstLaunch(context: Context): Boolean {
        val isFirst = getPreferences(context).getBoolean(KEY_FIRST_LAUNCH, true)
        if (isFirst) {
            getPreferences(context).edit()
                .putBoolean(KEY_FIRST_LAUNCH, false)
                .apply()
        }
        return isFirst
    }
    
    /**
     * 清除所有设置
     */
    fun clearAllPreferences(context: Context) {
        getPreferences(context).edit().clear().apply()
    }
}
