package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.confirmation

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class BankTransferAmountConfirmationState @Inject constructor() : BaseState(), IBankTransferAmountConfirmation.State {
    override var transferFee: MutableLiveData<String> = MutableLiveData("0.00")
    override var beneficiaryFirstName: MutableLiveData<String> = MutableLiveData("Sufyan")
}