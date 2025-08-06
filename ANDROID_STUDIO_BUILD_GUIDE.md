# Android Studio APK构建详细步骤指南

## 准备工作
**项目路径**: `/Users/ganguoguo/Desktop/未命名文件夹 3`

---

## 第一步：启动Android Studio并打开项目

### 1.1 启动Android Studio
- 在Mac上，从启动台或应用程序文件夹启动Android Studio
- 如果是首次使用，可能需要等待初始化完成

### 1.2 打开项目
1. **选择打开方式（三选一）**：
   - **方法A**: 点击 "Open an Existing Project" 
   - **方法B**: File → Open
   - **方法C**: 如果AS已打开，File → Open

2. **导航到项目文件夹**：
   ```
   /Users/ganguoguo/Desktop/未命名文件夹 3
   ```

3. **确认选择**：
   - 选中整个项目文件夹（包含build.gradle的根目录）
   - 点击"Open"按钮

### 1.3 项目导入等待
- Android Studio会自动识别这是一个Android项目
- 等待Gradle同步完成（可能需要5-15分钟）
- 底部状态栏会显示"Gradle Sync"进度

---

## 第二步：等待项目同步完成

### 2.1 观察同步状态
- **进度指示器**: 底部状态栏显示同步进度
- **可能的提示**: 
  - "Gradle project sync in progress..."
  - "Downloading dependencies..."
  - "Building project..."

### 2.2 解决可能的问题
如果遇到错误提示：

**🔧 常见问题解决**：
- **SDK版本问题**: Tools → SDK Manager 检查Android SDK
- **Gradle版本问题**: File → Project Structure 检查Gradle版本
- **网络问题**: 确保网络连接正常，可能需要代理设置

### 2.3 同步完成标志
- ✅ 底部状态栏显示"Gradle sync finished"
- ✅ 项目结构在左侧Project面板完整显示
- ✅ 没有红色错误提示

---

## 第三步：构建APK

### 3.1 打开Build菜单
1. 点击顶部菜单栏的 **"Build"**
2. 在下拉菜单中选择 **"Generate Signed Bundle / APK..."**

### 3.2 选择构建类型
1. **弹出对话框**会显示两个选项：
   - ○ Android App Bundle (.aab)
   - ● **APK** ← **选择这个**
   
2. 点击 **"Next"** 按钮

### 3.3 选择签名方式
由于是调试版本，有两个选项：

**选项A：创建新的keystore（推荐）**
```
Key store path: [点击Create new...创建新keystore]
Password: 输入密码（记住这个密码）
Key alias: androidtvapp
Key password: 输入密码（可以和keystore密码相同）
Validity (years): 25
Certificate: 
  First and Last Name: Your Name
  Organization Unit: Dev
  Organization: Personal
  City: Your City
  State: Your State
  Country Code: CN
```

**选项B：使用调试签名（更简单）**
- 如果只是测试，可以取消当前对话框
- 直接使用Build → Build Bundle(s) / APK(s) → Build APK(s)

### 3.4 选择构建变体
1. **Module**: app
2. **Build Variants**: 
   - ● **debug** ← **选择这个（用于测试）**
   - ○ release（需要正式签名）

3. 勾选 **"V1 (Jar Signature)"** 和 **"V2 (Full APK Signature)"**

4. 点击 **"Next"**

### 3.5 选择输出目录
1. **Destination Folder**: 
   ```
   /Users/ganguoguo/Desktop/未命名文件夹 3/app/build/outputs/apk/debug
   ```
   
2. 点击 **"Finish"**

---

## 第四步：等待构建完成

### 4.1 构建进度监控
- **Build工具窗口**：底部会显示构建日志
- **进度指示器**：任务栏显示构建进度
- **预计时间**：首次构建可能需要10-20分钟

### 4.2 构建日志查看
在底部Build窗口可以看到：
```
> Task :app:preBuild UP-TO-DATE
> Task :app:preDebugBuild UP-TO-DATE
> Task :app:compileDebugKotlin
> Task :app:processDebugResources
> Task :app:packageDebug
> Task :app:assembleDebug
BUILD SUCCESSFUL in 2m 15s
```

### 4.3 构建成功标志
- ✅ 显示 "BUILD SUCCESSFUL"
- ✅ 弹出通知："APK(s) generated successfully"
- ✅ 提供"locate"或"analyze"按钮

---

## 第五步：获取APK文件

### 5.1 找到APK文件
构建成功后，APK文件位置：
```
/Users/ganguoguo/Desktop/未命名文件夹 3/app/build/outputs/apk/debug/app-debug.apk
```

### 5.2 验证APK
- **文件大小**：应该在5-20MB之间
- **文件名**：app-debug.apk
- **修改时间**：刚刚构建的时间

### 5.3 快速定位
- 点击构建成功通知中的"locate"按钮
- 或者在Finder中导航到上述路径

---

## 第六步：安装到电视盒子

### 6.1 通过ADB安装（推荐）
```bash
# 连接电视盒子
adb connect [电视盒子IP地址]

# 安装APK
adb install app-debug.apk
```

### 6.2 通过U盘安装
1. 将app-debug.apk复制到U盘
2. 在电视盒子上插入U盘
3. 使用文件管理器找到APK文件
4. 点击安装

### 6.3 通过网络安装
1. 上传APK到云盘或文件服务器
2. 在电视盒子上下载APK文件
3. 使用文件管理器安装

---

## 故障排除

### 常见错误及解决方案

**错误1**: "Gradle sync failed"
- 解决：File → Project Structure → Project → Gradle Version 设置为8.5

**错误2**: "SDK not found"
- 解决：Tools → SDK Manager → 安装Android SDK Platform-Tools

**错误3**: "Build failed with compilation errors"
- 解决：Build → Clean Project，然后重新构建

**错误4**: 中文路径问题
- 解决：将项目复制到无中文字符的路径

---

## 应用功能验证

安装完成后，在电视盒子上测试：
1. ✅ 应用图标正常显示
2. ✅ 启动应用无崩溃
3. ✅ 遥控器导航正常
4. ✅ 年级选择功能
5. ✅ 视频列表加载
6. ✅ 视频播放功能

---

**构建完成后，您将获得可以直接安装在电视盒子上的Android TV教育应用！**
