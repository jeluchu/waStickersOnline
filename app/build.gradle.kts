plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.jeluchu.wastickersonline"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "2.0.0"
        vectorDrawables.useSupportLibrary = true
        val contentProviderAuthority = "$applicationId.provider.StickerContentProvider"
        manifestPlaceholders = mapOf("contentProviderAuthority" to contentProviderAuthority)
        buildConfigField("String", "CONTENT_PROVIDER_AUTHORITY", "\"${contentProviderAuthority}\"")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            isZipAlignEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            isZipAlignEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {

    // GOOGLE LIBRARY ------------------------------------------------------------------------------
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.preference:preference-ktx:1.1.1")

    // KOTLIN LIBRARY ------------------------------------------------------------------------------
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.10")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1")
    implementation("androidx.core:core-ktx:1.3.2")

    // KOIN LIBRARY --------------------------------------------------------------------------------
    implementation("org.koin:koin-androidx-scope:2.1.6")
    implementation("org.koin:koin-androidx-viewmodel:2.1.6")
    implementation("org.koin:koin-android-ext:2.1.6")

    // LIFECYCLE LIBRARY ---------------------------------------------------------------------------
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")

    // ROOM LIBRARY --------------------------------------------------------------------------------
    implementation("androidx.room:room-runtime:2.2.5")
    implementation("androidx.browser:browser:1.2.0")
    kapt("androidx.room:room-compiler:2.2.5")

    // RETROFIT ------------------------------------------------------------------------------------
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // MULTIMEDIA ----------------------------------------------------------------------------------
    implementation("io.coil-kt:coil:1.0.0")

}

repositories { mavenCentral() }
