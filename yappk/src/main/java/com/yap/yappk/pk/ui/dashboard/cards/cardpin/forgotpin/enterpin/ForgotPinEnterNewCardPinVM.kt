package com.yap.yappk.pk.ui.dashboard.cards.cardpin.forgotpin.enterpin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.utils.SingleEvent
import com.yap.yappk.R
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.cards.cardpin.IPasscodeValidator
import com.yap.yappk.pk.ui.dashboard.cards.usecases.CreateOtpUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.ForgotCardPINUC
import com.yap.yappk.pk.ui.dashboard.cards.usecases.usecasebase.UseCaseCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPinEnterNewCardPinVM @Inject constructor(
    override val viewState: ForgotPinEnterNewCardPinState,
    private val resourcesProvider: ResourcesProviders,
    private val createOtpUC: CreateOtpUC,
    private val forgotCardPINUC: ForgotCardPINUC,
    private val passCodeValidator: IPasscodeValidator
) : BaseViewModel<IForgotPinEnterNewCardPin.State>(), IForgotPinEnterNewCardPin.ViewModel {

    override var confirmPin: String = ""

    private val _openConfirmPin = MutableLiveData<SingleEvent<Int>>()
    override val openConfirmPin: LiveData<SingleEvent<Int>> get() = _openConfirmPin

    private val _openVerifyOtp = MutableLiveData<SingleEvent<Int>>()
    override val openVerifyOtp: LiveData<SingleEvent<Int>> get() = _openVerifyOtp

    private val _openForgotCardPinSuccess = MutableLiveData<SingleEvent<Int>>()
    override val openForgotCardPinSuccess: LiveData<SingleEvent<Int>> get() = _openForgotCardPinSuccess

    private val _isOtpCreated = MutableLiveData<Boolean>()
    override val isOtpCreated: LiveData<Boolean> = _isOtpCreated

    private val _isCardPinCreated = MutableLiveData<Boolean>()
    override val isCardPinCreated: LiveData<Boolean> = _isCardPinCreated

    private val _isForgotCardPinOtpCreated: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    override val isForgotCardPinOtpCreated: LiveData<Boolean> = _isForgotCardPinOtpCreated


    override fun handlePressOnNext() {
        val passcodeError = passCodeValidator.validatePasscode(viewState.pin.value)
        viewState.pinError.value = if (passcodeError.isNullOrBlank()) "" else passcodeError
    }

    override fun handlePressOnCreate() {
        val confirmPasscodeError = passCodeValidator.validateConfirmPasscode(
            passcode = viewState.pin.value,
            confirmPasscode = confirmPin
        )
        viewState.pinError.value = if (confirmPasscodeError.isNullOrBlank()) "" else confirmPasscodeError
    }

    override fun openCardConfirmPinScreen() {
        _openConfirmPin.value =
            SingleEvent(R.id.action_forgotPinEnterNewCardPinFragment_self)
    }

    override fun openVerifyOtpScreen() {
        _openVerifyOtp.value =
            SingleEvent(R.id.action_forgotPinEnterNewCardPinFragment_to_verifyOtpFragment)
    }

    override fun createOtpForgotCardPin(action: String) {
        showLoading()
        createOtpUC.executeUseCase(
            CreateOtpUC.RequestValues(action),
            object :
                UseCaseCallback<CreateOtpUC.ResponseValue, CreateOtpUC.ErrorValue> {
                override fun onSuccess(response: CreateOtpUC.ResponseValue) {
                    hideLoading()
                    _isForgotCardPinOtpCreated.value = true
                }

                override fun onError(error: CreateOtpUC.ErrorValue) {
                    hideLoading()
                    _isForgotCardPinOtpCreated.value = false
                    showAlertMessage(error.msg)
                }
            })
    }

    override fun createForgotCardPin(cardSerialNumber: String, newPin: String, otpToken: String) {
        showLoading()
        forgotCardPINUC.executeUseCase(
            ForgotCardPINUC.RequestValues(
                cardSerialNumber = cardSerialNumber,
                newPin = newPin,
                token = otpToken
            ),
            object :
                UseCaseCallback<ForgotCardPINUC.ResponseValue, ForgotCardPINUC.ErrorValue> {
                override fun onSuccess(response: ForgotCardPINUC.ResponseValue) {
                    hideLoading()
                    _isCardPinCreated.value = true
                }

                override fun onError(error: ForgotCardPINUC.ErrorValue) {
                    hideLoading()
                    _isCardPinCreated.value = false
                    showAlertMessage(error.msg)
                }
            })
    }

    override fun openForgotCardPinSuccessScreen() {
        _openForgotCardPinSuccess.value =
            SingleEvent(R.id.action_forgotPinEnterNewCardPinFragment_to_forgotCardPinUpdateSuccessFragment)
    }

}
