package com.yap.yappk.pk.ui.kyc.failed

interface IOnBoardingFailedDataComposer {
    fun composeData(status: String): OnBoardingFailedModel
}