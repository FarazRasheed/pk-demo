/*
 * *
 *  * Created by farazrasheed on 25/08/2021, 3:16 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 25/08/2021, 3:16 PM
 *
 */

package com.digitify.testyappakistan.onboarding.accountSelection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.digitify.testyappakistan.BR
import com.digitify.testyappakistan.R
import com.digitify.testyappakistan.databinding.FragmentAccountSelectionBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountSelectionFragment :
    BaseNavFragment<FragmentAccountSelectionBinding, IAccountSelection.State, IAccountSelection.ViewModel>
        (R.layout.fragment_account_selection),
    IAccountSelection.View {

    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IAccountSelection.ViewModel by viewModels<AccountSelectionVM>()

    @Inject
    lateinit var animation: AccountSelectionAnimation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animation.setupPlayer(mViewBinding.andExoPlayerView, mViewBinding.tvCaption)
    }

    override fun onClick(id: Int) {
        when (id) {
            R.id.tvSignIn -> {
                navigateWithPopup(
                    R.id.action_accountSelectionFragment_to_loginFragment,
                    R.id.accountSelectionFragment
                )
            }
            R.id.btnPersonal -> {
                navigate(R.id.action_accountSelectionFragment_to_mobileNumFragment)
            }
        }
    }

}