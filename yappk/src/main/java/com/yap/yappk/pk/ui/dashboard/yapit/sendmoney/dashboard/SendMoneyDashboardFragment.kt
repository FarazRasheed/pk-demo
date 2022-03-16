package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.dashboard

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.finish
import com.digitify.core.extensions.observe
import com.digitify.core.utils.NAVIGATION_GRAPH_START_DESTINATION_ID
import com.yap.permissionx.PermissionX
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentSendMoneyDashboardBinding
import com.yap.yappk.localization.common_text_permissions_denied
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.message_contact_permission_denied
import com.yap.yappk.localization.open_setting
import com.yap.yappk.networking.microservices.customers.responsedtos.Contact
import com.yap.yappk.networking.microservices.customers.responsedtos.ContactAccountDetail
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.CardDetailMainActivity
import com.yap.yappk.pk.ui.dashboard.yapit.qrcode.main.MainQrActivity
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.main.ISendMoneyDashboardMain
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.main.SendMoneyDashboardMainVM
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.yaptoyap.sendtoyapmain.transfer.main.YapToYapTransferFragment
import com.yap.yappk.pk.utils.ItemCard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SendMoneyDashboardFragment :
    BaseNavFragment<PkFragmentSendMoneyDashboardBinding, ISendMoneyDashboard.State, ISendMoneyDashboard.ViewModel>(
        R.layout.pk_fragment_send_money_dashboard
    ),
    ISendMoneyDashboard.View, ToolBarView.OnToolBarViewClickListener,
    ItemCard.OnCardItemClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ISendMoneyDashboard.ViewModel by viewModels<SendMoneyDashboardVM>()
    private val parentViewModel: ISendMoneyDashboardMain.ViewModel by activityViewModels<SendMoneyDashboardMainVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewModelObservers()
        parentViewModel.getRecentTransferBeneficiaries()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    override fun initListeners() {
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        mViewBinding.icBankTransfer.setOnItemCardClickListener(this)
        mViewBinding.icYapToYap.setOnItemCardClickListener(this)
        mViewBinding.icQr.setOnItemCardClickListener(this)
        viewModel.recentTransferAdapter.onItemClickListener = rvItemClickListener
        viewModel.recentTransferAdapter.allowFullItemClickListener = true
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
            R.id.action_sendMoneyDashboardFragment_to_pk_y2y_transfer_nav,
            bundleOf(YapToYapTransferFragment::class.java.name to contact)
        )
    }


    override fun onClick(id: Int) {
        when (id) {
            R.id.icYapToYap -> checkPermission(
                listOf(Manifest.permission.READ_CONTACTS)
            ) { openYapToYapScreen() }
            R.id.icBankTransfer -> openBankTransferMainFragment()
            R.id.icQr -> openQrCodeScreen()
            R.id.tvRecentTransfer, R.id.hideRecentText -> {
                viewModel.viewState.isRecentTransfersVisible.value =
                    mViewBinding.hideRecentText.visibility == View.VISIBLE
                mViewBinding.rvRecentBeneficiaries.visibility =
                    if (mViewBinding.hideRecentText.visibility == View.GONE) View.VISIBLE else View.GONE
            }
        }
    }

    private fun openQrCodeScreen() {
        val intent = Intent(requireContext(), MainQrActivity::class.java)
        intent.putExtra(
            NAVIGATION_GRAPH_START_DESTINATION_ID, R.id.scanQrFragment
        )
        activityLauncher.launch(intent)
    }

    private fun openBankTransferMainFragment() {
        navigate(
            R.id.action_sendMoneyDashboardFragment_to_pk_send_money_bank_transfer
        )
    }

    override fun onStartIconClicked() {
        this.finish()
    }

    override fun onItemClicked(view: View) {
        onClick(view.id)
    }

    override fun openAccountDetailScreen() = Unit

    fun setAdapter(list: List<IBeneficiary>) {
        if (!list.isNullOrEmpty())
            viewModel.recentTransferAdapter.setList(list)
        else
            viewModel.viewState.isNoRecentTransfers.value = true
    }

    override fun onEndIconClicked() {
        checkPermission(
            listOf(Manifest.permission.READ_CONTACTS)
        ) { openSearchScreen() }
    }

    override fun openYapToYapScreen() {
        navigate(R.id.action_sendMoneyDashboardFragment_to_pk_send_money_to_yap)
    }

    override fun openSearchScreen() {
        navigate(R.id.action_sendMoneyDashboardFragment_to_globalSearchFragment)
    }

    private fun checkPermission(permissionsList: List<String>, function: () -> Unit) {
        PermissionX.init(this).permissions(
            permissionsList
        ).onForwardToSettings { scope, deniedList ->
            scope.showForwardToSettingsDialog(
                deniedList,
                requireContext().getString(message_contact_permission_denied),
                requireContext().getString(open_setting)
            )
        }.request { allGranted, grantedList, deniedList ->
            if (allGranted) {
                function.invoke()
            } else {
                showToast(requireContext().getString(common_text_permissions_denied))
            }
        }
    }

    override fun setViewModelObservers() {
        observe(parentViewModel.recentBeneficiariesList, ::setAdapter)
    }
}
