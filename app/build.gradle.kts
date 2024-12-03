import org.jetbrains.kotlin.kapt3.base.Kapt.kapt


@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("kotlin-parcelize")
    alias(libs.plugins.googleGmsGoogleServices)
}

android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("D:\\Project\\Capstone Project\\BerkebunKeystore\\keystore.jks")
            storePassword = "berkebunplus"
            keyAlias = "keyberkebun"
            keyPassword = "berkebunplus"
        }
    }
    namespace = "com.capstone.berkebunplus"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.capstone.berkebunplus"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        signingConfig = signingConfigs.getByName("debug")
        buildConfigField("String", "BASE_URL_WEATHER", "\"https://api.openweathermap.org/\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            buildConfigField("String", "API_KEY", "\"81755ae6c9198d678de929351b503a1c\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            buildConfigField("String", "API_KEY", "\"81755ae6c9198d678de929351b503a1c\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // CameraX
    val cameraxVersion = "1.4.0"
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")

    //Firebase
    implementation (libs.firebase.auth)
    implementation (platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation ("com.google.firebase:firebase-analytics:22.1.2")
    implementation ("com.google.firebase:firebase-auth-ktx")

    implementation ("com.google.android.gms:play-services-auth:21.2.0")

    // Data Store
    implementation ("androidx.datastore:datastore-preferences:1.1.1")

    // OnBoarding page indicator
    implementation("com.tbuonomo:dotsindicator:5.0")

    // Swipe Refresh
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //Glide
    implementation ("com.github.bumptech.glide:glide:4.15.1")

    implementation ("com.google.firebase:firebase-storage:20.2.0")

    // Dependensi Firebase SDK
    implementation ("com.google.firebase:firebase-analytics-ktx")

    // Jangan lupa untuk menambahkan plugin Firebase di bawah
    implementation (platform("com.google.firebase:firebase-bom:32.1.1"))

    implementation ("androidx.core:core-ktx:1.10.1")

}