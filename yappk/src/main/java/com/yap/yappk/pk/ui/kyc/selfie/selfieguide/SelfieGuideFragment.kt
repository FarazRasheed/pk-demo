package com.yap.yappk.pk.ui.kyc.selfie.selfieguide

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentSelfieGuideBinding
import com.yap.yappk.pk.ui.kyc.main.IKycDashboard
import com.yap.yappk.pk.ui.kyc.main.KycDashboardVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelfieGuideFragment :
    BaseNavFragment<FragmentSelfieGuideBinding, ISelfieGuide.State, ISelfieGuide.ViewModel>(R.layout.fragment_selfie_guide),
    ISelfieGuide.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: ISelfieGuide.ViewModel by viewModels<SelfieGuideVM>()
    private val parentViewModel: IKycDashboard.ViewModel by activityViewModels<KycDashboardVM>()

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnTakeSelfie -> {
                navigate(R.id.action_selfieGuideFragment_to_takeSelfieFragment)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentViewModel.setProgressVisibility(false)
    }
}