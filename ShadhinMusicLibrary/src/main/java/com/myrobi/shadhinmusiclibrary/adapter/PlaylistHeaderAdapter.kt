package com.myrobi.shadhinmusiclibrary.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.SongDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.myrobi.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper
import java.text.SimpleDateFormat
import java.util.*

internal class PlaylistHeaderAdapter(
    var homePatchDetail: HomePatchDetailModel?,
    private val itemClickCB: CommonPlayControlCallback,
    private val cacheRepository: CacheRepository?,
    private val favViewModel: FavViewModel
) : RecyclerView.Adapter<PlaylistHeaderAdapter.PlaylistHeaderVH>() {

    private var dataSongDetail: MutableList<IMusicModel> = mutableListOf()
    private var parentView: View? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHeaderVH {
        parentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_playlist_header, parent, false)
        return PlaylistHeaderVH(parentView!!)
    }

    override fun onBindViewHolder(holder: PlaylistHeaderVH, position: Int) {
        holder.bindItems(homePatchDetail)
        itemClickCB.getCurrentVH(holder, dataSongDetail)
        holder.ivPlayBtn.setOnClickListener {
            itemClickCB.onRootClickItem(dataSongDetail, position)
        }
    }

    override fun getItemViewType(position: Int) = AlbumHeaderAdapter.VIEW_TYPE

    override fun getItemCount(): Int {
        return 1
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSongAndData(data: MutableList<SongDetailModel>, homePatchDetail: HomePatchDetailModel) {
        this.dataSongDetail = mutableListOf()
        for (songItem in data) {
            dataSongDetail.add(
                UtilHelper.getMixdUpIMusicWithRootData(
                    songItem.apply { isSeekAble = true},
                    homePatchDetail
                )
            )
        }
        this.homePatchDetail = homePatchDetail
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(homePatchDetail: HomePatchDetailModel) {
        this.homePatchDetail = homePatchDetail
        notifyDataSetChanged()
    }

    inner class PlaylistHeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mContext = itemView.context
        private lateinit var ivThumbCurrentPlayItem: ImageView
        private lateinit var tvCurrentAlbumName: TextView
        private lateinit var tvArtistName: TextView
        internal lateinit var ivPlayBtn: ImageView
        var ivFavorite: ImageView? = null
        //var ivPlayBtn: ImageView? = null
        var menu: ImageView? = null
        val formatedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val formatedTime = SimpleDateFormat("HH:mm").format(Date())
        val DateTime = "$formatedDate  $formatedTime"
        fun bindItems(homePatchDetail: HomePatchDetailModel?) {

            ivThumbCurrentPlayItem =
                itemView.findViewById(R.id.iv_thumb_current_play_item)
            Glide.with(mContext)
                .load(homePatchDetail?.imageUrl)
                .into(ivThumbCurrentPlayItem)
            tvCurrentAlbumName =
                itemView.findViewById(R.id.tv_current_album_name)
            tvCurrentAlbumName.text = homePatchDetail?.titleName
            if (homePatchDetail?.titleName.isNullOrEmpty()) {
                tvCurrentAlbumName.text = homePatchDetail?.album_Name
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
            val isAddedToFav = cacheRepository?.getFavoriteById(homePatchDetail?.content_Id ?: "")
            if (isAddedToFav?.content_Id != null) {
                ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                isFav = true
            } else {
                ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                isFav = false
            }

            ivFavorite?.setOnClickListener {
                if (isFav.equals(true)) {
                    homePatchDetail?.content_Id?.let { it1 ->
                        favViewModel.deleteFavContent(
                            it1,
                            homePatchDetail?.content_Type ?: ""
                        )
                    }
                    cacheRepository?.deleteFavoriteById(homePatchDetail?.content_Id.toString())
                    Toast.makeText(mContext, "Removed from favorite", Toast.LENGTH_LONG).show()
                    ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                    isFav = false
                } else {
                    favViewModel.addFavContent(
                        homePatchDetail?.content_Id.toString(),
                        homePatchDetail?.content_Type.toString()
                    )
                    ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                    Log.e("TAG", "NAME123: " + isFav)
                    cacheRepository?.insertFavSingleContent(
                        FavDataModel()
                            .apply {
                                content_Id = homePatchDetail?.content_Id.toString()
                                album_Id = homePatchDetail?.album_Id
                                albumImage = homePatchDetail?.imageUrl
                                artistName = homePatchDetail?.artistName?:""
                                artist_Id = homePatchDetail?.artist_Id
                                clientValue = 2
                                content_Type = homePatchDetail?.content_Type.toString()
                                fav = "1"
                                imageUrl = homePatchDetail?.imageUrl
                                playingUrl = homePatchDetail?.playingUrl
                                rootContentId = homePatchDetail?.rootContentId
                                rootContentType = homePatchDetail?.rootContentType
                                titleName = homePatchDetail?.titleName
                                createDate = DateTime
                            }
                    )
                    isFav = true
                    Toast.makeText(mContext, "Added to favorite", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        const val VIEW_TYPE = 1
    }
}