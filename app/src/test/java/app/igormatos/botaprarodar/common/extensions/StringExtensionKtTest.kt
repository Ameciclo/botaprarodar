package app.igormatos.botaprarodar.common.extensions

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class StringExtensionKtTest {

    @Before
    fun setUp() {
        stopKoin()
    }

    @Test
    fun `When validate valid email, then should return true`() {
        assertTrue("fake@fake.com".isValidEmail())
    }

    @Test
    fun `When validate invalid email, then should return false`() {
        assertFalse("fake.com".isValidEmail())
    }

    @Test
    fun `When validate valid password, then should return true`(){
        assertTrue("123456".isValidPassword())
    }

    @Test
    fun `When validate invalid password, then should return false`(){
        assertFalse("12345".isValidPassword())
    }

    @Test
    fun `When validate empty password, then should return false`(){
        assertFalse("".isValidPassword())
    }
}