#!/bin/bash

# 明诚教育TV APK构建脚本
# 使用此脚本在有Android开发环境的机器上构建APK

echo "==== 明诚教育TV APK构建脚本 ===="
echo ""

# 检查Android SDK
if [ -z "$ANDROID_HOME" ]; then
    echo "❌ 错误: ANDROID_HOME环境变量未设置"
    echo "请安装Android SDK并设置ANDROID_HOME环境变量"
    exit 1
fi

# 检查gradle
if ! command -v gradle &> /dev/null; then
    echo "❌ 错误: gradle未安装"
    echo "请安装gradle: brew install gradle"
    exit 1
fi

echo "✅ Android SDK路径: $ANDROID_HOME"
echo "✅ Gradle版本: $(gradle --version | head -1)"
echo ""

# 清理项目
echo "🧹 清理项目..."
gradle clean

# 构建APK
echo "🔨 构建APK..."
gradle assembleDebug

# 检查构建结果
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"
if [ -f "$APK_PATH" ]; then
    echo ""
    echo "🎉 APK构建成功！"
    echo "📍 APK位置: $APK_PATH"
    echo "📦 APK大小: $(du -h "$APK_PATH" | cut -f1)"
    echo ""
    echo "安装方法："
    echo "1. U盘安装: 复制APK到U盘，插入机顶盒安装"
    echo "2. ADB安装: adb install $APK_PATH"
    echo "3. 网络安装: 上传APK到网站，机顶盒下载安装"
else
    echo ""
    echo "❌ APK构建失败"
    echo "请检查错误信息并重试"
fi
