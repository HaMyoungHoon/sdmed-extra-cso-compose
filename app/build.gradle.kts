plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "sdmed.extra.cso"
    compileSdk = 35

    defaultConfig {
        applicationId = "sdmed.extra.cso"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "2.0.0"
        val googleApiKey: String by project
        resValue("string", "googleApiKey", googleApiKey)
        manifestPlaceholders.put("googleApiKey", googleApiKey)
    }
    sourceSets {
        getByName("main") {
            res.srcDirs(
                "src/main/res",

                "src/main/res/drawables",
                "src/main/res/drawables/shape",
                "src/main/res/drawables/selector",
                "src/main/res/drawables/vector",
                "src/main/res/drawables/image"
            )
        }
    }

    signingConfigs {
        val keyStore: String by project
        val keyStorePassword: String by project
        val keyAlias: String by project
        val keyPassword: String by project
        create("releaseWithKey") {
            storeFile = file(keyStore)
            storePassword = keyStorePassword
            this.keyAlias = keyAlias
            this.keyPassword = keyPassword
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("releaseWithKey")
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            signingConfig = signingConfigs.getByName("releaseWithKey")
        }
    }
    flavorDimensions += "version"
    productFlavors {
        create("kr") {
            dimension = "version"
            applicationId = "kr.sdmed.extra.cso"
            resValue("string", "dynamic_app_name", "@string/app_name")
        }
        create("dev") {
            dimension = "version"
            applicationId = "kr.sdmed.extra.cso.dev"
            resValue("string", "dynamic_app_name", "@string/app_name_dev")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    dataBinding {
        enable = true
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/io.netty.versions.properties"
        }
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.window)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.viewmodel)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.security.cryptoKtx)

    implementation(libs.google.location)
    implementation(libs.google.maps)
    implementation(libs.google.map.util)
    implementation(libs.google.play)

    implementation(libs.kodein.core)
    implementation(libs.kodein.framework)

    implementation(libs.glide)
    implementation(libs.glide.okhttp)
    kapt(libs.glide.compiler)
    implementation(libs.webpdecoder)

    implementation(libs.fasterxml.jacson)
    implementation(libs.hivemq.client)
    implementation(libs.jwt.decode)

    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okhttp.log)
    implementation(libs.squareup.retrofit) {
        exclude(module = "okhttp")
    }
    implementation(libs.squareup.retrofit.scalars)
    implementation(libs.squareup.retrofit.gson)

    implementation(libs.jetbrains.coroutines.core)
    implementation(libs.jetbrains.coroutines.android)
    implementation(libs.jetbrains.kotlin.std)

    implementation(libs.greenrobot.eventbus)
    implementation(libs.tedpermission)
}