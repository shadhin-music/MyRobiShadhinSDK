package com.myrobi.shadhinmusiclibrary.adapter

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.myrobi.shadhinmusiclibrary.data.IMusicModel
import com.myrobi.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.myrobi.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.myrobi.shadhinmusiclibrary.data.model.podcast.EpisodeModel
import com.myrobi.shadhinmusiclibrary.data.model.podcast.SongTrackModel
import com.myrobi.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.myrobi.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.myrobi.shadhinmusiclibrary.utils.ExpandableTextView
import com.myrobi.shadhinmusiclibrary.utils.UtilHelper
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

internal class PodcastHeaderAdapter(
    private val pcOnCallback: CommonPlayControlCallback,
    private val cacheRepository: CacheRepository?,
    private val favViewModel: FavViewModel,
    private val homePatchDetail: HomePatchDetailModel?
) : RecyclerView.Adapter<PodcastHeaderAdapter.PodcastHeaderVH>() {
    private var listSongTrack: MutableList<IMusicModel> = mutableListOf()
    private var episode: List<EpisodeModel>? = null
    private var parentView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastHeaderVH {
        parentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_podcast_header_layout, parent, false)
        return PodcastHeaderVH(parentView!!)
    }

    override fun onBindViewHolder(holder: PodcastHeaderVH, position: Int) {
        holder.bindItems(position)

        pcOnCallback.getCurrentVH(holder, listSongTrack)
        holder.ivPlayBtn?.setOnClickListener {
            pcOnCallback.onRootClickItem(listSongTrack, position)
        }
    }

    override fun getItemViewType(position: Int) = VIEW_TYPE

    override fun getItemCount(): Int {
        return 1
    }

    fun setTrackData(
        episode: List<EpisodeModel>,
        data: MutableList<SongTrackModel>,
        rootPatch: HomePatchDetailModel
    ) {
//        this.listSongTrack = mutableListOf()
//        for (songItem in data) {
//            listSongTrack.add(
//                songItem.apply {
//                    rootContentId = episodeId
//                    rootContentType = rootPatch.content_Type
//                    rootImage = rootPatch.imageUrl
//                    isSeekAble = true
//                }
//            )
//        }

        this.episode = ArrayList(episode)
        notifyDataSetChanged()
    }

