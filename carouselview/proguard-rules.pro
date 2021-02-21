# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\alireza\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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
#  public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable
#-keepattributes LocalVariableTable, LocalVariableTypeTable
#-optimizations !method/removal/parameter

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#-dontwarn java.nio.file.Files
#-dontwarn java.nio.file.Path
#-dontwarn java.nio.file.OpenOption
#-dontwarn org.apache.http.client.HttpClient
#-dontwarn android.net.http.AndroidHttpClient
#-dontwarn android.net.http.**

#-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
#-keepnames class org.jsoup.nodes.Entities
#-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
#-keepnames class org.jsoup.nodes.Entities
#-dontwarn javax.annotation.**

#-dontwarn retrofit.**
#-keep class retrofit.** { *; }
#-keepattributes Signature
#-keepattributes Exceptions
#
#-keepattributes Signature
#-keepattributes *Annotation*
#-keep class okhttp3.** { *; }
#-keep interface okhttp3.** { *; }
#-dontwarn okhttp3.**
#-dontnote okhttp3.**
#
## Okio
#-keep class sun.misc.Unsafe { *; }
#-dontwarn java.nio.file.*
#-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

