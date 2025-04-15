apply(plugin = "kotlin-jpa")
apply(plugin = "kotlin")
apply(plugin = "kotlin-spring")
apply(plugin = "kotlin-kapt")
apply(plugin = "io.spring.dependency-management")

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.MappedSuperclass")
}

dependencies {
    implementation(project(":exception"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
    implementation("io.github.openfeign.querydsl:querydsl-jpa:6.10.1")
    kapt("io.github.openfeign.querydsl:querydsl-apt:6.10.1:jpa")
    implementation(kotlin("stdlib-jdk8"))
}

kapt {
    correctErrorTypes = true
    arguments {
        arg("querydsl.entityAccessors", "true")
    }
}

sourceSets["main"].java.srcDir(layout.buildDirectory.dir("/generated/source/kapt/main"))

tasks {
    bootJar {
        enabled = false
    }
    jar {
        enabled = true
    }
}