//    fun setHeader(itEpisod: MutableList<EpisodeModel>, trackList: MutableList<SongTrackModel>) {
//
//    }

    /* @SuppressLint("NotifyDataSetChanged")
     fun setHeader(episode: List<EpisodeModel>, trackList: MutableList<SongTrackModel>) {
         this.episode = episode
         listSongTrack = trackList.toMutableList()
         notifyDataSetChanged()
     }*/

    inner class PodcastHeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.context
        var ivPlayBtn: ImageView? = null
        var ivFavorite: ImageView? = null

        fun bindItems(position: Int) {
            val imageView: ImageView = itemView.findViewById(R.id.thumb)
            val textArtist: TextView? = itemView.findViewById(R.id.name)
            ivPlayBtn = itemView.findViewById(R.id.iv_play_btn)
            val url: String? = episode?.get(0)?.ImageUrl
            val details: String = episode?.get(position)?.Details ?: ""
            ivFavorite = itemView.findViewById(R.id.favorite)
            // ivFavorite?.visibility = GONE
            val result = Html.fromHtml(details).toString()
//            if(textArtist?.text.isNullOrEmpty())
            // Log.e("TAG","Name :"+episode?.get(position))
            textArtist?.text = episode?.get(position)?.Name ?: ""
            val textView: ExpandableTextView? = itemView.findViewById(R.id.tvDescription)
            val moreText: TextView? = itemView.findViewById(R.id.tvReadMore)
            textView?.text = result
            moreText?.setOnClickListener {
                if (textView!!.isExpanded) {
                    textView.collapse()
                    moreText.text = "Read More"
                } else {
                    textView.expand()
                    moreText.text = "Less"
                }
            }
//            val tvName: TextView = itemView?.findViewById(R.id.tvName)!!
//            tvName.text = argHomePatchDetail.Artist + "'s"
            // val imageArtist: ImageView = itemView!!.findViewById(R.id.imageArtist)
            Glide.with(context)
                .load(UtilHelper.getImageUrlSize300(url ?: ""))
                .into(imageView)
            var isFav = false
            kotlin.runCatching {episode?.get(position)?.TrackList?.first()}.getOrNull()?.content_Id?.let { contentId ->
                val isAddedToFav =
                    cacheRepository?.getFavoriteById(contentId)
                if (isAddedToFav?.content_Id != null) {
                    ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                    isFav = true
                } else {
                    ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                    isFav = false
                }

            }

            if(episode?.get(position)?.TrackList.isNullOrEmpty().equals(false)) {

                ivFavorite?.setOnClickListener {
                    if (isFav.equals(true)) {
                        kotlin.runCatching { episode?.get(position)?.TrackList?.first() }
                            .getOrNull()?.content_Id?.let { contentId ->
                            favViewModel.deleteFavContent(
                                contentId,
                                episode?.get(position)?.TrackList?.get(0)?.content_Type ?: ""
                            )
                        }


                        cacheRepository?.deleteFavoriteById(episode?.get(position)?.TrackList?.get(0)?.content_Id.toString())
                        Toast.makeText(context, "Removed from favorite", Toast.LENGTH_LONG).show()
                        ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                        isFav = false
                    } else {
                        kotlin.runCatching { episode?.get(position)?.TrackList?.first() }
                            .getOrNull()?.content_Id?.let { contentId ->
                            favViewModel.addFavContent(
                                contentId,
                                episode?.get(position)?.TrackList?.get(0)?.content_Type.toString()
                            )
                        }
                        val formatedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
                        val formatedTime = SimpleDateFormat("HH:mm").format(Date())
                        val DateTime = "$formatedDate  $formatedTime"
                        ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                        cacheRepository?.insertFavSingleContent(
                            FavDataModel().apply {
                                kotlin.runCatching { episode?.get(position)?.TrackList?.first() }
                                    .getOrNull()?.content_Id?.let { contentId ->
                                    content_Id = contentId
                                }
//                            content_Id =
//                                episode?.get(position)?.TrackList?.get(0)?.content_Id.toString()
                                album_Id = episode?.get(position)?.Id.toString()
                                albumImage = episode?.get(position)?.ImageUrl
                                kotlin.runCatching { episode?.get(position)?.TrackList?.first() }
                                    .getOrNull()?.artistName?.let { artistname ->
                                    artistName = artistname
                                }
                                // artistName = episode?.get(position)?.TrackList?.get(0)?.artistName
                                artist_Id = ""
                                clientValue = 2
                                content_Type = episode?.get(position)?.ContentType.toString()
                                fav = "1"
                                kotlin.runCatching { episode?.get(position)?.TrackList?.first() }
                                    .getOrNull()?.imageUrl?.let { imageurl ->
                                    imageUrl = imageurl
                                }
//                            if(episode?.get(position)?.TrackList?.get(0)?.playingUrl?.isNullOrEmpty()==false){
//                                playingUrl = homePatchDetail?.playingUrl
//                                Log.e("TAG","Playurl"+ playingUrl)
//                            }else {
                                kotlin.runCatching {
                                    episode?.get(position)?.TrackList?.first()
                                }.getOrNull()?.playingUrl?.let { playingurl ->
                                    playingUrl = playingurl

                                }
                                //  }
//                            imageUrl = episode?.get(position)?.TrackList?.get(0)?.imageUrl
//                            playingUrl = episode?.get(position)?.TrackList?.get(0)?.playingUrl
                                rootContentId = ""
                                rootContentType = ""
                                titleName =
                                    episode?.get(position)?.Name ?: homePatchDetail?.titleName
                                total_duration = ""
                                createDate = DateTime
                            }
                        )
                        isFav = true
                        Toast.makeText(context, "Added to favorite", Toast.LENGTH_LONG).show()
                    }
                }
            } else{
               // Toast.makeText(context,"Track is empty",LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val VIEW_TYPE = 1
    }
}