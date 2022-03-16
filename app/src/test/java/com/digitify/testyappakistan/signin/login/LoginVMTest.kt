package com.digitify.testyappakistan.signin.login


import android.content.Context
import com.digitify.testyappakistan.base.BaseTestCase
import com.digitify.testyappakistan.base.getOrAwaitValue
import com.digitify.testyappakistan.configurations.LoadConfig
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.models.BaseResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.configurations.onboarding.PKOnboardApis
import com.yap.yappk.pk.utils.PhoneUtils
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginVMTest : BaseTestCase() {

    // Subject under test
    private lateinit var loginVM: LoginVM

    // Use a fake UseCase to be injected into the viewModel
    private val viewState: LoginState = LoginState()

    private val phoneUtils: PhoneUtils = PhoneUtils()

    // Mock API responses
    private lateinit var mockVerifyUserResponse: BaseResponse<Boolean>

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()

    // Mock context using mockk lib
    private val mContextMock = mockk<Context>(relaxed = true)

    private val mockPkApi = mockk<PKOnboardApis>(relaxed = true)
    private val loadConfig = LoadConfig(mockPkApi, mockk())
    private val mockPkConfig = mockk<PKBuildConfigurations>(relaxed = true)


    @Before
    fun setUp() {
        mockVerifyUserResponse = BaseResponse()
    }

    @Test
    fun `test valid phone number pakistan`() {
        loginVM = LoginVM(viewState, phoneUtils, mockk(), mockk(), mockk())
        loginVM.viewState.countryCode.set("+92")
        loginVM.onPhoneNumberTextChanged("3034270955", 0, 0, 0)
        Assert.assertEquals(true, loginVM.viewState.isValid.value)
    }

    @Test
    fun `test invalid phone number pakistan`() {
        loginVM = LoginVM(viewState, phoneUtils, mockk(), mockk(), mockk())
        loginVM.viewState.countryCode.set("+92")
        loginVM.onPhoneNumberTextChanged("30342709", 0, 0, 0)
        Assert.assertEquals(false, loginVM.viewState.isValid.value)
    }

    @Test
    fun `test valid phone number uae`() {
        loginVM = LoginVM(viewState, phoneUtils, mockk(), mockk(), mockk())
        loginVM.viewState.countryCode.set("+971")
        loginVM.onPhoneNumberTextChanged("555369987", 0, 0, 0)
        Assert.assertEquals(true, loginVM.viewState.isValid.value)
    }

    @Test
    fun `test invalid phone number uae`() {
        loginVM = LoginVM(viewState, phoneUtils, mockk(), mockk(), mockk())
        loginVM.viewState.countryCode.set("+971")
        loginVM.onPhoneNumberTextChanged("5553699", 0, 0, 0)
        Assert.assertEquals(false, loginVM.viewState.isValid.value)
    }

    @Test
    fun `test verify user with verified number api success`() {
        mockVerifyUserResponse.data = true
        val user = "00923224642870"
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            every { mockPkApi.verifyUser(any(), any()) } answers {
                secondArg<(Boolean) -> Unit>().invoke(true)
            }

            //2-Call
            loginVM = LoginVM(viewState, phoneUtils, mContextMock, mockPkConfig, loadConfig)
            loginVM.verifyUser(user)

            //Verify
            Assert.assertEquals(true, loginVM.verifyUserEvent.getOrAwaitValue())
        }
    }

    @Test
    fun `test verify user with unverified number api success`() {
        mockVerifyUserResponse.data = false
        val user = "00923034270955"
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {

            every { mockPkApi.verifyUser(any(), any()) } answers {
                secondArg<(Boolean) -> Unit>().invoke(false)
            }

            //2-Call
            loginVM = LoginVM(viewState, phoneUtils, mContextMock, mockPkConfig, loadConfig)
            loginVM.verifyUser(user)

            Assert.assertEquals(false, loginVM.verifyUserEvent.getOrAwaitValue())

        }
    }

    @Test
    fun `test verify user api error`() {
        mockVerifyUserResponse.data = null
        val user = "00923034270955"
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Error(
                ApiError(400, "Something went wrong!")
            )

            every { mockPkApi.verifyUser(any(), any()) } answers {
                secondArg<(String) -> Unit>().invoke(response.error.actualCode)
            }

            //2-Call
            loginVM = LoginVM(viewState, phoneUtils, mContextMock, mockPkConfig, loadConfig)
            loginVM.verifyUser(user)

            //verify
            Assert.assertEquals(false, loginVM.verifyUserEvent.getOrAwaitValue())
        }
    }

    @Test
    fun `test verify user api error account blocked with actual error code AD-10018`() {
        mockVerifyUserResponse.data = null
        val user = "00923034270955"
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Error(
                ApiError(
                    400,
                    "Your account is blocked. Please contact system administrator.",
                    "AD-10018"
                )
            )
            every { mockPkApi.verifyUser(any(), any()) } answers {
                secondArg<(String) -> Unit>().invoke(response.error.actualCode)
            }
//            coEvery { customersApi.verifyUser(user) } returns response
            //2-Call
            loginVM = LoginVM(viewState, phoneUtils, mContextMock, mockPkConfig, loadConfig)
            loginVM.verifyUser(user)

            //verify
            Assert.assertEquals(true, loginVM.isAccountBlocked.getOrAwaitValue())
        }
    }
}