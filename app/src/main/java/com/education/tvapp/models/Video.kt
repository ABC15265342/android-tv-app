package com.education.tvapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 视频数据模型 - 适配明诚教育网站
 */
@Parcelize
data class Video(
    val id: String,
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val duration: String = "未知",
    val subject: String, // 学科：数学、语文、英语等
    val grade: Int, // 年级：1-12
    val uploadDate: String = "",
    val instructor: String = "",
    val viewCount: Int = 0,
    val fileSize: String = "",
    val videoType: String = "mp4"
) : Parcelable

/**
 * 课程分类数据模型
 */
data class CourseCategory(
    val id: String,
    val name: String,
    val grade: Int,
    val subject: String,
    val videos: List<Video>
)

/**
 * 年级信息
 */
data class Grade(
    val id: Int,
    val name: String,
    val stage: String, // 小学、初中、高中
    val subjects: List<String>
)

/**
 * API响应模型
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?
)

/**
 * 视频上传响应
 */
data class UploadResponse(
    val fileId: String,
    val fileName: String,
    val fileUrl: String
)
