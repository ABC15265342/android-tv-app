#!/bin/bash

echo "=== 明诚教育TV APK构建状态检查 ==="
echo ""

# 检查gradle进程
GRADLE_PID=$(ps aux | grep "gradle" | grep -v grep | head -1 | awk '{print $2}')
if [ ! -z "$GRADLE_PID" ]; then
    echo "✅ Gradle构建进程运行中 (PID: $GRADLE_PID)"
else
    echo "❌ 没有发现Gradle构建进程"
fi

echo ""

# 检查构建目录
if [ -d "app/build" ]; then
    echo "✅ 应用构建目录已创建"
    if [ -d "app/build/outputs" ]; then
        echo "✅ 输出目录已创建"
        if [ -d "app/build/outputs/apk" ]; then
            echo "✅ APK输出目录已创建"
            APK_COUNT=$(find app/build/outputs/apk -name "*.apk" 2>/dev/null | wc -l)
            if [ $APK_COUNT -gt 0 ]; then
                echo "🎉 发现 $APK_COUNT 个APK文件!"
                find app/build/outputs/apk -name "*.apk" 2>/dev/null
            else
                echo "⏳ APK文件还在生成中..."
            fi
        else
            echo "⏳ APK输出目录还未创建，构建中..."
        fi
    else
        echo "⏳ 输出目录还未创建，正在编译..."
    fi
else
    echo "⏳ 构建目录还未创建，正在配置项目..."
fi

echo ""

# 检查网络活动（gradle下载依赖）
NETWORK_ACTIVITY=$(netstat -an | grep :443 | wc -l)
if [ $NETWORK_ACTIVITY -gt 0 ]; then
    echo "🌐 检测到网络活动，可能正在下载依赖包..."
else
    echo "🌐 无明显网络活动"
fi

echo ""
echo "💡 构建过程包括："
echo "   1. 下载Android Gradle插件和依赖 (可能需要10-20分钟)"
echo "   2. 编译Kotlin源代码"
echo "   3. 处理资源文件"
echo "   4. 生成APK安装包"
echo ""
echo "🕐 预计总构建时间：15-30分钟（首次构建）"
echo ""
echo "请耐心等待，或使用 Ctrl+C 中断当前构建"
