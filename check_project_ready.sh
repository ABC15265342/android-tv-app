#!/bin/bash

echo "🔍 Android Studio 构建准备检查"
echo "=================================="
echo ""

# 检查项目根目录
echo "📁 检查项目结构..."
if [ -f "build.gradle" ] && [ -f "settings.gradle" ]; then
    echo "✅ 项目根配置文件存在"
else
    echo "❌ 缺少必要的项目配置文件"
    exit 1
fi

# 检查app模块
echo ""
echo "📱 检查app模块..."
if [ -f "app/build.gradle" ] && [ -f "app/src/main/AndroidManifest.xml" ]; then
    echo "✅ app模块配置正确"
else
    echo "❌ app模块配置不完整"
    exit 1
fi

# 检查核心源文件
echo ""
echo "💻 检查核心源文件..."
REQUIRED_FILES=(
    "app/src/main/java/com/example/androidtvapp/MainActivity.kt"
    "app/src/main/java/com/example/androidtvapp/VideoPlayerActivity.kt"
    "app/src/main/java/com/example/androidtvapp/VideoDetailsActivity.kt"
    "app/src/main/java/com/example/androidtvapp/models/Video.kt"
)

for file in "${REQUIRED_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file"
    else
        echo "❌ $file 缺失"
    fi
done

# 检查资源文件
echo ""
echo "🎨 检查资源文件..."
RESOURCE_FILES=(
    "app/src/main/res/layout/activity_main.xml"
    "app/src/main/res/layout/activity_video_player.xml"
    "app/src/main/res/values/strings.xml"
    "app/src/main/res/values/colors.xml"
)

for file in "${RESOURCE_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "✅ $file"
    else
        echo "❌ $file 缺失"
    fi
done

# 检查Gradle Wrapper
echo ""
echo "⚙️  检查Gradle Wrapper..."
if [ -f "gradlew" ] && [ -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo "✅ Gradle Wrapper配置完整"
    chmod +x gradlew
    echo "✅ gradlew权限已设置"
else
    echo "❌ Gradle Wrapper配置不完整"
fi

# 显示项目信息
echo ""
echo "📊 项目信息摘要："
echo "----------------------------------------"
echo "项目名称: Android TV 教育应用"
echo "包名: com.example.androidtvapp"
echo "目标SDK: 34"
echo "最小SDK: 21"
echo "构建工具: Gradle 8.5"
echo "语言: Kotlin"
echo "架构: MVVM"
echo ""

echo "🚀 下一步操作："
echo "1. 启动 Android Studio"
echo "2. 选择 'Open an Existing Project'"
echo "3. 导航到当前目录: $(pwd)"
echo "4. 等待 Gradle 同步完成"
echo "5. Build → Generate Signed Bundle / APK"
echo ""

echo "✨ 项目检查完成！准备在Android Studio中构建。"
