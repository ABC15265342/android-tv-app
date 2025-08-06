# 🚀 Android TV 教育应用

专为机顶盒设计的教育视频播放应用程序

## 🎯 功能特点
- 📺 Android TV 优化界面
- 🎬 支持MP4视频播放
- 🎮 遥控器完美支持
- 📚 按学科分类浏览
- 🌐 网络视频流播放

## 🏗️ 在线构建方法

### 方式一：GitHub Actions（推荐）
1. 将项目上传到GitHub
2. 推送代码后自动触发构建
3. 在Actions页面下载生成的APK

### 方式二：手动触发构建
1. 进入GitHub仓库的Actions页面
2. 选择"🚀 构建Android TV APK"工作流
3. 点击"Run workflow"按钮
4. 等待构建完成并下载APK

## 📱 安装说明
1. 下载构建好的APK文件
2. 在机顶盒上启用"未知来源"安装
3. 使用USB或网络传输APK到设备
4. 安装并享受使用

## 🛠️ 技术栈
- **语言**: Kotlin
- **框架**: Android TV Leanback
- **视频**: ExoPlayer 2.19.1
- **网络**: Retrofit2 + OkHttp
- **图片**: Glide

## 📞 支持
如有问题，请查看构建日志或联系开发者。

---
*本应用专为教育用途设计*

## 功能特点

- 🎥 **MP4视频播放**: 支持高质量MP4格式视频播放
- 📺 **Android TV界面**: 专为电视屏幕优化的用户界面
- 🎮 **遥控器操作**: 完全支持机顶盒遥控器控制
- 📚 **分类浏览**: 按学科分类组织课程内容
- 🔍 **搜索功能**: 快速找到需要的课程
- 📱 **响应式设计**: 适配不同尺寸的电视屏幕

## 系统要求

- Android 5.0 (API 21) 或更高版本
- Android TV 设备或机顶盒
- 网络连接（WiFi或有线）
- 至少1GB RAM

## 技术架构

### 核心技术栈
- **Kotlin**: 主要编程语言
- **Android TV Leanback**: TV界面框架
- **ExoPlayer**: 视频播放引擎
- **Retrofit2**: 网络API调用
- **Glide**: 图片加载和缓存
- **MVVM**: 架构模式

### 项目结构
```
app/
├── src/main/
│   ├── java/com/education/tvapp/
│   │   ├── MainActivity.kt           # 主界面 - 视频分类浏览
│   │   ├── VideoDetailsActivity.kt   # 视频详情页面
│   │   ├── VideoPlayerActivity.kt    # 视频播放器
│   │   ├── models/                   # 数据模型
│   │   ├── viewmodels/              # ViewModel层
│   │   ├── presenters/              # TV界面展示器
│   │   └── services/                # 网络服务
│   ├── res/
│   │   ├── layout/                  # 布局文件
│   │   ├── values/                  # 资源文件
│   │   └── drawable/                # 图标和图片
│   └── AndroidManifest.xml
└── build.gradle
```

## 安装和构建

### 前提条件
1. Android Studio (最新版本)
2. Android SDK (API 21+)
3. Java 8 或更高版本

### 构建步骤
1. 克隆项目到本地
2. 在Android Studio中打开项目
3. 等待Gradle同步完成
4. 连接Android TV设备或使用模拟器
5. 点击"Run"构建并安装应用

## 配置说明

### 视频源配置
在`VideoService.kt`中配置您的视频API：

```kotlin
companion object {
    private const val BASE_URL = "https://your-education-website.com/api/"
}
```

### 视频格式要求
- **格式**: MP4
- **编码**: H.264 (推荐)
- **分辨率**: 1080p 或 720p
- **音频**: AAC 编码

## 使用指南

### 遥控器操作
- **方向键**: 导航菜单和选项
- **确认键**: 选择和播放
- **返回键**: 返回上级菜单
- **播放/暂停**: 控制视频播放
- **快进/快退**: 视频进度控制

### 界面导航
1. **主界面**: 显示视频分类列表
2. **分类浏览**: 按学科查看课程
3. **视频详情**: 查看课程信息和描述
4. **视频播放**: 全屏播放视频内容

## API集成

### 数据格式
应用期望的JSON数据格式：

```json
{
  "categories": [
    {
      "id": "math",
      "name": "数学",
      "videos": [
        {
          "id": "1",
          "title": "高等数学 - 微积分基础",
          "description": "学习微积分的基本概念",
          "videoUrl": "https://your-domain.com/videos/math1.mp4",
          "thumbnailUrl": "https://your-domain.com/thumbnails/math1.jpg",
          "duration": "45:30",
          "category": "数学",
          "instructor": "张教授"
        }
      ]
    }
  ]
}
```

### API端点
- `GET /api/categories` - 获取所有分类和视频
- `GET /api/videos?category={id}` - 按分类获取视频
- `GET /api/search?q={query}` - 搜索视频

## 部署指南

### 发布准备
1. 更新版本号（`build.gradle`）
2. 配置签名证书
3. 测试所有核心功能
4. 优化APK大小

### 机顶盒兼容性
- 小米盒子
- 华为机顶盒
- 海信电视
- TCL电视
- 创维电视

## 故障排除

### 常见问题

**视频无法播放**
- 检查网络连接
- 确认视频URL可访问
- 验证视频格式为MP4

**遥控器无响应**
- 确认设备支持Android TV
- 检查应用焦点处理
- 重启设备

**界面显示异常**
- 检查电视分辨率设置
- 确认HDMI连接稳定
- 调整显示缩放比例

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 发起 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情。

## 支持和反馈

如有问题或建议，请：
- 提交 Issue
- 发送邮件至 support@example.com
- 查看文档和FAQ

---

**注意**: 这是一个示例项目，实际部署时请根据您的具体需求进行调整和配置。
# 触发构建 2025年 8月 7日 星期四 01时08分31秒 CST
