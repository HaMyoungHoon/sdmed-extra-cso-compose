package sdmed.extra.cso.utils

import android.content.Context
import com.auth0.android.jwt.JWT
import com.google.gson.Gson
import sdmed.extra.cso.bases.FBaseViewModel
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.fDate.FDateTime2
import sdmed.extra.cso.models.RestResultT
import sdmed.extra.cso.models.retrofit.FRetrofitVariable
import sdmed.extra.cso.models.retrofit.users.UserMultiLoginModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object FAmhohwa {
    fun decodeUtf8(data: String): String {
        val encoded = data.toCharArray().joinToString("") {
            "%${it.code.toString(16).padStart(2, '0')}"
        }
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8.name())
    }
    fun intervalBetweenDate(expiredDate: Long): Boolean {
        val now = FDateTime2().setThis(System.currentTimeMillis())
        val expired = FDateTime2().setThis(expiredDate * 1000).addDays(-10)
        // 이거 나중에 수치 좀 바꿔야겠다
        return expired.getDaysBetween(now) > 0
    }
    fun tokenIntervalValid(token: String?): Boolean {
        if (token == null) return false
        try {
            if (intervalBetweenDate(JWT(token).claims[FConstants.CLAIMS_EXP]?.asLong() ?: 0)) {
                return false
            }
            return true
        } catch (_: Exception) {
            return false
        }
    }
    fun checkInvalidToken(context: Context): Boolean {
        if (FRetrofitVariable.token.value.isNullOrEmpty()) {
            FRetrofitVariable.token.value = FStorage.getAuthToken(context)
        }
        val tokenRefresh = FRetrofitVariable.token.value ?: return false
        return tokenIntervalValid(tokenRefresh)
    }
    fun rhsTokenIsMost(newToken: String): Boolean {
        val tokenAccess = FRetrofitVariable.token.value ?: return true
        return try {
            val previousLong = JWT(tokenAccess).claims[FConstants.CLAIMS_EXP]?.asLong() ?: 0
            val newLong = JWT(newToken).claims[FConstants.CLAIMS_EXP]?.asLong() ?: 0
            newLong >= previousLong
        } catch (_: Exception) {
            true
        }
    }
    fun logout(context: Context?, expired: Boolean = false) {
        context ?: return
        removeLoginData(context)
        FStorage.logoutMultiLoginData(context)
//        FExtensions.moveToLandingActivity(context, expired)
    }
    fun removeLoginData(context: Context) {
        FRetrofitVariable.token.value = ""
        FStorage.removeAuthToken(context)
    }
    fun addMultiLoginData(context: Context) {
        FStorage.addMultiLoginData(context, UserMultiLoginModel().apply {
            thisPK = getThisPK(context)
            id = getTokenID(context)
            name = getTokenName(context)
            token = FStorage.getAuthToken(context) ?: ""
            isLogin = true
        })
    }
    fun getUserID(context: Context): String {
        return decodeUtf8(getTokenID(context))
    }
    fun getUserName(context: Context): String {
        return decodeUtf8(getTokenName(context))
    }
    fun getTokenID(context: Context): String {
        val token = FRetrofitVariable.token.value ?: FStorage.getAuthToken(context) ?: return ""
        return try {
            JWT(token).subject ?: ""
        } catch (_: Exception) {
            ""
        }
    }
    fun getTokenName(context: Context): String {
        val token = FRetrofitVariable.token.value ?: FStorage.getAuthToken(context) ?: return ""
        return try {
            JWT(token).claims[FConstants.CLAIMS_NAME]?.asString() ?: ""
        } catch (_: Exception) {
            ""
        }
    }
    fun getThisPK(context: Context): String {
        val token = FRetrofitVariable.token.value ?: FStorage.getAuthToken(context) ?: return ""
        return try {
            JWT(token).claims[FConstants.CLAIMS_INDEX]?.asString() ?: ""
        } catch (_: Exception) {
            ""
        }
    }
    fun <T> toJson(data: T): String {
        return try {
            Gson().toJson(data)
        } catch (_: Exception) {
            ""
        }
    }
    fun <T> fromJson(data: String, clazz: Class<T>): T? {
        return try {
            Gson().fromJson(data, clazz)
        } catch (_: Exception) {
            null
        }
    }
    fun urlEncoder(data: String): String {
        return URLEncoder.encode(data)
    }

    fun tokenCheck(dataContext: FBaseViewModel): Boolean {
        val context = dataContext.context
        if (FRetrofitVariable.token.value == null) {
            FRetrofitVariable.token.value = FStorage.getAuthToken(context)
        }
        if (FRetrofitVariable.token.value.isNullOrBlank()) {
            return false
        }
        return checkInvalidToken(context)
    }
    fun tokenRefresh(dataContext: FBaseViewModel, ret: ((RestResultT<String>) -> Unit)? = null) {
        if (FRetrofitVariable.refreshing.get()) {
            return
        }
        if (FRetrofitVariable.token.value.isNullOrBlank()) {
            ret?.invoke(RestResultT<String>().setFail())
            return
        }
        FRetrofitVariable.refreshing.set(true)
        val context = dataContext.context
        FCoroutineUtil.coroutineScope({
            val buff = dataContext.tokenRefresh()
            FRetrofitVariable.refreshing.set(false)
            if (buff.result == true) {
                val newToken = buff.data ?: ""
                if (rhsTokenIsMost(newToken)) {
                    FStorage.setAuthToken(context, newToken)
                    addLoginData(context)
                }
                FRetrofitVariable.token.value = FStorage.getAuthToken(context)
            } else {
                if (buff.code == -10002) {
                    delLoginData(context)
                }
                FStorage.removeAuthToken(context)
                FRetrofitVariable.token.value = null
            }
            ret?.invoke(buff)
        })
    }
    fun addLoginData(context: Context) {
        FStorage.addMultiLoginData(context, UserMultiLoginModel().apply {
            thisPK = getThisPK(context)
            id = getTokenID(context)
            name = getTokenName(context)
            token = FStorage.getAuthToken(context) ?: ""
            isLogin = true
        })
    }
    fun delLoginData(context: Context) {
        FStorage.delMultiLoginData(context, UserMultiLoginModel().apply {
            thisPK = getThisPK(context)
            id = getUserID(context)
            name = getUserName(context)
            token = FStorage.getAuthToken(context) ?: ""
            isLogin = true
        })
    }
}