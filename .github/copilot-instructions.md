<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# Education TV App - Android TV Project

这是一个专为机顶盒设计的教育视频播放应用程序。应用程序使用Android TV框架构建，支持MP4格式的视频播放。

## 项目特点

- **Android TV优化**: 使用Leanback库提供专门的电视界面体验
- **视频播放**: 使用ExoPlayer播放MP4格式的网课视频
- **遥控器支持**: 完全支持机顶盒遥控器操作
- **分类浏览**: 按学科分类组织视频内容
- **网络API**: 预留了与教育网站后端的API接口

## 技术栈

- **语言**: Kotlin
- **UI框架**: Android TV Leanback
- **视频播放**: ExoPlayer 2.19.1
- **网络请求**: Retrofit2 + OkHttp
- **图片加载**: Glide
- **架构**: MVVM + LiveData

## 开发说明

在开发过程中，请遵循以下原则：

1. **Android TV最佳实践**: 确保所有UI组件都支持遥控器导航
2. **视频播放优化**: 优先考虑播放性能和用户体验
3. **网络处理**: 妥善处理网络错误和连接超时
4. **内存管理**: 注意图片和视频资源的内存释放

## API集成

项目预留了VideoService类用于与教育网站后端API集成。在实际部署时，需要：

1. 更新BASE_URL为实际的API地址
2. 实现具体的API调用逻辑
3. 调整视频URL格式以匹配后端返回的数据

## 部署指南

1. 确保视频文件为MP4格式且编码兼容
2. 配置HTTPS访问以支持网络视频播放
3. 测试遥控器操作的响应性
4. 验证不同分辨率下的播放效果
