package com.yap.yappk.pk.ui.dashboard.home

import com.digitify.core.base.BaseViewModel
import com.digitify.core.base.Dispatcher
import com.digitify.core.utils.SharedPreferenceManager
import com.yap.yappk.pk.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    override val viewState: HomeState,
    val sessionManager: SessionManager,
    val sharedPreferenceManager: SharedPreferenceManager,
    ) : BaseViewModel<IHome.State>(), IHome.ViewModel {

    override fun logout(accountUUID: String, success: () -> Unit) {
        launch(Dispatcher.Main) {
            showLoading(true)
            sessionManager.doLogOut(accountUUID) { errorMessage ->
                if (errorMessage.isNullOrBlank()) {
                    hideLoading()
                    success.invoke()
                } else {
                    showAlertMessage(errorMessage)
                    hideLoading()
                }
            }
        }
    }
}