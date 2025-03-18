package sdmed.extra.cso.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sdmed.extra.cso.bases.FConstants
import sdmed.extra.cso.models.retrofit.users.UserMultiLoginModel

object FStorage {
    fun sharedPreferences(context: Context): SharedPreferences = context.getSharedPreferences(
        FConstants.SHARED_FILE_NAME, Context.MODE_PRIVATE)
    fun cryptoSharedPreferences(context: Context) = EncryptedSharedPreferences.create(
        context,
        FConstants.SHARED_CRYPT_FILE_NAME,
        MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getAuthToken(context: Context) = getString(context, FConstants.AUTH_TOKEN)
    fun setAuthToken(context: Context, data: String?) = data?.let { putString(context, FConstants.AUTH_TOKEN, it) }
    fun removeAuthToken(context: Context) = removeData(context, FConstants.AUTH_TOKEN)
    fun getRefreshing(context: Context) = getBool(context, FConstants.TOKEN_REFRESHING)
    fun setRefreshing(context: Context, data: Boolean?) = data?.let { putBool(context, FConstants.TOKEN_REFRESHING, it) }
    fun getHomeMenuIndex(context: Context) = getInt(context, FConstants.HOME_MENU_INDEX)
    fun setHomeMenuIndex(context: Context, data: Int?) = data?.let { putInt(context, FConstants.HOME_MENU_INDEX, it) }
    fun getGoogleMapStyleIndex(context: Context) = getInt(context, FConstants.GOOGLE_MAP_STYLE_INDEX)
    fun setGoogleMapStyleIndex(context: Context, data: Int?) = data?.let { putInt(context, FConstants.GOOGLE_MAP_STYLE_INDEX, it) }
    fun getMultiLoginData(context: Context): List<UserMultiLoginModel>? = getList(context, FConstants.MULTI_LOGIN_TOKEN)
    fun setMultiLoginData(context: Context, data: List<UserMultiLoginModel>) = putList(context, FConstants.MULTI_LOGIN_TOKEN, data)
    fun logoutMultiLoginData(context: Context) {
        val item = getMultiLoginData(context) ?: return
        item.forEach { it.isLogin = false }
        removeMultiLoginData(context)
        setMultiLoginData(context, item)
    }
    fun addMultiLoginData(context: Context, data: UserMultiLoginModel?) {
        data ?: return
        try {
            val ret = mutableListOf<UserMultiLoginModel>().apply {
                val pastData = getMultiLoginData(context)
                if (!pastData.isNullOrEmpty()) {
                    addAll(pastData)
                }
            }.distinctBy { it }.toMutableList()
            ret.forEach { x -> x.isLogin = false }
            val findBuff = ret.find { it.thisPK == data.thisPK }
            if (findBuff == null) {
                ret.add(data)
            } else {
                findBuff.safeCopy(data)
            }
            removeMultiLoginData(context)
            setMultiLoginData(context, ret)
        } catch (_: Exception) {
            removeMultiLoginData(context)
        }
    }
    fun delMultiLoginData(context: Context, data: UserMultiLoginModel?) {
        data ?: return
        try {
            val ret = mutableListOf<UserMultiLoginModel>().apply {
                val pastData = getMultiLoginData(context)
                if (!pastData.isNullOrEmpty()) {
                    addAll(pastData)
                }
            }.distinctBy { it }.toMutableList()
            val findBuff = ret.find { it.thisPK == data.thisPK }
            if (findBuff != null) {
                ret.remove(findBuff)
            }
            removeMultiLoginData(context)
            setMultiLoginData(context, ret)
        } catch (_: Exception) {
            removeMultiLoginData(context)
        }
    }
    fun removeMultiLoginData(context: Context) = removeData(context, FConstants.MULTI_LOGIN_TOKEN)

    private fun getBool(context: Context, keyName: String, defData: Boolean = false) = cryptoSharedPreferences(context).getBoolean(keyName, defData)
    private fun putBool(context: Context, keyName: String, data: Boolean) = cryptoSharedPreferences(context).apply { edit { putBoolean(keyName, data) } }
    private fun getInt(context: Context, keyName: String, defData: Int = -1) = cryptoSharedPreferences(context).getInt(keyName, defData)
    private fun putInt(context: Context, keyName: String, data: Int) = cryptoSharedPreferences(context).apply { edit { putInt(keyName, data) } }
    private fun getFloat(context: Context, keyName: String, defData: Float = -1F) = cryptoSharedPreferences(context).getFloat(keyName, defData)
    private fun putFloat(context: Context, keyName: String, data: Float) = cryptoSharedPreferences(context).apply { edit { putFloat(keyName, data) } }
    private fun getString(context: Context, keyName: String, defData: String = "") = cryptoSharedPreferences(context).getString(keyName, defData)
    private fun putString(context: Context, keyName: String, data: String) = cryptoSharedPreferences(context).apply { edit { putString(keyName, data) } }
    inline fun <reified T> getModel(context: Context, keyName: String): T = try {
        Gson().fromJson(cryptoSharedPreferences(context).getString(keyName, ""), (object: TypeToken<T>() { }).type)
    } catch (_: Exception) {
        cryptoSharedPreferences(context).apply { edit { remove(keyName) } }
        T::class.java.getDeclaredConstructor().newInstance()
    }
    inline fun <reified T> putModel(context: Context, keyName: String, data: T) = try {
        val buff = Gson().toJson(data, (object: TypeToken<T>() { }).type)
        cryptoSharedPreferences(context).apply { edit { putString(keyName, buff) } }
    } catch (_: Exception) { }
    inline fun <reified T> getList(context: Context, keyName: String): List<T>? = try {
        Gson().fromJson(cryptoSharedPreferences(context).getString(keyName, ""), (object: TypeToken<List<T>>() { }).type)
    } catch (_: Exception) {
        cryptoSharedPreferences(context).apply { edit { remove(keyName) } }
        arrayListOf()
    }
    inline fun <reified T> putList(context: Context, keyName: String, data: List<T>) = try {
        val buff = Gson().toJson(data, (object: TypeToken<MutableList<T>>() { }).type)
        cryptoSharedPreferences(context).apply { edit { putString(keyName, buff) } }
    } catch (_: Exception) { }
    private fun removeData(context: Context, keyName: String) = cryptoSharedPreferences(context).apply { edit { remove(keyName) } }
    inline fun <reified T: Parcelable> Intent.putParcelable(key: String, data: T) = putExtra(key, data)
    inline fun <reified T: Parcelable> Intent.getParcelable(key: String): T? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key)
    }
    inline fun <reified T: Parcelable> Bundle.putParcelable(intent: Intent, key: String, data: T) = intent.putParcelable(key, data)
    inline fun <reified T: Parcelable> Bundle.getParcelable(intent: Intent, key: String): T? = intent.getParcelable(key)
    inline fun <reified T: Parcelable> Intent.putParcelableList(key: String, data: ArrayList<T>) = putParcelableArrayListExtra(key, data)
    inline fun <reified T: Parcelable> Intent.getParcelableList(key: String): ArrayList<T>? = when {
        Build.VERSION.SDK_INT >= 33 -> getParcelableArrayListExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
    }
    inline fun <reified T: Parcelable> Bundle.putParcelableList(intent: Intent, key: String, data: ArrayList<T>) = intent.putParcelableList(key, data)
    inline fun <reified T: Parcelable> Bundle.getParcelableList(intent: Intent, key: String): List<T>? = intent.getParcelableList(key)
}