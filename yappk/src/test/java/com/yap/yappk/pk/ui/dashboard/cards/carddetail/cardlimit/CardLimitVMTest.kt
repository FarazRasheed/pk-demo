package com.yap.yappk.pk.ui.dashboard.cards.carddetail.cardlimit

import com.yap.yappk.base.BaseTestCase
import com.yap.yappk.base.getOrAwaitValue
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import com.yap.yappk.networking.microservices.cards.CardsApi
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CardLimitVMTest : BaseTestCase() {
    // Subject under test
    lateinit var viewModel: CardLimitVM

    // Use a fake UseCase to be injected into the viewModel
    private val cardApi: CardsApi = mockk()

    @Before
    fun setUp() {

    }

    @Test
    fun `atm config change success`() {
        //Given
        val cardSerialNo = "Absjag"
        //1- Mock calls
        coEvery { cardApi.configAllowAtm(cardSerialNumber = cardSerialNo) } returns mockk<ApiResponse.Success<BaseApiResponse>>()

        //2-Call
        viewModel = CardLimitVM(mockk(relaxed = true), cardApi)
        viewModel.updateAtmConfig(cardSerialNumber = cardSerialNo)

        //3-verify
        Assert.assertEquals(true, viewModel.isAtmConfigUpdated.getOrAwaitValue())
    }

    @Test
    fun `atm config change error`() {
        //Given
        val cardSerialNo = "Absjag"
        //1- Mock calls
        coEvery { cardApi.configAllowAtm(cardSerialNumber = cardSerialNo) } returns mockk<ApiResponse.Error>()

        //2-Call
        viewModel = CardLimitVM(mockk(relaxed = true), cardApi)
        viewModel.updateAtmConfig(cardSerialNumber = cardSerialNo)

        //3-verify
        Assert.assertEquals(false, viewModel.isAtmConfigUpdated.getOrAwaitValue())
    }

    @Test
    fun `retail config change success`() {
        //Given
        val cardSerialNo = "Absjag"
        //1- Mock calls
        coEvery { cardApi.configRetailPayment(cardSerialNumber = cardSerialNo) } returns mockk<ApiResponse.Success<BaseApiResponse>>()

        //2-Call
        viewModel = CardLimitVM(mockk(relaxed = true), cardApi)
        viewModel.updateRetailConfig(cardSerialNumber = cardSerialNo)

        //3-verify
        Assert.assertEquals(true, viewModel.isRetailConfigUpdated.getOrAwaitValue())
    }

    @Test
    fun `retail config change error`() {
        //Given
        val cardSerialNo = "Absjag"
        //1- Mock calls
        coEvery { cardApi.configRetailPayment(cardSerialNumber = cardSerialNo) } returns mockk<ApiResponse.Error>()

        //2-Call
        viewModel = CardLimitVM(mockk(relaxed = true), cardApi)
        viewModel.updateRetailConfig(cardSerialNumber = cardSerialNo)

        //3-verify
        Assert.assertEquals(false, viewModel.isRetailConfigUpdated.getOrAwaitValue())
    }

}
