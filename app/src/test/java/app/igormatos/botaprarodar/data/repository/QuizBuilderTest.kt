package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.local.quiz.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test


class QuizBuilderTest {

    @Test
    fun `when build then should return expected answer list`() {
        val expectedAnswers = listOf(
            1,
            "segunda resposta",
            1.1,
            4,
            5
        )
        val quizBuilder = WithDrawBikeQuizBuilder().apply {
            withAnswer1(expectedAnswers[0])
            withAnswer2(expectedAnswers[1])
            withAnswer3(expectedAnswers[2])
            withAnswer4(expectedAnswers[3])
            withAnswer5(expectedAnswers[4])
        }

        val result = quizBuilder.build().answerList.map { it.value }

        assertEquals(expectedAnswers, result)
    }

    @Test
    fun `when build then should return expected ReturnBikeQuizBuilder`() {
        val expectedAnswers = listOf(
            1,
            "segunda resposta",
            1.1,
            4,
            5,
            6
        )

        val quizBuilder = ReturnBikeQuizBuilder().apply {
            withAnswer1(expectedAnswers[0])
            withAnswer2(expectedAnswers[1])
            withAnswer3(expectedAnswers[2])
            withAnswer4(expectedAnswers[3])
            withAnswer5(expectedAnswers[4])
            withAnswer6(expectedAnswers[5])
        }

        val result = quizBuilder.build().answerList.map { it.value }

        assertEquals(expectedAnswers, result)
    }

    @Test
    fun `when missing a quiz answer then build should throw MissingAnswerException with expected quizAnswerNames`() {

        val quizAnswerNames = listOf(
            DevolutionQuizAnswerName.RESPOSTA_2.getQuizName(),
            DevolutionQuizAnswerName.RESPOSTA_3.getQuizName(),
            DevolutionQuizAnswerName.RESPOSTA_4.getQuizName(),
            DevolutionQuizAnswerName.RESPOSTA_5.getQuizName(),
        )

        val quizBuilder = ReturnBikeQuizBuilder().apply {
            withAnswer1("")
        }

        val exception: MissingAnswerException = assertThrows(MissingAnswerException::class.java) {
            quizBuilder.build()
        }

        assertEquals(exception.missingAnswers, quizAnswerNames)
    }
}
