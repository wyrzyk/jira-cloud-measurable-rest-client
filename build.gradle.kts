import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    kotlin("jvm").version("1.3.41")
    `java-library`
}

repositories {
    mavenCentral()
}

dependencies {
    api("javax.json:javax.json-api:[1.0.0,2.0.0)")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.glassfish:javax.json:1.1.4")
    implementation("org.apache.httpcomponents:httpclient:4.5.11")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.10.2")

    testCompile("org.assertj:assertj-core:3.15.0")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

tasks.getByName("test", Test::class).apply {
    useJUnitPlatform()
    filter {
        exclude("**/*IT.class")
    }
}

val testIntegration = task<Test>("testIntegration") {
    useJUnitPlatform()
    testLogging {
        events = setOf(TestLogEvent.PASSED, TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        exceptionFormat = TestExceptionFormat.FULL
    }
    filter {
        include("**/*IT.class")
    }
}

tasks["check"].dependsOn(testIntegration)