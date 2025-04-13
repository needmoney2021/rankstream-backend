package com.rankstream.backend.internalapi

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication(scanBasePackages = ["com.rankstream.backend"])
class InternalApiApplication {

}

fun main(args: Array<String>) {
    SpringApplication.run(InternalApiApplication::class.java, *args)
}
