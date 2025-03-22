import com.diffplug.gradle.spotless.FormatExtension
import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.spotless.LineEnding
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
	kotlin("jvm") version "2.1.20"
	kotlin("kapt") version "2.1.20"
	kotlin("plugin.serialization") version "2.1.20"

	id("com.github.ben-manes.versions") version "0.52.0"
	id("se.patrikerdes.use-latest-versions") version "0.2.18"
	id("com.diffplug.spotless") version "7.0.3"

	application
}

repositories {
	mavenCentral()
	google()
	maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
	implementation("info.picocli:picocli:4.7.6")
	kapt("info.picocli:picocli-codegen:4.7.6")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
	implementation("com.google.code.gson:gson:2.13.0")
	implementation("io.github.oshai:kotlin-logging-jvm:7.0.6")
	implementation("ch.qos.logback:logback-classic:1.5.18")

	implementation("io.github.skylot:jadx-plugins-tools:1.5.2-SNAPSHOT") {
		isChanging = true
	}
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(11))
	}
}

application {
	mainClass.set("jadx.plugins.list.ListBuilderCLI")
}

kapt {
	arguments {
		arg("project", "${project.group}/${project.name}")
	}
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
