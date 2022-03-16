package com.yap.yappk.pk.ui.dashboard.cards.carddetail.primarycarddetaildashboard.report

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.yap.uikit.utils.helpers.confirm
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentReportCardBinding
import com.yap.yappk.localization.*
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.CardDetailMainVM
import com.yap.yappk.pk.ui.dashboard.cards.carddetail.main.ICardDetailMain
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getCardImageResource
import com.yap.yappk.pk.ui.dashboard.cards.reorder.main.ReorderCardActivity
import com.yap.yappk.pk.utils.ImageBinder.loadImage
import com.yap.yappk.pk.utils.ItemCard
import com.yap.yappk.pk.utils.REASON_DAMAGE
import com.yap.yappk.pk.utils.REASON_LOST_STOLEN
import com.yap.yappk.pk.utils.formatCardString
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReportCardFragment :
    BaseNavFragment<PkFragmentReportCardBinding, IReportCard.State, IReportCard.ViewModel>(
        R.layout.pk_fragment_report_card
    ), IReportCard.View, ItemCard.OnCardItemClickListener, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IReportCard.ViewModel by viewModels<ReportCardVM>()
    private val parentViewModel: ICardDetailMain.ViewModel by activityViewModels<CardDetailMainVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
        viewModel.getHelpDeskNumber()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        mViewBinding.tvCardNumber.text = parentViewModel.card?.maskedCardNo.formatCardString()
        mViewBinding.icDamage.setOnItemCardClickListener(this)
        mViewBinding.icLostStolen.setOnItemCardClickListener(this)
        mViewBinding.ivReportCard.loadImage(
            parentViewModel.card?.frontImage,
            ContextCompat.getDrawable(
                requireContext(),
                parentViewModel.card?.getCardImageResource() ?: R.drawable.pk_card_spare
            ),
            ContextCompat.getDrawable(requireContext(), R.drawable.pk_bg_card_place_holder)
        )
    }

    override fun onClick(id: Int) {
        if (id != R.id.btnBlock) viewModel.toggleReportLostAndDamageCardOptions(id == R.id.icDamage)
        when (id) {
            R.id.btnBlock -> confirm(
                title = requireContext().getString(screen_report_lost_and_stolen_card_display_text_dialog_title),
                message = requireContext().getString(
                    screen_report_lost_and_stolen_card_display_text_dialog_description
                ),
                positiveButton = requireContext().getString(common_button_confirm),
                negativeButton = requireContext().getString(common_button_cancel),
                cancelable = false
            ) {
                viewModel.requestConfirmBlockCard(parentViewModel.card)
            }
            R.id.icDamage -> viewModel.HOT_LIST_REASON = REASON_DAMAGE
            R.id.icLostStolen -> viewModel.HOT_LIST_REASON = REASON_LOST_STOLEN
        }
    }

    override fun onItemClicked(view: View) {
        onClick(view.id)
    }

    override fun onItemIconClicked(view: View) {
        onClick(view.id)
    }

    override fun onItemTitleClicked(view: View) {
        onClick(view.id)
    }

    private fun navigateNext(isSuccess: Boolean) {
        if (isSuccess) startReorderFlow()
    }

    private fun startReorderFlow() {
        val intent = Intent(requireContext(), ReorderCardActivity::class.java)
        intent.putExtra(ReorderCardActivity::class.java.name, parentViewModel.card)
        activityLauncher.launch(intent) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.getStringExtra("cardPosition") != null) {
                requireActivity().setResult(Activity.RESULT_OK)
                requireActivity().finish()
            }
        }
    }

    override fun onStartIconClicked() {
        super.onStartIconClicked()
        requireActivity().onBackPressed()
    }

    override fun viewModelObservers() {
        observe(viewModel.reportCardSuccess, ::navigateNext)
    }

}
