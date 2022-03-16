package com.yap.yappk.pk.ui.dashboard.more

import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentMoreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment :
    BaseNavFragment<FragmentMoreBinding, IMore.State, IMore.ViewModel>(R.layout.fragment_more) {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IMore.ViewModel by viewModels<MoreVM>()

    override fun onClick(id: Int) {
        when (id) {
        }
    }

}