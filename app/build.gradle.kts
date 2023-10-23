plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

var contentProviderAuthority = ""

android {
    namespace = "com.jeluchu.wastickersonline"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.jeluchu.wastickersonline"
        minSdk = 22
        targetSdk = 34
        versionCode = 1
        versionName = "3.0.0"
        vectorDrawables.useSupportLibrary = true
        contentProviderAuthority = "$applicationId.provider.StickerContentProvider"
        manifestPlaceholders["contentProviderAuthority"] = contentProviderAuthority
        buildConfigField("String", "CONTENT_PROVIDER_AUTHORITY", "\"${contentProviderAuthority}\"")
    }
    composeOptions.kotlinCompilerExtensionVersion = "1.5.3"
    buildFeatures {
        compose = true
        viewBinding = true
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {

    // KOTLIN LIBRARY ------------------------------------------------------------------------------
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // GOOGLE LIBRARY ------------------------------------------------------------------------------
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.preference:preference-ktx:1.2.1")

    // LIFECYCLE LIBRARY ---------------------------------------------------------------------------
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.browser:browser:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    implementation(libs.bundles.hilt)
    implementation(libs.bundles.coil)
    implementation(libs.bundles.jeluchu)
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.squareup)
    ksp(libs.com.google.dagger.hilt.android.compiler)
    ksp(libs.androidx.hilt.hilt.compiler)
    ksp(libs.androidx.room.room.compiler)
}