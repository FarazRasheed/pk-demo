package com.yap.yappk.pk.ui.dashboard.yapit.sendmoney.banktransfer.bankbeneficiarydetail.updateprofileimage

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.core.extensions.hideSystemUI
import com.digitify.core.extensions.observe
import com.digitify.core.extensions.showSystemUI
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.PkFragmentUpdateBankBeneficiaryImageBinding
import com.yap.yappk.networking.microservices.customers.responsedtos.IBeneficiary
import com.yap.yappk.pk.utils.ImageBinder.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateBankBeneficiaryImageFragment :
    BaseNavFragment<PkFragmentUpdateBankBeneficiaryImageBinding, IUpdateBankBeneficiaryImage.State, IUpdateBankBeneficiaryImage.ViewModel>(
        R.layout.pk_fragment_update_bank_beneficiary_image
    ),
    IUpdateBankBeneficiaryImage.View {
    override fun getBindingVariable(): Int = BR.viewModel

    override val viewModel: IUpdateBankBeneficiaryImage.ViewModel by viewModels<UpdateBankBeneficiaryImageVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelObservers()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFragmentArguments()
        requireActivity().hideSystemUI(mViewBinding.root)
    }

    override fun getFragmentArguments() {
        arguments?.let {
            viewModel.setBeneficiary(it.getParcelable(IBeneficiary::class.java.name))
        }
    }

    private fun handleBeneficiary(iBeneficiary: IBeneficiary) {
        mViewBinding.ivImage.loadImage(
            resource = iBeneficiary.imgUri,
            errorRecourse = ContextCompat.getDrawable(requireContext(), R.drawable.pk_ic_profile)
        )
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.ivClose -> navigateBack()
            R.id.btnUsePhoto -> {
                viewModel.updateBeneficiary(
                    viewModel.beneficiary.value?.beneficiaryId ?: "",
                    viewModel.beneficiary.value?.imgUri
                )
            }
            R.id.btnRetake -> navigateBackWithResult(resultCode = Activity.RESULT_OK)
        }
    }

    private fun handleUpdateBeneficiaries(beneficiary: IBeneficiary?) {
        beneficiary?.let {
            navigateBackWithResult(
                resultCode = Activity.RESULT_OK,
                bundleOf(IBeneficiary::class.java.name to it)
            )
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().showSystemUI(mViewBinding.root)
    }

    override fun viewModelObservers() {
        observe(viewModel.beneficiaryUpdated, ::handleUpdateBeneficiaries)
        observe(viewModel.beneficiary, ::handleBeneficiary)
    }
}