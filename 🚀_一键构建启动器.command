#!/bin/bash

# 一键启动Android TV APK构建
# 支持本地构建和GitHub在线构建

PROJECT_DIR="/Users/ganguoguo/Desktop/未命名文件夹 3"

# 使用AppleScript显示选择对话框
CHOICE=$(osascript << EOF
set choices to {"🔧 本地构建 (需要Gradle环境)", "🌐 GitHub在线构建 (推荐)", "📤 上传到GitHub"}
set selectedChoice to choose from list choices with prompt "选择构建方式:" default items {"🌐 GitHub在线构建 (推荐)"}
if selectedChoice is false then
    return "cancelled"
else
    return item 1 of selectedChoice
end if
EOF
)

if [ "$CHOICE" = "cancelled" ]; then
    echo "用户取消操作"
    exit 0
fi

# 根据选择执行不同操作
case "$CHOICE" in
    "🔧 本地构建 (需要Gradle环境)")
        # 原本地构建逻辑
        osascript << EOF
        tell application "Terminal"
            activate
            set newWindow to do script "
                echo '� Android TV APK 本地构建器'
                echo '============================='
                echo '项目: Android TV 教育应用'
                echo '开始时间: ' \$(date)
                echo ''
                
                cd '$PROJECT_DIR'
                echo '📁 进入项目目录: ' \$(pwd)
                
                # 检查Gradle环境
                if command -v gradle >/dev/null 2>&1; then
                    echo '✅ 找到系统Gradle'
                    gradle --version
                    echo '🚀 开始构建...'
                    gradle clean assembleDebug
                elif [ -f 'gradlew' ]; then
                    echo '✅ 使用项目Gradle Wrapper'
                    chmod +x gradlew
                    ./gradlew clean assembleDebug
                else
                    echo '❌ 未找到Gradle环境'
                    echo '💡 建议使用GitHub在线构建'
                fi
                
                echo ''
                echo '� 构建完成！'
                echo '按任意键关闭终端...'
                read -n 1
            "
        end tell
EOF
        ;;
        
    "🌐 GitHub在线构建 (推荐)")
        osascript << EOF
        display dialog "🌐 GitHub在线构建指南

1️⃣ 将项目上传到GitHub
   • 创建GitHub账号（免费）
   • 新建仓库（repository）
   • 上传项目代码

2️⃣ 自动构建APK
   • 推送代码后自动构建
   • 或手动触发构建

3️⃣ 下载APK
   • 在Actions页面下载
   • 支持直接安装到设备

💡 优势：
   ✅ 无需本地环境
   ✅ 云端自动构建  
   ✅ 永久免费使用
   ✅ 多设备同步

是否现在打开GitHub网站？" buttons {"取消", "🚀 打开GitHub", "📤 上传项目"} default button "📤 上传项目"
        set response to button returned of result
        
        if response is "🚀 打开GitHub" then
            open location "https://github.com"
        else if response is "📤 上传项目" then
            tell application "Terminal"
                activate
                do script "cd '$PROJECT_DIR' && chmod +x 🌐_GitHub上传脚本.sh && ./🌐_GitHub上传脚本.sh"
            end tell
        end if
EOF
        ;;
        
    "📤 上传到GitHub")
        osascript << EOF
        tell application "Terminal"
            activate
            set newWindow to do script "
                echo '📤 GitHub 项目上传助手'
                echo '======================'
                echo ''
                
                cd '$PROJECT_DIR'
                chmod +x 🌐_GitHub上传脚本.sh
                ./🌐_GitHub上传脚本.sh
                
                echo ''
                echo '按任意键关闭终端...'
                read -n 1
            "
        end tell
EOF
        ;;
esac
