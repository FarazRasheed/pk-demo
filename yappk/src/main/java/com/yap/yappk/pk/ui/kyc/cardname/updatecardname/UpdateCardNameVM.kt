package com.yap.yappk.pk.ui.kyc.cardname.updatecardname

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.kyc.cardname.updatecardname.adapter.SuggestedNameAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateCardNameVM @Inject constructor(
    override val viewState: UpdateCardNameState,
    override val sessionManager: SessionManager
) : BaseViewModel<IUpdateCardName.State>(), IUpdateCardName.ViewModel {

    override val suggestedNameAdapter: SuggestedNameAdapter = SuggestedNameAdapter(arrayListOf())

    private val _namesList: MutableLiveData<MutableList<String>> = MutableLiveData()
    override val namesList: LiveData<MutableList<String>> = _namesList

    override val nameLength: Int = 27
    private val startIndex: Int = 0
    private val endIndex: Int = 26
    private val minNameSplit: Int = 2
    private val removingIndex: Int = 1

    override fun generateNameList(name: String) {
        val namesList: MutableList<String> = mutableListOf()
        val nameList = name.split(" ")
        val firstName = nameList.first()
        namesList.add(getNameWithLengthCheck(name))
        if (nameList.size > minNameSplit) {
            nameList.subList(removingIndex, nameList.size).forEach {
                namesList.add(getNameWithLengthCheck("$firstName $it"))
            }
        } else {
            namesList.add(getNameWithLengthCheck("${firstName.first()} ${nameList.last()}"))
        }
        _namesList.value = namesList
    }

    private fun getNameWithLengthCheck(name: String): String {
        return if (name.length < nameLength) name
        else name.subSequence(startIndex = startIndex, endIndex = endIndex).toString()
    }
}