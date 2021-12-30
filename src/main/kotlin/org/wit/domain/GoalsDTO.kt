package org.wit.domain

import org.joda.time.DateTime
import java.util.*

data class GoalsDTO (var goalId: Int,
                     var steps: Int,
                     var heartPoints:Int,
                     var calories: Int,
                     var distance: Int,
                     var water: Int,
                     var sleep: Int,
                     var userId: Int)