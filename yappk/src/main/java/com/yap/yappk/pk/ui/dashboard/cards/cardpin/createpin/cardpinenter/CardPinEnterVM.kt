package com.yap.yappk.pk.ui.dashboard.cards.cardpin.createpin.cardpinenter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.utils.extensions.hasAllSameChars
import com.yap.uikit.utils.extensions.isSequenced
import com.yap.yappk.R
import com.yap.yappk.localization.error_passcode_same_digits
import com.yap.yappk.localization.error_passcode_sequence
import com.yap.yappk.pk.di.ResourcesProviders
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardPinEnterVM @Inject constructor(
    override val viewState: CardPinEnterState,
    private val resourcesProviders: ResourcesProviders
) :
    BaseViewModel<ICardPinEnter.State>(), ICardPinEnter.ViewModel {
    private val _openConfirmPin = MutableLiveData<SingleEvent<Int>>()
    override val openConfirmPin: LiveData<SingleEvent<Int>> get() = _openConfirmPin


    override fun handlePressOnNext() {
        if (isPasscodeValid(viewState.pin.value ?: "")) {
            viewState.pinError.value = ""
        } else {
            viewState.pinError.value = getPassCodeError(viewState.pin.value ?: "")
        }
    }

    private fun isPasscodeValid(passcode: String): Boolean {
        return !passcode.hasAllSameChars() && !passcode.isSequenced()
    }

    private fun getPassCodeError(passcode: String): String {
        if (passcode.isSequenced()) return resourcesProviders.getString(keyID = error_passcode_sequence)
        if (passcode.hasAllSameChars()) return resourcesProviders.getString(keyID = error_passcode_same_digits)

        return ""
    }

    override fun openCardConfirmPinScreen() {
        _openConfirmPin.value =
            SingleEvent(R.id.action_cardPinEnterFragment_to_cardConfirmPinFragment)
    }

}