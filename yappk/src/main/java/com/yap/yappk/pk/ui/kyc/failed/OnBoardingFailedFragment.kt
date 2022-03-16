package com.yap.yappk.pk.ui.kyc.failed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.observe
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentOnboardingFailedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFailedFragment :
    BaseNavFragment<PkFragmentOnboardingFailedBinding, IOnBoardingFailed.State, IOnBoardingFailed.ViewModel>(R.layout.pk_fragment_onboarding_failed),
    IOnBoardingFailed.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IOnBoardingFailed.ViewModel by viewModels<OnBoardingFailedVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFragmentArguments()?.let { onBoardingFailedStatus ->
            viewModel.setOnBoardingFailedStateBy(onBoardingFailedStatus)
        }
    }

    override fun getFragmentArguments(): String? {
        return arguments?.getString(OnBoardingFailedFragment::class.java.name)
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnDone -> {

            }
        }
    }

    private fun handleOnBoardingFailedState(dataModel: OnBoardingFailedModel) {
        mViewBinding.tvTitle.text = getString(dataModel.title)
        mViewBinding.tvSubTitle.text = getString(dataModel.description)
        mViewBinding.btnDone.text = getString(dataModel.primaryButtonTitle)
        mViewBinding.tvGoToDashboard.text = getString(dataModel.secondaryButtonTitle)
    }

    override fun viewModelObservers() {
        observe(viewModel.onBoardingFailedDataModel, ::handleOnBoardingFailedState)
    }
}