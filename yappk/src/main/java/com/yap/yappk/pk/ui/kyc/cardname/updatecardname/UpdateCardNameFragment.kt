package com.yap.yappk.pk.ui.kyc.cardname.updatecardname

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.yap.uikit.widget.toolbarview.ToolBarView
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentUpdateCardNameBinding
import com.yap.yappk.pk.ui.kyc.cardname.cardnameconfirmation.CardNameConfirmationFragment
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateCardNameFragment :
    BaseNavFragment<FragmentUpdateCardNameBinding, IUpdateCardName.State, IUpdateCardName.ViewModel>(
        R.layout.fragment_update_card_name
    ), IUpdateCardName.View, ToolBarView.OnToolBarViewClickListener {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IUpdateCardName.ViewModel by viewModels<UpdateCardNameVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()
    private val CARD_NAME_REQUEST = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewBinding.tbView.setOnToolBarViewClickListener(this)
        setValues(viewModel.sessionManager.userAccount.value?.cnicName)
        viewModel.generateNameList(viewModel.viewState.cardName.value ?: "")
    }

    private fun handleNamesList(list: MutableList<String>) {
        setValues(list.first())
        viewModel.suggestedNameAdapter.setList(list)
        viewModel.suggestedNameAdapter.allowFullItemClickListener = true
        viewModel.suggestedNameAdapter.onItemClickListener = onItemClickListener
    }

    private val onItemClickListener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (pos == RecyclerView.NO_POSITION) {
                mViewBinding.btnNext.isEnabled = false
                return
            }
            // Updating old as well as new positions
            viewModel.suggestedNameAdapter.notifyItemChanged(viewModel.suggestedNameAdapter.selectedPosition.get())
            viewModel.suggestedNameAdapter.selectedPosition.set(pos)
            viewModel.suggestedNameAdapter.notifyItemChanged(viewModel.suggestedNameAdapter.selectedPosition.get())
            mViewBinding.btnNext.isEnabled = true

            viewModel.viewState.cardName.value = data as String
            viewModel.viewState.count.value = (data as String).length.toString()
            viewModel.viewState.isValid.value = (data as String).isNotEmpty()
        }
    }


    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> moveNext()
        }
    }

    private fun moveNext() {
        navigateBackWithResult(
            CARD_NAME_REQUEST,
            bundleOf(UpdateCardNameFragment::class.java.name to viewModel.viewState.cardName.value?.trim())
        )
    }

    override fun getFragmentArguments() {
        arguments?.let {
            setValues(it.getString(CardNameConfirmationFragment::class.java.name))
        }
    }

    private fun setValues(name: String?) {
        viewModel.viewState.cardName.value = name
        viewModel.viewState.isValid.value =
            (viewModel.viewState.cardName.value?.isNotEmpty() == true && viewModel.viewState.cardName.value?.length ?: 0 < viewModel.nameLength)
        viewModel.viewState.count.value = viewModel.viewState.cardName.value?.length.toString()
    }

    override fun onStartIconClicked() {
        requireActivity().onBackPressed()
    }

    override fun viewModelObservers() {
        observe(viewModel.namesList, ::handleNamesList)
    }

}