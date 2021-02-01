package app.igormatos.botaprarodar.createcommunity

import app.igormatos.botaprarodar.BaseRobot
import app.igormatos.botaprarodar.R

fun addCommunity(executeFun: AddCommunityActivityRobot.() -> Unit) = AddCommunityActivityRobot().apply{ executeFun() }

class AddCommunityActivityRobot : BaseRobot() {

    infix fun verify(executeFun: AddCommunityActivityRobot.() -> Unit) {
        executeFun()
    }

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

    fun findItemOnRecyclerView(name: String) {
        findItemInRecyclerView(R.id.rvCommunityList, name)
    }

    fun clickSaveCommunity() {
        clickButton(R.id.addCommunityButton)
    }

    fun selectCommunity() {
        selectAnyItemInRecyclerView(R.id.rvCommunityList)
    }

    fun showCommunityScreen(){
        waitViewByResId("communityAdapterContainer")
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

    fun fillCommunityDataWithWrongEmailFormat() {
        fillNameField("Name")
        fillDescriptionField("Descricao teste")
        fillAddressField("Rua Teste, 123")
        fillOrgNameField("Nome Org Teste")
        fillOrgEmailField("orgtest.com")
        clickSaveCommunity()
    }

}