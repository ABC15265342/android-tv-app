#!/bin/bash

# Android TV APK 完整构建脚本
# 由GitHub Copilot自动生成

set -e  # 遇到错误立即停止

echo "🚀 Android TV教育应用 - 自动构建脚本"
echo "========================================"
echo "开始时间: $(date)"
echo "构建环境: macOS"
echo ""

# 项目配置
PROJECT_NAME="Android TV 教育应用"
PACKAGE_NAME="com.example.androidtvapp"
VERSION_NAME="1.0"
VERSION_CODE="1"
PROJECT_DIR="/Users/ganguoguo/Desktop/未命名文件夹 3"

# 进入项目目录
cd "$PROJECT_DIR"
echo "📁 项目目录: $(pwd)"

# 验证项目结构
echo ""
echo "🔍 验证项目结构..."

REQUIRED_FILES=(
    "build.gradle"
    "settings.gradle"
    "app/build.gradle"
    "app/src/main/AndroidManifest.xml"
    "app/src/main/java/com/education/tvapp/MainActivity.kt"
    "gradlew"
    "gradle/wrapper/gradle-wrapper.properties"
    "gradle/wrapper/gradle-wrapper.jar"
)

for file in "${REQUIRED_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file"
    else
        echo "❌ 缺少文件: $file"
        exit 1
    fi
done

echo ""
echo "🔧 环境检查..."

# 检查Java环境
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1)
    echo "✅ Java环境: $JAVA_VERSION"
else
    echo "❌ 未找到Java环境"
    echo "请安装Java 11或更高版本"
    exit 1
fi

# 检查网络连接
echo ""
echo "🌐 检查网络连接..."
if ping -c 1 -W 5000 services.gradle.org &> /dev/null; then
    echo "✅ 网络连接正常"
elif ping -c 1 -W 5000 google.com &> /dev/null; then
    echo "⚠️ 网络连接可能较慢，但会尝试继续"
else
    echo "❌ 网络连接异常"
    echo "首次构建需要网络连接下载依赖"
    echo "请检查网络设置后重试"
    exit 1
fi

# 设置权限
echo ""
echo "⚙️ 设置构建权限..."
chmod +x gradlew
echo "✅ gradlew权限已设置"

# 清理项目
echo ""
echo "🧹 清理项目..."
./gradlew clean --no-daemon --quiet || {
    echo "⚠️ 清理失败，但继续构建..."
}

# 显示构建信息
echo ""
echo "📋 构建信息:"
echo "   项目名称: $PROJECT_NAME"
echo "   包名: $PACKAGE_NAME"
echo "   版本: $VERSION_NAME ($VERSION_CODE)"
echo "   构建类型: Debug"
echo "   目标平台: Android TV"
echo ""

# 开始构建APK
echo "🔨 开始构建APK..."
echo "⏱️ 预计时间: 15-30分钟 (首次构建)"
echo "📥 需要下载约200-500MB依赖包"
echo ""
echo "构建进度日志:"
echo "----------------------------------------"

# 使用超时保护构建过程
timeout 3600 ./gradlew assembleDebug --no-daemon --stacktrace --info || {
    EXIT_CODE=$?
    echo ""
    echo "----------------------------------------"
    
    if [ $EXIT_CODE -eq 124 ]; then
        echo "⏰ 构建超时 (60分钟限制)"
        echo "可能是网络较慢导致依赖下载时间过长"
    else
        echo "❌ 构建失败 (退出码: $EXIT_CODE)"
    fi
    
    echo ""
    echo "🔧 故障排除建议:"
    echo "1. 检查网络连接速度"
    echo "2. 重试构建: ./gradlew assembleDebug"
    echo "3. 清理后重建: ./gradlew clean && ./gradlew assembleDebug"
    echo "4. 使用Android Studio图形界面构建"
    
    # 即使构建失败，也检查是否有部分APK生成
    echo ""
    echo "检查是否有部分构建结果..."
}

echo ""
echo "----------------------------------------"
echo "🔍 检查构建结果..."

# 查找APK文件
APK_PATHS=(
    "app/build/outputs/apk/debug/app-debug.apk"
    "app/build/outputs/apk/app-debug.apk"
    "build/outputs/apk/debug/app-debug.apk"
)

APK_FOUND=""
for apk_path in "${APK_PATHS[@]}"; do
    if [ -f "$apk_path" ]; then
        APK_FOUND="$apk_path"
        break
    fi
done

if [ -n "$APK_FOUND" ]; then
    echo ""
    echo "🎉 APK构建成功!"
    echo "📱 APK位置: $APK_FOUND"
    
    # 获取文件信息
    FILE_SIZE=$(ls -lh "$APK_FOUND" | awk '{print $5}')
    FILE_TIME=$(ls -l "$APK_FOUND" | awk '{print $6, $7, $8}')
    
    echo "📊 文件大小: $FILE_SIZE"
    echo "📅 创建时间: $FILE_TIME"
    
    # 验证APK文件完整性
    if [ -s "$APK_FOUND" ] && [ $(stat -f%z "$APK_FOUND") -gt 1000000 ]; then
        echo "✅ APK文件验证通过"
        
        # 显示APK信息
        echo ""
        echo "📋 APK详细信息:"
        echo "   完整路径: $(realpath "$APK_FOUND")"
        echo "   文件类型: Android APK"
        echo "   适用平台: Android TV (API 21+)"
        echo "   安装包名: $PACKAGE_NAME"
        
        # 尝试打开包含APK的文件夹
        APK_DIR=$(dirname "$APK_FOUND")
        echo ""
        echo "📂 正在打开APK所在文件夹..."
        open "$APK_DIR" 2>/dev/null || {
            echo "无法自动打开文件夹，请手动导航到:"
            echo "   $APK_DIR"
        }
        
        echo ""
        echo "🎯 下一步操作指南:"
        echo ""
        echo "方法1 - ADB安装 (推荐):"
        echo "   adb connect [电视盒子IP地址]"
        echo "   adb install \"$APK_FOUND\""
        echo ""
        echo "方法2 - U盘安装:"
        echo "   1. 将APK文件复制到U盘"
        echo "   2. 在电视盒子上插入U盘"
        echo "   3. 使用文件管理器安装APK"
        echo ""
        echo "方法3 - 网络安装:"
        echo "   1. 上传APK到云盘或文件服务器"
        echo "   2. 在电视盒子上下载APK文件"
        echo "   3. 点击安装"
        
    else
        echo "❌ APK文件可能损坏或不完整"
        echo "文件大小异常，建议重新构建"
    fi
    
else
    echo "❌ 未找到APK文件"
    echo ""
    echo "🔍 搜索所有可能的APK文件..."
    find . -name "*.apk" -type f 2>/dev/null | head -10 || echo "项目中未找到任何APK文件"
    
    echo ""
    echo "🔧 问题诊断:"
    echo "1. 构建可能未完全成功"
    echo "2. 输出路径可能不同"
    echo "3. 需要检查构建日志中的错误信息"
    
    echo ""
    echo "💡 建议解决方案:"
    echo "1. 重新运行构建: ./gradlew clean assembleDebug"
    echo "2. 检查Java和Android SDK环境"
    echo "3. 使用Android Studio进行图形化构建"
    echo "4. 查看详细错误日志"
fi

echo ""
echo "✨ 构建脚本执行完成"
echo "结束时间: $(date)"
echo ""
echo "📞 如需技术支持，请提供构建日志中的错误信息"
