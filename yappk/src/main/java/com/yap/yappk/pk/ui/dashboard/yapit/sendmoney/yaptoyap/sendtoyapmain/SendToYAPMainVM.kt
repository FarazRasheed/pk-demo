package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.yap.yappk.localization.screen_send_yap_to_yap_display_text_all_contact
import com.yap.yappk.localization.screen_send_yap_to_yap_display_text_yap_contacts
import com.yap.yappk.networking.apiclient.base.ApiResponse
import com.yap.yappk.networking.microservices.customers.CustomersApi
import com.yap.yappk.networking.microservices.customers.requestsdtos.SendInviteFriendRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.networking.microservices.customers.responsedtos.ReferralAmount
import com.yap.yappk.pk.di.ResourcesProviders
import com.yap.yappk.pk.ui.dashboard.yapit.enum.ContactFragmentType
import com.yap.yappk.pk.ui.generic.recents.CoreRecentTransferAdapter
import com.yap.yappk.pk.utils.PhoneContactsProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.ceil

@HiltViewModel
class SendToYAPMainVM @Inject constructor(
    override val viewState: SendToYAPMainState,
    private val customersApi: CustomersApi,
    private val resourcesProvider: ResourcesProviders,
    private val phoneContactsProvider: PhoneContactsProvider
) : BaseViewModel<ISendToYAPMain.State>(), ISendToYAPMain.ViewModel {
    private val _referralAmount: MutableLiveData<ReferralAmount> = MutableLiveData()
    override var referralAmount: LiveData<ReferralAmount> = _referralAmount

    private val _recentBeneficiariesList: MutableLiveData<List<IBeneficiary>> = MutableLiveData()
    override val recentBeneficiariesList: LiveData<List<IBeneficiary>> = _recentBeneficiariesList

    private val _localContacts: MutableLiveData<List<Contact>> = MutableLiveData()
    override val localContacts: LiveData<List<Contact>> = _localContacts

    private val _phoneContacts: MutableLiveData<List<Contact>> = MutableLiveData()
    override val phoneContacts: LiveData<List<Contact>> = _phoneContacts

    private val _yapContacts: MutableLiveData<List<Contact>> = MutableLiveData()
    override val yapContacts: LiveData<List<Contact>> = _yapContacts

    private val _remoteContacts: MutableLiveData<List<Contact>> = MutableLiveData()
    override val remoteContacts: LiveData<List<Contact>> = _remoteContacts

    override val recentTransferAdapter: CoreRecentTransferAdapter = CoreRecentTransferAdapter(
        mutableListOf()
    )
    private val threshold: Int = 3000
    private var totalNumberOfIteration = 1
    private var numberOfIteration = 0
    override fun inviteAFriend(request: SendInviteFriendRequest) {
        launch {
            customersApi.sendInviteFriend(request)
        }
    }

    override fun getLocalContacts() {
        launch(Dispatcher.LongOperation) {
            showLoading(onBackGround = true)
            val contacts = phoneContactsProvider.getLocalContacts()
            withContext(Dispatchers.Main) {
                _localContacts.value = contacts
            }
        }
    }

    override fun getY2YUsersApiCall(contacts: List<Contact>) {
        var lastCount = 0
        totalNumberOfIteration =
            ceil((contacts.size.toDouble()) / threshold.toDouble()).toInt()
        for (x in 1..totalNumberOfIteration) {
            val itemsToPost = contacts.subList(
                lastCount,
                if ((x * threshold) > contacts.size) contacts.size else x * threshold
            )
            getY2YUsers(itemsToPost)
            lastCount = x * threshold
        }
        if (totalNumberOfIteration == 0) hideLoading()
    }

    override fun getY2YUsers(contacts: List<Contact>) {
        launch {
            val response = customersApi.getY2YUsers(contacts)
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        numberOfIteration = numberOfIteration.plus(1)
                        if (shouldPostData()) hideLoading()
                        _remoteContacts.value = response.data.data
                    }
                    is ApiResponse.Error -> {
                        showAlertMessage(response.error.message)
                        hideLoading()
                        _remoteContacts.value = null
                    }
                }
            }
        }
    }

    override fun setContacts(contacts: List<Contact>) {
        val partition = contacts.partition { contact -> contact.yapUser == true }
        val list: ArrayList<Contact> = arrayListOf()
        list.addAll(phoneContacts.value ?: arrayListOf())
        list.addAll(partition.second)
        _phoneContacts.value = list
        val ylist: ArrayList<Contact> = arrayListOf()
        ylist.addAll(_yapContacts.value ?: arrayListOf())
        ylist.addAll(partition.first)
        _yapContacts.value = ylist
    }

    override fun getReferralAmount() {
        launch {
            val response = customersApi.getReferralAmount()
            withContext(Dispatchers.Main) {
                when (response) {
                    is ApiResponse.Success -> {
                        _referralAmount.value = response.data.data
                    }
                    is ApiResponse.Error -> {
                        showAlertMessage(response.error.message)
                        _referralAmount.value = null
                    }
                }
            }
        }
    }

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

    override fun shouldPostData(): Boolean {
        return numberOfIteration == totalNumberOfIteration
    }

    override fun setBeneficiaryList(beneficiaries: List<IBeneficiary>?) {
        _recentBeneficiariesList.value = beneficiaries ?: emptyList()
    }
}