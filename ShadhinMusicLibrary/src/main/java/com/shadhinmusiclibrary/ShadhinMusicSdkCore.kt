package com.shadhinmusiclibrary

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.shadhinmusiclibrary.activities.MusicActivity
import com.shadhinmusiclibrary.activities.SDKMainActivity
import com.shadhinmusiclibrary.data.model.APIResponse
import com.shadhinmusiclibrary.data.model.SongDetailModel
import com.shadhinmusiclibrary.data.remote.ApiService
import com.shadhinmusiclibrary.di.ShadhinApp
import com.shadhinmusiclibrary.di.single.RetrofitClient
import com.shadhinmusiclibrary.di.single.SingleCallback
import com.shadhinmusiclibrary.di.single.SingleMusicServiceConnection
import com.shadhinmusiclibrary.di.single.SinglePlayerApiService
import com.shadhinmusiclibrary.fragments.home.HomeFragment
import com.shadhinmusiclibrary.library.player.data.model.MusicPlayList
import com.shadhinmusiclibrary.utils.AppConstantUtils
import com.shadhinmusiclibrary.utils.UtilHelper
import com.shadhinmusiclibrary.utils.UtilsOkHttp
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Keep
object ShadhinMusicSdkCore {
    private var backPressCount = 0
    private var scope: CoroutineScope? = null
    private var sdkCallback:ShadhinSDKCallback ?= null
    //get Music frangment
    @Deprecated("please use openMusic()")
    @JvmStatic
    fun getMusicFragment(): Fragment {
        return HomeFragment()
    }

    @JvmStatic
    fun initializeInternalSDK(context: Context) {
        ShadhinApp.module(context).musicServiceController
    }

    /**
    @param token required login auth code
    @param refSdkCall ShadhinSDKCallback
     */
    @JvmStatic
    fun initializeSDK(context: Context, token: String, refSdkCall: ShadhinSDKCallback) {
        SingleCallback.INSTANCE = refSdkCall
        scope = CoroutineScope(Dispatchers.IO)
        scope?.launch {
            val res = ShadhinApp.module(context).authRepository().login(token)
            withContext(Dispatchers.Main) {
                SingleCallback.INSTANCE?.tokenStatus(res.first, res.second ?: "")
            }
        }
    }

    @JvmStatic
    fun openMusic(reqContext: Context) {
        val intent = Intent(reqContext, SDKMainActivity::class.java)
        intent.putExtra(AppConstantUtils.UI_Request_Type, AppConstantUtils.HOME_PATCH)
        reqContext.startActivity(intent)
    }

    @JvmStatic
    fun openPatch(reqContext: Context, requestId: String) {
        reqContext.startActivity(
            Intent(
                reqContext,
                SDKMainActivity::class.java
            ).apply {
                putExtra(AppConstantUtils.UI_Request_Type, AppConstantUtils.Requester_Name_API)
                putExtra(AppConstantUtils.DataContentRequestId, requestId)
            }
        )
    }
    @JvmStatic
    fun openHomePatch(context: Context, patchCode: String) {
        context.startActivity(
            Intent(
                context,
                SDKMainActivity::class.java
            ).apply {
                putExtra(AppConstantUtils.UI_Request_Type, AppConstantUtils.RequesterHomePatch)
                putExtra(AppConstantUtils.DataContentRequestId, patchCode)
            }
        )
    }
    @JvmStatic
    fun openFromRC(context: Context, rc:String){
        context.startActivity(
            Intent(
                context,
                SDKMainActivity::class.java
            ).apply {
                putExtra(AppConstantUtils.UI_Request_Type, AppConstantUtils.RequesterRC)
                putExtra(AppConstantUtils.DataContentRequestId, rc)
            }
        )
    }

    @JvmStatic
    fun openRadio(reqContext: Context, radioId: String) {
        //todo retrieve playlist tracks by api
        getAndStartRadio(reqContext, radioId)
        //todo for loop set seekable false
    }



    private fun getAndStartRadio(
        reqContext: Context,
        contentId: String
    ) {
        val apiServiceIns =
            RetrofitClient.getInstance(UtilsOkHttp.getBaseOkHttpClientWithTokenAndClient())
                .create(ApiService::class.java)

        val serviceCall: Call<APIResponse<MutableList<SongDetailModel>>> =
            apiServiceIns.fetchGetRadioListByContentById(contentId)
        serviceCall.enqueue(object : Callback<APIResponse<MutableList<SongDetailModel>>> {
            override fun onResponse(
                call: Call<APIResponse<MutableList<SongDetailModel>>>,
                response: Response<APIResponse<MutableList<SongDetailModel>>>
            ) {
                if (response.isSuccessful) {
                    val listRadios = mutableListOf<SongDetailModel>()
                    for (songItem in response.body()?.data!!) {
                        listRadios.add(
                            UtilHelper.getRadioSong(
                                songItem.apply {
                                    isSeekAble = false
                                    rootContentId = contentId
                                }
                            )
                        )
                    }

                    SingleMusicServiceConnection.getInstance(reqContext).apply {
                        unSubscribe()
                        subscribe(
                            MusicPlayList(
                                UtilHelper.getMusicListToSongDetailList(listRadios.toMutableList()),
                                0
                            ),
                            true,
                            0
                        )
                    }
                }
            }

            override fun onFailure(
                call: Call<APIResponse<MutableList<SongDetailModel>>>,
                t: Throwable
            ) {
                Toast.makeText(reqContext, "" + t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    @JvmStatic
    fun destroySDK(context: Context) {
        scope?.cancel()
        ShadhinApp.module(context).musicServiceController.disconnect()
        ShadhinApp.onDestroy()
        SinglePlayerApiService.destroy()
        RetrofitClient.destroy()
        SingleMusicServiceConnection.destroy()
        ShadhinApp.onDestroy()
    }

    internal fun pressCountIncrement() {
        backPressCount += 1
    }

    internal fun pressCountDecrement(): Int {
        backPressCount -= 1
        return backPressCount
    }

    const val API_LatestRelease = 1
    const val API_FeaturedPodcast = 2
    const val API_PopularArtists = 3
    const val API_Videos = 4
    const val API_Tunes = 5
    internal var USER_TOKEN = ""
}