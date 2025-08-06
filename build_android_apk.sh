#!/bin/bash
set -e

echo "=== Android TV APK 构建脚本 ==="
echo "开始时间: $(date)"
echo "当前目录: $(pwd)"

# 检查必要文件
echo ""
echo "检查项目文件..."
if [ ! -f "build.gradle" ]; then
    echo "错误: build.gradle 文件不存在"
    exit 1
fi

if [ ! -f "gradlew" ]; then
    echo "错误: gradlew 文件不存在" 
    exit 1
fi

if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "错误: gradle-wrapper.jar 文件不存在"
    exit 1
fi

# 设置权限
chmod +x gradlew

echo ""
echo "检查Gradle版本..."
timeout 60 ./gradlew --version || {
    echo "Gradle版本检查超时或失败"
    exit 1
}

echo ""
echo "清理项目..."
timeout 120 ./gradlew clean || {
    echo "清理项目失败"
    exit 1
}

echo ""
echo "开始构建APK..."
timeout 600 ./gradlew assembleDebug --stacktrace || {
    echo "APK构建失败"
    exit 1
}

echo ""
echo "构建完成！查找APK文件..."
find . -name "*.apk" -type f 2>/dev/null || echo "未找到APK文件"

echo ""
echo "构建结束时间: $(date)"
