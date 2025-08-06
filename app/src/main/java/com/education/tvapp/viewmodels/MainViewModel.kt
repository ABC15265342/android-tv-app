package com.education.tvapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.tvapp.models.*
import com.education.tvapp.services.VideoService
import kotlinx.coroutines.launch

/**
 * 主Activity的ViewModel
 * 管理明诚教育网站的课程数据加载和状态
 */
class MainViewModel : ViewModel() {
    
    private val videoService = VideoService()
    private val _courseCategories = MutableLiveData<List<CourseCategory>>()
    private val _selectedGrade = MutableLiveData<Grade>()
    private val _loading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<String>()

    fun getCourseCategories(): LiveData<List<CourseCategory>> = _courseCategories
    fun getSelectedGrade(): LiveData<Grade> = _selectedGrade
    fun getLoading(): LiveData<Boolean> = _loading
    fun getError(): LiveData<String> = _error

    init {
        // 默认选择初一年级
        setSelectedGrade(7)
    }

    fun setSelectedGrade(gradeId: Int) {
        val grade = videoService.getAllGrades().find { it.id == gradeId }
        if (grade != null) {
            _selectedGrade.value = grade
            loadCoursesForGrade(gradeId)
        }
    }

    private fun loadCoursesForGrade(grade: Int) {
        _loading.value = true
        _error.value = ""
        
        viewModelScope.launch {
            try {
                val result = videoService.getCoursesByGrade(grade)
                result.fold(
                    onSuccess = { categories ->
                        _courseCategories.value = categories
                        _loading.value = false
                    },
                    onFailure = { exception ->
                        _error.value = "加载课程失败: ${exception.message}"
                        _loading.value = false
                    }
                )
            } catch (e: Exception) {
                _error.value = "网络连接失败: ${e.message}"
                _loading.value = false
            }
        }
    }

    fun searchVideos(query: String) {
        if (query.isBlank()) {
            // 如果搜索为空，重新加载当前年级的课程
            _selectedGrade.value?.let { grade ->
                loadCoursesForGrade(grade.id)
            }
            return
        }

        _loading.value = true
        _error.value = ""
        
        viewModelScope.launch {
            try {
                val currentGrade = _selectedGrade.value?.id
                val result = videoService.searchVideos(query, currentGrade)
                result.fold(
                    onSuccess = { videos ->
                        // 将搜索结果转换为分类格式
                        val searchCategory = CourseCategory(
                            id = "search_results",
                            name = "搜索结果: \"$query\"",
                            grade = currentGrade ?: 0,
                            subject = "搜索",
                            videos = videos
                        )
                        _courseCategories.value = if (videos.isNotEmpty()) {
                            listOf(searchCategory)
                        } else {
                            emptyList()
                        }
                        _loading.value = false
                    },
                    onFailure = { exception ->
                        _error.value = "搜索失败: ${exception.message}"
                        _loading.value = false
                    }
                )
            } catch (e: Exception) {
                _error.value = "搜索失败: ${e.message}"
                _loading.value = false
            }
        }
    }

    fun getVideosBySubject(subject: String) {
        val grade = _selectedGrade.value?.id ?: return
        
        _loading.value = true
        _error.value = ""
        
        viewModelScope.launch {
            try {
                val result = videoService.getVideosBySubject(grade, subject)
                result.fold(
                    onSuccess = { videos ->
                        val subjectCategory = CourseCategory(
                            id = "${grade}_${subject}",
                            name = subject,
                            grade = grade,
                            subject = subject,
                            videos = videos
                        )
                        _courseCategories.value = listOf(subjectCategory)
                        _loading.value = false
                    },
                    onFailure = { exception ->
                        _error.value = "加载${subject}课程失败: ${exception.message}"
                        _loading.value = false
                    }
                )
            } catch (e: Exception) {
                _error.value = "网络连接失败: ${e.message}"
                _loading.value = false
            }
        }
    }

    fun getAllGrades(): List<Grade> {
        return videoService.getAllGrades()
    }

    fun refreshCurrentGrade() {
        _selectedGrade.value?.let { grade ->
            loadCoursesForGrade(grade.id)
        }
    }
}
