plugins {
  java
  id("org.springframework.boot") version "3.1.4"
  id("io.spring.dependency-management") version "1.1.3"
  // OpenAPI
  id("org.springdoc.openapi-gradle-plugin") version "1.7.0"
  id("org.openapi.generator") version "7.0.1"
  // Code quality
  id("org.sonarqube") version "4.4.1.3373"
  id("com.diffplug.spotless") version "6.22.0"
}

group = "com.trancamlong"

version = "0.0.1-SNAPSHOT"

java { sourceCompatibility = JavaVersion.VERSION_17 }

repositories { mavenCentral() }

apply(plugin = "com.diffplug.spotless")

spotless {
  kotlinGradle {
    target("**/*.gradle.kts")
    ktfmt()
  }
  java {
    targetExclude("**/generated/**/*.java")
    removeUnusedImports()
    importOrder()
    googleJavaFormat()
    endWithNewline()
  }
}

sonar {
  properties {
    property("sonar.host.url", "http://localhost:9001")
    property("sonar.projectKey", "com.trancamlong.geonote.auth")
    property("sonar.projectName", ".geonote-auth-svc")
    property("sonar.java.sources", "./src")
    property("sonar.dynamicAnalysis", "reuseReports")
    property("sonar.junit.reportsPath", "**/build/test-results/test/TEST-*.xml")
    property("sonar.test.exclusions", "**/test/**")
    property("sonar.coverage.exclusions", "**/model/*, **/constant/*, **/api/*")
  }
}

tasks.build { dependsOn("spotlessApply") }

dependencies {
  implementation("org.springframework.boot:spring-boot-starter")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-validation")

  // lombok compile and processor
  compileOnly("org.projectlombok:lombok")
  annotationProcessor("org.projectlombok:lombok")

  // OpenAPI
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

  implementation("org.slf4j:slf4j-jdk14:1.7.25")

  testImplementation("org.springframework.boot:spring-boot-starter-test")

  // Actuator, metric, logging and tracing
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-logging")

  // Open telemetry
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("io.micrometer:micrometer-tracing-bridge-otel")
  implementation("io.opentelemetry:opentelemetry-exporter-zipkin")
  implementation("com.github.loki4j:loki-logback-appender:1.4.2")
}

tasks.withType<Test> { useJUnitPlatform() }

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

buildscript {
  dependencies { classpath("io.spring.gradle:dependency-management-plugin:0.5.2.RELEASE") }
}

dependencyManagement {
  imports { mavenBom("org.springframework.cloud:spring-cloud-dependencies:2022.0.2") }
}
