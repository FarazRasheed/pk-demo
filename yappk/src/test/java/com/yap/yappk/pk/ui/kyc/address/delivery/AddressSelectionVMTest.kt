package com.yap.yappk.pk.ui.kyc.address.delivery

import com.digitify.core.utils.SharedPreferenceManager
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.ReadAssetFile
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiError
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.apiclient.models.BaseListResponse
import com.yap.yappk.networking.microservices.authentication.AuthApi
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.SessionManager
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class AddressSelectionVMTest : BaseTestCase() {

    // Subject under test
    private lateinit var addressSelectionVM: AddressSelectionVM

    // Use a fake UseCase to be injected into the viewModel
    private val customersApi: CustomersApi = mockk()
    private lateinit var authApi: AuthApi

    private val viewState: AddressSelectionState = AddressSelectionState()

    // Mock API response
    private lateinit var mockAccountsResponse: BaseListResponse<AccountInfo>

    // Mock SharePref
    private lateinit var sharedPreferenceManager: SharedPreferenceManager

    private lateinit var sessionManager: SessionManager

    @Before
    fun setUp() {
        mockAccountsResponse = getAccountsMockResponse("accountsResponse.json")
        sharedPreferenceManager = Mockito.mock(SharedPreferenceManager::class.java)
        authApi = Mockito.mock(AuthApi::class.java)
        sessionManager = SessionManager(customersApi, authApi, sharedPreferenceManager)
    }

    @Test
    fun `test save address with card ordered success api success`() {
        //Given
        mockAccountsResponse.data?.get(0)?.isSecretQuestionVerified = true
        addressSelectionVM = AddressSelectionVM(viewState, customersApi, sessionManager)
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Success(200, BaseApiResponse())
            coEvery { customersApi.saveAddressAndOrderCard(addressSelectionVM.getRequestSaveAddress()) } returns response
            val resAccounts = ApiResponse.Success(200, mockAccountsResponse)
            coEvery { customersApi.getAccountInfo() } returns resAccounts

            //2-Call
            addressSelectionVM.saveUserAddress(addressSelectionVM.getRequestSaveAddress())

            val actual = addressSelectionVM.openCardOrderedSuccess.getOrAwaitValue()
            //Verify
            Assert.assertEquals(
                true,
                actual.getContentIfNotHandled()
            )
        }
    }

    @Test
    fun `test save address with manual verification api success`() {
        //Given
        mockAccountsResponse.data?.get(0)?.isSecretQuestionVerified = false
        addressSelectionVM = AddressSelectionVM(viewState, customersApi, sessionManager)
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Success(200, BaseApiResponse())
            coEvery { customersApi.saveAddressAndOrderCard(addressSelectionVM.getRequestSaveAddress()) } returns response
            val resAccounts = ApiResponse.Success(200, mockAccountsResponse)
            coEvery { customersApi.getAccountInfo() } returns resAccounts

            //2-Call
            addressSelectionVM.saveUserAddress(addressSelectionVM.getRequestSaveAddress())

            val actual = addressSelectionVM.openManualVerification.getOrAwaitValue()
            //Verify
            Assert.assertEquals(
                true,
                actual.getContentIfNotHandled()
            )
        }
    }

    @Test
    fun `test save address with card ordered success api error`() {
        //Given
        mockAccountsResponse.data?.get(0)?.isSecretQuestionVerified = true
        addressSelectionVM = AddressSelectionVM(viewState, customersApi, sessionManager)
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Error(ApiError(400, "Error"))
            coEvery { customersApi.saveAddressAndOrderCard(addressSelectionVM.getRequestSaveAddress()) } returns response

            //2-Call
            addressSelectionVM.saveUserAddress(addressSelectionVM.getRequestSaveAddress())

            val actual = addressSelectionVM.openCardOrderedSuccess.getOrAwaitValue()
            //Verify
            Assert.assertEquals(
                false,
                actual.getContentIfNotHandled()
            )
        }
    }

    @Test
    fun `test save address with manual verification api error`() {
        //Given
        mockAccountsResponse.data?.get(0)?.isSecretQuestionVerified = false
        addressSelectionVM = AddressSelectionVM(viewState, customersApi, sessionManager)
        //1- Mock calls
        mainCoroutineRule.runBlockingTest {
            val response = ApiResponse.Error(ApiError(400, "Error"))
            coEvery { customersApi.saveAddressAndOrderCard(addressSelectionVM.getRequestSaveAddress()) } returns response

            //2-Call
            addressSelectionVM.saveUserAddress(addressSelectionVM.getRequestSaveAddress())

            val actual = addressSelectionVM.openManualVerification.getOrAwaitValue()
            //Verify
            Assert.assertEquals(
                false,
                actual.getContentIfNotHandled()
            )
        }
    }

    @Test
    fun `test address Line1 not empty`() {
        //Given
        viewState.addressLine2.value = "Avenue street"
        viewState.cityName.value = "Lahore"
        //when
        addressSelectionVM = AddressSelectionVM(viewState, mockk(), mockk())
        addressSelectionVM.onAddressFieldTextChanged("Johar Town", 0, 0, 0)

        //Then
        Assert.assertEquals(
            true,
            addressSelectionVM.viewState.isValid.value
        )
    }

    @Test
    fun `test address Line1 empty`() {
        //Given
        viewState.addressLine2.value = "Avenue street"
        viewState.cityName.value = "Lahore"
        //when
        addressSelectionVM = AddressSelectionVM(viewState, mockk(), mockk())
        addressSelectionVM.onAddressFieldTextChanged("", 0, 0, 0)

        //Then
        Assert.assertEquals(
            false,
            addressSelectionVM.viewState.isValid.value
        )
    }


    @Test
    fun `test address Line2 not empty`() {
        //Given
        viewState.addressLine1.value = "Avenue street"
        viewState.cityName.value = "Lahore"
        //when
        addressSelectionVM = AddressSelectionVM(viewState, mockk(), mockk())
        addressSelectionVM.onAddressField2TextChanged("Johar Town", 0, 0, 0)

        //Then
        Assert.assertEquals(
            true,
            addressSelectionVM.viewState.isValid.value
        )
    }

    @Test
    fun `test address Line2 empty`() {
        //Given
        viewState.addressLine1.value = "Avenue street"
        viewState.cityName.value = "Lahore"
        //when
        addressSelectionVM = AddressSelectionVM(viewState, mockk(), mockk())
        addressSelectionVM.onAddressField2TextChanged("", 0, 0, 0)

        //Then
        Assert.assertEquals(
            false,
            addressSelectionVM.viewState.isValid.value
        )
    }

    @Test
    fun `test city name not empty`() {
        //Given
        viewState.addressLine2.value = "Avenue street"
        viewState.addressLine1.value = "Johar Town"
        //when
        addressSelectionVM = AddressSelectionVM(viewState, mockk(), mockk())
        addressSelectionVM.onCityTextChanged("Lahore", 0, 0, 0)

        //Then
        Assert.assertEquals(
            true,
            addressSelectionVM.viewState.isValid.value
        )
    }

    @Test
    fun `test city name empty`() {
        //Given
        viewState.addressLine2.value = "Avenue street"
        viewState.addressLine1.value = "Johar Town"
        //when
        addressSelectionVM = AddressSelectionVM(viewState, mockk(), mockk())
        addressSelectionVM.onCityTextChanged("", 0, 0, 0)

        //Then
        Assert.assertEquals(
            false,
            addressSelectionVM.viewState.isValid.value
        )
    }


    private fun getAccountsMockResponse(fileName: String): BaseListResponse<AccountInfo> {
        val gson = GsonBuilder().create()
        val itemType = object : TypeToken<BaseListResponse<AccountInfo>>() {}.type
        return gson.fromJson(ReadAssetFile.readFileFromTestResources(fileName), itemType)
    }
}