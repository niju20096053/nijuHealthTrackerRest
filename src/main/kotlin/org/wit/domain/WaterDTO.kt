package org.wit.domain

import org.joda.time.DateTime
import java.util.*

data class WaterDTO (var waterId: Int,
                     var dateTimeOfDrinking: DateTime,
                     var quantity : Int,
                     var userId: Int)