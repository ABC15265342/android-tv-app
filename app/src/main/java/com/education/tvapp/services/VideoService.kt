package com.education.tvapp.services

import com.education.tvapp.models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 视频服务类 - 连接明诚教育网站API
 */
class VideoService {
    
    companion object {
        private const val BASE_URL = "http://43.138.218.45:3000/"
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(VideoApiInterface::class.java)

    /**
     * 获取指定年级的课程分类
     */
    suspend fun getCoursesByGrade(grade: Int): Result<List<CourseCategory>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getCoursesByGrade(grade)
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    Result.success(data)
                } else {
                    Result.failure(Exception("获取课程失败: ${response.code()}"))
                }
            } catch (e: Exception) {
                // 如果API调用失败，返回模拟数据
                Result.success(getMockDataForGrade(grade))
            }
        }
    }

    /**
     * 根据学科获取视频列表
     */
    suspend fun getVideosBySubject(grade: Int, subject: String): Result<List<Video>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getVideosBySubject(grade, subject)
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    Result.success(data)
                } else {
                    Result.failure(Exception("获取视频失败: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.success(getMockVideosForSubject(grade, subject))
            }
        }
    }

    /**
     * 搜索视频
     */
    suspend fun searchVideos(query: String, grade: Int? = null): Result<List<Video>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchVideos(query, grade)
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    Result.success(data)
                } else {
                    Result.failure(Exception("搜索失败: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.success(searchMockData(query, grade))
            }
        }
    }

    /**
     * 获取所有年级信息
     */
    fun getAllGrades(): List<Grade> {
        return listOf(
            // 小学
            Grade(1, "一年级", "小学", listOf("数学", "语文")),
            Grade(2, "二年级", "小学", listOf("数学", "语文")),
            Grade(3, "三年级", "小学", listOf("数学", "语文", "英语")),
            Grade(4, "四年级", "小学", listOf("数学", "语文", "英语")),
            Grade(5, "五年级", "小学", listOf("数学", "语文", "英语")),
            Grade(6, "六年级", "小学", listOf("数学", "语文", "英语")),
            
            // 初中
            Grade(7, "初一（七年级）", "初中", listOf("数学", "语文", "英语", "生物", "历史", "地理", "政治")),
            Grade(8, "初二（八年级）", "初中", listOf("数学", "语文", "英语", "物理", "生物", "历史", "地理", "政治")),
            Grade(9, "初三（九年级）", "初中", listOf("数学", "语文", "英语", "物理", "化学", "历史", "政治")),
            
            // 高中
            Grade(10, "高一", "高中", listOf("数学", "语文", "英语", "物理", "化学", "生物", "历史", "地理", "政治")),
            Grade(11, "高二", "高中", listOf("数学", "语文", "英语", "物理", "化学", "生物", "历史", "地理", "政治")),
            Grade(12, "高三", "高中", listOf("数学", "语文", "英语", "物理", "化学", "生物", "历史", "地理", "政治"))
        )
    }

    private fun getMockDataForGrade(grade: Int): List<CourseCategory> {
        val gradeInfo = getAllGrades().find { it.id == grade } ?: return emptyList()
        
        return gradeInfo.subjects.map { subject ->
            CourseCategory(
                id = "${grade}_${subject}",
                name = subject,
                grade = grade,
                subject = subject,
                videos = getMockVideosForSubject(grade, subject)
            )
        }
    }

    private fun getMockVideosForSubject(grade: Int, subject: String): List<Video> {
        val baseUrl = "http://43.138.218.45:3000"
        val gradeText = getAllGrades().find { it.id == grade }?.name ?: "${grade}年级"
        
        return when (subject) {
            "数学" -> listOf(
                Video(
                    id = "${grade}_math_1",
                    title = "${gradeText}数学 - 基础概念",
                    description = "学习${gradeText}数学的基础概念和原理",
                    videoUrl = "$baseUrl/videos/math/grade${grade}_math_basic.mp4",
                    thumbnailUrl = "$baseUrl/thumbnails/math_basic.jpg",
                    duration = "45:30",
                    subject = subject,
                    grade = grade,
                    instructor = "数学老师"
                ),
                Video(
                    id = "${grade}_math_2", 
                    title = "${gradeText}数学 - 应用练习",
                    description = "通过实际例题掌握数学应用方法",
                    videoUrl = "$baseUrl/videos/math/grade${grade}_math_practice.mp4",
                    thumbnailUrl = "$baseUrl/thumbnails/math_practice.jpg",
                    duration = "38:15",
                    subject = subject,
                    grade = grade,
                    instructor = "数学老师"
                )
            )
            "语文" -> listOf(
                Video(
                    id = "${grade}_chinese_1",
                    title = "${gradeText}语文 - 阅读理解",
                    description = "提高${gradeText}语文阅读理解能力",
                    videoUrl = "$baseUrl/videos/chinese/grade${grade}_reading.mp4",
                    thumbnailUrl = "$baseUrl/thumbnails/chinese_reading.jpg",
                    duration = "42:20",
                    subject = subject,
                    grade = grade,
                    instructor = "语文老师"
                )
            )
            "英语" -> listOf(
                Video(
                    id = "${grade}_english_1",
                    title = "${gradeText}英语 - 基础语法",
                    description = "掌握${gradeText}英语基础语法知识",
                    videoUrl = "$baseUrl/videos/english/grade${grade}_grammar.mp4",
                    thumbnailUrl = "$baseUrl/thumbnails/english_grammar.jpg",
                    duration = "35:45",
                    subject = subject,
                    grade = grade,
                    instructor = "英语老师"
                )
            )
            else -> listOf(
                Video(
                    id = "${grade}_${subject}_1",
                    title = "${gradeText}${subject} - 基础课程",
                    description = "学习${gradeText}${subject}的基础知识",
                    videoUrl = "$baseUrl/videos/${subject}/grade${grade}_basic.mp4",
                    thumbnailUrl = "$baseUrl/thumbnails/${subject}_basic.jpg",
                    duration = "40:00",
                    subject = subject,
                    grade = grade,
                    instructor = "${subject}老师"
                )
            )
        }
    }

    private fun searchMockData(query: String, grade: Int?): List<Video> {
        val allGrades = if (grade != null) listOf(grade) else (1..12).toList()
        val results = mutableListOf<Video>()
        
        allGrades.forEach { g ->
            val gradeData = getMockDataForGrade(g)
            gradeData.forEach { category ->
                val matchingVideos = category.videos.filter { video ->
                    video.title.contains(query, ignoreCase = true) ||
                    video.description.contains(query, ignoreCase = true) ||
                    video.subject.contains(query, ignoreCase = true)
                }
                results.addAll(matchingVideos)
            }
        }
        
        return results.take(20) // 限制搜索结果数量
    }
}

/**
 * Retrofit API接口定义
 */
interface VideoApiInterface {
    
    @GET("api/courses")
    suspend fun getCoursesByGrade(@Query("grade") grade: Int): Response<List<CourseCategory>>
    
    @GET("api/videos")
    suspend fun getVideosBySubject(
        @Query("grade") grade: Int,
        @Query("subject") subject: String
    ): Response<List<Video>>
    
    @GET("api/search")
    suspend fun searchVideos(
        @Query("q") query: String,
        @Query("grade") grade: Int? = null
    ): Response<List<Video>>
}
