package app.igormatos.botaprarodar.data.local.quiz

data class QuizAnswer(val value: Any, private val quizName: QuizAnswerName)

interface QuizAnswerName{
    fun getQuizName(): String
}
enum class WithdrawQuizAnswerName(name: String): QuizAnswerName{
    RESPOSTA_1("resposta 1") {
        override fun getQuizName(): String = name
    },
    RESPOSTA_2("resposta 2") {
        override fun getQuizName(): String = name
    },
    RESPOSTA_3("resposta 3"){
        override fun getQuizName(): String = name
    },
    RESPOSTA_4("resposta 4"){
        override fun getQuizName(): String = name
    },
    RESPOSTA_5("resposta 5"){
        override fun getQuizName(): String = name
    }
}
enum class DevolutionQuizAnswerName(name: String): QuizAnswerName{
    RESPOSTA_1("resposta 1") {
        override fun getQuizName(): String = name
    },
    RESPOSTA_2("resposta 2") {
        override fun getQuizName(): String = name
    },
    RESPOSTA_3("resposta 3"){
        override fun getQuizName(): String = name
    },
    RESPOSTA_4("resposta 4"){
        override fun getQuizName(): String = name
    },
    RESPOSTA_5("resposta 5"){
        override fun getQuizName(): String = name
    },
    RESPOSTA_6("resposta 6"){
        override fun getQuizName(): String = name
    }
}