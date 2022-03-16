package com.yap.yappk.pk.ui.kyc.cardname.cardnameconfirmation

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BackNavigationResult
import com.digitify.core.base.BackNavigationResultListener
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.observeEvent
import com.digitify.core.utils.SingleEvent
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentCardNameConfirmationBinding
import com.yap.yappk.pk.SessionManager
import com.yap.yappk.pk.ui.kyc.cardname.updatecardname.UpdateCardNameFragment
import com.yap.yappk.pk.ui.kyc.enums.CardSchemeEnum
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import com.yap.yappk.pk.utils.enums.PKAccountStatus
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val CARD_NAME_REQUEST = 1000

@AndroidEntryPoint
class CardNameConfirmationFragment :
    BaseNavFragment<FragmentCardNameConfirmationBinding, ICardNameConfirmation.State, ICardNameConfirmation.ViewModel>(
        R.layout.fragment_card_name_confirmation
    ), ICardNameConfirmation.View, BackNavigationResultListener,
    ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ICardNameConfirmation.ViewModel by viewModels<CardNameConfirmationVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnFine -> viewModel.saveCardName(viewModel.viewState.cardName.value?.trim() ?: "")
            R.id.btnEdit -> openUpdateNameFragment()
        }
    }

    private fun openUpdateNameFragment() {
        navigateForResult(
            requestCode = CARD_NAME_REQUEST,
            resId = R.id.action_cardNameConfirmationFragment_to_updateCardNameFragment,
            args = bundleOf(CardNameConfirmationFragment::class.java.name to viewModel.viewState.cardName.value)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setValues()
        viewModelObservers()
    }

    override fun onStartIconClicked() {
        navigateBack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        setCardImage()
    }

    private fun setCardImage() {
        when (parentViewModel.cardScheme?.schemeName) {
            CardSchemeEnum.PayPak.scheme -> {
                mViewBinding.ivCard.setImageResource(R.drawable.pk_spare_card_paypak)
            }
            else -> {
                mViewBinding.ivCard.setImageResource(R.drawable.pk_card_spare)
            }
        }
    }

    private fun setValues() {
        viewModel.viewState.cardName.value =
            if (sessionManager.userAccount.value?.cnicName?.length ?: 0 < 27)
                sessionManager.userAccount.value?.cnicName
            else
                sessionManager.userAccount.value?.cnicName?.substring(0, 27)
    }

    private fun openAddressScreen(navigationEvent: SingleEvent<Int>) {
        navigationEvent.getContentIfNotHandled()?.let { destinationId ->
            navigate(destinationId)
        }
    }

    private fun openCardDashboard(navigationEvent: SingleEvent<Int>) {
        navigationEvent.getContentIfNotHandled()?.let { destinationId ->
            navigate(destinationId)
        }
    }

    private fun cardNameSaved(isCardNameSaved: Boolean) {
        if (isCardNameSaved) {
            if (parentViewModel.cardScheme?.fee ?: 0.0 > 0.0)
                navigateToNextScreen()
            else
                viewModel.openAddressScreen()
        }
    }

    private fun navigateToNextScreen() {
        when (sessionManager.userAccount.value?.notificationStatuses) {
            PKAccountStatus.CARD_SCHEME_WITH_EXTERNAL_CARD_PENDING.name -> viewModel.openCardDashboardScreen()
            else -> viewModel.openAddCardScreen()
        }
    }

    private fun openAddCardPaymentScreen(navigationEvent: SingleEvent<Int>) {
        navigationEvent.getContentIfNotHandled()?.let { destinationId ->
            navigate(destinationId)
        }
    }

    override fun onNavigationResult(result: BackNavigationResult) {
        viewModel.viewState.cardName.value =
            result.data?.getString(UpdateCardNameFragment::class.java.name)
    }

    override fun viewModelObservers() {
        observeEvent(viewModel.openCardPaymentAddCard, ::openAddCardPaymentScreen)
        observeEvent(viewModel.openAddressConfirmation, ::openAddressScreen)
        observeEvent(viewModel.openCardDashBoard, ::openCardDashboard)
        observe(viewModel.isCardNameSaved, ::cardNameSaved)
    }

}