package org.wit.domain

import org.joda.time.DateTime
import java.util.*

data class SleepDTO (var sleepTimeId: Int,
                     var sleepStart:DateTime,
                     var sleepEnd: DateTime,
                     var sleepTimeInMinutes: Int,
                     var userId: Int)