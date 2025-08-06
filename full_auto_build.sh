#!/bin/bash

# Android TV APK 全自动构建系统
# 完全无人值守，自动处理所有步骤

echo "🤖 Android TV APK 全自动构建系统启动"
echo "====================================="
echo "版本: 1.0 - 完全自动化"
echo "开始时间: $(date)"
echo ""

# 项目配置
PROJECT_DIR="/Users/ganguoguo/Desktop/未命名文件夹 3"
LOG_FILE="$PROJECT_DIR/auto_build.log"
APK_OUTPUT_DIR="$PROJECT_DIR/READY_APK"

# 创建日志文件
exec > >(tee -a "$LOG_FILE") 2>&1

echo "📁 项目目录: $PROJECT_DIR"
echo "📄 构建日志: $LOG_FILE"
echo "📱 APK输出: $APK_OUTPUT_DIR"
echo ""

# 自动进入项目目录
cd "$PROJECT_DIR" || {
    echo "❌ 无法进入项目目录"
    exit 1
}

# 自动创建APK输出目录
mkdir -p "$APK_OUTPUT_DIR"

echo "🔧 全自动环境配置..."

# 自动设置所有必要权限
find . -name "*.sh" -exec chmod +x {} \;
chmod +x gradlew 2>/dev/null
chmod +x "🚀_一键构建启动器.command" 2>/dev/null

echo "✅ 所有脚本权限已自动设置"

# 自动检测并配置Java环境
echo ""
echo "☕ 自动检测Java环境..."
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    echo "✅ 检测到Java版本: $JAVA_VERSION"
    
    # 自动设置JAVA_HOME（如果需要）
    if [ -z "$JAVA_HOME" ]; then
        if [ -d "/Library/Java/JavaVirtualMachines" ]; then
            LATEST_JAVA=$(ls -1 /Library/Java/JavaVirtualMachines/ | tail -1)
            if [ -n "$LATEST_JAVA" ]; then
                export JAVA_HOME="/Library/Java/JavaVirtualMachines/$LATEST_JAVA/Contents/Home"
                echo "✅ 自动配置JAVA_HOME: $JAVA_HOME"
            fi
        fi
    fi
else
    echo "❌ 未检测到Java环境"
    echo "🔄 尝试自动安装Java..."
    
    # 检查是否有Homebrew
    if command -v brew &> /dev/null; then
        echo "📦 使用Homebrew自动安装OpenJDK..."
        brew install openjdk@17 || echo "⚠️ Homebrew安装失败，需要手动安装Java"
    else
        echo "⚠️ 需要手动安装Java 11或更高版本"
        echo "   推荐: https://adoptium.net/"
    fi
fi

# 自动网络检测和优化
echo ""
echo "🌐 自动网络检测和优化..."

# 测试多个网络节点
NETWORK_HOSTS=("services.gradle.org" "repo1.maven.org" "dl.google.com" "google.com")
NETWORK_OK=false

for host in "${NETWORK_HOSTS[@]}"; do
    if ping -c 1 -W 3000 "$host" &> /dev/null; then
        echo "✅ 网络连接正常: $host"
        NETWORK_OK=true
        break
    fi
done

if [ "$NETWORK_OK" = false ]; then
    echo "⚠️ 网络连接异常，但会尝试离线构建"
fi

# 自动清理和准备
echo ""
echo "🧹 自动清理项目..."
./gradlew clean --no-daemon --quiet 2>/dev/null || echo "⚠️ 清理跳过"

# 全自动构建开始
echo ""
echo "🚀 开始全自动APK构建..."
echo "⏱️ 预计时间: 15-30分钟"
echo "🤖 完全自动化，无需人工干预"
echo ""

# 显示实时进度
echo "📊 构建进度监控:"
echo "----------------------------------------"

# 后台启动构建进度监控
(
    sleep 5
    while true; do
        if pgrep -f "gradlew" > /dev/null; then
            echo "⚡ 构建进行中... $(date '+%H:%M:%S')"
        else
            break
        fi
        sleep 30
    done
) &

MONITOR_PID=$!

# 执行自动构建，使用最大兼容性参数
./gradlew assembleDebug \
    --no-daemon \
    --stacktrace \
    --info \
    --parallel \
    --build-cache \
    --configure-on-demand || {
    
    BUILD_EXIT=$?
    echo ""
    echo "⚠️ 标准构建方式遇到问题，尝试备用方案..."
    
    # 备用构建方案1: 简化参数
    ./gradlew assembleDebug --no-daemon --stacktrace || {
        
        echo "⚠️ 备用方案1失败，尝试最小化构建..."
        
        # 备用构建方案2: 最小化构建
        ./gradlew assembleDebug || {
            echo "❌ 所有自动构建方案均失败"
            echo "需要手动诊断问题"
        }
    }
}

