plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

var contentProviderAuthority = ""

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.jeluchu.wastickersonline"
        minSdkVersion(21)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "2.0.1"
        vectorDrawables.useSupportLibrary = true
        contentProviderAuthority = "$applicationId.provider.StickerContentProvider"
        manifestPlaceholders(mapOf("contentProviderAuthority" to contentProviderAuthority))
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
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.preference:preference-ktx:1.1.1")

    // KOTLIN LIBRARY ------------------------------------------------------------------------------
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.5.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0-native-mt")
    implementation("androidx.core:core-ktx:1.3.2")

    // KOIN LIBRARY --------------------------------------------------------------------------------
    implementation("io.insert-koin:koin-android:3.0.1")
    implementation("io.insert-koin:koin-android-ext:3.0.1")

    // LIFECYCLE LIBRARY ---------------------------------------------------------------------------
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.3.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")

    // ROOM LIBRARY --------------------------------------------------------------------------------
    implementation("androidx.room:room-runtime:2.3.0")
    implementation("androidx.browser:browser:1.3.0")
    kapt("androidx.room:room-compiler:2.3.0")

    // RETROFIT ------------------------------------------------------------------------------------
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // MULTIMEDIA ----------------------------------------------------------------------------------
    implementation("io.coil-kt:coil:1.2.1")

}

repositories { mavenCentral() }
