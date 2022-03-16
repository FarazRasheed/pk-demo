package com.yap.yappk.pk.ui.auth.forgotpasscode.createnewpasscode

import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.CreateNewPasscodeRequest
import com.yap.yappk.pk.di.ResourcesProviders
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class CreateNewPasscodeVMTest : BaseTestCase() {
    // Subject under test
    private lateinit var createNewPasscodeVM: CreateNewPasscodeVM

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()


    //Mock ResourcesProvider
    private lateinit var resourcesProviders: ResourcesProviders

    @Before
    fun setUp() {
        resourcesProviders = Mockito.mock(ResourcesProviders::class.java)
    }

    @Test
    fun `test create new passcode api success`() {
        //Given
        val mockCreateNewPasscodeRequest = CreateNewPasscodeRequest(
            mobileNo = "009230342955",
            token = "Absnsny",
            newPassword = "1212"
        )
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Success(200, BaseApiResponse())
            coEvery { customersApi.createNewPasscode(mockCreateNewPasscodeRequest) } returns response

            // Call VM
            createNewPasscodeVM =
                CreateNewPasscodeVM(
                    viewState = CreateNewPasscodeState(),
                    resourcesProviders = resourcesProviders,
                    customersApi = customersApi,
                    sharedPreferenceManager = mockk()
                )
            //2-Call
            createNewPasscodeVM.createNewPasscode(mockCreateNewPasscodeRequest)

            //Verify
            Assert.assertEquals(true, createNewPasscodeVM.isPasscodeCreated.getOrAwaitValue())
        }
    }

    @Test
    fun `test create new passcode api error`() {
        //Given
        val mockCreateNewPasscodeRequest = CreateNewPasscodeRequest(
            mobileNo = "009230342955",
            token = "Absnsny",
            newPassword = "1212"
        )
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Error(ApiError(400, "Error"))
            coEvery { customersApi.createNewPasscode(mockCreateNewPasscodeRequest) } returns response

            // Call VM
            createNewPasscodeVM =
                CreateNewPasscodeVM(
                    viewState = CreateNewPasscodeState(),
                    resourcesProviders = resourcesProviders,
                    customersApi = customersApi,
                    sharedPreferenceManager = mockk()
                )
            //2-Call
            createNewPasscodeVM.createNewPasscode(mockCreateNewPasscodeRequest)

            //Verify
            Assert.assertEquals(false, createNewPasscodeVM.isPasscodeCreated.getOrAwaitValue())
        }
    }

}