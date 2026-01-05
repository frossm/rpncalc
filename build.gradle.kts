plugins {
   java
   application
   id("com.github.ben-manes.versions") version "0.53.0"  // Check for dependency updates
}

group = "org.fross"
version = "5.11.01"

repositories {
   mavenCentral()
}

dependencies {
   // 1. JCommander (The one we just found was missing!)
   implementation("com.beust:jcommander:1.82")

   // 2. JLine 3 and its Windows color support
   implementation("org.fusesource.jansi:jansi:2.4.2")
   implementation("org.jline:jline:3.30.6")
   implementation("org.jline:jline-terminal-jansi:3.30.6")

   // 3. Apache Commons Math
   implementation("org.apache.commons:commons-math3:3.6.1")

   // JUnit5 Testing
   testImplementation("org.junit.jupiter:junit-jupiter-api:6.1.0-M1")
   testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.1.0-M1")
   testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
   mainClass.set("org.fross.rpncalc.Main")
}


// Custom task to build the Fat Jar (Uber Jar)
tasks.register<Jar>("fatJar") {
   group = "build"
   description = "Assembles a fat jar containing all dependencies."

   dependsOn(tasks.test)

   archiveFileName.set("${project.name}.jar")

   manifest {
      attributes(
         "Main-Class" to application.mainClass.get(),
         "Implementation-Title" to project.name,
         "Implementation-Version" to project.version
      )
   }

   // Standard fat jar logic: include your code and all libraries
   from(sourceSets.main.get().output)
   from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

   duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}


// test:  Run the JUnit5 tests
tasks.test {
   useJUnitPlatform()

   // This makes the console output much more useful
   testLogging {
      events("passed", "skipped", "failed")
      showStandardStreams = true
      exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
   }
}


// Update the app.properties variables with the values specified in the gradle.properties file
tasks.processResources {
   // Gradle automatically finds "version" and "inceptionYear" from gradle.properties
   val tokens = mapOf(
      "project.version" to project.version.toString(),
      "project.inceptionYear" to project.property("inceptionYear").toString()
   )

   inputs.properties(tokens)

   filesMatching("**/app.properties") {
      filter(org.apache.tools.ant.filters.ReplaceTokens::class, "tokens" to tokens)
   }
}


// install:  This copies the fat jar to the C:\Utils directory
tasks.register<Copy>("install") {
   group = "distribution"
   description = "Builds, tests, and copies the fatJar to C:/Utils"

   dependsOn("fatJar")

   from(tasks.named("fatJar"))
   into("C:/Utils")

   // Capture values into local variables so the cache is happy
   val progName = project.name
   val progVersion = project.version.toString()

   doLast {
      println("\n--- RELEASE COMPLETE ---")
      println("Installed: $progName.jar -> C:/Utils")
      println("Version:   $progVersion")
      println("------------------------")
   }
}