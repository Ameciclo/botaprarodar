package app.igormatos.botaprarodar.createcommunity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.withText
import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun addCommunity(executeFun: AddCommunityActivityRobot.() -> Unit) = AddCommunityActivityRobot().apply{ executeFun() }

class AddCommunityActivityRobot : BaseRobot() {

    fun clickAddCommunity() {
        clickButtonByText("ADICIONAR COMUNIDADE")
    }

    fun confirmDataInput() {
        clickButtonByText("TUDO CERTO!")
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

    fun saveNewCommunity(name: String) {
        clickAddCommunity()
        fillNameField(name)
        fillDescriptionField("Descricao teste")
        fillAddressField("Rua Teste, 123")
        fillOrgNameField("Nome Org Teste")
        fillOrgEmailField("orgtest@orgtest.com")
        clickSaveCommunity()
        confirmDataInput()
    }

    fun saveCommunityWithNoData() {
        clickAddCommunity()
        clickSaveCommunity()
    }

    fun checkTextInItemList(name: String) {
        onView(withText(name)).inRoot(isDialog()).perform(scrollTo())
    }

    infix fun verify(executeFun: AddCommunityActivityRobot.() -> Unit) {
        executeFun()
    }

}