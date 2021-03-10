package app.igormatos.botaprarodar.data.local.quiz

class WithDrawBikeQuizBuilder : QuizBuilder {

    override val answers = mutableListOf<QuizAnswer>()

    override val missingAnswers = mutableListOf(
        WithdrawQuizAnswerName.RESPOSTA_1,
        WithdrawQuizAnswerName.RESPOSTA_2,
        WithdrawQuizAnswerName.RESPOSTA_3,
        WithdrawQuizAnswerName.RESPOSTA_4,
        WithdrawQuizAnswerName.RESPOSTA_5
    )

    private fun setAnswer(answer: Any, answerName: WithdrawQuizAnswerName) {
        answers.add(QuizAnswer(answer, answerName))
        missingAnswers.remove(answerName)
    }


    fun withAnswer1(answer: Any): WithDrawBikeQuizBuilder {
        setAnswer(answer, WithdrawQuizAnswerName.RESPOSTA_1)
        return this
    }


    fun withAnswer2(answer: Any): WithDrawBikeQuizBuilder {
        setAnswer(answer, WithdrawQuizAnswerName.RESPOSTA_2)

        return this
    }

    fun withAnswer3(answer: Any): WithDrawBikeQuizBuilder {
        setAnswer(answer, WithdrawQuizAnswerName.RESPOSTA_3)

        return this
    }

    fun withAnswer4(answer: Any): WithDrawBikeQuizBuilder {
        setAnswer(answer, WithdrawQuizAnswerName.RESPOSTA_4)

        return this
    }

    fun withAnswer5(answer: Any): WithDrawBikeQuizBuilder {
        setAnswer(answer, WithdrawQuizAnswerName.RESPOSTA_5)
        return this
    }
}