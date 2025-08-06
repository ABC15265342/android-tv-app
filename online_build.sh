#!/bin/bash
set -e

PROJECT_DIR="/Users/ganguoguo/Desktop/未命名文件夹 3"
cd "$PROJECT_DIR"

echo "🚀 开始在线构建Android TV APK"
echo "项目目录: $(pwd)"
echo "时间: $(date)"
echo ""

# 检查必要文件
echo "📋 检查项目文件..."
if [ ! -f "gradlew" ]; then
    echo "❌ gradlew文件不存在"
    exit 1
fi

if [ ! -f "app/build.gradle" ]; then
    echo "❌ app/build.gradle文件不存在"
    exit 1
fi

echo "✅ 项目文件检查通过"

# 设置权限
chmod +x gradlew

# 清理项目
echo ""
echo "🧹 清理项目..."
timeout 300 ./gradlew clean --no-daemon --stacktrace || {
    echo "⚠️ 清理超时，继续构建..."
}

# 构建APK
echo ""
echo "🔨 构建调试版APK..."
echo "预计时间: 10-30分钟（首次构建）"

timeout 1800 ./gradlew assembleDebug --no-daemon --stacktrace --info || {
    echo "❌ APK构建失败或超时"
    echo "尝试查看错误日志..."
    exit 1
}

# 检查APK文件
echo ""
echo "🔍 查找生成的APK文件..."
APK_FILE=$(find . -name "*.apk" -type f | head -1)

if [ -n "$APK_FILE" ]; then
    echo "✅ APK构建成功!"
    echo "📱 APK文件位置: $APK_FILE"
    echo "📊 文件大小: $(ls -lh "$APK_FILE" | awk '{print $5}')"
    echo ""
    echo "🎯 安装说明:"
    echo "1. ADB安装: adb install $APK_FILE"
    echo "2. 手动安装: 复制到U盘，在电视盒子上安装"
    echo ""
    echo "✨ 构建完成时间: $(date)"
else
    echo "❌ 未找到APK文件"
    echo "检查构建输出目录..."
    ls -la app/build/outputs/ 2>/dev/null || echo "构建输出目录不存在"
    exit 1
fi
