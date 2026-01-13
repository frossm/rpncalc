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
import java.security.MessageDigest

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

// Define what custom tasks are run with the clean task
tasks.clean {
   dependsOn("cleanMdBook")
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

// ShadowJar:  Create the fully executable shadowJar (FatJar)
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

   // Always generate the checksums after building the shadowJar
   finalizedBy(generateChecksums)
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
   description = "Builds, tests, and copies the shadowJar to the install directory"

   val installDirectory = "C:/Utils"

   // This install task depends on shadowJar
   val shadowTask = tasks.named<ShadowJar>("shadowJar")
   dependsOn(shadowTask)

   from(tasks.named("shadowJar"))
   into(installDirectory)

   // Force Gradle to ignore the cache and copy the file every time
   outputs.upToDateWhen { false }

   // Capture these here so they are available during execution and Gradle won't throw an error
   val progName = project.name
   val progVersion = project.version.toString()

   doLast {
      println("\n-------------------- RELEASE COMPLETE --------------------")
      println("Installed: $progName.jar -> $installDirectory")
      println("Version:   $progVersion")
      println("----------------------------------------------------------")
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

// Clean mdBook
val cleanMdBook by tasks.registering(Exec::class) {
   group = "documentation"
   description = "Runs mdbook clean from the 'mdbook' directory"

   // The root directory of the mdbook
   val baseDir = layout.projectDirectory.dir("mdbook")

   // The actual guide folder for the existence check
   val guideDir = baseDir.dir("RPNCalc-UserGuide")

   // Change into the parent mdbook folder
   workingDir = baseDir.asFile

   // Just run if the RPNCalc-UserGuide directory exists
   onlyIf {
      guideDir.asFile.exists()
   }

   // Execute the mdbook clean command
   commandLine("mdbook", "clean")
}

// Generate Checksums during builds automatically
val generateChecksums by tasks.registering {
   group = "distribution"
   description = "Generates MD5, SHA-1, and SHA-256 checksums for the shadow JAR"

   // Link this task to the shadowJar task by having that as a dependency
   val shadowJarTask = tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar")
   dependsOn(shadowJarTask)

   // Define Inputs/Outputs for Gradle's "Up-To-Date" check
   val archiveFile = shadowJarTask.get().archiveFile.get().asFile
   val outputDir = archiveFile.parentFile

   inputs.file(archiveFile)

   // List the specific files we will create as outputs
   outputs.files(
      outputDir.resolve("CHECKSUM.MD5"),
      outputDir.resolve("CHECKSUM.SHA1"),
      outputDir.resolve("CHECKSUM.SHA256")
   )

   doLast {
      val fileName = archiveFile.name

      mapOf(
         "MD5" to "CHECKSUM.MD5",
         "SHA-1" to "CHECKSUM.SHA1",
         "SHA-256" to "CHECKSUM.SHA256"
      ).forEach { (algorithm, outName) ->
         val digest = MessageDigest.getInstance(algorithm)

         // Efficiently read file and generate hex string
         val hash = archiveFile.readBytes().let { bytes ->
            digest.digest(bytes).joinToString("") { "%02x".format(it) }
         }

         val outFile = File(outputDir, outName)
         outFile.writeText("$hash  $fileName\n")
         println("Generated $outName in build/libs")
      }
   }
}

// Publish the RPNCalc UserGuide
val publishUserGuide by tasks.registering(Sync::class) {
   group = "documentation"
   description = "Mirrors User Guide using robust relative path resolution"

   // Resolve paths relative to this project's root
   val sourceDir = projectDir.resolve("mdbook/RPNCalc-UserGuide")
   val websiteRepo = projectDir.parentFile.resolve("frossm.github.io")
   val targetDir = websiteRepo.resolve("RPNCalc-UserGuide")

   // Build the mdBook before we do anything
   dependsOn("buildMdBook")

   // Configure the Gradle sync
   from(sourceDir)
   into(targetDir)

   // Ensure the mdbook RPNCalc-UserGuide source and destination directory exist
   doFirst {
      // Ensure the source directory exists
      if (!sourceDir.exists()) {
         throw GradleException("Could not find website repo at: ${websiteRepo.canonicalPath}")
      }

      // Ensure the parent of the future destination (frossm.github.io) exists
      if (!websiteRepo.exists()) {
         throw GradleException("Website repo not found at: ${websiteRepo.canonicalPath}")
      }
   }

   doLast {
      // Display the summary
      val count = targetDir.walkTopDown().filter { it.isFile }.count()
      println("\n-------------------- PUBLISH COMPLETE --------------------")
      println("Generated from: ${sourceDir.canonicalPath}")
      println("Published to:   ${targetDir.canonicalPath}")
      println("Sync complete:  $count files verified")
      println("----------------------------------------------------------")
   }
}

// Runs the mdBook build task to generate the RPNCalc-UserGuide book
tasks.register<Exec>("buildMdBook") {
   val mdbookRootDir = layout.projectDirectory.dir("mdbook")

   workingDir = mdbookRootDir.asFile
   commandLine("mdbook", "build")

   // Optimization: Only run if the markdown source files changed
   inputs.dir(mdbookRootDir.dir("src"))
   outputs.dir(mdbookRootDir.dir("RPNCalc-UserGuide"))
}