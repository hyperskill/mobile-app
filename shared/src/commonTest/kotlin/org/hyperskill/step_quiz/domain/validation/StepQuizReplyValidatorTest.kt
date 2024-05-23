package org.hyperskill.step_quiz.domain.validation

import kotlin.test.Test
import kotlin.test.assertTrue
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step_quiz.domain.model.attempts.Dataset
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.domain.validation.StepQuizReplyValidator
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.step_quiz.domain.model.attempts.Pair as DatasetPair

class StepQuizReplyValidatorTest {
    private val validator = StepQuizReplyValidator(ResourceProviderStub())

    @Test
    fun `Matching reply validation should return error when ordering is null or empty`() {
        val dataset = Dataset(pairs = listOf(DatasetPair("1", "2"), DatasetPair("3", "4")))
        listOf(
            Reply(ordering = null),
            Reply(ordering = emptyList())
        ).forEach { reply ->
            val result = validator.validate(dataset, reply, BlockName.MATCHING)
            assertTrue(result is ReplyValidationResult.Error)
        }
    }

    @Test
    fun `Matching reply validation should return error when ordering size does not match options count`() {
        val dataset = Dataset(pairs = listOf(DatasetPair("1", "2"), DatasetPair("3", "4"), DatasetPair("5", "6")))
        listOf(
            Reply(ordering = listOf(1, 2)),
            Reply(ordering = listOf(1, null, 2))
        ).forEach { reply ->
            val result = validator.validate(dataset, reply, BlockName.MATCHING)

            assertTrue(result is ReplyValidationResult.Error)
        }
    }

    @Test
    fun `Matching reply validation should return success when ordering size matches options count`() {
        val reply = Reply(ordering = listOf(1, 2, 3))
        val dataset = Dataset(pairs = listOf(DatasetPair("1", "2"), DatasetPair("3", "4"), DatasetPair("5", "6")))

        val result = validator.validate(dataset, reply, BlockName.MATCHING)

        assertTrue(result is ReplyValidationResult.Success)
    }
}