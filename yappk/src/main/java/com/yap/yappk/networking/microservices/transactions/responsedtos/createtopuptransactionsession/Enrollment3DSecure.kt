package com.yap.yappk.networking.microservices.transactions.responsedtos.createtopuptransactionsession

import com.google.gson.annotations.SerializedName

data class Enrollment3DSecure(@SerializedName("authenticationRedirect") val authenticationRedirect: AuthenticationRedirect)