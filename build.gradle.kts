plugins {
	java
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	id("org.springdoc.openapi-gradle-plugin") version "1.7.0"
	id("org.openapi.generator") version "7.0.1"
}

group = "com.trancamlong"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// lombok compile and processor
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// OpenAPI
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

	implementation("org.slf4j:slf4j-log4j12:1.7.25")
	implementation("org.slf4j:slf4j-jdk14:1.7.25")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.openApiGenerate {
	generatorName.set("spring")
	inputSpec.set("$projectDir/src/main/resources/static/api.yaml")
	outputDir.set("$buildDir/generated/openapi")
	apiPackage.set("com.trancamlong.geonote.api")
	modelPackage.set("com.trancamlong.geonote.model")
	configOptions.set(
			mapOf(
					"dateLibrary" to "java8",
					"generateApis" to "true",
					"generateApiTests" to "false",
					"generateModels" to "true",
					"generateModelTests" to "false",
					"generateModelDocumentation" to "false",
					"generateSupportingFiles" to "false",
					"hideGenerationTimestamp" to "true",
					"interfaceOnly" to "true",
					"library" to "spring-boot",
					"serializableModel" to "true",
					"useBeanValidation" to "true",
					"useTags" to "true",
					"implicitHeaders" to "true",
					"openApiNullable" to "false",
					"oas3" to "true",
					"useSpringBoot3" to "true"))
}

sourceSets { main { java { srcDirs("$buildDir/generated/openapi/src/main/java") } } }

tasks.withType<JavaCompile> { dependsOn(tasks.openApiGenerate) }