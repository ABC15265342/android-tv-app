#!/bin/bash

# 全自动Android TV APK构建器
# 双击此文件即可开始自动构建

PROJECT_DIR="/Users/ganguoguo/Desktop/未命名文件夹 3"

osascript << EOF
tell application "Terminal"
    activate
    do script "
        clear
        echo '🤖 Android TV APK 全自动构建器'
        echo '============================'
        echo '📱 项目: Android TV 教育应用'
        echo '⏰ 开始: ' \$(date)
        echo ''
        echo '🔄 正在启动自动构建系统...'
        echo ''
        
        cd '$PROJECT_DIR'
        
        # 设置权限
        chmod +x full_auto_build.sh 2>/dev/null
        chmod +x gradlew 2>/dev/null
        
        # 启动构建
        if [ -f 'full_auto_build.sh' ]; then
            ./full_auto_build.sh
        else
            echo '⚠️ 使用备用构建方法...'
            chmod +x gradlew
            ./gradlew assembleDebug
        fi
        
        echo ''
        echo '✨ 构建过程完成！'
        echo '按回车键关闭终端...'
        read
    "
end tell
EOF
