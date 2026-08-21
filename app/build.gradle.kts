plugins {

    alias(libs.plugins.android.application)

    alias(libs.plugins.kotlin.compose)
}

android {

    namespace = "com.guinchou.app"

    /*
     * Atualizado para API 37.
     *
     * Algumas bibliotecas atuais do AndroidX,
     * como Lifecycle 2.11.0, exigem compileSdk 37.
     *
     * Isso NÃO significa que o aplicativo
     * só funcionará no Android 17.
     */
    compileSdk = 37

    defaultConfig {

        applicationId = "com.guinchou.app"

        /*
         * Continua suportando Android 7.0+
         * conforme configuramos anteriormente.
         */
        minSdk = 24

        /*
         * Mantemos o target atual em 35.
         *
         * compileSdk e targetSdk são independentes.
         */
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
     * COMPOSE BOM
     * =========================================
     */

    implementation(
        platform(
            libs.androidx.compose.bom
        )
    )


    /*
     * =========================================
     * JETPACK COMPOSE
     * =========================================
     */

    implementation(
        libs.androidx.activity.compose
    )

    implementation(
        libs.androidx.compose.material3
    )

    implementation(
        libs.androidx.compose.ui
    )

    implementation(
        libs.androidx.compose.ui.graphics
    )

    implementation(
        libs.androidx.compose.ui.tooling.preview
    )


    /*
     * =========================================
     * ANDROID CORE
     * =========================================
     */

    implementation(
        libs.androidx.core.ktx
    )

    implementation(
        libs.androidx.lifecycle.runtime.ktx
    )


    /*
     * =========================================
     * VIEWMODEL + COMPOSE
     * =========================================
     */

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
     * TESTES
     * =========================================
     */

    testImplementation(
        libs.junit
    )

    androidTestImplementation(
        platform(
            libs.androidx.compose.bom
        )
    )

    androidTestImplementation(
        libs.androidx.compose.ui.test.junit4
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
        libs.androidx.compose.ui.test.manifest
    )

    debugImplementation(
        libs.androidx.compose.ui.tooling
    )
}