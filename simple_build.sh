#!/bin/bash

# 简化的APK构建器 - 绕过Gradle问题
# 使用系统Android SDK直接构建

echo "🔧 简化APK构建器"
echo "==================="
echo "开始时间: $(date)"

PROJECT_DIR="/Users/ganguoguo/Desktop/未命名文件夹 3"
cd "$PROJECT_DIR"

echo "📁 项目目录: $(pwd)"

# 检查是否有Android SDK
if [ -d "$HOME/Library/Android/sdk" ]; then
    export ANDROID_HOME="$HOME/Library/Android/sdk"
    echo "✅ 找到Android SDK: $ANDROID_HOME"
elif [ -d "/Applications/Android Studio.app/Contents/sdk" ]; then
    export ANDROID_HOME="/Applications/Android Studio.app/Contents/sdk"
    echo "✅ 找到Android SDK: $ANDROID_HOME"
else
    echo "⚠️ 未找到Android SDK，使用系统gradle"
fi

# 使用系统gradle构建
echo ""
echo "🔨 使用系统gradle构建..."

if command -v gradle &> /dev/null; then
    echo "✅ 找到系统gradle"
    gradle clean
    gradle assembleDebug
    
    # 检查APK
    APK_FILE=$(find . -name "app-debug.apk" -type f | head -1)
    if [ -n "$APK_FILE" ]; then
        echo ""
        echo "🎉 APK构建成功!"
        echo "📱 位置: $APK_FILE"
        echo "📊 大小: $(ls -lh "$APK_FILE" | awk '{print $5}')"
        
        # 复制到桌面
        cp "$APK_FILE" ~/Desktop/AndroidTV_教育应用.apk
        echo "📂 已复制到桌面: AndroidTV_教育应用.apk"
        
        # 打开桌面文件夹
        open ~/Desktop/
    else
        echo "❌ 未找到APK文件"
    fi
else
    echo "❌ 系统gradle不可用"
    echo "📋 建议使用Android Studio图形界面构建"
fi

echo ""
echo "✨ 构建脚本完成: $(date)"
