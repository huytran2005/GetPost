plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.api"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.api"
        minSdk = 24
        targetSdk = 35
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
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0") // Sử dụng phiên bản mới nhất
    // LiveData (nếu dùng)
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0") // Sử dụng phiên bản mới nhất
    // ViewModel Compose Integration
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0") // Sử dụng phiên bản mới nhất

    // Retrofit Coroutine Adapter (để suspend fun trả về trực tiếp dữ liệu)
    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Phiên bản Retrofit của bạn
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2") // Adapter
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")


}