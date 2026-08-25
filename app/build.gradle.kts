plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
}

android {

    namespace = "com.guinchou.app"

    compileSdk = 37

    defaultConfig {

        applicationId = "com.guinchou.app"

        minSdk = 26

        targetSdk = 35

        versionCode = 1

        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_17

        targetCompatibility =
            JavaVersion.VERSION_17
    }

    buildFeatures {

        compose = true
    }
}

dependencies {

    /*
     * =========================================
     * COMPOSE
     * =========================================
     */

    val composeBom =
        platform(
            "androidx.compose:compose-bom:2026.08.00"
        )

    implementation(composeBom)

    androidTestImplementation(
        composeBom
    )

    implementation(
        "androidx.compose.runtime:runtime"
    )

    implementation(
        "androidx.compose.ui:ui"
    )

    implementation(
        "androidx.compose.ui:ui-graphics"
    )

    implementation(
        "androidx.compose.ui:ui-tooling-preview"
    )

    implementation(
        "androidx.compose.foundation:foundation"
    )

    implementation(
        "androidx.compose.material3:material3"
    )


    /*
     * =========================================
     * ACTIVITY
     * =========================================
     */

    implementation(
        "androidx.activity:activity-compose:1.13.0"
    )


    /*
     * =========================================
     * CORE
     * =========================================
     */

    implementation(
        libs.androidx.core.ktx
    )


    /*
     * =========================================
     * LIFECYCLE
     * =========================================
     */

    implementation(
        "androidx.lifecycle:lifecycle-runtime-ktx:2.11.0"
    )

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-compose:2.11.0"
    )


    /*
     * =========================================
     * NAVIGATION
     * =========================================
     */

    implementation(
        "androidx.navigation:navigation-compose:2.9.8"
    )


    /*
     * =========================================
     * GPS
     * =========================================
     */

    implementation(
        "com.google.android.gms:play-services-location:21.4.0"
    )


    /*
     * =========================================
     * SUPABASE
     * =========================================
     */

    implementation(
        platform(
            "io.github.jan-tennert.supabase:bom:3.7.0"
        )
    )

    implementation(
        "io.github.jan-tennert.supabase:postgrest-kt"
    )

    implementation(
        "io.github.jan-tennert.supabase:auth-kt"
    )

    implementation(
        "io.github.jan-tennert.supabase:realtime-kt"
    )

    implementation(
        "io.github.jan-tennert.supabase:storage-kt"
    )


    /*
     * =========================================
     * KTOR
     * =========================================
     */

    implementation(
        "io.ktor:ktor-client-android:3.5.1"
    )


    /*
     * =========================================
     * TESTES
     * =========================================
     */

    testImplementation(
        libs.junit
    )

    androidTestImplementation(
        "androidx.compose.ui:ui-test-junit4"
    )

    androidTestImplementation(
        libs.androidx.espresso.core
    )

    androidTestImplementation(
        libs.androidx.junit
    )


    /*
     * =========================================
     * DEBUG
     * =========================================
     */

    debugImplementation(
        "androidx.compose.ui:ui-tooling"
    )

    debugImplementation(
        "androidx.compose.ui:ui-test-manifest"
    )
}