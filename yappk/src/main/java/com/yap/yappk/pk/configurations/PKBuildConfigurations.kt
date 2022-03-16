package com.yap.yappk.pk.configurations

import android.content.Context
import android.util.Base64
import com.digitify.core.analytics.AnalyticsEvent
import com.digitify.core.configurations.BuildConfigs
import com.digitify.core.enums.ProductFlavour
import com.digitify.core.utils.KEY_APP_UUID
import com.digitify.core.utils.REFERRAL_ID
import com.digitify.core.utils.REFERRAL_TIME
import com.digitify.core.utils.SharedPreferenceManager
import com.yap.yappk.networking.apiclient.base.RetroNetwork
import com.yap.yappk.networking.apiclient.base.interfaces.NetworkConstraintsListener
import com.yap.yappk.networking.microservices.customers.responsedtos.accountinfo.AccountInfo
import com.yap.yappk.pk.utils.enums.PkAppEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


/**
Created by Faheem Riaz on 16/08/2021.
 **/
@Singleton
class PKBuildConfigurations @Inject constructor(@ApplicationContext val context: Context) {

    var configManager: BuildConfigurations? = null
    private var adjustAppId: String = ""

    private val retroNetwork: RetroNetwork = RetroNetwork(context)

    fun configure(
        config: BuildConfigs,
        googleMapsApiKey: String,
        analyticsEvent: AnalyticsEvent? = null,
        callBack: (event: PkAppEvent) -> Unit,
    ): BuildConfigurations {
        val productFlavour = when (config.flavour) {
            ProductFlavour.DEV.flavour -> {
                ProductFlavour.DEV
            }
            ProductFlavour.QA.flavour -> {
                ProductFlavour.QA
            }
            ProductFlavour.STG.flavour -> {
                ProductFlavour.STG
            }
            ProductFlavour.PREPROD.flavour -> {
                ProductFlavour.PREPROD
            }
            ProductFlavour.PROD.flavour -> {
                ProductFlavour.PROD
            }
            else -> ProductFlavour.INTERNAL
        }


        configManager = BuildConfigurations(
            md5 = md5(productFlavour).decode(),
            sha1 = sha1(productFlavour).decode(),
            sha256 = sha256(productFlavour).decode(),
            leanPlumSecretKey = leanPlumKey(productFlavour, config.buildType).first,
            leanPlumKey = leanPlumKey(productFlavour, config.buildType).second,
            adjustToken = adjustToken(productFlavour),
            baseUrl = baseUrl(productFlavour),
            buildType = config.buildType,
            flavor = config.flavour,
            versionName = config.versionName,
            versionCode = config.versionCode,
            applicationId = config.applicationId,
            sslPin1 = sslPin1(productFlavour),
            sslPin2 = sslPin2(productFlavour),
            sslPin3 = sslPin3(productFlavour),
            sslHost = sslHost(productFlavour),
            googleMapsApiKey = googleMapsApiKey,
            analyticsEvent = analyticsEvent,
            callBack = callBack,
        )
        retroNetwork.build(configManager?.baseUrl ?: "")
        retroNetwork.listenNetworkConstraints(networkListener)
        setAppUniqueId()
        AppConfigurations.init(configManager, retroNetwork)

        return configManager!!
    }

    private val networkListener = object : NetworkConstraintsListener {
        override fun onInternetUnavailable() = Unit

        override fun onCacheUnavailable() = Unit

        override fun onSessionInvalid() {
            configManager?.callBack?.invoke(PkAppEvent.LOGOUT)
        }

    }

