package org.wit.domain

import java.sql.Date
import java.time.LocalDate


data class UserDTO(
    var id: Int,
    var firstName:String,
    var lastName:String,
    var gender:String,
    var email:String,
    var mobile:String,
    var age: Int,
    var address:String,
    var height:Double,
    var weight:Double,
    var userName:String,
    var password:String)