package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.local.quiz.QuizAnswer
import app.igormatos.botaprarodar.data.local.quiz.QuizBuilder
import app.igormatos.botaprarodar.data.local.quiz.QuizForm
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class QuizRepositoryTest {

    private val quizRepository = QuizRepository()

    @Test
    fun `when startQuiz and sendQuiz then should return QuizForm`() {
        val expectedQuizForm = QuizForm(listOf(QuizAnswer("", mockk())))
        val quizBuilderMock = mockk<QuizBuilder>()

        every { quizBuilderMock.build() } returns expectedQuizForm

        quizRepository.startQuiz(quizBuilderMock)
        val result = quizRepository.sendQuiz()

        assertEquals(expectedQuizForm, result)
    }

    @Test(expected = QuizNotStartedException::class)
    fun `when sendQuiz without startQuiz then should throw QuizNullException`() {
        quizRepository.sendQuiz()
    }
}