package org.wit.domain

import org.joda.time.DateTime
import java.util.*

data class HealthConditionDTO (var healthConditionId: Int,
                               var pulseRate: Int,
                               var bloodPressure:String,
                               var cholesterol: Int,
                               var bloodSugar: Int,
                               var bmi: Double,
                               var userId: Int)