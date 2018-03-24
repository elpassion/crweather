@file:Suppress("unused")

/**
 * Common dependencies versions for java/kotlin/android projects
 *
 * @see <a href="https://github.com/langara/deps.kt">https://github.com/langara/deps.kt</a>
 */
object Vers {
    val kotlinMajor = 1
    val kotlinMinor = 2
    val kotlinPatch = 30
    val kotlin = "$kotlinMajor.$kotlinMinor.$kotlinPatch"
    val kotlinxCoroutines = "0.22"
    val androidGradlePlugin = "3.2.0-alpha02"
    val androidMavenGradlePlugin = "2.0"
    val androidCompileSdk = 27
    val androidMinSdk = 23
    val androidTargetSdk = 27
    val androidBuildTools = "27.0.3"
    val androidSupport = "27.0.2"
    val androidSupportConstraint = "1.0.2"
    val androidArchLifecycle = "1.1.0"
    val androidArchPersistenceRoom = "1.0.0"
    val androidSupportTest = "1.0.1"
    val androidEspresso = "3.0.1"
    val androidCommons = "0.0.21"
    val rxjava = "2.1.1"
    val rxkotlin = "2.1.0"
    val rxbinding = "2.0.0"
    val rxrelay = "2.0.0"
    val rxandroid = "2.0.1"
    val rxlifecycle = "2.2.1"
    val retrofit = "2.3.0"
    val okhttp = "3.9.1"
    val javaWebsocket = "1.3.4"
    val playServices = "11.8.0"
    val picasso = "2.5.2"
    val materialDialogs = "0.9.6.0"
    val leakcanary = "1.5.4"
    val paperwork = "1.2.7"
    val mockitoKotlin = "2.0.0-alpha02"
    val mockitoAndroid = "2.10.0"
    val junit = "4.12"
    val googleTruth = "0.28"
    val androidTestRunnerClass = "android.support.test.runner.AndroidJUnitRunner"
}

/**
 * Common dependencies for java/kotlin/android projects
 *
 * @see <a href="https://github.com/langara/deps.kt">https://github.com/langara/deps.kt</a>
 */
