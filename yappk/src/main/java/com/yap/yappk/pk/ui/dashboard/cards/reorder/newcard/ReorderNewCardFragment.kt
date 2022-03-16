package com.yap.yappk.pk.ui.dashboard.cards.reorder.newcard

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BackNavigationResult
import com.digitify.core.base.BackNavigationResultListener
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentReorderNewCardBinding
import com.yap.yappk.localization.common_button_next
import com.yap.yappk.localization.getString
import com.yap.yappk.localization.screen_reorder_address_selection_display_text_description
import com.yap.yappk.localization.screen_reorder_address_selection_display_text_header
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getCardImageResource
import com.yap.yappk.pk.ui.dashboard.cards.reorder.main.IReorderCard
import com.yap.yappk.pk.ui.dashboard.cards.reorder.main.ReorderCardVM
import com.yap.yappk.pk.ui.generic.address.GenericAddressDataModel
import com.yap.yappk.pk.ui.generic.address.GenericAddressSelectionFragment
import com.yap.yappk.pk.utils.ADDRESS_DATA_MODEL
import com.yap.yappk.pk.utils.ADDRESS_MODEL
import com.yap.yappk.pk.utils.GENERIC_ADDRESS_RESULT_CODE
import com.yap.yappk.pk.utils.ImageBinder.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReorderNewCardFragment :
    BaseNavFragment<PkFragmentReorderNewCardBinding, IReorderNewCard.State, IReorderNewCard.ViewModel>(
        R.layout.pk_fragment_reorder_new_card
    ), ToolBarView.OnToolBarViewClickListener, IReorderNewCard.View, BackNavigationResultListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IReorderNewCard.ViewModel by viewModels<ReorderNewCardVM>()
    private val parentViewModel: IReorderCard.ViewModel by activityViewModels<ReorderCardVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
        viewModel.requestGetAddressForPhysicalCard()
        viewModel.getPhysicalCardFee()
        viewModel.getCardBalance(parentViewModel.card?.cardSerialNumber)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        mViewBinding.ivReorderCard.loadImage(
            parentViewModel.card?.frontImage,
            ContextCompat.getDrawable(
                requireContext(),
                parentViewModel.card?.getCardImageResource() ?: R.drawable.pk_card_spare
            ),
            ContextCompat.getDrawable(requireContext(), R.drawable.pk_bg_card_place_holder)
        )
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.tvLocationEdit -> navigateForResult(
                requestCode = GENERIC_ADDRESS_RESULT_CODE,
                resId = R.id.action_reorderCardFragment_to_pk_address_selection_navigation,
                args = bundleOf(
                    ADDRESS_DATA_MODEL to GenericAddressDataModel(
                        toolbarTitle = requireContext().getString(
                            screen_reorder_address_selection_display_text_header
                        ),
                        title = viewModel.viewState.cardAddressTitle?.value,
                        subTitle = requireContext().getString(
                            screen_reorder_address_selection_display_text_description
                        ), buttonText = requireContext().getString(common_button_next)
                    ), ADDRESS_MODEL to viewModel.address?.value
                )
            )
            R.id.btnConfirmPurchase -> {
                viewModel.address?.value.also {
                    it?.cardSerialNumber = parentViewModel.card?.cardSerialNumber
                }
                viewModel.reorderDebitCard(viewModel.address?.value)
            }
            R.id.tvDoItLater -> setBackData()
        }
    }

    override fun onStartIconClicked() {
        setBackData()
    }

    private fun setBackData() {
        val activity = requireActivity()
        val intent = activity.intent
        intent.putExtra("cardPosition", "1")
        activity.setResult(Activity.RESULT_OK, intent)
        activity.finish()
    }

    private fun navigateNext(reorderSuccess: Boolean) {
        if (reorderSuccess) navigate(R.id.action_reorderCardFragment_to_reorderNewCardSuccessFragment)
    }

    override fun onNavigationResult(result: BackNavigationResult) {
        if (result.requestCode == GENERIC_ADDRESS_RESULT_CODE) {
            result.data?.let { address ->
                viewModel.address?.value =
                    address.getParcelable(GenericAddressSelectionFragment::class.java.name)
                viewModel.viewState.cardAddressTitle?.value = viewModel.address?.value?.address1
                viewModel.viewState.cardAddressSubTitle?.value = viewModel.address?.value?.address2
            }
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.reorderCardSuccess, ::navigateNext)
    }
}