# 停止进度监控
kill $MONITOR_PID 2>/dev/null

echo ""
echo "----------------------------------------"

# 全自动APK处理
echo "🔍 自动查找和处理APK文件..."

# 智能APK查找
APK_LOCATIONS=(
    "app/build/outputs/apk/debug/app-debug.apk"
    "app/build/outputs/apk/app-debug.apk"
    "build/outputs/apk/debug/app-debug.apk"
    "app/build/apk/debug/app-debug.apk"
)

FOUND_APK=""
for location in "${APK_LOCATIONS[@]}"; do
    if [ -f "$location" ]; then
        FOUND_APK="$location"
        break
    fi
done

if [ -n "$FOUND_APK" ]; then
    echo ""
    echo "🎉 APK自动构建成功!"
    
    # 获取APK信息
    APK_SIZE=$(ls -lh "$FOUND_APK" | awk '{print $5}')
    APK_DATE=$(ls -l "$FOUND_APK" | awk '{print $6, $7, $8}')
    
    echo "📱 APK位置: $FOUND_APK"
    echo "📊 文件大小: $APK_SIZE"
    echo "📅 构建时间: $APK_DATE"
    
    # 自动复制APK到便于访问的位置
    FINAL_APK="$APK_OUTPUT_DIR/AndroidTV_教育应用_v1.0.apk"
    cp "$FOUND_APK" "$FINAL_APK"
    echo "📂 APK已自动复制到: $FINAL_APK"
    
    # 自动验证APK
    if [ -s "$FINAL_APK" ] && [ $(stat -f%z "$FINAL_APK") -gt 5000000 ]; then
        echo "✅ APK自动验证通过"
        
        # 自动生成安装说明
        cat > "$APK_OUTPUT_DIR/安装说明.txt" << 'INSTALL_EOF'
📱 Android TV 教育应用 - 安装说明

📋 应用信息:
- 应用名称: Android TV 教育应用
- 包名: com.example.androidtvapp
- 版本: 1.0
- 适用平台: Android TV (API 21+)
- 文件大小: 约8-15MB

🎯 安装方法:

方法1 - ADB安装 (推荐):
1. 确保电视盒子开启ADB调试
2. 连接到同一网络
3. 执行命令: adb install AndroidTV_教育应用_v1.0.apk

方法2 - U盘安装:
1. 将APK文件复制到U盘
2. 在电视盒子上插入U盘
3. 使用文件管理器找到APK文件
4. 点击安装

方法3 - 网络安装:
1. 上传APK到云盘
2. 在电视盒子上下载APK
3. 点击安装

✨ 应用功能:
- 年级选择 (小学/初中/高中)
- 课程分类浏览
- MP4视频在线播放
- 遥控器完整支持
- 10英尺电视界面

🔗 教育网站: http://43.138.218.45:3000

构建时间: $(date)
INSTALL_EOF

        echo "📄 安装说明已自动生成"
        
        # 自动打开APK所在文件夹
        echo "📂 自动打开APK文件夹..."
        open "$APK_OUTPUT_DIR" 2>/dev/null || echo "请手动打开: $APK_OUTPUT_DIR"
        
        # 显示最终状态
        echo ""
        echo "🎊 全自动构建完成!"
        echo "━━━━━━━━━━━━━━━━━━━━━━━━━━"
        echo "📱 应用: Android TV 教育应用"
        echo "📂 位置: $FINAL_APK"
        echo "📊 大小: $APK_SIZE"
        echo "⏱️ 用时: 构建于 $(date)"
        echo "🎯 状态: 可立即安装使用"
        echo ""
        echo "🚀 下一步: 将APK安装到电视盒子"
        
    else
        echo "⚠️ APK文件可能有问题，大小异常"
        ls -la "$FINAL_APK"
    fi
    
else
    echo "❌ 未找到APK文件"
    echo "🔍 搜索项目中的所有APK文件..."
    find . -name "*.apk" -type f 2>/dev/null | head -5
    
    echo ""
    echo "📋 故障诊断信息:"
    echo "1. 检查构建日志: $LOG_FILE"
    echo "2. 验证Java环境: java -version"
    echo "3. 检查网络连接"
    echo "4. 考虑使用Android Studio图形界面"
fi

echo ""
echo "📋 全自动构建日志已保存: $LOG_FILE"
echo "✨ 构建系统执行完成: $(date)"

# 保持终端打开以便查看结果
echo ""
echo "按回车键关闭..."
read
