plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.vou_mobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.vou_mobile"
        minSdk = 24
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
    dataBinding{
        enable = true
    }
}

dependencies {
    // lottie file
    implementation("com.airbnb.android:lottie:5.2.0")
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-analytics")
    // Add the dependency for the Firebase Authentication library
    implementation("com.google.firebase:firebase-auth")
    // Firebase Realtime
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    // Recyclerview
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    // Picasso
    implementation("com.squareup.picasso:picasso:2.71828")
    // Tap host
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.core:core-ktx:1.13.1")
    // Scanner
    implementation("com.github.yuriy-budiyev:code-scanner:2.3.0")
    // QR generate
    implementation("com.journeyapps:zxing-android-embedded:4.3.0@aar")
    implementation("com.google.zxing:core:3.4.1")
    //gson
    implementation("com.google.code.gson:gson:2.10")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //webSocket
    implementation ("io.socket:socket.io-client:2.1.1")

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.8.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
    implementation("androidx.test:core-ktx:1.6.1")

    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("commons-codec:commons-codec:1.15")

    implementation("com.paypal.sdk:paypal-android-sdk:2.16.0")

    // ZaloPay
    implementation(fileTree(mapOf(
        "dir" to "../ZaloPayLib",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}