apply(plugin = "kotlin")
apply(plugin = "kotlin-spring")
apply(plugin = "kotlin-kapt")
apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation(project(":domain"))
    implementation(project(":exception"))
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.auth0:java-jwt:4.5.0")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }
}
