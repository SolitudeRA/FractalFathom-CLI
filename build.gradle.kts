plugins {
    id("application")
    kotlin("jvm") version "2.0.21"
    id("io.github.mictaege.spoon-gradle-plugin") version "2024.2"
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

// Run the application
tasks.register<JavaExec>("runApp") {
    group = "application"
    description = "Run the application"
    mainClass.set(application.mainClass.get())
    classpath = sourceSets.main.get().runtimeClasspath
    standardInput = System.`in`
    dependsOn("startPythonServer")
    finalizedBy("stopPythonServer")
}

// Task to start the Python server
tasks.register<Exec>("startPythonServer") {
    group = "application"
    description = "Starts the Python server script"
    commandLine("python", pythonScriptPath)
    isIgnoreExitValue = true
    doLast {
        println("Python server script started.")
    }
}

// Task to stop the Python server
tasks.register("stopPythonServer") {
    group = "application"
    description = "Stops the Python server script if it is running"
    doLast {
        try {
            exec {
                commandLine("pkill", "-f", "python $pythonScriptPath")
            }
            println("Python server script stopped.")
        } catch (e: Exception) {
            println("No running Python server process found.")
        }
    }
}

// Path to the Python server script
val pythonScriptPath = "${projectDir}/src/main/kotlin/org/protogalaxy/fractalfathom/cli/modelInference/server.py"

// Create a fat jar
tasks.register<Jar>("fatJar") {
    group = "build"
    description = "Assembles a jar archive containing the main classes and dependencies."
    archiveClassifier.set("all")

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = application.mainClass.get()
    }
    from(sourceSets.main.get().output)

    // Add dependencies and exclude signature files
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    }) {
        exclude("META-INF/*.SF", "META-INF/*.RSA", "META-INF/*.DSA")
    }
}

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


