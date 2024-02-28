plugins {
//  id 'eclipse'
  id("idea")
  id("maven-publish")
  id("net.minecraftforge.gradle") version "[6.0,6.2)"
  kotlin("jvm") version "1.9.20"
}

val mod_id: String by ext
val mod_version: String by ext
val mod_group_id: String by ext
val minecraft_version: String by ext
val forge_version: String by ext

version = "${mod_version}-${minecraft_version}"
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

minecraft {
  mappings("official", "1.20.1")
  
  copyIdeResources.set(true)
  
  accessTransformers("src/main/resources/META-INF/accesstransformer.cfg")
  
  runs {
    configureEach {
      workingDirectory(project.file("run"))
      property("forge.logging.markers", "REGISTRIES")
      property("forge.logging.console.level", "debug")
    }
    
    create("client") {
      property("forge.enabledGameTestNamespaces", mod_id)
    }
    
    create("server") {
      property("forge.enabledGameTestNamespaces", mod_id)
      args("--nogui")
    }
    
    create("gameTestServer") {
      property("forge.enabledGameTestNamespaces", mod_id)
    }
    
    create("data") {
      args(
        "--mod", mod_id,
        "--all",
        "--output", file("src/generated/resources/").absolutePath,
        "--existing", file("src/main/resources/").absolutePath
      )
    }
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
  val minecraft = configurations.getByName("minecraft")
  minecraft("net.minecraftforge:forge:${minecraft_version}-${forge_version}")

//  implementation("net.neoforged:forge:${minecraft_version}-${forge_version}")
  implementation("thedarkcolour:kotlinforforge:4.10.0")
}

// Others

tasks.named<ProcessResources>("processResources") {
  val minecraft_version_range: String by ext
  val forge_version_range: String by ext
  val mod_loader: String by ext
  val loader_version_range: String by ext
  val mod_name: String by ext
  val mod_license: String by ext
  val mod_authors: String by ext
  val mod_description: String by ext
  
  
  val replaceProperties = mapOf(
    "minecraft_version" to minecraft_version,
    "minecraft_version_range" to minecraft_version_range,
    "forge_version" to forge_version,
    "forge_version_range" to forge_version_range,
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
  
  filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) {
    expand(replaceProperties + ("project" to project))
  }
}