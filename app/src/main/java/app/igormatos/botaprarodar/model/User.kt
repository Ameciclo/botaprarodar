package app.igormatos.botaprarodar.model

class User : Item {

    var id: Int = 0
    lateinit var name: String
    lateinit var created_date: String
    lateinit var birthday: String
    lateinit var address: String
    lateinit var gender: String
    lateinit var profile_picture: String
    lateinit var residence_proof_picture: String
    var doc_type: Int = 0
    var doc_number: Long = 0


    override fun title(): String {
        return name
    }

    override fun iconPath(): String {
        return profile_picture
    }

    override fun subtitle(): String {
        return "Cadastrado desde "
    }

}