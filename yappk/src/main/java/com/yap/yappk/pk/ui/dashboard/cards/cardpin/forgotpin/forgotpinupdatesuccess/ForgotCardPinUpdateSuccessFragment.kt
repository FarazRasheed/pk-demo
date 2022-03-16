package com.yap.yappk.pk.ui.dashboard.cards.cardpin.forgotpin.forgotpinupdatesuccess

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentForgotPinUpdateSuccessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotCardPinUpdateSuccessFragment :
    BaseNavFragment<FragmentForgotPinUpdateSuccessBinding, IForgotCardPinUpdateSuccess.State, IForgotCardPinUpdateSuccess.ViewModel>(
        R.layout.fragment_forgot_pin_update_success
    ), IForgotCardPinUpdateSuccess.View {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IForgotCardPinUpdateSuccess.ViewModel by viewModels<ForgotCardPinUpdateSuccessVM>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBackButtonDispatcher()
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.btnDone -> navigateBackWithResult(100)
        }
    }
}