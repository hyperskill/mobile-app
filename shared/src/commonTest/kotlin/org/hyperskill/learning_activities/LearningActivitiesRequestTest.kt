package org.hyperskill.learning_activities

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.learning_activities.remote.model.LearningActivitiesRequest

class LearningActivitiesRequestTest {
    @Test
    fun `If number of ids greater than 100 then chunkedParameters should be greater than 1`() {
        val learningActivitiesRequest = LearningActivitiesRequest(
            activitiesIds = (1L..167L).toList(),
            types = emptySet()
        )

        assertEquals(2, learningActivitiesRequest.chunkedParameters.size)
        assertEquals(
            (1L..100L).toList().joinToString(","),
            learningActivitiesRequest.chunkedParameters[0]["ids"]
        )
        assertEquals(
            (101L..167L).toList().joinToString(","),
            learningActivitiesRequest.chunkedParameters[1]["ids"]
        )
    }
}