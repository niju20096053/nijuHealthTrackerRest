package org.wit

import org.wit.config.DBConfig
import org.wit.config.JavalinConfig

fun main() {

    DBConfig().getDbConnection()
    JavalinConfig().startJavalinService()

}