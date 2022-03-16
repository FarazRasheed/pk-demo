package com.yap.yappk.pk.ui.kyc.failed

sealed class OnBoardingFailedStatuses(var type: String) {
    object NonValidPakistaniCnic : OnBoardingFailedStatuses("NOT_PAKISTANI")
    object ExpiredCNIC : OnBoardingFailedStatuses("EXPIRED_CNIC")
    object CNICDoesNotExist : OnBoardingFailedStatuses("CNIC_DOES_NOT_EXIST")
    object Under18CNIC : OnBoardingFailedStatuses("UNDER_18")
    object AlreadyExistCNIC : OnBoardingFailedStatuses("CNIC_ALREADY_EXIST")
    object BlackListedCNIC : OnBoardingFailedStatuses("BLACK_LISTED")
}