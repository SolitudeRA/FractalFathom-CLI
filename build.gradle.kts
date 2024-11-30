plugins {
    id("application")
    id("jacoco")
    kotlin("jvm") version "2.0.21"
    id("com.github.johnrengelman.shadow") version "8.1.1"
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
val neo4jVersion = "5.25.1"
val junitVersion = "5.11.3"
val mockkVersion = "1.13.13"

jacoco {
    toolVersion = "0.8.12"
}

// Separate source set for annotations
sourceSets {
    create("annotations") {
        java.srcDir("src/annotations/java")
    }
}

// Build task to create the annotations JAR
tasks.register<Jar>("buildLib") {
    archiveClassifier.set("annotations")
    from(sourceSets["annotations"].output)
    destinationDirectory.set(layout.buildDirectory.dir("libs"))
}

tasks.shadowJar {
    isZip64 = true

    manifest {
        attributes["Main-Class"] = "org.protogalaxy.fractalfathom.cli.MainKt"
    }

    // Exclude signature files to prevent SecurityException
    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
}

tasks.named<CreateStartScripts>("startScripts") {
    dependsOn(tasks.shadowJar)
    classpath = files(tasks.shadowJar.get().archiveFile)
}

tasks.named<Zip>("distZip") {
    dependsOn(tasks.shadowJar)
}

tasks.named<Tar>("distTar") {
    dependsOn(tasks.shadowJar)
}

dependencies {
    implementation(platform("com.squareup.okhttp3:okhttp-bom:$okhttpVersion"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("fr.inria.gforge.spoon:spoon-core:$spoonVersion")
    implementation("net.sourceforge.plantuml:plantuml:$plantUMLVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.neo4j:neo4j:$neo4jVersion")

    // Test dependencies
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))

    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.junit.jupiter:junit-jupiter")

}

application {
    mainClass.set("org.protogalaxy.fractalfathom.cli.MainKt")
    distributions {
        main {
            contents {
                from(tasks.shadowJar) {
                    into("lib")
                }
                // Exclude the default JAR file
                exclude("lib/${tasks.jar.get().archiveFileName.get()}")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

kotlin {
    jvmToolchain(21)
}