object Deps {
    val kotlinGradlePlugin = dep("org.jetbrains.kotlin", "kotlin-gradle-plugin", Vers.kotlin)
    val androidGradlePlugin = dep("com.android.tools.build", "gradle", Vers.androidGradlePlugin)
    val androidMavenGradlePlugin = dep("com.github.dcendents", "android-maven-gradle-plugin", Vers.androidMavenGradlePlugin)
    val kotlinStdlib = dep("org.jetbrains.kotlin", "kotlin-stdlib-jre7", Vers.kotlin)
    val kotlinxCoroutinesCore = dep("org.jetbrains.kotlinx", "kotlinx-coroutines-core", Vers.kotlinxCoroutines)
    val kotlinxCoroutinesAndroid = dep("org.jetbrains.kotlinx", "kotlinx-coroutines-android", Vers.kotlinxCoroutines)
    val androidSupportV4 = dep("com.android.support", "support-v4", Vers.androidSupport)
    val androidSupportV13 = dep("com.android.support", "support-v13", Vers.androidSupport)
    val androidSupportAppcompat = dep("com.android.support", "appcompat-v7", Vers.androidSupport)
    val androidSupportRecyclerview = dep("com.android.support", "recyclerview-v7", Vers.androidSupport)
    val androidSupportCardview = dep("com.android.support", "cardview-v7", Vers.androidSupport)
    val androidSupportDesign = dep("com.android.support", "design", Vers.androidSupport)
    val androidSupportAnnotations = dep("com.android.support", "support-annotations", Vers.androidSupport)
    val androidSupportPreference = dep("com.android.support", "preference-v14", Vers.androidSupport)
    val androidSupportCustomtabs = dep("com.android.support", "customtabs", Vers.androidSupport)
    val androidSupportPercent = dep("com.android.support", "percent", Vers.androidSupport)
    val androidSupportConstraint = dep("com.android.support.constraint", "constraint-layout", Vers.androidSupportConstraint)
    val androidArchLifecycleExtensions = dep("android.arch.lifecycle", "extensions", Vers.androidArchLifecycle)
    val androidArchLifecycleViewModel = dep("android.arch.lifecycle", "viewmodel", Vers.androidArchLifecycle)
    val androidArchLifecycleLiveData = dep("android.arch.lifecycle", "livedata", Vers.androidArchLifecycle)
    val androidArchLifecycleCompiler = dep("android.arch.lifecycle", "compiler", Vers.androidArchLifecycle)
    val androidArchPersistenceRoomRuntime = dep("android.arch.persistence.room", "runtime", Vers.androidArchPersistenceRoom)
    val androidArchPersistenceRoomCompiler = dep("android.arch.persistence.room", "compiler", Vers.androidArchPersistenceRoom)
    val androidEspresso = dep("com.android.support.test.espresso", "espresso-core", Vers.androidEspresso)
    val rxjava = dep("io.reactivex.rxjava2", "rxjava", Vers.rxjava)
    val rxkotlin = dep("io.reactivex.rxjava2", "rxkotlin", Vers.rxkotlin)
    val rxrelay = dep("com.jakewharton.rxrelay2", "rxrelay", Vers.rxrelay)
    val rxbindingKotlin = dep("com.jakewharton.rxbinding2", "rxbinding-kotlin", Vers.rxbinding)
    val rxlifecycleComponents = dep("com.trello.rxlifecycle2", "rxlifecycle-components", Vers.rxlifecycle)
    val rxlifecycleKotlin = dep("com.trello.rxlifecycle2", "rxlifecycle-kotlin", Vers.rxlifecycle)
    val retrofit = dep("com.squareup.retrofit2", "retrofit", Vers.retrofit)
    val retrofitMoshi = dep("com.squareup.retrofit2", "converter-moshi", Vers.retrofit)
    val retrofitRxjava = dep("com.squareup.retrofit2", "adapter-rxjava2", Vers.retrofit)
    val okhttp = dep("com.squareup.okhttp3", "okhttp", Vers.okhttp)
    val okhttpLogging = dep("com.squareup.okhttp3", "logging-interceptor", Vers.okhttp)
    val javaWebsocket = dep("org.java-websocket", "Java-WebSocket", Vers.javaWebsocket)
    val playServicesBase = dep("com.google.android.gms", "play-services-base", Vers.playServices)
    val firebaseAppIndexing = dep("com.google.firebase", "firebase-appindexing", Vers.playServices)
    val picasso = dep("com.squareup.picasso", "picasso", Vers.picasso)
    val materialDialogsCore = dep("com.afollestad.material-dialogs", "core", Vers.materialDialogs)
    val materialDialogsCommons = dep("com.afollestad.material-dialogs", "commons", Vers.materialDialogs)
    val leakcanary = dep("com.squareup.leakcanary", "leakcanary-android", Vers.leakcanary)
    val leakcanaryNoOp = dep("com.squareup.leakcanary", "leakcanary-android-no-op", Vers.leakcanary)
    val paperwork = dep("hu.supercluster", "paperwork", Vers.paperwork)
    val paperworkPlugin = dep("hu.supercluster", "paperwork-plugin", Vers.paperwork)
    val junit = dep("junit", "junit", Vers.junit)
    val googleTruth = dep("com.google.truth", "truth", Vers.googleTruth)
    val mockitoKotlin = dep("com.nhaarman.mockitokotlin2", "mockito-kotlin", Vers.mockitoKotlin)
    val androidTestRunner = dep("com.android.support.test", "runner", Vers.androidSupportTest)
    val androidTestRules = dep("com.android.support.test", "rules", Vers.androidSupportTest)
}

private fun dep(group: String, name: String, version: String) = mapOf(
        "group" to group,
        "name" to name,
        "version" to version
)

