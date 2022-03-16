package com.yap.yappk.pk.ui.dashboard.cards.reorder.success

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.finish
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentReorderNewCardSuccessBinding
import com.yap.yappk.pk.ui.dashboard.cards.extensions.getCardImageResource
import com.yap.yappk.pk.ui.dashboard.cards.reorder.main.IReorderCard
import com.yap.yappk.pk.ui.dashboard.cards.reorder.main.ReorderCardVM
import com.yap.yappk.pk.utils.ImageBinder.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReorderNewCardSuccessFragment :
    BaseNavFragment<PkFragmentReorderNewCardSuccessBinding, IReorderNewCardSuccess.State, IReorderNewCardSuccess.ViewModel>(
        R.layout.pk_fragment_reorder_new_card_success
    ) {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IReorderNewCardSuccess.ViewModel by viewModels<ReorderNewCardSuccessVM>()
    private val parentViewModel: IReorderCard.ViewModel by activityViewModels<ReorderCardVM>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackButtonDispatcher()
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
            R.id.btnDone -> {
                setBackData()
                this.finish()
            }
        }
    }

    private fun setBackData() {
        val intent = requireActivity().intent
        intent.putExtra("cardPosition", "1")
        requireActivity().setResult(Activity.RESULT_OK, intent)
    }
}
