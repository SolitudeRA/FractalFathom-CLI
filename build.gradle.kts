plugins {
    id("application")
    kotlin("jvm") version "2.0.21"
}

group = "org.protogalaxy"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val kotlinVersion = "2.0.21"
val okhttpVersion = "4.12.0"
val jacksonVersion = "2.18.0"
val spoonVersion = "11.1.0"
val plantUMLVersion = "1.2024.7"
val coroutinesVersion = "1.9.0"
val slf4jVersion = "2.0.16"
val junitVersion = "5.11.3"
val mockkVersion = "1.13.13"

// Separate source set for annotations
sourceSets {
    create("annotations") {
        java.srcDir("src/annotations/java")
    }
}

// Build task to create a separate JAR for annotations
tasks.register<Jar>("buildLib") {
    archiveClassifier.set("annotations")
    from(sourceSets["annotations"].output)
    destinationDirectory.set(layout.buildDirectory.dir("libs"))
}

// Run the application
tasks.register<JavaExec>("runApp") {
    group = "application"
    description = "Run the application"
    mainClass.set(application.mainClass.get())
    classpath = sourceSets.main.get().runtimeClasspath
    standardInput = System.`in`
    dependsOn("startCodeInsightServer")
    finalizedBy("stopCodeInsightServer")
}

// Task to start the Python server
tasks.register<Exec>("startCodeInsightServer") {
    group = "application"
    description = "Starts the code insight server script"
    commandLine("python", pythonScriptPath)
    isIgnoreExitValue = true
    doLast {
        println("Code insight server started.")
    }
}

// Task to stop the Python server
tasks.register("stopCodeInsightServer") {
    group = "application"
    description = "Stops the code insight server script if it is running"
    doLast {
        try {
            exec {
                commandLine("pkill", "-f", "python $pythonScriptPath")
            }
            println("Code insight server script stopped.")
        } catch (_: Exception) {
            println("No running code insight server process found.")
        }
    }
}

// Path to the Python server script
val pythonScriptPath = "${projectDir}/src/main/kotlin/org/protogalaxy/fractalfathom/cli/modelInference/server.py"

// Clean and build the project
tasks.register("cleanBuild") {
    group = "build"
    description = "Clean and build the project"
    dependsOn("clean", "build")
}

// Run all tests
tasks.register("runTests") {
    group = "verification"
    description = "Run all tests"
    dependsOn("test")
}

dependencies {
    implementation(platform("com.squareup.okhttp3:okhttp-bom:$okhttpVersion"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("fr.inria.gforge.spoon:spoon-core:$spoonVersion")
    implementation("org.slf4j:slf4j-simple:$slf4jVersion")
    implementation("net.sourceforge.plantuml:plantuml:$plantUMLVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // Test dependencies
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))

    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.junit.jupiter:junit-jupiter")

}

application {
    mainClass.set("org.protogalaxy.fractalfathom.cli.MainKt")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

kotlin {
    jvmToolchain(21)
}


