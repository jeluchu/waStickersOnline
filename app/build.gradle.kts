plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

var contentProviderAuthority = ""

android {

    compileSdk = 33
    defaultConfig {
        applicationId = "com.jeluchu.wastickersonline"
        minSdk = 22
        targetSdk = 33
        versionCode = 1
        versionName = "2.0.1"
        vectorDrawables.useSupportLibrary = true
        contentProviderAuthority = "$applicationId.provider.StickerContentProvider"
        manifestPlaceholders["contentProviderAuthority"] = contentProviderAuthority
        buildConfigField("String", "CONTENT_PROVIDER_AUTHORITY", "\"${contentProviderAuthority}\"")
    }
    buildFeatures.viewBinding = true
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {

    // GOOGLE LIBRARY ------------------------------------------------------------------------------
    implementation("androidx.appcompat:appcompat:1.6.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.preference:preference-ktx:1.2.0")

    // JCHUCOMPONENTS LIBRARY ----------------------------------------------------------------------
    implementation("com.github.jeluchu.jchucomponents:jchucomponents-core:1.0.1")
    implementation("com.github.jeluchu.jchucomponents:jchucomponents-ktx:1.0.1")

    // KOTLIN LIBRARY ------------------------------------------------------------------------------
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.core:core-ktx:1.9.0")

    // KOIN LIBRARY --------------------------------------------------------------------------------
    implementation("io.insert-koin:koin-android:3.3.2")
    implementation("io.insert-koin:koin-android-ext:3.0.2")

    // LIFECYCLE LIBRARY ---------------------------------------------------------------------------
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")

    // ROOM LIBRARY --------------------------------------------------------------------------------
    implementation("androidx.room:room-runtime:2.5.0")
    implementation("androidx.browser:browser:1.4.0")
    kapt("androidx.room:room-compiler:2.5.0")

    // RETROFIT ------------------------------------------------------------------------------------
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // MULTIMEDIA ----------------------------------------------------------------------------------
    implementation("io.coil-kt:coil:2.2.2")

}