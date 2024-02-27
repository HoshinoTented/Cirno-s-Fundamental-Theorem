plugins {
//  id 'eclipse'
  id("idea")
  id("maven-publish")
  id("net.neoforged.gradle.userdev") version "7.0.80"
  kotlin("jvm") version "1.9.20"
}

val mod_id: String by ext
val mod_version: String by ext
val mod_group_id: String by ext
val minecraft_version: String by ext
val neo_version: String by ext

version = mod_version
group = mod_group_id

// Compilation

base {
  archivesName.set(mod_id)
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))

sourceSets.main {
  resources {
    srcDir("src/generated/resources")
  }
}

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
}

// Minecraft

runs {
  configureEach {
    // Recommended logging data for a userdev environment
    // The markers can be added/remove as needed separated by commas.
    // "SCAN": For mods scan.
    // "REGISTRIES": For firing of registry events.
    // "REGISTRYDUMP": For getting the contents of all registries.
    systemProperty("forge.logging.markers", "REGISTRIES")
    
    // Recommended logging level for the console
    // You can set various levels here.
    // Please read: https://stackoverflow.com/questions/2031163/when-to-use-the-different-log-levels
    systemProperty("forge.logging.console.level", "debug")
    
    modSource(project.sourceSets.main.get())
  }
  
  create("client") {
    systemProperty("forge.enabledGameTestNamespaces", mod_id)
  }
  
  create("server") {
    systemProperty("forge.enabledGameTestNamespaces", mod_id)
    programArgument("--nogui")
  }
  
  // This run config launches GameTestServer and runs all registered gametests, then exits.
  // By default, the server will crash when no gametests are provided.
  // The gametest system is also enabled by default for other run configs under the /test command.
  create("gameTestServer") {
    systemProperty("forge.enabledGameTestNamespaces", mod_id)
  }
  
  create("data") {
    // example of overriding the workingDirectory set in configureEach above, uncomment if you want to use it
    // workingDirectory project.file('run-data')
    
    // Specify the modid for data generation, where to output the resulting resource, and where to look for existing resources.
    programArguments.addAll(
      "--mod", mod_id,
      "--all",
      "--output", file("src/generated/resources/").absolutePath,
      "--existing", file("src/main/resources/").absolutePath
    )
  }
}

// Dependency

repositories {
  maven {
    name = "Kotlin for Forge"
    url = uri("https://thedarkcolour.github.io/KotlinForForge/")
  }
}

dependencies {
  implementation("net.neoforged:neoforge:${neo_version}")
  implementation("thedarkcolour:kotlinforforge-neoforge:4.10.0")
}

// Others

// This block of code expands all declared replace properties in the specified resource targets.
// A missing property will result in an error. Properties are expanded using ${} Groovy notation.
// When "copyIdeResources" is enabled, this will also run before the game launches in IDE environments.
// See https://docs.gradle.org/current/dsl/org.gradle.language.jvm.tasks.ProcessResources.html
tasks.named<ProcessResources>("processResources") {
  val minecraft_version_range: String by ext
  val neo_version_range: String by ext
  val mod_loader: String by ext
  val loader_version_range: String by ext
  val mod_name: String by ext
  val mod_license: String by ext
  val mod_authors: String by ext
  val mod_description: String by ext
  
  
  val replaceProperties = mapOf(
    "minecraft_version" to minecraft_version,
    "minecraft_version_range" to minecraft_version_range,
    "neo_version" to neo_version,
    "neo_version_range" to neo_version_range,
    "mod_loader" to mod_loader,
    "loader_version_range" to loader_version_range,
    "mod_id" to mod_id,
    "mod_name" to mod_name,
    "mod_license" to mod_license,
    "mod_version" to mod_version,
    "mod_authors" to mod_authors,
    "mod_description" to mod_description,
  )
  
  inputs.properties(replaceProperties)
  
  filesMatching(listOf("META-INF/mods.toml")) {
    expand(replaceProperties + ("project" to project))
  }
}