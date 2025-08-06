# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep ExoPlayer classes
-keep class com.google.android.exoplayer2.** { *; }
-dontwarn com.google.android.exoplayer2.**

# Keep Retrofit classes
-keep class retrofit2.** { *; }
-keep class com.squareup.okhttp3.** { *; }
-dontwarn retrofit2.**
-dontwarn com.squareup.okhttp3.**

# Keep Glide classes
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# Keep Leanback classes
-keep class androidx.leanback.** { *; }
-dontwarn androidx.leanback.**

# Keep Parcelize classes
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
