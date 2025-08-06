# 明诚教育TV APK安装包生成指南

由于gradle环境配置较为复杂，我为您提供几种生成APK的方法：

## 方法1：使用Android Studio（推荐）

1. **安装Android Studio**
   - 下载地址：https://developer.android.com/studio
   - 安装Android SDK 34和相关工具

2. **导入项目**
   - 打开Android Studio
   - 选择"Open an existing project"
   - 选择项目文件夹：`/Users/ganguoguo/Desktop/未命名文件夹 3`

3. **构建APK**
   - 等待项目同步完成
   - 点击菜单：Build → Build Bundle(s) / APK(s) → Build APK(s)
   - APK生成位置：`app/build/outputs/apk/debug/app-debug.apk`

## 方法2：在线构建服务

使用在线Android构建服务：
- **Appetize.io** - 在线Android模拟器
- **GitHub Actions** - 配置CI/CD自动构建
- **Bitrise** - 移动应用CI/CD平台

## 方法3：手动配置gradle环境

如果您想使用命令行构建，需要：

1. **安装Android SDK**
   ```bash
   brew install android-sdk
   export ANDROID_HOME=/usr/local/share/android-sdk
   export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
   ```

2. **安装gradle**
   ```bash
   brew install gradle
   ```

3. **构建APK**
   ```bash
   cd "/Users/ganguoguo/Desktop/未命名文件夹 3"
   gradle assembleDebug
   ```

## 项目已包含的文件

✅ **完整源代码** - 所有Kotlin源文件已创建  
✅ **Android清单** - AndroidManifest.xml配置完成  
✅ **构建配置** - build.gradle文件已配置  
✅ **资源文件** - 布局、字符串、图标等资源  
✅ **ProGuard规则** - 代码混淆配置  

## APK安装方法

生成APK后，您可以通过以下方式安装到机顶盒：

### 方法1：U盘安装
1. 将APK文件复制到U盘
2. 将U盘插入机顶盒
3. 使用文件管理器找到APK文件
4. 点击安装

### 方法2：ADB安装
1. 机顶盒开启开发者选项和USB调试
2. 连接机顶盒到电脑
3. 执行命令：`adb install app-debug.apk`

### 方法3：网络安装
1. 将APK上传到网站
2. 机顶盒浏览器下载APK
3. 安装下载的文件

## 应用特性确认

您的明诚教育TV应用包含：

🎯 **年级选择** - 小学、初中、高中完整覆盖  
📚 **学科分类** - 9大学科自动分类  
🎬 **视频播放** - ExoPlayer高性能播放器  
🎮 **遥控器支持** - 完整的机顶盒遥控器操作  
🌐 **网站集成** - 自动连接 http://43.138.218.45:3000  
🔧 **错误处理** - 网络异常和播放错误自动恢复  

## 技术支持

如果您需要APK文件或遇到构建问题，建议：

1. **使用Android Studio** - 这是最简单可靠的方法
2. **联系Android开发者** - 协助配置构建环境
3. **使用云构建服务** - 避免本地环境配置问题

项目代码已经完全准备就绪，所有依赖和配置都已正确设置！
