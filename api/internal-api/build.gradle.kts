apply(plugin = "kotlin")
apply(plugin = "kotlin-spring")
apply(plugin = "kotlin-kapt")
apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

dependencies {
    implementation(project(":config"))
    implementation(project(":domain"))
    implementation(project(":auth"))
    implementation("org.springframework.security:spring-security-web")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

springBoot {
    mainClass.set("com.rankstream.backend.internalapi.InternalApiApplication")
}
