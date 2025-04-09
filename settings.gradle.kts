plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "backend"
include("domain")
include("config")
include("exception")
include("api:internal-api")
findProject(":api:internal-api")?.name = "internal-api"
include("auth")
