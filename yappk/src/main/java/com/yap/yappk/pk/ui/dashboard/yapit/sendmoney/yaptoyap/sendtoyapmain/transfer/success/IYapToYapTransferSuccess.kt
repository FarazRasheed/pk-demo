package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.success

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.networking.microservices.transactions.responsedtos.YapToYapTransaction

interface IYapToYapTransferSuccess {
    interface View : IBase.View<ViewModel>
    interface ViewModel : IBase.ViewModel<State>
    interface State : IBase.State {
        var contact: MutableLiveData<IBeneficiary?>
        var yapToYapTransaction: MutableLiveData<YapToYapTransaction>
        var transferredAmount: MutableLiveData<String>
    }
}