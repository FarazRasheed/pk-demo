package com.yap.yappk.pk.ui.kyc.cardname.updatecardname

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.interfaces.IBase
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.kyc.cardname.updatecardname.adapter.SuggestedNameAdapter

interface IUpdateCardName {
    interface View : IBase.View<ViewModel> {
        fun getFragmentArguments()
        fun viewModelObservers()
    }

    interface ViewModel : IBase.ViewModel<State> {
        val suggestedNameAdapter: SuggestedNameAdapter
        val sessionManager: SessionManager
        val namesList: LiveData<MutableList<String>>
        val nameLength: Int
        fun generateNameList(name: String)
    }

    interface State : IBase.State {
        val cardName: MutableLiveData<String>
        val count: MutableLiveData<String>
        val isValid: MutableLiveData<Boolean>
    }
}