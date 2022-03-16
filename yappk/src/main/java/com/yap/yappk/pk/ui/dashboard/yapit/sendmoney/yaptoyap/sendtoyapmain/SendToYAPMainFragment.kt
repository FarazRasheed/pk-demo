package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.digitify.core.utils.DateUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentSendToYapMainBinding
import com.yap.yappk.networking.microservices.customers.requestsdtos.SendInviteFriendRequest
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.ContactAccountDetail
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.networking.microservices.customers.responsedtos.ReferralAmount
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.configurations.PKBuildConfigurations
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.main.ISendMoneyDashboardMain
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.main.SendMoneyDashboardMainVM
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.adapter.ContactsPagerAdapter
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.search.ContactSearchFragment
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.main.YapToYapTransferFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SendToYAPMainFragment :
    BaseNavFragment<FragmentSendToYapMainBinding, ISendToYAPMain.State, ISendToYAPMain.ViewModel>(
        R.layout.fragment_send_to_yap_main
    ),
    ISendToYAPMain.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ISendToYAPMain.ViewModel by viewModels<SendToYAPMainVM>()
    private val parentViewModel: ISendMoneyDashboardMain.ViewModel by activityViewModels<SendMoneyDashboardMainVM>()

    @Inject
    lateinit var pkBuildConfigurations: PKBuildConfigurations

    @Inject
    lateinit var sessionManager: SessionManager
    override var pagerSelectedPosition: Int = -1

    private var pagerAdapter: ContactsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
        viewModel.getLocalContacts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        setUpRecyclerView()
        referralApiCall()
        viewModel.setBeneficiaryList(parentViewModel.recentBeneficiariesList.value?.filter { iBeneficiary -> iBeneficiary.isYapUser })
    }

    private fun referralApiCall() {
        if (viewModel.referralAmount.value == null)
            viewModel.getReferralAmount()
        else
            setUpPagerAdapter(viewModel.referralAmount.value ?: ReferralAmount())
    }


    private fun setUpRecyclerView() {
        viewModel.recentTransferAdapter.allowFullItemClickListener = true
        viewModel.recentTransferAdapter.onItemClickListener = rvItemClickListener
        mViewBinding.rvRecentBeneficiaries.adapter = viewModel.recentTransferAdapter
    }

    private val rvItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            when (data) {
                is IBeneficiary -> {
                    routeToTransferScreen(data)
                }
            }
        }
    }
    private fun routeToTransferScreen(data: IBeneficiary) {
        if (data.isYapUser) {
            openY2YTransferScreen(
                Contact(
                    title = data.fullName,
                    countryCode = data.countryCde,
                    mobileNo = data.mobileNumber,
                    beneficiaryPictureUrl = data.imgUrl,
                    yapUser = data.isYapUser,
                    beneficiaryCreationDate = data.creationDateOfBeneficiary,
                    accountDetailList = listOf(ContactAccountDetail(accountUuid = data.accountUUID))
                )
            )
        }
    }

    private fun openY2YTransferScreen(contact: Contact) {
        navigate(
            R.id.action_sendToYAPMainFragment_to_pk_y2y_transfer_nav,
            bundleOf(YapToYapTransferFragment::class.java.name to contact)
        )
    }

    private fun setUpPagerAdapter(referralAmount: ReferralAmount) {
        pagerAdapter =
            ContactsPagerAdapter(
                requireActivity(),
                viewModel.phoneContacts.value ?: listOf(),
                viewModel.yapContacts.value ?: listOf(),
                referralAmount
            ) { pos: Int ->

            }
        mViewBinding.vpContacts.adapter = pagerAdapter
        mViewBinding.vpContacts.isUserInputEnabled = false
        mViewBinding.vpContacts.offscreenPageLimit = 1
        handleViewPagerCallback()
        TabLayoutMediator(
            mViewBinding.tlContacts,
            mViewBinding.vpContacts
        ) { tab, position ->
            tab.text = viewModel.getTabTitle(position)
        }.attach()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.tvRecentTransfer, R.id.hideRecentText -> {
                handleRecentVisibility()
            }
            R.id.clSearch -> {
                navigate(
                    R.id.action_sendToYAPMainFragment_to_contactSearchFragment,
                    bundleOf(ContactSearchFragment::class.java.name to pagerSelectedPosition)
                )
            }
        }
    }

    private fun handleRecentVisibility() {
        viewModel.viewState.isRecentTransfersVisible.value =
            mViewBinding.hideRecentText.visibility == View.VISIBLE
        mViewBinding.rvRecentBeneficiaries.visibility =
            if (mViewBinding.hideRecentText.visibility == View.GONE) View.VISIBLE else View.GONE
        mViewBinding.vLine.visibility =
            if (mViewBinding.hideRecentText.visibility == View.GONE) View.VISIBLE else View.GONE
    }

    override fun onStartIconClicked() {
        navigateBack()
    }

    override fun onEndIconClicked() {
//        sendInviteApiCall()
//        requireContext().share(
//            text = requireContext().getString(
//                screen_invite_friend_display_text_share_url,
//                pkBuildConfigurations.getAdjustURL(sessionManager.userAccount.value)
//            )
//        )
    }

    private fun sendInviteApiCall() {
        val request = SendInviteFriendRequest(
            inviterCustomerId = sessionManager.userAccount.value?.currentCustomer?.customerId
                ?: "",
            referralDate = DateUtils.getCurrentDateWithFormat(DateUtils.SERVER_DATE_FULL_FORMAT)
        )
        viewModel.inviteAFriend(request)
    }

    private fun handleReferralAmount(referralAmount: ReferralAmount) = Unit

    private fun handleBeneficiaries(list: List<IBeneficiary>) {
        if (!list.isNullOrEmpty())
            viewModel.recentTransferAdapter.setList(list)
        else
            viewModel.viewState.isNoRecentTransfers.value = true
    }

    private fun handleViewPagerCallback() {
        mViewBinding.vpContacts.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                pagerSelectedPosition = position
            }
        })
    }

    private fun handleLocalPhoneContacts(list: List<Contact>) {
        viewModel.getY2YUsersApiCall(list)
    }

    private fun handleRemoteContacts(list: List<Contact>) {
        viewModel.setContacts(list)
    }

    private fun handleYapContacts(list: List<Contact>) {
        if (viewModel.shouldPostData()) {
            parentViewModel.setYapContact(list)
            setUpPagerAdapter(viewModel.referralAmount.value ?: ReferralAmount())
        }
    }

    private fun handleRemotePhoneContacts(list: List<Contact>) {
        if (viewModel.shouldPostData()) {
            pagerAdapter?.setPhoneContactList(list)
            parentViewModel.setPhoneContact(list)
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.referralAmount, ::handleReferralAmount)
        observe(viewModel.recentBeneficiariesList, ::handleBeneficiaries)
        observe(viewModel.localContacts, ::handleLocalPhoneContacts)
        observe(viewModel.remoteContacts, ::handleRemoteContacts)
        observe(viewModel.yapContacts, ::handleYapContacts)
        observe(viewModel.phoneContacts, ::handleRemotePhoneContacts)
    }
}


