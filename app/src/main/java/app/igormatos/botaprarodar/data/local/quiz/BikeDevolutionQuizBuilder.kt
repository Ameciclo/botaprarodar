package app.igormatos.botaprarodar.data.local.quiz

class BikeDevolutionQuizBuilder : QuizBuilder {
    override val answers = mutableListOf<QuizAnswer>()

    override val missingAnswers = mutableListOf(
        DevolutionQuizAnswerName.REASON,
        DevolutionQuizAnswerName.DESTINATION,
        DevolutionQuizAnswerName.SUFFERED_VIOLENCE,
        DevolutionQuizAnswerName.GIVE_RIDE
    )

    private fun setAnswer(answer: Any, answerName: DevolutionQuizAnswerName) {
        if (answers.isNotEmpty()) {
            answers.forEach {
                if (it.quizName.getQuizName() == answerName.getQuizName()) {
                    updateValue(it, answer)
                    return
                }
            }
            addNewAnswer(answer, answerName)
            return
        } else {
            addNewAnswer(answer, answerName)
        }
    }

    private fun updateValue(
        it: QuizAnswer,
        answer: Any
    ) {
        it.value = answer
    }

    private fun addNewAnswer(
        answer: Any,
        answerName: DevolutionQuizAnswerName
    ) {
        answers.add(QuizAnswer(answer, answerName))
        missingAnswers.remove(answerName)
    }

    fun withAnswer1(answer: Any): BikeDevolutionQuizBuilder {
        setAnswer(answer, DevolutionQuizAnswerName.REASON)
        return this
    }

    fun withAnswer2(answer: Any): BikeDevolutionQuizBuilder {
        setAnswer(answer, DevolutionQuizAnswerName.DESTINATION)
        return this
    }

    fun withAnswer3(answer: Any): BikeDevolutionQuizBuilder {
        setAnswer(answer, DevolutionQuizAnswerName.SUFFERED_VIOLENCE)
        return this
    }

    fun withAnswer4(answer: Any): BikeDevolutionQuizBuilder {
        setAnswer(answer, DevolutionQuizAnswerName.GIVE_RIDE)
        return this
    }
}
