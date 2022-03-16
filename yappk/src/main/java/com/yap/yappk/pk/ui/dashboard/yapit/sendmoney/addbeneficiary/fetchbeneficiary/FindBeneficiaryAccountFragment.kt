package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.fetchbeneficiary

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.yap.uikit.widget.nameinitialscircleimageview.NameInitialsCircleImageView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentFindBeneficiaryAccountBinding
import com.yap.yappk.networking.microservices.customers.responsedtos.BankData
import com.yap.yappk.networking.microservices.customers.responsedtos.FindBankAccountInfoResponse
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.accountdetails.BeneficiaryAccountDetailsFragment
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.AddBeneficiaryVM
import com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.addbeneficiary.main.IAddBeneficiary
import com.yap.yappk.pk.utils.cancelAllSnackBar
import com.yap.yappk.pk.utils.popupwindow.Popup
import com.yap.yappk.pk.utils.popupwindow.RelativePopupWindow
import com.yap.yappk.pk.utils.showTextUpdatedAbleSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FindBeneficiaryAccountFragment :
    BaseNavFragment<PkFragmentFindBeneficiaryAccountBinding, IFindBeneficiaryAccount.State, IFindBeneficiaryAccount.ViewModel>(
        R.layout.pk_fragment_find_beneficiary_account
    ),
    IFindBeneficiaryAccount.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IFindBeneficiaryAccount.ViewModel by viewModels<FindBeneficiaryAccountVM>()
    private val parentViewModel: IAddBeneficiary.ViewModel by activityViewModels<AddBeneficiaryVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.toolBarVisibility.value = View.VISIBLE
        parentViewModel.stateList.value = viewModel.handleState()
        getFragmentArguments()
        initListeners()
    }

    override fun getFragmentArguments() {
        viewModel.setBankDetails(arguments?.getParcelable(FindBeneficiaryAccountFragment::class.java.name))
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnFindAccount -> {
                val ibanNumber =
                    if (mViewBinding.etBankNumber.text.toString().length >= viewModel.bankDetails.value?.ibanMinLength ?: 0) mViewBinding.etBankNumber.text.toString() else null
                val accountNumber =
                    if (mViewBinding.etBankNumber.text.toString().length < viewModel.bankDetails.value?.ibanMinLength ?: 0) mViewBinding.etBankNumber.text.toString() else null
                viewModel.fetchAccountInfoRequest(
                    accountNumber,
                    ibanNumber,
                    viewModel.bankDetails.value?.consumerId
                )
            }
        }
    }

    private fun initPopupWindow() {
        val popup = Popup(
            requireContext(),
            viewModel.bankDetails.value?.formatMessage ?: ""
        )
        popup.width = ViewGroup.LayoutParams.WRAP_CONTENT
        popup.height = ViewGroup.LayoutParams.WRAP_CONTENT
        popup.showOnAnchor(
            anchor = mViewBinding.tlAccountHolderNumber,
            vertPos = RelativePopupWindow.VerticalPosition.ABOVE,
            horizPos = RelativePopupWindow.HorizontalPosition.CENTER,
            x = 100,
            y = 0,
            fitInScreen = true
        )
    }

    override fun initListeners() {
        mViewBinding.tlAccountHolderNumber.setEndIconOnClickListener {
            initPopupWindow()
        }
    }

    private fun handleError(errorMessage: CharSequence) {
        if (errorMessage.isEmpty().not()) {
            showTextUpdatedAbleSnackBar(
                msg = errorMessage,
                viewBgColor = R.color.pkWarningBackground,
                colorOfMessage = R.color.pkWarning,
                gravity = Gravity.TOP
            )
        } else cancelAllSnackBar()
    }

    private fun setScreenData(bankDetail: BankData) {
        mViewBinding.ivBankLogo.setImageInfo(
            NameInitialsCircleImageView.ImageInfo
                .Builder(bankDetail.bankName ?: "")
                .setIndex(0)
                .setImageUrl(bankDetail.bankLogoUrl)
                .build()
        )
    }

    private fun getBankAccountInfo(bankAccountInfo: FindBankAccountInfoResponse) {
        navigate(
            R.id.action_findBeneficiaryAccountFragment_to_beneficiaryAccountDetailsFragment, bundleOf(
                BeneficiaryAccountDetailsFragment::class.java.name to viewModel.bankDetails.value
            )
        )
    }

    override fun viewModelObservers() {
        observe(viewModel.bankDetails, ::setScreenData)
        observe(viewModel.bankAccountData, ::getBankAccountInfo)
        observe(viewModel.errorDescription, ::handleError)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelAllSnackBar()
    }
}