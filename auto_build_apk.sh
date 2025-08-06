#!/bin/bash

echo "🚀 自动构建Android TV APK"
echo "==============================="
echo "开始时间: $(date)"
echo ""

# 项目信息
PROJECT_DIR="/Users/ganguoguo/Desktop/未命名文件夹 3"
APK_OUTPUT="app/build/outputs/apk/debug/app-debug.apk"

# 检查网络连接
echo "🌐 检查网络连接..."
if ping -c 1 google.com &> /dev/null; then
    echo "✅ 网络连接正常"
else
    echo "⚠️ 外网连接可能有问题，但会尝试继续构建"
fi

# 进入项目目录
cd "$PROJECT_DIR"
echo "📁 当前目录: $(pwd)"

# 设置权限
chmod +x gradlew
echo "✅ 已设置gradlew执行权限"

# 检查Java环境
echo ""
echo "☕ 检查Java环境..."
java -version 2>&1 | head -3

# 开始构建流程
echo ""
echo "🧹 第1步: 清理项目..."
./gradlew clean --no-daemon --quiet

echo ""
echo "🔨 第2步: 构建APK (这可能需要15-30分钟)..."
echo "状态: 正在下载依赖和编译代码..."

# 使用超时保护，避免无限等待
timeout 2400 ./gradlew assembleDebug --no-daemon --stacktrace --info || {
    EXITCODE=$?
    if [ $EXITCODE -eq 124 ]; then
        echo "⏰ 构建超时 (40分钟)，可能网络较慢"
    else
        echo "❌ 构建过程出现错误 (退出码: $EXITCODE)"
    fi
    echo "尝试继续检查是否有APK生成..."
}

# 检查构建结果
echo ""
echo "🔍 检查构建结果..."
if [ -f "$APK_OUTPUT" ]; then
    echo "🎉 APK构建成功!"
    echo "📱 APK位置: $APK_OUTPUT"
    echo "📊 文件大小: $(ls -lh "$APK_OUTPUT" | awk '{print $5}')"
    echo "📅 创建时间: $(ls -l "$APK_OUTPUT" | awk '{print $6, $7, $8}')"
    echo ""
    echo "🎯 安装说明:"
    echo "1. ADB安装: adb install \"$APK_OUTPUT\""
    echo "2. 手动安装: 复制APK到U盘，在电视盒子上安装"
    echo ""
    echo "✨ 构建完成时间: $(date)"
    
    # 尝试打开包含APK的文件夹
    open "$(dirname "$APK_OUTPUT")" 2>/dev/null || echo "请手动导航到APK文件位置"
else
    echo "❌ 未找到APK文件"
    echo "可能的原因:"
    echo "1. 网络连接问题，无法下载依赖"
    echo "2. Java环境配置问题"
    echo "3. Gradle版本兼容性问题"
    echo ""
    echo "🔧 建议解决方案:"
    echo "1. 检查网络连接"
    echo "2. 重试构建: ./gradlew assembleDebug"
    echo "3. 或使用Android Studio图形界面构建"
fi

echo ""
echo "📋 构建日志保存在项目目录中"
echo "如需帮助，请提供错误信息以便进一步诊断"
