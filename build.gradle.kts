/* ------------------------------------------------------------------------------
 * RPNCalc
 *
 * RPNCalc is is an easy to use console based RPN calculator
 *
 *  Copyright (c) 2011-2026 Michael Fross
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * ------------------------------------------------------------------------------*/
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
   java
   application
   id("com.github.ben-manes.versions") version "0.53.0"
   id("com.gradleup.shadow") version "9.3.1"
}

group = "org.fross"
val javaVersion = 21

application {
   mainClass.set("org.fross.rpncalc.Main")
}

// Tell Gradle to output the right java version's bytecode
tasks.withType<JavaCompile> {
   options.release.set(javaVersion)
}

// Ensure we use JUnit
tasks.withType<Test> {
   useJUnitPlatform()
}

repositories {
   mavenCentral()
}

dependencies {
   implementation("com.beust:jcommander:1.82")
   implementation("org.apache.commons:commons-math3:3.6.1")

   // --- JLine Terminal Access ---
   implementation("org.jline:jline-reader:3.30.6")
   implementation("org.jline:jline-terminal:3.30.6")
   implementation("org.jline:jline-native:3.30.6")          // Native support for Linux/Mac/Win
   implementation("org.jline:jline-terminal-ffm:3.30.6")

   // --- JUnit Testing ---
   testImplementation("org.junit.jupiter:junit-jupiter-api:6.1.0-M1")
   testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.1.0-M1")
   testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.1.0-M1")
}

// Let Gradle know that to not try and cached the Versions plugin and prevent warnings
// Hopefully this won't be needed with future versions of the plugin
tasks.named<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask>("dependencyUpdates") {
   notCompatibleWithConfigurationCache("The versions plugin is not yet compatible with the configuration cache.")
}

// Update the Java resources with project version and inception date
tasks.processResources {
   // Before you process resources, update the snapcraft version
   dependsOn("updateSnapVersion")

   val tokens = mapOf(
      "project.version" to project.version.toString(),

      // If it can't get the property, default to 2011
      "project.inceptionYear" to (project.findProperty("inceptionYear")?.toString() ?: "2011")
   )

   inputs.properties(tokens)
   filesMatching("**/app.properties") {
      filter(org.apache.tools.ant.filters.ReplaceTokens::class, "tokens" to tokens)
   }
}

// Create the fully executable shadowJar (FatJar)
tasks.named<ShadowJar>("shadowJar") {
   group = "build"
   description = "Creates a 'Fat Jar' file containing all dependencies"
   dependsOn("test")
   archiveFileName.set("${project.name}.jar")

   // Merge ServiceLoader files so JLine can find its Terminal providers
   mergeServiceFiles()

   minimize {
      // Don't let the "minifier" remove JLine classes used via reflection
      exclude(dependency("org.jline:.*:.*"))
   }

   // Standard excludes to keep the JAR clean
   exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
   exclude("META-INF/LICENSE*", "META-INF/NOTICE*", "META-INF/maven/**")
}

// Execute JUnit Tests
tasks.test {
   useJUnitPlatform()

   // This makes the console output much more useful
   testLogging {
      events("passed", "skipped", "failed")
      showStandardStreams = true
      exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
   }
}

// install:  This copies the fat jar to the C:\Utils directory after building and testing it
tasks.register<Copy>("install") {
   group = "distribution"
   description = "Builds, tests, and copies the shadowJar to C:/Utils"
   dependsOn("shadowJar")

   from(tasks.named("shadowJar"))
   into("C:/Utils")

   // Force Gradle to ignore the cache and copy the file every time
   outputs.upToDateWhen { false }

   // Capture these here so they are available during execution and Gradle won't throw an error
   val progName = project.name
   val progVersion = project.version.toString()

   doLast {
      println("\n---------- RELEASE COMPLETE ----------")
      println("Installed: $progName.jar -> C:/Utils")
      println("Version:   $progVersion")
      println("--------------------------------------")
   }
}

// updateSnapVersion:  Update application version in snapcraft.yaml
tasks.register("updateSnapVersion") {
   group = "versioning"
   description = "Updates the version in snapcraft.yaml to match the project version"

   // Capture the values into local variables OUTSIDE doLast to avoid Gradle warnings
   val newVersion = project.version.toString()
   val snapFileLocation = layout.projectDirectory.file("snap/snapcraft.yaml").asFile

   doLast {
      if (!snapFileLocation.exists()) {
         throw GradleException("FATAL: snapcraft.yaml not found at ${snapFileLocation.path}. Build aborted.")
      }

      println("Updating Snapcraft file: ${snapFileLocation.path}")
      val content = snapFileLocation.readText()

      if (!content.contains(Regex("""(?m)^version:"""))) {
         throw GradleException("FATAL: 'version:' key not found in snapcraft.yaml. Check file formatting.")
      }

      val updatedContent = content.replace(
         Regex("""(?m)^version:\s*['"]?.*['"]?"""), "version: '$newVersion'"
      )

      snapFileLocation.writeText(updatedContent)
      println("Successfully updated snapcraft.yaml to version: $newVersion")
   }
}