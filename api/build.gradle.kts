apply(plugin = "kotlin")
apply(plugin = "kotlin-spring")
apply(plugin = "kotlin-kapt")
apply(plugin = "io.spring.dependency-management")

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }
}
