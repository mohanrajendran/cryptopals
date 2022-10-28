plugins {
    kotlin("jvm") version "1.7.20"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.kotest:kotest-runner-junit5:5.5.2")
    testImplementation("io.kotest:kotest-framework-datatest:5.5.2")
    testImplementation("io.kotest:kotest-assertions-core:5.5.2")
    testImplementation("io.kotest:kotest-property:5.5.2")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xuse-experimental=kotlin.ExperimentalUnsignedTypes",
            "-XXLanguage:+InlineClasses"
        )
    }
}
