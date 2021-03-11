package app.igormatos.botaprarodar.data.repository

import app.igormatos.botaprarodar.data.local.quiz.QuizBuilder
import app.igormatos.botaprarodar.data.local.quiz.QuizForm
import kotlin.jvm.Throws

class QuizRepository {
    private var _quizBuilder: QuizBuilder? = null

    fun startQuiz(quizBuilder: QuizBuilder) {
        _quizBuilder = quizBuilder
    }

    @Throws(QuizNotStartedException::class)
    fun sendQuiz(): QuizForm {
        return _quizBuilder?.build() ?: throw QuizNotStartedException()
    }
}

class QuizNotStartedException: Exception()
