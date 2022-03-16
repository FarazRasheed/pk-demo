package com.yap.yappk.pk.ui.kyc.secretquestions.mothersmaiden

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentMotherMaidenNameBinding
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import com.yap.yappk.pk.utils.KYC_SECRET_ANSWER_MOTHER_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MothersMaidenNameFragment :
    BaseNavFragment<FragmentMotherMaidenNameBinding, IMotherMaidenName.State, IMotherMaidenName.ViewModel>(
        R.layout.fragment_mother_maiden_name
    ), IMotherMaidenName.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IMotherMaidenName.ViewModel by viewModels<MotherMaidenNameVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> {
                navigate(
                    R.id.action_mothersMaidenNameFragment_to_placeOfBirthFragment,
                    bundleOf(KYC_SECRET_ANSWER_MOTHER_NAME to viewModel.mothersName)
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.setProgressVisibility(true)
        parentViewModel.setProgress(75)
    }

    private fun handleMotherNamesList(list: MutableList<String>) {
        viewModel.secretQuestionsAdapter.setList(list)
        viewModel.secretQuestionsAdapter.allowFullItemClickListener = true
        viewModel.secretQuestionsAdapter.onItemClickListener = onItemClickListener
    }

    private val onItemClickListener: OnItemClickListener = object : OnItemClickListener {
        override fun onItemClick(view: View, data: Any, pos: Int) {
            if (pos == RecyclerView.NO_POSITION) {
                mViewBinding.btnNext.isEnabled = false
                return
            }

            // Updating old as well as new positions
            viewModel.secretQuestionsAdapter.notifyItemChanged(viewModel.secretQuestionsAdapter.selectedPosition.get())
            viewModel.secretQuestionsAdapter.selectedPosition.set(pos)
            viewModel.secretQuestionsAdapter.notifyItemChanged(viewModel.secretQuestionsAdapter.selectedPosition.get())
            mViewBinding.btnNext.isEnabled = true
            viewModel.mothersName = data as String
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.motherNamesList, ::handleMotherNamesList)
    }
}