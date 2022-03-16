package com.yap.yappk.pk.ui.auth.main

import com.digitify.core.base.BaseState
import javax.inject.Inject

class AuthState @Inject constructor() : BaseState(), IAuth.State{
    override var isAccountBlocked: Boolean = false
}