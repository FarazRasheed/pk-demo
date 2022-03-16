package com.yap.yappk.networking.microservices.customers.responsedtos

import com.google.gson.annotations.SerializedName

data class WaitingListResponse(
    @SerializedName("waitingNewRank") var rank: String?,
    @SerializedName("waitingBehind") var waitingBehind: String?,
    @SerializedName("jump") var jump: String?,
    @SerializedName("completedKyc") var completedKyc: Boolean?,
    @SerializedName("waiting") var waiting: Boolean?,
    @SerializedName("viewable") var viewable: Boolean?,
    @SerializedName("gainPoints") var gainPoints: String?,
    @SerializedName("inviteeDetails") var inviteeDetails: ArrayList<InviteeDetails>?,
    @SerializedName("totalGainedPoints") var totalGainedPoints: String?
)