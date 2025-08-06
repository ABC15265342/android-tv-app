#!/bin/bash

# Android TV APK 自动构建脚本
# 双击此文件即可开始构建

# 设置项目目录
PROJECT_DIR="/Users/ganguoguo/Desktop/未命名文件夹 3"

# 打开终端并显示构建信息
osascript -e "
tell application \"Terminal\"
    activate
    do script \"
        echo '🚀 Android TV APK 自动构建开始'
        echo '======================================='
        echo '项目: Android TV 教育应用'
        echo '时间: $(date)'
        echo '目录: $PROJECT_DIR'
        echo ''
        
        cd '$PROJECT_DIR'
        
        echo '🔧 检查Java环境...'
        java -version
        echo ''
        
        echo '📁 设置权限...'
        chmod +x gradlew
        echo '✅ 权限设置完成'
        echo ''
        
        echo '🌐 检查网络连接...'
        if ping -c 1 google.com &> /dev/null; then
            echo '✅ 网络连接正常'
        else
            echo '⚠️ 网络连接可能有问题，但会尝试继续构建'
        fi
        echo ''
        
        echo '🧹 步骤1: 清理项目...'
        ./gradlew clean --no-daemon
        echo ''
        
        echo '🔨 步骤2: 构建APK (预计15-30分钟)...'
        echo '正在下载依赖和编译代码，请耐心等待...'
        ./gradlew assembleDebug --no-daemon --stacktrace
        
        echo ''
        echo '🔍 检查构建结果...'
        if [ -f \"app/build/outputs/apk/debug/app-debug.apk\" ]; then
            echo '🎉 APK构建成功!'
            echo '📱 APK位置: app/build/outputs/apk/debug/app-debug.apk'
            echo '📊 文件大小:' $(ls -lh app/build/outputs/apk/debug/app-debug.apk | awk '{print \$5}')
            echo ''
            echo '🎯 下一步: 安装到电视盒子'
            echo '   adb install app/build/outputs/apk/debug/app-debug.apk'
            echo ''
            echo '✨ 构建完成时间:' $(date)
            
            # 打开包含APK的文件夹
            open app/build/outputs/apk/debug/
        else
            echo '❌ APK构建失败'
            echo '请检查上面的错误信息'
        fi
        
        echo ''
        echo '按任意键关闭终端...'
        read -n 1
    \"
end tell
"
