package app.igormatos.botaprarodar.data.local.quiz

class BikeDevolutionQuizBuilder : QuizBuilder {
    override val answers = mutableListOf<QuizAnswer>()

    override val missingAnswers = mutableListOf(
        DevolutionQuizAnswerName.RESPOSTA_1,
        DevolutionQuizAnswerName.RESPOSTA_2,
        DevolutionQuizAnswerName.RESPOSTA_3,
        DevolutionQuizAnswerName.RESPOSTA_4,
        DevolutionQuizAnswerName.RESPOSTA_5
    )

    private fun setAnswer(answer: Any, answerName: DevolutionQuizAnswerName) {
        answers.add(QuizAnswer(answer, answerName))
        missingAnswers.remove(answerName)
    }

    fun withAnswer1(answer: Any): BikeDevolutionQuizBuilder {
        setAnswer(answer, DevolutionQuizAnswerName.RESPOSTA_1)
        return this
    }


    fun withAnswer2(answer: Any): BikeDevolutionQuizBuilder {
        setAnswer(answer, DevolutionQuizAnswerName.RESPOSTA_2)

        return this
    }

    fun withAnswer3(answer: Any): BikeDevolutionQuizBuilder {
        setAnswer(answer, DevolutionQuizAnswerName.RESPOSTA_3)

        return this
    }

    fun withAnswer4(answer: Any): BikeDevolutionQuizBuilder {
        setAnswer(answer, DevolutionQuizAnswerName.RESPOSTA_4)

        return this
    }

    fun withAnswer5(answer: Any): BikeDevolutionQuizBuilder {
        setAnswer(answer, DevolutionQuizAnswerName.RESPOSTA_5)
        return this
    }

    fun withAnswer6(answer: Any): BikeDevolutionQuizBuilder {
        setAnswer(answer, DevolutionQuizAnswerName.RESPOSTA_6)
        return this
    }
}
