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

# Preserve the User data class and its fields
-keep class com.google.rapidrescue.Authentication.User {
    <fields>;
    <init>(...);
}

# News Fragment fixed
-keep class com.google.rapidrescue.ui.News.News {
<fields>;
<init>(...);
}

-keep class com.google.rapidrescue.ui.News.NewsAdapter {
<fields>;
<init>(...);
}

-keep class com.google.rapidrescue.ui.News.NewsAPI {
<fields>;
<init>(...);
}

-keep class com.google.rapidrescue.ui.News.NewsFragment {
<fields>;
<init>(...);
}

-keep class com.google.rapidrescue.ui.News.NewsRepository {
<fields>;
<init>(...);
}

-keep class com.google.rapidrescue.ui.News.NewsResponse {
<fields>;
<init>(...);
}

-keep class com.google.rapidrescue.ui.News.NewsViewModel {
<fields>;
<init>(...);
}

-keep class com.google.rapidrescue.ui.News.OnItemClickListener {
<fields>;
<init>(...);
}

-keep class com.google.rapidrescue.ui.News.RetrofitInstance {
<fields>;
<init>(...);
}

# Weather fixed
# Keep RetrofitInstance class and its methods
-keep class com.google.rapidrescue.ui.WeatherSafety.RetrofitInstance {
    <fields>;
    <init>(...);
    *;
}

# Keep WeatherAdapter class and its methods
-keep class com.google.rapidrescue.ui.WeatherSafety.WeatherAdapter {
    <fields>;
    <init>(...);
    *;
}

# Keep WeatherApiService interface and its methods
-keep interface com.google.rapidrescue.ui.WeatherSafety.WeatherApiService {
    <methods>;
}

# Keep WeatherRepository class and its methods
-keep class com.google.rapidrescue.ui.WeatherSafety.WeatherRepository {
    <fields>;
    <init>(...);
    *;
}

# Keep WeatherResponse class and its fields, constructors, and methods
-keep class com.google.rapidrescue.ui.WeatherSafety.WeatherResponse {
    <fields>;
    <init>(...);
    *;
}

# Keep WeatherSafety class and its methods
-keep class com.google.rapidrescue.ui.WeatherSafety.WeatherSafety {
    <fields>;
    <init>(...);
    *;
}

# Keep WeatherViewModel class and its methods
-keep class com.google.rapidrescue.ui.WeatherSafety.WeatherViewModel {
    <fields>;
    <init>(...);
    *;
}

# Keep all models used by Retrofit and Gson
-keep class com.google.rapidrescue.ui.WeatherSafety.** {
    *;
}

# Keep Retrofit annotations and Gson annotations
-keepattributes Signature, *Annotation*

# Keep Retrofit classes
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Keep Gson classes
-keep class com.google.gson.** { *; }
-keep interface com.google.gson.** { *; }


