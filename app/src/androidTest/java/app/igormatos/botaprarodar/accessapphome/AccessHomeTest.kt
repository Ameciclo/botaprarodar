package app.igormatos.botaprarodar.accessapphome

import app.igormatos.botaprarodar.login.login
import org.junit.Test

class AccessHomeTest {

    @Test
    fun access_home_as_admin(){
        login {
            doLogin("brunotmg@gmail.com", "abcd1234")
            sleep(3000)
        }
        selectCommunity{
            find
        }
    }

    @Test
    fun access_home_as_user(){

    }
}