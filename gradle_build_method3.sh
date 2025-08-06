#!/bin/bash

echo "🚀 开始方法三：直接Gradle构建"
echo "================================="
echo "时间: $(date)"
echo ""

# 项目目录
PROJECT_DIR="/Users/ganguoguo/Desktop/未命名文件夹 3"
cd "$PROJECT_DIR"

echo "📁 当前目录: $(pwd)"
echo ""

# 检查gradlew文件
if [ -f "gradlew" ]; then
    echo "✅ gradlew文件存在"
    chmod +x gradlew
    echo "✅ 已设置执行权限"
else
    echo "❌ gradlew文件不存在"
    exit 1
fi

echo ""
echo "🔧 检查Java环境..."
java -version 2>&1 | head -3

echo ""
echo "🌐 检查网络连接..."
if ping -c 1 services.gradle.org &> /dev/null; then
    echo "✅ 可以访问Gradle服务器"
else
    echo "⚠️ 无法访问Gradle服务器，但会尝试继续"
fi

echo ""
echo "🔨 开始执行: ./gradlew assembleDebug"
echo "⏱️ 预计时间: 15-30分钟 (首次构建)"
echo "📥 需要下载: 约200-500MB依赖包"
echo ""
echo "构建日志:"
echo "----------------------------------------"

# 执行构建，显示详细输出
./gradlew assembleDebug --stacktrace --info

# 检查构建结果
BUILD_EXIT_CODE=$?
echo ""
echo "----------------------------------------"

if [ $BUILD_EXIT_CODE -eq 0 ]; then
    echo "🎉 构建成功!"
    
    # 查找APK文件
    APK_FILE=$(find . -name "app-debug.apk" -type f | head -1)
    
    if [ -n "$APK_FILE" ]; then
        echo "📱 APK文件位置: $APK_FILE"
        echo "📊 文件大小: $(ls -lh "$APK_FILE" | awk '{print $5}')"
        echo "📅 创建时间: $(ls -l "$APK_FILE" | awk '{print $6, $7, $8}')"
        
        # 验证APK文件
        if [ -s "$APK_FILE" ]; then
            echo "✅ APK文件验证通过"
            
            # 打开包含APK的文件夹
            APK_DIR=$(dirname "$APK_FILE")
            echo "📂 打开APK所在文件夹: $APK_DIR"
            open "$APK_DIR" 2>/dev/null || echo "请手动导航到: $APK_DIR"
            
            echo ""
            echo "🎯 下一步操作:"
            echo "1. ADB安装: adb install \"$APK_FILE\""
            echo "2. 手动安装: 复制到U盘，在电视盒子上安装"
            echo "3. 应用测试: 验证年级选择和视频播放功能"
        else
            echo "❌ APK文件为空或损坏"
        fi
    else
        echo "❌ 未找到APK文件"
        echo "检查可能的输出位置..."
        find . -name "*.apk" -type f 2>/dev/null || echo "整个项目中未找到APK文件"
    fi
else
    echo "❌ 构建失败 (退出码: $BUILD_EXIT_CODE)"
    echo ""
    echo "🔧 可能的解决方案:"
    echo "1. 检查网络连接"
    echo "2. 重试构建: ./gradlew clean && ./gradlew assembleDebug"
    echo "3. 检查Java版本: java -version"
    echo "4. 使用Android Studio图形界面构建"
fi

echo ""
echo "✨ 构建结束时间: $(date)"
