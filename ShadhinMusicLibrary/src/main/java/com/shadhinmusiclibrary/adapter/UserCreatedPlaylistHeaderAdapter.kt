package com.shadhinmusiclibrary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.shadhinmusiclibrary.library.player.utils.CacheRepository

internal class UserCreatedPlaylistHeaderAdapter(
    var homePatchDetail: HomePatchDetailModel?,
    var playlistName: String?,
    private val itemClickCB: CommonPlayControlCallback,
    private val cacheRepository: CacheRepository?,
    private val favViewModel: FavViewModel,
    val gradientDrawable: Int
) : RecyclerView.Adapter<UserCreatedPlaylistHeaderAdapter.UserCreatedPlaylistHeaderVH>() {

    var dataSongDetail: MutableList<IMusicModel> = mutableListOf()
    private var parentView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCreatedPlaylistHeaderVH {
        parentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_playlist_header, parent, false)
        return UserCreatedPlaylistHeaderVH(parentView!!)
    }

    override fun onBindViewHolder(holder: UserCreatedPlaylistHeaderVH, position: Int) {
        holder.bindItems(homePatchDetail)
        itemClickCB.getCurrentVH(holder, dataSongDetail.toMutableList())
        holder.ivPlayBtn?.setOnClickListener {
            itemClickCB.onRootClickItem(dataSongDetail.toMutableList(), position)
        }
    }

    override fun getItemViewType(position: Int) = AlbumHeaderAdapter.VIEW_TYPE

    override fun getItemCount(): Int {
        return 1
    }

    fun setSongAndData(data: MutableList<IMusicModel>, rootConId: String) {
        for (songItem in data) {
            dataSongDetail.add(
                songItem.apply {
                    isSeekAble = true
                    rootContentId = rootConId
                    rootContentType = content_Type
                }
            )
        }

        notifyDataSetChanged()
    }

    inner class UserCreatedPlaylistHeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mContext = itemView.context
        private lateinit var ivThumbCurrentPlayItem: ImageView
        private lateinit var tvCurrentAlbumName: TextView
        private lateinit var tvArtistName: TextView

        private lateinit var ivFavorite: ImageView
        var ivPlayBtn: ImageView? = null
        var menu: ImageView? = null
        fun bindItems(homePatchDetail: HomePatchDetailModel?) {

            ivThumbCurrentPlayItem =
                itemView.findViewById(R.id.iv_thumb_current_play_item)

            ivThumbCurrentPlayItem.setImageResource(gradientDrawable)

            ivThumbCurrentPlayItem.setBackgroundResource(R.drawable.my_bl_sdk_playlist_bg)
            tvCurrentAlbumName =
                itemView.findViewById(R.id.tv_current_album_name)
            tvCurrentAlbumName.text = playlistName
            tvArtistName =
                itemView.findViewById(R.id.tv_artist_name)
            tvArtistName.text = dataSongDetail.size.toString() + " Songs"

            ivFavorite = itemView.findViewById(R.id.iv_favorite)
            ivFavorite.visibility = GONE
            ivPlayBtn = itemView.findViewById(R.id.iv_play_btn)
            menu = itemView.findViewById(R.id.iv_song_menu_icon)
            var isFav = false
            val isAddedToFav =
                cacheRepository?.getFavoriteById(homePatchDetail?.content_Id.toString())
            if (isAddedToFav?.content_Id != null) {
                ivFavorite.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                isFav = true
            } else {
                ivFavorite.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                isFav = false
            }

            ivFavorite.setOnClickListener {
                if (isFav.equals(true)) {
                    homePatchDetail?.content_Id?.let { it1 ->
                        favViewModel.deleteFavContent(
                            it1,
                            homePatchDetail.content_Type ?: ""
                        )
                    }
                    cacheRepository?.deleteFavoriteById(homePatchDetail?.content_Id.toString())
                    Toast.makeText(mContext, "Removed from favorite", Toast.LENGTH_LONG).show()
                    ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                    isFav = false
                    Log.e("TAG", "NAME: " + isFav)
                } else {
                    favViewModel.addFavContent(
                        homePatchDetail?.content_Id.toString(),
                        homePatchDetail?.content_Type.toString()
                    )

                    ivFavorite.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                    Log.e("TAG", "NAME123: " + isFav)
                    cacheRepository?.insertFavSingleContent(
                        FavDataModel().apply {
                            content_Id = homePatchDetail?.content_Id.toString()
                            album_Id = homePatchDetail?.album_Id
                            imageUrl = homePatchDetail?.imageUrl
                            artistName = homePatchDetail?.artistName
                            artist_Id = homePatchDetail?.artist_Id
                            clientValue = 2
                            content_Type = homePatchDetail?.content_Type.toString()
                            fav = "1"
                            imageUrl = homePatchDetail?.imageUrl
                            playingUrl = homePatchDetail?.playingUrl
                            rootContentId = homePatchDetail?.rootContentId
                            rootContentType = homePatchDetail?.rootContentType
                            titleName = homePatchDetail?.titleName
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