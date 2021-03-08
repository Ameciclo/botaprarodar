package app.igormatos.botaprarodar.data.local.quiz

import java.lang.Exception

interface QuizBuilder {
    val answers: List<QuizAnswer>
    val missingAnswers: List<QuizAnswerName>

    @Throws(MissingAnswerException::class)
    fun build(): QuizForm {
        if (missingAnswers.isNotEmpty())
            throw MissingAnswerException(missingAnswers.map { it.getQuizName() })

        return QuizForm(
            answers
        )
    }

}

class WithDrawBikeQuizBuilder : QuizBuilder {

    override val answers = mutableListOf<QuizAnswer>()

    override val missingAnswers = mutableListOf(
        WithdrawQuizQuizAnswerName.RESPOSTA_1,
        WithdrawQuizQuizAnswerName.RESPOSTA_2,
        WithdrawQuizQuizAnswerName.RESPOSTA_3,
        WithdrawQuizQuizAnswerName.RESPOSTA_4,
        WithdrawQuizQuizAnswerName.RESPOSTA_5
    )

    private fun setAnswer(answer: Any, answerName: WithdrawQuizQuizAnswerName) {
        answers.add(QuizAnswer(answer, answerName))
        missingAnswers.remove(answerName)
    }


    fun withAnswer1(answer: Any): WithDrawBikeQuizBuilder {
        setAnswer(answer, WithdrawQuizQuizAnswerName.RESPOSTA_1)
        return this
    }


    fun withAnswer2(answer: Any): WithDrawBikeQuizBuilder {
        setAnswer(answer, WithdrawQuizQuizAnswerName.RESPOSTA_2)

        return this
    }

    fun withAnswer3(answer: Any): WithDrawBikeQuizBuilder {
        setAnswer(answer, WithdrawQuizQuizAnswerName.RESPOSTA_3)

        return this
    }

    fun withAnswer4(answer: Any): WithDrawBikeQuizBuilder {
        setAnswer(answer, WithdrawQuizQuizAnswerName.RESPOSTA_4)

        return this
    }

    fun withAnswer5(answer: Any): WithDrawBikeQuizBuilder {
        setAnswer(answer, WithdrawQuizQuizAnswerName.RESPOSTA_5)
        return this
    }

}

class MissingAnswerException(val missingAnswers: List<String>) : Exception()

class ReturnBikeQuizBuilder : QuizBuilder {

    override val answers = mutableListOf<QuizAnswer>()

    override val missingAnswers = mutableListOf(
        ReturnQuizQuizAnswerName.RESPOSTA_1,
        ReturnQuizQuizAnswerName.RESPOSTA_2,
        ReturnQuizQuizAnswerName.RESPOSTA_3,
        ReturnQuizQuizAnswerName.RESPOSTA_4,
        ReturnQuizQuizAnswerName.RESPOSTA_5
    )

    private fun setAnswer(answer: Any, answerName: ReturnQuizQuizAnswerName) {
        answers.add(QuizAnswer(answer, answerName))
        missingAnswers.remove(answerName)
    }


    fun withAnswer1(answer: Any): ReturnBikeQuizBuilder {
        setAnswer(answer, ReturnQuizQuizAnswerName.RESPOSTA_1)
        return this
    }


    fun withAnswer2(answer: Any): ReturnBikeQuizBuilder {
        setAnswer(answer, ReturnQuizQuizAnswerName.RESPOSTA_2)

        return this
    }

    fun withAnswer3(answer: Any): ReturnBikeQuizBuilder {
        setAnswer(answer, ReturnQuizQuizAnswerName.RESPOSTA_3)

        return this
    }

    fun withAnswer4(answer: Any): ReturnBikeQuizBuilder {
        setAnswer(answer, ReturnQuizQuizAnswerName.RESPOSTA_4)

        return this
    }

    fun withAnswer5(answer: Any): ReturnBikeQuizBuilder {
        setAnswer(answer, ReturnQuizQuizAnswerName.RESPOSTA_5)
        return this
    }

    fun withAnswer6(answer: Any): ReturnBikeQuizBuilder {
        setAnswer(answer, ReturnQuizQuizAnswerName.RESPOSTA_6)
        return this
    }
}
