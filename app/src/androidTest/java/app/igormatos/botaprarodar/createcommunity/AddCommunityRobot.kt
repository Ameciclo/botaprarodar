package app.igormatos.botaprarodar.createcommunity

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun addCommunity(executeFun: AddCommunityRobot.() -> Unit) = AddCommunityRobot().apply{ executeFun() }

class AddCommunityRobot : BaseRobot() {


    fun fillFieldById(resId: Int, content: String) {
        onView(withId(resId)).perform(typeText(content), closeSoftKeyboard())
    }

    fun clickAddCommunity() {
        clickButtonByText("ADICIONAR COMUNIDADE")
    }

    fun fillNameField(name: String) {
        fillFieldById(R.id.communityNameInput, name)
    }

    fun fillDescriptionField(description: String) {
        fillFieldById(R.id.communityDescriptionInput, description)
    }

    fun fillAddressField(address: String) {
        fillFieldById(R.id.communityAddressInput, address)
    }

    fun fillOrgNameField(orgName: String) {
        fillFieldById(R.id.communityOrgNameInput, orgName)
    }

    fun fillOrgEmailField(orgEmail: String) {
        fillFieldById(R.id.communityOrgEmailInput, orgEmail)
    }

    fun clickSaveCommunity() {
        clickButton(R.id.addCommunityButton)
    }

    fun saveNewCommunity() {
        clickAddCommunity()
        fillNameField("Nome Teste")
        fillDescriptionField("Descricao teste")
        fillAddressField("Rua Teste, 123")
        fillOrgNameField("Nome Org Teste")
        fillOrgEmailField("orgtest@orgtest.com")
    }

    infix fun verify(executeFun: AddCommunityRobot.() -> Unit) {
        executeFun()
    }

}