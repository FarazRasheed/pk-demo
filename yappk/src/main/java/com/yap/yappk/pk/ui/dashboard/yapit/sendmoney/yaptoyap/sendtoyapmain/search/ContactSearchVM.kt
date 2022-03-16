package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.yap.yappk.localization.screen_send_yap_to_yap_display_text_all_contact
import com.yap.yappk.localization.screen_send_yap_to_yap_display_text_yap_contacts
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.enum.ContactFragmentType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactSearchVM @Inject constructor(
    override val viewState: ContactSearchState,
    private val customersApi: CustomersApi,
    private val resourcesProvider: ResourcesProviders
) : BaseViewModel<IContactSearch.State>(), IContactSearch.ViewModel {


    override fun getTabTitle(position: Int): String {
        return when (position) {
            ContactFragmentType.yapContact.type -> resourcesProvider.getString(
                screen_send_yap_to_yap_display_text_yap_contacts
            )
            ContactFragmentType.phoneContact.type -> resourcesProvider.getString(
                screen_send_yap_to_yap_display_text_all_contact
            )
            else -> throw IllegalStateException("Not a valid position found $position")
        }
    }
}