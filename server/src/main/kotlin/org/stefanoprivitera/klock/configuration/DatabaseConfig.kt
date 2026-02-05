package org.stefanoprivitera.klock.configuration

import io.ktor.server.config.*
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.core.annotation.Single
import org.postgresql.ds.PGSimpleDataSource


@Single
class DbSettings(
    val database: Database
) {
    constructor(applicationConfig: ApplicationConfig) : this(
        database = Database.connect(
            PGSimpleDataSource().apply {
                user = applicationConfig.property("database.user").getString()
                password = applicationConfig.property("database.password").getString()
                databaseName = applicationConfig.property("database.name").getString()
                portNumbers = intArrayOf(applicationConfig.property("database.port").getString().toInt())
            }
        )
    )
}