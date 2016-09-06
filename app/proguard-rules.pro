# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\SDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

# 腾讯的bugly
-keep public class com.tencent.bugly.**{*;}

#-keep class butterknife. { *; }
#-dontwarn butterknife.internal.*
#-keep class $$ViewInjector { *; }
#-keepclasseswithmembernames class  {
#    @butterknife. <fields>;
#}
#-keepclasseswithmembernames class  {
#    @butterknife.* <methods>;
#}

-dontwarn sun.misc.*
-keepclassmembers class rx.internal.util.unsafe.ArrayQueueField {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontwarn retrofit.
-keep class retrofit. { *; }
-keepattributes Signature
-keepattributes Exceptions
-keep class com.life.me.entity.postentity{*;}
-keep class com.life.me.entity.resultentity{*;}

-dontwarn com.squareup.okhttp.
-keep class com.squareup.okhttp.{*;}
-keep class com.zhy.http.okhttp.{*;}
-keep interface com.squareup.okhttp. { *; }
-dontwarn okio.*
-keep class com.google.gson. {*;}
-keep class com.google.gson.JsonObject { *; }

