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
            4
        )

        val quizBuilder = BikeDevolutionQuizBuilder().apply {
            withAnswer1(expectedAnswers[0])
            withAnswer2(expectedAnswers[1])
            withAnswer3(expectedAnswers[2])
            withAnswer4(expectedAnswers[3])
        }

        val result = quizBuilder.build().answerList.map { it.value }

        assertEquals(expectedAnswers, result)
    }

    @Test
    fun `when missing a quiz answer then build should throw MissingAnswerException with expected quizAnswerNames`() {

        val missingAnswersName = listOf(
            DevolutionQuizAnswerName.DESTINATION.getQuizName(),
            DevolutionQuizAnswerName.SUFFERED_VIOLENCE.getQuizName(),
            DevolutionQuizAnswerName.GIVE_RIDE.getQuizName(),
        )

        val quizBuilder = BikeDevolutionQuizBuilder().apply {
            withAnswer1("")
        }

        val exception: MissingAnswerException = assertThrows(MissingAnswerException::class.java) {
            quizBuilder.build()
        }

        assertEquals(exception.missingAnswers, missingAnswersName)
    }

    @Test
    fun `when fill a quiz answer and after change it the answer list should contains just one register to that answer name`() {
        val quizBuilder = BikeDevolutionQuizBuilder().apply {
            withAnswer1("")
            withAnswer2("resposta 2")
            withAnswer3("resposta 3")
            withAnswer4("resposta 4")
        }

        quizBuilder.apply {
            withAnswer1("Nova resposta")
        }

        val expectedAnswers = listOf(
            "Nova resposta",
            "resposta 2",
            "resposta 3",
            "resposta 4"
        )

        val result = quizBuilder.build().answerList.map { it.value }

        assertEquals(expectedAnswers, result)
    }
}
