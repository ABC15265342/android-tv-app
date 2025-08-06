#!/bin/bash

# 双击即可全自动构建Android TV APK
# 完全无人值守，自动处理一切

PROJECT_DIR="/Users/ganguoguo/Desktop/未命名文件夹 3"

osascript << EOF
tell application "Terminal"
    activate
    set newWindow to do script "
        echo '🤖 Android TV APK 全自动构建系统'
        echo '=============================='
        echo '版本: 2.0 - 零配置全自动'
        echo '开始时间: ' \$(date)
        echo ''
        
        cd '$PROJECT_DIR'
        
        # 自动设置所有权限
        echo '🔧 自动设置权限...'
        chmod +x full_auto_build.sh
        chmod +x gradlew
        find . -name '*.sh' -exec chmod +x {} \\;
        echo '✅ 权限自动配置完成'
        echo ''
        
        # 启动全自动构建
        echo '🚀 启动全自动构建系统...'
        ./full_auto_build.sh
        
        echo ''
        echo '🎉 全自动构建完成!'
        echo '📱 APK文件已准备就绪'
        echo ''
        echo '按任意键关闭终端...'
        read -n 1
    "
end tell
EOF
