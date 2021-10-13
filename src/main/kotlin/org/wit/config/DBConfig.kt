package org.wit.config

import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.name

class DBConfig {

    private val logger = KotlinLogging.logger {}


    //NOTE: you need the ?sslmode=require otherwise you get an error complaining about the ssl certificate
    fun getDbConnection() :Database{
        logger.info{"Starting DB Connection..."}

        val dbConfig = Database.connect(
            "jdbc:postgresql://ec2-54-166-120-40.compute-1.amazonaws.com:5432/dfkie268785hbf?sslmode=require",
            driver = "org.postgresql.Driver",
            user = "qmjpmeyypashrr",
            password = "3790fb7d6dbad8565fc872051583e4569245d7aea0f28aa2ccf047711bc0a344")

        logger.info{"DbConfig name = " + dbConfig.name}
        logger.info{"DbConfig url = " + dbConfig.url}

        return dbConfig

    }
}