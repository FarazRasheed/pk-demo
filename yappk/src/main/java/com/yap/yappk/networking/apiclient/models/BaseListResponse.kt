package com.yap.yappk.networking.apiclient.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.yap.yappk.networking.apiclient.base.BaseApiResponse
import kotlinx.parcelize.Parcelize

@Parcelize
open class BaseListResponse<T : Any> : BaseApiResponse(), Parcelable {
    @SerializedName("data")
    var data: MutableList<T>? = mutableListOf()
}