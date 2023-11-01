import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
	kotlin("plugin.serialization") version "1.9.10"
}

group = "podongdaeng"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-web")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa") // TODO: not yet used
//	runtimeOnly("com.h2database:h2")
//	implementation("org.springframework.boot:spring-boot-starter-mustache")
//	runtimeOnly("org.springframework.boot:spring-boot-devtools")
	// https://mvnrepository.com/artifact/org.seleniumhq.selenium/selenium-java
	implementation("org.seleniumhq.selenium:selenium-java:4.14.1")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
