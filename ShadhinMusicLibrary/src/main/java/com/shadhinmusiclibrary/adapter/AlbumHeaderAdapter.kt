package com.shadhinmusiclibrary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.ShadhinSDKCallback
import com.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.SongDetailModel
import com.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.utils.UtilHelper
import java.text.SimpleDateFormat
import java.util.*

internal class AlbumHeaderAdapter(
    var homePatchDetail: HomePatchDetailModel?,
    private val itemClickCB: CommonPlayControlCallback,
    private val sdkCallback: ShadhinSDKCallback?,
) : RecyclerView.Adapter<AlbumHeaderAdapter.HeaderViewHolder>() {

    var cacheRepository: CacheRepository? = null
    private var favViewModel: FavViewModel? = null
    private var dataSongDetail: MutableList<IMusicModel> = mutableListOf()
    private var parentView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        parentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_playlist_header, parent, false)
        return HeaderViewHolder(parentView!!)
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) {
        if (cacheRepository != null) {
            favViewModel?.let { holder.bindItems(homePatchDetail, cacheRepository!!, it) }
        }

        itemClickCB.getCurrentVH(holder, dataSongDetail)
        holder.ivPlayBtn?.setOnClickListener {
            itemClickCB.onRootClickItem(dataSongDetail, position)
        }
        holder.ivShareBtn?.setOnClickListener {
            val str = "${homePatchDetail?.artist_Id?:""}_${homePatchDetail?.content_Type?:""}"

            // val  rccode =str.toBase64()//homePatchDetail?.artist_Id+"_"+homePatchDetail?.content_Type?.toBase64()
            // val encodedString: String = Base64.getEncoder().encodeToString(rccode.toByteArray())
            val code = UtilHelper.generateShareStrings(homePatchDetail?.album_Id?:"", homePatchDetail?.content_Type?:"")
            sdkCallback?.onShare(code)
            // Log.e("TAG","RCCODE: "+ homePatchDetail?.artist_Id+"_"+homePatchDetail?.content_Type)
//            Log.e("TAG", "RCCODE:  e ${rccode}" )
//            Log.e("TAG","RCCODE: "+ rccode.fromBase64())
            // itemClickCB.onRootClickItem(dataSongDetail, position)
        }
    }

    override fun getItemViewType(position: Int) = VIEW_TYPE

    override fun getItemCount(): Int {
        return 1
    }

    fun setSongAndData(
        data: MutableList<SongDetailModel>,
        homePatchDetail: HomePatchDetailModel,
        cacheRepository: CacheRepository,
        favViewModel: FavViewModel
    ) {
        this.dataSongDetail = mutableListOf()
        for (songItem in data) {
            dataSongDetail.add(
                UtilHelper.getMixdUpIMusicWithRootData(
                    songItem.apply {
                        isSeekAble = true
                    }, homePatchDetail
                )
            )
        }
        this.homePatchDetail = homePatchDetail
        this.cacheRepository = cacheRepository
        this.favViewModel = favViewModel

        notifyDataSetChanged()
    }

    fun setData(homePatchDetail: HomePatchDetailModel) {
        this.homePatchDetail = homePatchDetail
        notifyDataSetChanged()
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mContext = itemView.context
        private lateinit var ivThumbCurrentPlayItem: ImageView
        private lateinit var tvCurrentAlbumName: TextView
        private lateinit var tvArtistName: TextView
        var ivShareBtn: ImageView? = null
        val formatedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val formatedTime = SimpleDateFormat("HH:mm").format(Date())
        val DateTime = "$formatedDate  $formatedTime"
        var ivFavorite: ImageView? = null
        var ivPlayBtn: ImageView? = null
        var menu: ImageView? = null
        fun bindItems(
            homePatchDetail: HomePatchDetailModel?,
            cacheRepository: CacheRepository,
            favViewModel: FavViewModel
        ) {
            ivShareBtn = itemView.findViewById(R.id.share_btn_fab)
            ivShareBtn?.visibility = GONE
            ivThumbCurrentPlayItem =
                itemView.findViewById(R.id.iv_thumb_current_play_item)
            Glide.with(mContext)
                .load(homePatchDetail?.imageUrl?.let { UtilHelper.getImageUrlSize300(it) })
                .into(ivThumbCurrentPlayItem)
            tvCurrentAlbumName =
                itemView.findViewById(R.id.tv_current_album_name)
            tvCurrentAlbumName.text = homePatchDetail?.titleName ?: ""
            if (homePatchDetail?.titleName.isNullOrEmpty()) {
                tvCurrentAlbumName.text = homePatchDetail?.album_Name ?: ""
            }
//            if(root.Artist.isNullOrEmpty()){
//                tvArtistName.text = rootDataContent?.AlbumName
//            }
            tvArtistName =
                itemView.findViewById(R.id.tv_artist_name)
            tvArtistName.text = homePatchDetail?.artistName

            ivFavorite = itemView.findViewById(R.id.iv_favorite)
            ivPlayBtn = itemView.findViewById(R.id.iv_play_btn)
            menu = itemView.findViewById(R.id.iv_song_menu_icon)
            var isFav = false
            val isAddedToFav = homePatchDetail?.let { it?.content_Id?.let { it1 ->
                cacheRepository.getFavoriteById(it1)
            } }
            if (isAddedToFav?.content_Id != null) {
                ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                isFav = true
            } else {
                ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                isFav = false
            }

            ivFavorite?.setOnClickListener {
                if (isFav.equals(true)) {
                    homePatchDetail?.let { it1 ->
                        it1?.content_Id?.let { it2 ->
                            favViewModel.deleteFavContent(
                                it2,
                                homePatchDetail?.content_Type ?: ""
                            )
                        }
                    }
                    homePatchDetail?.let { it1 -> it1?.content_Id?.let { it2 ->
                        cacheRepository.deleteFavoriteById(it2)
                    } }
                    Toast.makeText(mContext, "Removed from favorite", Toast.LENGTH_LONG).show()
                    ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                    isFav = false
                } else {
                    homePatchDetail?.let { it1 ->
                        it1?.content_Id?.let { it2 ->
                            favViewModel.addFavContent(
                                it2,
                                homePatchDetail?.content_Type ?: ""
                            )
                        }
                    }
                    ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                    cacheRepository.insertFavSingleContent(
                        FavDataModel().apply {
                            content_Id = homePatchDetail?.content_Id.toString()
                            album_Id = homePatchDetail?.album_Id
                            albumImage = homePatchDetail?.imageUrl
                            if (homePatchDetail != null) {
                                artistName = homePatchDetail.artistName
                            }
                            if (homePatchDetail != null) {
                                artist_Id = homePatchDetail.artist_Id
                            }
                            clientValue = 2
                            if (homePatchDetail != null) {
                                content_Type = homePatchDetail.content_Type
                            }
                            fav = "1"
                            if (homePatchDetail != null) {
                                imageUrl = homePatchDetail.imageUrl
                            }
                            if (homePatchDetail != null) {
                                playingUrl = homePatchDetail.playingUrl
                            }
                            if (homePatchDetail != null) {
                                rootContentId = homePatchDetail.rootContentId
                            }
                            if (homePatchDetail != null) {
                                rootContentType = homePatchDetail.rootContentType
                            }
                            if (homePatchDetail != null) {
                                titleName = homePatchDetail.titleName?:homePatchDetail.album_Name
                                Log.e("TAG","NAME: " + homePatchDetail.album_Name )
//                                Log.e("TAG","NAME123: " + dataSongDetail.get(absoluteAdapterPosition).album_Id)
//                                Log.e("TAG","NAME123: " + dataSongDetail.get(absoluteAdapterPosition).album_Name)
                            }
                            createDate = DateTime
                        }
                    )
                    isFav = true
                    Toast.makeText(mContext, "Added to favorite", Toast.LENGTH_LONG).show()
                }
                // favClickCallback.favItemClick(songDetail)
            }
        }
    }

    companion object {
        const val VIEW_TYPE = 1
    }
}