    private fun sha1(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD, ProductFlavour.PREPROD -> "ODU6OUY6NjM6N0M6NjI6N0I6Qjc6N0E6MDg6RTQ6OEI6MDY6OUU6M0U6MkQ6RTU6MEQ6OEM6Mjg6MjU="
            ProductFlavour.STG -> "REI6QTg6REE6OTg6RUY6ODA6QkY6ODQ6MDQ6RDE6NzM6Rjg6QzE6RjE6QzA6MTU6NTk6MjA6MTY6RDI="
            ProductFlavour.QA -> ""
            ProductFlavour.DEV -> ""
            ProductFlavour.INTERNAL -> ""
        }
    }

    private fun md5(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD, ProductFlavour.PREPROD -> "MDg6NzM6ODQ6RTI6NEM6NTc6RTU6MUU6OEY6ODU6RTM6OTg6MUM6NDM6Qjg6NEE="
            ProductFlavour.STG -> "MjU6ODQ6MUY6RTE6RjE6QTg6QzI6NTg6N0I6QUU6RUE6QjM6NDE6NjU6NzY6RkU="
            ProductFlavour.QA -> ""
            ProductFlavour.DEV -> ""
            ProductFlavour.INTERNAL -> ""
        }
    }

    private fun sha256(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "ODY6QTE6MzQ6NEU6RkM6OTQ6M0I6NzA6Mjk6MjE6OUU6M0I6NzA6MzM6NDI6RUM6M0M6NjI6M0E6MkI6MEU6N0M6QkM6MDc6RTU6N0Q6M0M6Mjk6RTg6MkE6Q0Y6NTM="
            ProductFlavour.PREPROD -> "ODY6QTE6MzQ6NEU6RkM6OTQ6M0I6NzA6Mjk6MjE6OUU6M0I6NzA6MzM6NDI6RUM6M0M6NjI6M0E6MkI6MEU6N0M6QkM6MDc6RTU6N0Q6M0M6Mjk6RTg6MkE6Q0Y6NTM="
            ProductFlavour.STG -> "QTQ6QUM6MTQ6RjM6REQ6RDg6NTc6RTk6RkM6QUM6N0M6MDk6NkM6QTQ6MEQ6RUM6QjU6MEU6RTE6OTY6QTI6RjA6Qjc6Q0M6QjA6MEY6MDc6MDA6Qzc6N0M6RjM6Qjg="
            ProductFlavour.QA -> ""
            ProductFlavour.DEV -> ""
            ProductFlavour.INTERNAL -> ""
        }
    }

    private fun baseUrl(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "https://pk-prod.yap.com/"
            ProductFlavour.PREPROD -> "https://pk-preprod.yap.com/"
            ProductFlavour.STG -> "https://pk-stg.yap.co/"
            ProductFlavour.QA -> "https://pk-qa.yap.co/"
            ProductFlavour.DEV -> "https://pk-dev.yap.co/"
            ProductFlavour.INTERNAL -> "https://pk-stg.yap.co/"
        }
    }

    private fun adjustToken(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "xty7lf6skgsg"
            ProductFlavour.PREPROD -> "uv1oiis7wni8"
            ProductFlavour.STG -> "am0wjeshw5xc"
            ProductFlavour.QA -> "pa4xup5ybrwg"    // Yap Pakistan QA token
            ProductFlavour.DEV -> "pa4xup5ybrwg"      // Yap Pakistan QA token
            ProductFlavour.INTERNAL -> "am0wjeshw5xc"
        }
    }

    private fun sslPin1(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "sha256/SK10shgwb9jAeBvxJXrkBmjL2joCFoSq2Sp1tGyOcQk="
            ProductFlavour.PREPROD -> "sha256/SK10shgwb9jAeBvxJXrkBmjL2joCFoSq2Sp1tGyOcQk="
            ProductFlavour.STG -> "sha256/ZrRL6wSXl/4lm1KItkcZyh56BGOoxMWUDJr7YVqE4no="
            ProductFlavour.QA, ProductFlavour.DEV, ProductFlavour.INTERNAL -> "sha256/e5L5CAoQjV0HFzAnunk1mPHVx1HvPxcfJYI0UtLyBwY="
        }
    }

    private fun sslPin2(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD, ProductFlavour.PREPROD, ProductFlavour.STG -> "sha256/8Rw90Ej3Ttt8RRkrg+WYDS9n7IS03bk5bjP/UXPtaY8="
            ProductFlavour.QA, ProductFlavour.DEV, ProductFlavour.INTERNAL -> "sha256/JSMzqOOrtyOT1kmau6zKhgT676hGgczD5VMdRMyJZFA="
        }
    }

    private fun sslPin3(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD, ProductFlavour.PREPROD, ProductFlavour.STG -> "sha256/Ko8tivDrEjiY90yGasP6ZpBU4jwXvHqVvQI0GS3GNdA="
            ProductFlavour.QA, ProductFlavour.DEV, ProductFlavour.INTERNAL -> "sha256/++MBgDH5WGvL9Bcn5Be30cRcL0f5O+NyoXuWtQdX1aI="
        }
    }

    private fun sslHost(productFlavour: ProductFlavour): String {
        return when (productFlavour) {
            ProductFlavour.PROD -> "*.yap.com"
            ProductFlavour.PREPROD -> "*.yap.com"
            ProductFlavour.STG, ProductFlavour.QA, ProductFlavour.DEV, ProductFlavour.INTERNAL -> "*.yap.co"
        }
    }

    private fun leanPlumKey(
        productFlavour: ProductFlavour,
        buildType: String
    ): Pair<String, String> {

        return if (productFlavour == ProductFlavour.DEV && buildType == "debug") {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "dev_2ssrA8Mh1BazUIZHqIQabRP0a76cQwZ1MYfHsJpODMQ"
            )
        } else if (productFlavour == ProductFlavour.DEV && buildType == "release") {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "prod_KX4ktWrg5iHyP12VbRZ92U0SOVXyYrcWk5B68TfBAW0"
            )
        } else if (productFlavour == ProductFlavour.QA && buildType == "debug") {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "dev_2ssrA8Mh1BazUIZHqIQabRP0a76cQwZ1MYfHsJpODMQ"
            )
        } else if (productFlavour == ProductFlavour.QA && buildType == "release") {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "prod_KX4ktWrg5iHyP12VbRZ92U0SOVXyYrcWk5B68TfBAW0"
            )
        } else if (productFlavour == ProductFlavour.STG && buildType == "debug") {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "dev_2ssrA8Mh1BazUIZHqIQabRP0a76cQwZ1MYfHsJpODMQ"
            )
        } else if (productFlavour == ProductFlavour.STG && buildType == "release") {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "prod_KX4ktWrg5iHyP12VbRZ92U0SOVXyYrcWk5B68TfBAW0"
            )
        } else if (productFlavour == ProductFlavour.PREPROD && buildType == "debug") {
            Pair(
                "app_jvEgXTi9zZUpoFck8XVxVY4zBgAEYZrPVTliIuaO0IQ",
                "dev_HnmEVN0GDZbhInJjmX767e7InveRC23LkSokuLLuA3s"
            )
        } else if (productFlavour == ProductFlavour.PREPROD && buildType == "release") {
            Pair(
                "app_jvEgXTi9zZUpoFck8XVxVY4zBgAEYZrPVTliIuaO0IQ",
                "prod_EjIC6dCuGaGr36p2qRvG3GkRIhuYf9vgBEGjQ3jBqLM"
            )
        } else if (productFlavour == ProductFlavour.PROD && buildType == "debug") {
            Pair(
                "app_DtOp3ipxDUi9AM7Bg3jv351hZ4DVrLgC9JZX4L46lIc",
                "dev_RAFVBmDKypdOr3kbd326JUoqGLr8iSvt2Lei4BK48qk"
            )
        } else if (productFlavour == ProductFlavour.PROD && buildType == "release") {
            Pair(
                "app_DtOp3ipxDUi9AM7Bg3jv351hZ4DVrLgC9JZX4L46lIc",
                "prod_MfjUF6Sh3GuNE2RtQMkXZTeCUSTS3K0v2CLeGCp0gzk"
            )
        } else {
            Pair(
                "app_OjUbwCEcWfawOQzYABPyg5R7y9sFLgFm9C1JdgIa3Qk",
                "prod_MfjUF6Sh3GuNE2RtQMkXZTeCUSTS3K0v2CLeGCp0gzk"
            )
        }
    }

    private fun String.decode(): String {
        return Base64.decode(this, Base64.DEFAULT).toString(charset("UTF-8"))
    }

    private fun setAppUniqueId() {
        val sharedPreferenceManager = SharedPreferenceManager(context)
        var uuid: String?
        uuid = sharedPreferenceManager.getValueString(KEY_APP_UUID)
        if (uuid == null) {
            uuid = UUID.randomUUID().toString()
            sharedPreferenceManager.save(KEY_APP_UUID, uuid)
        }
    }

    fun getAdjustURL(userAccount: AccountInfo?): String {
        val serverDateFullFormat = "yyyy-MM-dd'T'HH:mm:ss"//2015-11-28 10:17:18
        val userId = userAccount?.currentCustomer?.customerId
        val time = getCurrentDateWithFormat(serverDateFullFormat)
        val queryParams = time.trim()
        val referUrl =
            "https://app.adjust.com/${adjustAppId}?deep_link=b2c_yap%3A%3A%2F%2Fyap_referral?${REFERRAL_ID}%3D$userId%26${REFERRAL_TIME}%3D${
                URLEncoder.encode(
                    queryParams,
                    "UTF-8"
                )
            }"

        return (when (configManager?.flavor) {
            ProductFlavour.PROD.flavour -> {
                "https://gqvg.adj.st?adjust_t=n44w5ee_6hpplis&${REFERRAL_ID}=$userId&${REFERRAL_TIME}=${time.trim()}"
            }
            ProductFlavour.PREPROD.flavour -> {
                "https://7s29.adj.st?adjust_t=v3jlxlh_oo71763&${REFERRAL_ID}=$userId&${REFERRAL_TIME}=${time.trim()}"
            }
            ProductFlavour.STG.flavour -> {
                "https://grwl.adj.st?adjust_t=q3o2z0e_sv94i35&${REFERRAL_ID}=$userId&${REFERRAL_TIME}=${time.trim()}"
            }
            ProductFlavour.INTERNAL.flavour -> {
                "https://grwl.adj.st?adjust_t=q3o2z0e_sv94i35&${REFERRAL_ID}=$userId&${REFERRAL_TIME}=${time.trim()}"
            }
            ProductFlavour.DEV.flavour, ProductFlavour.QA.flavour -> referUrl

            else -> throw IllegalStateException("Invalid build flavour found ${configManager?.flavor}")
        })
    }

    private fun getCurrentDateWithFormat(formal: String): String {
        val sdf = SimpleDateFormat(formal, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date())
    }

    fun setAdjustAppId(appId: String) {
        this.adjustAppId = appId
    }
}

