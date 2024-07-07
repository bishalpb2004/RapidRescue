plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.rapidrescue"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.rapidrescue"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        buildFeatures{
            viewBinding = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }


}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.firebase:firebase-messaging-ktx:24.0.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.activity:activity:1.9.0")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-android:2.8.2")
    implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.google.android.material:material:1.12.0")

    //Google maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.libraries.places:places:3.5.0")


    //Animation gif
    implementation("com.airbnb.android:lottie:6.0.0")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")
    //For AI bot
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.22"))
    implementation("com.google.android.libraries.places:places:3.5.0")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    //glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.bumptech.glide:compiler:4.12.0")

    //Picasso
    implementation("com.squareup.picasso:picasso:2.71828")

    //osm map
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    implementation("org.osmdroid:osmdroid-mapsforge:6.1.18")
    implementation("com.github.MKergall:osmbonuspack:6.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.2")


    //Maps
    implementation("com.squareup.okhttp3:okhttp:4.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")


}