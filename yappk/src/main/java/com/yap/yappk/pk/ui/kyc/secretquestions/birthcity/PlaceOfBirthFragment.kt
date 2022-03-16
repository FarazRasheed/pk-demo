package com.yap.yappk.pk.ui.kyc.secretquestions.birthcity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.base.interfaces.OnItemClickListener
import com.digitify.core.extensions.observe
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentPlaceOfBirthBinding
import com.yap.yappk.networking.microservices.customers.requestsdtos.VerifySecretQuestionsRequest
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import com.yap.yappk.pk.utils.KYC_SECRET_ANSWER_MOTHER_NAME
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceOfBirthFragment :
    BaseNavFragment<FragmentPlaceOfBirthBinding, IPlaceOfBirth.State, IPlaceOfBirth.ViewModel>(
        R.layout.fragment_place_of_birth
    ), IPlaceOfBirth.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IPlaceOfBirth.ViewModel by viewModels<PlaceOfBirthVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnNext -> {
                val request = VerifySecretQuestionsRequest(
                    motherMaidenName = viewModel.mothersMaidenNameAnswer,
                    cityOfBirth = viewModel.placeOfBirthCityAnswer
                )
                viewModel.verifyQuestionAnswers(request)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
        viewModel.mothersMaidenNameAnswer = arguments?.getString(KYC_SECRET_ANSWER_MOTHER_NAME)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.setProgressVisibility(true)
        parentViewModel.setProgress(75)
    }


    private fun handleQuestionsAnswersVerified(isVerified: Boolean) {
        navigateWithPopup(
            R.id.action_placeOfBirthFragment_to_selfieGuideFragment,
            R.id.mothersMaidenNameFragment
        )
    }

    private fun handlePlaceOfBirthCitiesNamesList(list: MutableList<String>) {
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

            viewModel.placeOfBirthCityAnswer = data as String
        }
    }

    override fun viewModelObservers() {
        observe(viewModel.cityOfBirthNamesList, ::handlePlaceOfBirthCitiesNamesList)
        observe(viewModel.isVerified, ::handleQuestionsAnswersVerified)
    }
}