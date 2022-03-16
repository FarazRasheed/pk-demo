package com.yap.yappk.pk.ui.dashboard.store

import androidx.fragment.app.viewModels
import com.digitify.core.base.BaseNavFragment
import com.yap.yappk.BR
import com.yap.yappk.R
import com.yap.yappk.databinding.FragmentStoreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoreFragment :
    BaseNavFragment<FragmentStoreBinding, IStore.State, IStore.ViewModel>(R.layout.fragment_store) {
    override fun getBindingVariable(): Int = BR.viewModel
    override val viewModel: IStore.ViewModel by viewModels<StoreVM>()

    override fun onClick(id: Int) {
        when (id) {
        }
    }

}