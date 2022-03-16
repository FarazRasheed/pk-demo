package com.yap.yappk.pk.ui.generic.address

import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseState
import javax.inject.Inject

class GenericAddressSelectionState @Inject constructor() : BaseState(), IGenericAddressSelection.State {
    override var title: MutableLiveData<String> = MutableLiveData()
    override val addressDataModel: MutableLiveData<GenericAddressDataModel> = MutableLiveData()
    override val addressLine1: MutableLiveData<String> = MutableLiveData()
    override val addressLine2: MutableLiveData<String> = MutableLiveData()
    override val cityName: MutableLiveData<String> = MutableLiveData()
    override val cardAddressLine1: MutableLiveData<String> = MutableLiveData()
    override val cardAddressLine2: MutableLiveData<String> = MutableLiveData()
    override val cardCityName: MutableLiveData<String> = MutableLiveData()
    override var isValid: MutableLiveData<Boolean> = MutableLiveData()
}