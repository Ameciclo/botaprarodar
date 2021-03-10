package app.igormatos.botaprarodar.data.local.quiz

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
