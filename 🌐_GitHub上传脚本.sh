#!/bin/bash

# 🚀 GitHub 上传和自动构建脚本
# 这个脚本会帮助您将项目上传到GitHub并触发自动构建

echo "🚀 Android TV应用 - GitHub上传脚本"
echo "=================================="
echo ""

# 检查是否已经是Git仓库
if [ ! -d ".git" ]; then
    echo "📁 初始化Git仓库..."
    git init
    echo "✅ Git仓库初始化完成"
else
    echo "✅ 已经是Git仓库"
fi

# 添加所有文件
echo "📋 添加项目文件..."
git add .

# 提交更改
echo "💾 提交更改..."
git commit -m "🎉 初始提交: Android TV教育应用

✨ 功能特点:
- 📺 Android TV优化界面
- 🎬 MP4视频播放支持
- 🎮 遥控器完美操控
- 📚 学科分类浏览
- 🌐 网络视频流播放
- 🚀 GitHub Actions自动构建

🛠️ 技术栈:
- Kotlin + Android TV Leanback
- ExoPlayer 2.19.1 视频引擎
- Retrofit2 + OkHttp 网络层
- MVVM架构模式

📱 支持设备:
- 小米盒子、华为机顶盒
- 海信、TCL、创维电视
- Android TV 5.0+ 设备"

echo ""
echo "✅ 文件提交完成！"
echo ""
echo "📤 接下来的步骤："
echo "1. 在GitHub上创建新仓库"
echo "2. 复制仓库URL"
echo "3. 运行以下命令连接远程仓库："
echo ""
echo "   git remote add origin <YOUR_REPO_URL>"
echo "   git branch -M main"
echo "   git push -u origin main"
echo ""
echo "4. 推送完成后，GitHub Actions将自动构建APK"
echo "5. 在仓库的Actions页面下载构建好的APK文件"
echo ""
echo "🎯 自动构建特性："
echo "- ✅ 每次推送代码自动构建"
echo "- ✅ 可手动触发构建"  
echo "- ✅ APK文件自动打包上传"
echo "- ✅ 保留30天下载期限"
echo ""

# 显示仓库状态
echo "📊 当前仓库状态："
git status
echo ""
echo "🎉 准备完成！请按照上述步骤将代码推送到GitHub"
