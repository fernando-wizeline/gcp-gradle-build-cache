/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("signing")
    id("com.gradle.plugin-publish") version "1.0.0"
    id("org.jetbrains.kotlin.jvm") version "1.7.10"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.google.cloud:google-cloud-storage:2.9.3")
}

pluginBundle {
    website = "https://github.com/fernando-wizeline/gcp-gradle-build-cache"
    vcsUrl = "https://github.com/fernando-wizeline/gcp-gradle-build-cache.git"
    tags = listOf("buildcache", "gcp", "caching")
}

gradlePlugin {
    plugins {
        create("gcpbuildcache") {
            id = "org.fernando-wizeline.gcpbuildcache"
            displayName = "Gradle GCP Build Cache Plugin"
            description = """
                - Warn when a user incorrectly configures GCP bucket to be used for the cache.
            """.trimIndent()
            implementationClass = "androidx.build.gradle.gcpbuildcache.GcpGradleBuildCachePlugin"
        }
    }
}

group = "org.fernando-wizeline.gcpbuildcache"
version = "1.0.0-beta01"

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use Kotlin Test Framework
            useKotlinTest()
        }

        // Create a new test suite
        val functionalTest by registering(JvmTestSuite::class) {
            // Use Kotlin Test Framework
            useKotlinTest()

            dependencies {
                // functionalTest test suite depends on the production code in tests
                implementation(project)
            }

            targets {
                all {
                    // This test suite should run after the built-in test suite has run its tests
                    testTask.configure { shouldRunAfter(test) }
                }
            }
        }
    }
}

gradlePlugin.testSourceSets(sourceSets["functionalTest"])

tasks.named<Task>("check") {
    // Include functionalTest as part of the check lifecycle
    dependsOn(testing.suites.named("functionalTest"))
}

tasks.withType<Sign>().configureEach {
    onlyIf { project.hasProperty("signing.keyId") }
}