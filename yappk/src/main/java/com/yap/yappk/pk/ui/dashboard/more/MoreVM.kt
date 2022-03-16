package com.yap.yappk.pk.ui.dashboard.more

import com.digitify.core.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreVM @Inject constructor(override val viewState: MoreState) :
    BaseViewModel<IMore.State>(), IMore.ViewModel