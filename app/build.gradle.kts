import com.android.build.api.dsl.AaptOptions
import com.android.build.api.dsl.AndroidResources

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.facerecoginationattendence"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.facerecoginationattendence"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true;
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true

        aaptOptions {
            noCompress ("tflite")
        }
    }

}

dependencies {
    implementation(libs.androidx.compose.runtime)
    //implementation(libs.androidx.compose.material3)
    val room_version = "2.8.4"

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    // Exclude the conflicting TFLite API from the ML Kit dependency
    implementation("com.google.mlkit:face-detection:16.1.7") {
        exclude(group = "com.google.ai.edge", module = "litert-api")
    }
    implementation("org.tensorflow:tensorflow-lite:2.12.0")

    implementation("org.tensorflow:tensorflow-lite-support:0.4.3")
    implementation("androidx.room:room-runtime:${room_version}")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:${room_version}")

    val nav_version = "2.9.6"

    implementation("androidx.navigation:navigation-compose:$nav_version")
    implementation(libs.kotlinx.serialization.json)
    // module-level build.gradle
        implementation ("io.github.boguszpawlowski.composecalendar:composecalendar:1.4.0")

        // separate artifact with utilities for working with kotlinx-datetime
        implementation ("io.github.boguszpawlowski.composecalendar:kotlinx-datetime:1.4.0")



    implementation(libs.androidx.appcompat)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}