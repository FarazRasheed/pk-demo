package com.yap.yappk.pk.ui.kyc.failed

import com.yap.yappk.R

class OnBoardingFailedComposer : IOnBoardingFailedDataComposer {
    override fun composeData(status: String): OnBoardingFailedModel {
        return when (status) {
            OnBoardingFailedStatuses.NonValidPakistaniCnic.type -> {
                OnBoardingFailedModel(
                    R.string.screen_on_boarding_failed_display_text_title_sorry,
                    R.string.screen_on_boarding_failed_display_text_sub_title_sorry,
                    R.string.common_button_logout,
                    R.string.empty_string
                )
            }
            OnBoardingFailedStatuses.ExpiredCNIC.type -> {
                OnBoardingFailedModel(
                    R.string.screen_on_boarding_failed_display_text_title_cnic_expired,
                    R.string.screen_on_boarding_failed_display_text_sub_title_cnic_expired,
                    R.string.screen_on_boarding_failed_display_text_button_title_re_scan,
                    R.string.screen_on_boarding_failed_display_text_button_go_to_dashboard
                )
            }
            OnBoardingFailedStatuses.CNICDoesNotExist.type -> {
                OnBoardingFailedModel(
                    R.string.screen_on_boarding_failed_display_text_title_cnic_do_not_exist,
                    R.string.screen_on_boarding_failed_display_text_sub_title_cnic_do_not_exist,
                    R.string.screen_on_boarding_failed_display_text_button_title_re_scan,
                    R.string.screen_on_boarding_failed_display_text_button_go_to_dashboard
                )
            }
            OnBoardingFailedStatuses.Under18CNIC.type -> {
                OnBoardingFailedModel(
                    R.string.screen_on_boarding_failed_display_text_title_under_eighteen,
                    R.string.screen_on_boarding_failed_display_text_sub_title_under_eighteen,
                    R.string.common_button_logout,
                    R.string.empty_string
                )
            }
            OnBoardingFailedStatuses.AlreadyExistCNIC.type -> {
                OnBoardingFailedModel(
                    R.string.screen_on_boarding_failed_display_text_title_cnic_already_exist,
                    R.string.screen_on_boarding_failed_display_text_sub_title_cnic_already_exist,
                    R.string.common_button_logout,
                    R.string.empty_string
                )
            }
            OnBoardingFailedStatuses.BlackListedCNIC.type -> {
                OnBoardingFailedModel(
                    R.string.screen_on_boarding_failed_display_text_title_sorry,
                    R.string.screen_on_boarding_failed_display_text_sub_title_sorry,
                    R.string.common_button_logout,
                    R.string.empty_string
                )
            }
            else -> {
                throw IllegalStateException("Invalid status")
            }
        }
    }
}
