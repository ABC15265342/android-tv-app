#!/bin/bash

# Android TV APK 一键构建脚本
# 请复制以下命令到您的终端中执行

echo "🚀 开始构建Android TV教育应用APK"
echo "========================================="

# 第1步：进入项目目录
cd "/Users/ganguoguo/Desktop/未命名文件夹 3"
echo "✅ 已进入项目目录: $(pwd)"

# 第2步：设置gradlew执行权限
chmod +x gradlew
echo "✅ 已设置gradlew执行权限"

# 第3步：检查项目结构
if [ -f "build.gradle" ] && [ -f "app/build.gradle" ]; then
    echo "✅ 项目结构检查通过"
else
    echo "❌ 项目结构不完整"
    exit 1
fi

# 第4步：清理项目（可选，但推荐）
echo "🧹 开始清理项目..."
./gradlew clean

# 第5步：构建调试版APK
echo "🔨 开始构建APK..."
echo "预计时间：首次构建15-30分钟，后续构建2-5分钟"
./gradlew assembleDebug

# 第6步：检查构建结果
if [ $? -eq 0 ]; then
    echo ""
    echo "🎉 APK构建成功！"
    echo "📱 APK文件位置："
    find . -name "*.apk" -type f
    echo ""
    echo "📊 文件信息："
    ls -lh app/build/outputs/apk/debug/app-debug.apk 2>/dev/null || echo "APK文件路径可能不同，请在build输出中查找"
    echo ""
    echo "🎯 下一步：安装到电视盒子"
    echo "   adb install app-debug.apk"
else
    echo "❌ APK构建失败，请检查错误信息"
fi
