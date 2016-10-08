# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/ashish/Android/Sdk/tools/proguard/proguard-android.txt
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

-dontwarn com.flurry.**
-dontwarn okio.**
-dontwarn oauth.signpost.signature.**
-dontwarn com.nhaarman.**
-dontwarn com.squareup.**
-dontwarn com.google.android.gms.**
-dontwarn com.androidquery.auth.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.CheckReturnValue
-dontwarn android.support.design.**
-dontwarn io.intercom.**
-dontwarn de.javakaffee.kryoserializers.**
-dontwarn fr.castorflex.**
-dontwarn org.objenesis.**

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.bussinessideas.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer


##Start App configuration file.
-keep class com.startapp.** {
      *;
}

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface
-dontwarn com.startapp.**
##---------------End: proguard configuration for Gson  ----------
