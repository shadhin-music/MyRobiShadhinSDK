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

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

-keepattributes Signature
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
  }
-keep,allowobfuscation @interface com.google.gson.annotations.SerializedName
-keep class org.xmlpull.v1.* {*;}
-keepclassmembers class com.gm.banglalinkestore.data.model** {
  *;
}

-keepattributes SourceFile,LineNumberTable        # Keep file names and line numbers.
-keep public class * extends java.lang.Exception  # Optional: Keep custom exceptions.
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keep class * extends androidx.fragment.app.Fragment{}
-keepnames class androidx.navigation.fragment.NavHostFragment