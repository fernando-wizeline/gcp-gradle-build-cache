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

/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package androidx.build.gradle.gcpbuildcache

import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import kotlin.test.Test

/**
 * A simple functional test for the 'androidx.build.gradle.gcpbuildcache.GcpBuildCache' plugin.
 */
class GcpGradleBuildCachePluginFunctionalTest {
    @get:Rule val tempFolder = TemporaryFolder()

    private fun getProjectDir() = tempFolder.root
    private fun getBuildFile() = getProjectDir().resolve("build.gradle.kts")
    private fun getSettingsFile() = getProjectDir().resolve("settings.gradle.kts")

    @Test
    fun `can run tasks task`() {
        // Setup the test build
        getSettingsFile().writeText("""
            plugins {
                id("io.github.fernando-wizeline.gcpbuildcache")
            }
            buildCache {
                remote(androidx.build.gradle.gcpbuildcache.GcpBuildCache::class) {
                    projectId = "foo"
                    bucketName = "bar"
                }
            }
        """.trimIndent())
        getBuildFile().writeText("")

        // Run the build
        val runner = GradleRunner.create()
        runner.forwardOutput()
        runner.withPluginClasspath()
        runner.withArguments("tasks")
        runner.withProjectDir(getProjectDir())
        runner.build();
    }
}
