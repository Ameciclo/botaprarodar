package app.igormatos.botaprarodar.utils

import app.igormatos.botaprarodar.data.local.quiz.BikeDevolutionQuizBuilder

val bikeDevolutionQuizBuilder = BikeDevolutionQuizBuilder().apply {
    withAnswer1("resposta 1")
    withAnswer2("resposta 2")
    withAnswer3("resposta 3")
    withAnswer4("resposta 4")
}