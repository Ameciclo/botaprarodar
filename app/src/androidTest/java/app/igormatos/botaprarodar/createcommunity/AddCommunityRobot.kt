package app.igormatos.botaprarodar.createcommunity

import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun addCommunity(executeFun: AddCommunityRobot.() -> Unit) = AddCommunityRobot().apply{ executeFun() }

class AddCommunityRobot : BaseRobot() {

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

    fun saveNewCommunity() {
        clickAddCommunity()
        fillNameField("Nome Teste")
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

    infix fun verify(executeFun: AddCommunityRobot.() -> Unit) {
        executeFun()
    }

}