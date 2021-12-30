package org.wit.domain

import org.joda.time.DateTime
import java.util.*

data class YogaDTO (var yogaId: Int,
                    var yogaName: String,
                    var yogaStart:DateTime,
                    var yogaEnd: DateTime,
                    var yogaDurationInMinutes: Int,
                    var userId: Int)