package com.yap.yappk.pk.ui.auth.forgotpasscode.newpasscodesuccess

import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentNewPasscodeSuccessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPasscodeSuccessFragment :
    BaseNavFragment<FragmentNewPasscodeSuccessBinding, INewPasscodeSuccess.State, INewPasscodeSuccess.ViewModel>(
        R.layout.fragment_new_passcode_success
    ), INewPasscodeSuccess.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: INewPasscodeSuccess.ViewModel by viewModels<NewPasscodeSuccessVM>()

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnDone -> requireActivity().onBackPressed()
        }
    }

}