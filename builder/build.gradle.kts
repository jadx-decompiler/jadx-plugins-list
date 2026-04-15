import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.LineEnding
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
	kotlin("jvm") version "2.3.20"
	kotlin("kapt") version "2.3.20"
	kotlin("plugin.serialization") version "2.3.20"

	id("com.github.ben-manes.versions") version "0.53.0"
	id("se.patrikerdes.use-latest-versions") version "0.2.19"
	id("com.diffplug.spotless") version "8.4.0"

	application
}

repositories {
	mavenCentral()
	google()
}

dependencies {
	implementation("info.picocli:picocli:4.7.7")
	kapt("info.picocli:picocli-codegen:4.7.7")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
	implementation("com.google.code.gson:gson:2.13.2")
	implementation("io.github.oshai:kotlin-logging-jvm:8.0.01")

	implementation("io.github.skylot:jadx-plugins-tools:1.5.5")

	implementation("io.github.skylot:jadx-gui:1.5.5")
}

application {
	mainClass.set("jadx.plugins.list.ListBuilderCliKt")
}

kapt {
	arguments {
		arg("project", "${project.group}/${project.name}")
	}
}

val dist by tasks.registering(JavaExec::class) {
	group = "dist"
	classpath = sourceSets.main.get().runtimeClasspath
	mainClass = "jadx.plugins.list.ListBuilderCliKt"
	val outZip = rootProject.layout.buildDirectory.file("dist/jadx-plugins-list.zip")
	setArgsString("-m DELTA -i ${rootDir.absolutePath}/list -o ${outZip.get().asFile.absolutePath}")
	// resole warning about multiple logback.xml files, set full path
	jvmArgs = listOf("-Dlogback.configurationFile=file:${project.projectDir.absolutePath}/src/main/resources/logback.xml")
}

tasks.withType<DependencyUpdatesTask> {
	rejectVersionIf {
		isNonStable(candidate.version)
	}
}

fun isNonStable(version: String): Boolean {
	val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
	val regex = "^[0-9,.v-]+(-r)?$".toRegex()
	val isStable = stableKeyword || regex.matches(version)
	return isStable.not()
}

configure<SpotlessExtension> {
	val editorConfigPath = "$rootDir/.editorconfig"
	kotlin {
		ktlint().setEditorConfigPath(editorConfigPath)
		commonFormatOptions()
	}
	kotlinGradle {
		ktlint().setEditorConfigPath(editorConfigPath)
		commonFormatOptions()
	}
}

fun FormatExtension.commonFormatOptions() {
	lineEndings = LineEnding.UNIX
	encoding = Charsets.UTF_8
	trimTrailingWhitespace()
	endWithNewline()
}
