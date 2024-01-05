plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}



android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.myapplication.test.RunCucumberTest"
        vectorDrawables {
            useSupportLibrary = true
        }

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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
    buildToolsVersion = "34.0.0"
}



dependencies {


    // Used for debugging
    //implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")

    // Used for connecting to the API
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    // Postgresql
    implementation("org.postgresql:postgresql:42.2.5")


    implementation("org.jsoup:jsoup:1.14.3")

    implementation("com.google.accompanist:accompanist-pager:0.28.0")
    implementation("com.google.accompanist:accompanist-pager-indicators:0.28.0")

    // UI elements from google
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui:1.0.5")
    implementation("androidx.compose.material:material:1.0.5")
    implementation("androidx.compose.material3:material3")

    // not sure
    implementation("androidx.navigation:navigation-compose:2.4.0")

    // Used for the observer pattern
    implementation("androidx.compose.runtime:runtime-livedata:1.5.2")

    // Used to import async images (dunno why we use this? - someone?)
    implementation("io.coil-kt:coil-compose:2.4.0")

    // used for reading xml files
    implementation("org.simpleframework:simple-xml:2.7.1")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation("com.google.firebase:firebase-analytics")

    // TODO: Add the dependencies for any other Firebase products you want to use
    // See https://firebase.google.com/docs/android/setup#available-libraries
    // For example, add the dependencies for Firebase Authentication and Cloud Firestore
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")

    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")


    // Cucumber in androidtest
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.5.4")


    androidTestImplementation("io.cucumber:cucumber-android:7.14.0")
    androidTestImplementation("io.cucumber:cucumber-picocontainer:7.14.1")


    // Testing dependencies
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Compose Test
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.5") // replace with your Compose version



}

tasks.withType<Test> {
    useJUnitPlatform()
    // Work around. Gradle does not include enough information to disambiguate
    // between different examples and scenarios.
    systemProperty("cucumber.junit-platform.naming-strategy", "long")
}


