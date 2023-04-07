plugins {
  kotlin("jvm") version "1.8.10"
  java
  id("fabric-loom") version "1.1-SNAPSHOT"
}

val mod_version: String by project
val minecraft_version: String by project
val maven_group: String by project
val archives_base_name: String by project

version = "$mod_version+mc$minecraft_version"
group = maven_group

repositories {
  mavenCentral()
  maven("https://maven.shedaniel.me/")
  maven("https://maven.terraformersmc.com/releases/")
  maven("https://api.modrinth.com/maven")
}

fun DependencyHandler.includeModCompileOnly(dep: String, action: Action<ExternalModuleDependency>) {
  modCompileOnly(dep)
  include(dep, action)
}

dependencies {
  val loader_version: String by project
  val fabric_version: String by project
  val fabric_kotlin_version: String by project

  minecraft("com.mojang:minecraft:$minecraft_version")
  mappings(loom.officialMojangMappings())
  modImplementation("net.fabricmc:fabric-loader:$loader_version")
  modImplementation("net.fabricmc:fabric-language-kotlin:$fabric_kotlin_version")

  val cc_restitched_version: String by project
  val tr_energy_version: String by project

  modCompileOnly("maven.modrinth:cc-restitched:$cc_restitched_version")
  includeModCompileOnly("teamreborn:energy:$tr_energy_version") {
    exclude(group = "net.fabricmc.fabric-api")
  }
}

val targetJavaVersion = 17

tasks {
  processResources {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
      expand("version" to project.version)
    }
  }

  compileKotlin {
    kotlinOptions.jvmTarget = "17"
  }

  withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
      options.release.set(targetJavaVersion)
    }
  }

  jar {
    from("LICENSE") {
      rename { "${it}_${archives_base_name}" }
    }

    archiveBaseName.set(archives_base_name)
  }
}

java {
  val javaVersion = JavaVersion.toVersion(targetJavaVersion)
  if (JavaVersion.current() < javaVersion) {
    toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
  }
}
