plugins {
   java
   application
   id("com.github.ben-manes.versions") version "0.53.0"  // Check for dependency updates
}

group = "org.fross"

repositories {
   mavenCentral()
}

dependencies {
   // https://mvnrepository.com/artifact/com.beust/jcommander
   implementation("com.beust:jcommander:1.82")

   // https://mvnrepository.com/artifact/org.fusesource.jansi/jansi
   implementation("org.fusesource.jansi:jansi:2.4.2")

   // https://mvnrepository.com/artifact/org.jline/jline-reader
   implementation("org.jline:jline:3.30.6")

   // https://mvnrepository.com/artifact/org.jline/jline-terminal-jansi
   implementation("org.jline:jline-terminal-jansi:3.30.6")

   // https://mvnrepository.com/artifact/org.apache.commons/commons-math3
   implementation("org.apache.commons:commons-math3:3.6.1")

   // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
   testImplementation("org.junit.jupiter:junit-jupiter-api:6.1.0-M1")

   // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
   testImplementation("org.junit.jupiter:junit-jupiter-engine:6.1.0-M1")

   // https://mvnrepository.com/artifact/org.junit.platform/junit-platform-launcher
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