package app.igormatos.botaprarodar.screens.createcommunity

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.Button
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import app.igormatos.botaprarodar.BuildConfig
import app.igormatos.botaprarodar.common.BprApplication
import app.igormatos.botaprarodar.screens.main.MainActivity
import com.google.android.material.textfield.TextInputEditText
import junit.framework.Assert.assertEquals
import kotlinx.android.synthetic.main.activity_add_community.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.Robolectric.buildActivity
import org.robolectric.Robolectric.setupActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
@LooperMode(LooperMode.Mode.PAUSED)
class AddCommunityActivityUnitTest {

    lateinit var activityController: ActivityController<AddCommunityActivity>
    lateinit var nameInput: TextInputEditText
    lateinit var descriptionInput: TextInputEditText
    lateinit var addressInput: TextInputEditText
    lateinit var orgNameInput: TextInputEditText
    lateinit var orgEmailInput: TextInputEditText
    lateinit var addButton: Button
    lateinit var addCommunityActivity: AddCommunityActivity

    @Before
    fun setUp() {
        activityController = buildActivity(AddCommunityActivity::class.java).setup()
        addCommunityActivity = activityController.create().get().apply {
            nameInput = communityNameInput
            descriptionInput = communityDescriptionInput
            addressInput = communityAddressInput
            orgNameInput = communityOrgNameInput
            orgEmailInput = communityOrgEmailInput
            addButton = addCommunityButton
        }
    }

    @Test
    fun nameInputVisibility_shouldReturnVisible() {
        assertEquals(View.VISIBLE, nameInput.visibility)
    }

    @Test
    fun nameInputHint_shouldBeNome() {
        assertEquals("Nome", nameInput.hint.toString())
    }

    @Test
    fun descriptionInputVisibility_shouldReturnVisible() {
        assertEquals(View.VISIBLE, descriptionInput.visibility)
    }

    @Test
    fun descriptionInputHint_shouldBeDescricao() {
        assertEquals("Descrição", descriptionInput.hint.toString())
    }

    @Test
    fun addressInputVisibility_shouldReturnVisible() {
        assertEquals(View.VISIBLE, addressInput.visibility)
    }

    @Test
    fun addressInputHint_shouldBeEndereco() {
        assertEquals("Endereço", addressInput.hint.toString())
    }

    @Test
    fun orgNameInputVisibility_shouldReturnVisible() {
        assertEquals(View.VISIBLE, orgNameInput.visibility)
    }

    @Test
    fun orgNameInputHint_shouldBeNomeResponsavel() {
        assertEquals("Nome do responsável", orgNameInput.hint.toString())
    }

    @Test
    fun orgEmailInputVisibility_shouldReturnVisible() {
        assertEquals(View.VISIBLE, orgEmailInput.visibility)
    }

    @Test
    fun orgEmailInputHintVisibility_shouldBeEmailResponsavel() {
        assertEquals("Email do responsável", orgEmailInput.hint.toString())
    }

    @Test
    fun addCommunityButtonVisibility_shouldReturnVisible() {
        assertEquals(View.VISIBLE, addButton.visibility)
    }

    @Test
    fun getCommunityFromInputs_shouldChangeLiveData_liveDataMustBeObserved() {

    }

}