package com.shadhinmusiclibrary.adapter

import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.ShadhinSDKCallback
import com.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.ArtistBannerModel
import com.shadhinmusiclibrary.data.model.ArtistContentDataModel
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.fav.FavDataModel
import com.shadhinmusiclibrary.data.model.lastfm.LastFmResult
import com.shadhinmusiclibrary.fragments.fav.FavViewModel
import com.shadhinmusiclibrary.library.player.utils.CacheRepository
import com.shadhinmusiclibrary.utils.ExpandableTextView
import com.shadhinmusiclibrary.utils.UtilHelper
import com.shadhinmusiclibrary.utils.fromBase64
import com.shadhinmusiclibrary.utils.toBase64
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import java.util.Base64.getDecoder


internal class ArtistHeaderAdapter(
    var homePatchDetail: HomePatchDetailModel?,
    private val itemClickCB: CommonPlayControlCallback,
    private val cacheRepository: CacheRepository?,
    private val sdkCallback: ShadhinSDKCallback?,
) : RecyclerView.Adapter<ArtistHeaderAdapter.ArtistHeaderVH>() {
    private var dataSongDetail: MutableList<IMusicModel> = mutableListOf()
    var bio: LastFmResult? = null
    var banner: ArtistBannerModel? = null
    private var parentView: View? = null

    private var favViewModel: FavViewModel? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistHeaderVH {
        parentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_artist_details_header, parent, false)
        return ArtistHeaderVH(parentView!!)
    }

    override fun onBindViewHolder(holder: ArtistHeaderVH, position: Int) {

        holder.bindItems(homePatchDetail)
        itemClickCB.getCurrentVH(holder, dataSongDetail)
        holder.ivPlayBtn?.setOnClickListener {
            itemClickCB.onRootClickItem(dataSongDetail, position)
        }
        holder.ivShareBtn?.setOnClickListener {
            val str = "${homePatchDetail?.artist_Id?:""}_${homePatchDetail?.content_Type?:""}"

           val code = UtilHelper.generateShareStrings(homePatchDetail?.artist_Id?:"", homePatchDetail?.content_Type?:"")
            sdkCallback?.onShare(code)

        }

    }



    override fun getItemViewType(position: Int) = VIEW_TYPE

    override fun getItemCount(): Int {
        return 1
    }

    fun setSongAndData(
        data: MutableList<ArtistContentDataModel>,
        rootPatch: HomePatchDetailModel,
    ) {
//        this.dataSongDetail = mutableListOf()
        for (songItem in data) {
            dataSongDetail.add(
                UtilHelper.getMixdUpIMusicWithRootData(songItem, rootPatch)
            )
        }
        this.homePatchDetail = homePatchDetail
        notifyDataSetChanged()
    }

    fun setData(homePatchDetail: HomePatchDetailModel) {
        this.homePatchDetail = homePatchDetail
        notifyDataSetChanged()
    }

    fun artistBio(bio: LastFmResult?, favViewModel: FavViewModel) {
        this.bio = bio
        this.favViewModel = favViewModel
        notifyDataSetChanged()
//        val moreText:TextView?= parentView?.findViewById(R.id.tvReadMore)
//        if (moreText != null) {
//            moreText.setOnClickListener { e ->
//                if (textView?.isExpanded() == true) {
//                    textView.collapse()
//                    moreText.text ="Read More"
//                } else {
//                    textView?.isExpanded()
//                   moreText.text ="Show less"
//                }
//            }
//        }
    }

    fun artistBanner(banner: ArtistBannerModel?) {
        this.banner = banner
        notifyDataSetChanged()
    }

    inner class ArtistHeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        var ivPlayBtn: ImageView? = null
        var ivShareBtn: ImageView? = null
        var ivFavorite: ImageView? = null
        val formatedDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        val formatedTime = SimpleDateFormat("HH:mm").format(Date())
        val DateTime = "$formatedDate  $formatedTime"
        fun bindItems(homePatchDetail: HomePatchDetailModel?) {
            if (homePatchDetail != null) {
                val imageView: ImageView = itemView.findViewById(R.id.thumb)
                ivPlayBtn = itemView.findViewById(R.id.iv_play_btn)
                ivShareBtn = itemView.findViewById(R.id.share_btn_fab)
                ivFavorite = itemView.findViewById(R.id.favorite)
//            val url: String = homePatchDetail!!.getImageUrl300Size()
                val textArtist: TextView = itemView.findViewById(R.id.name)
                textArtist.text = homePatchDetail.artistName ?: ""
                val textView: ExpandableTextView? = itemView.findViewById(R.id.tvDescription)
                val bio: String = bio?.artist?.bio?.summary ?: ""
                if (homePatchDetail.artistName.equals("Elita", true)) {

                    val elitaBio =
                        "Elita Karim is a Bangladeshi popular pop singer. She is one of the heart-touching female singers in the country. Her full name Dilshan Karim Elita but she is best known as Elita around the country.Elita Karim started her career as a journalist in the country’s leading English daily the Daily Star. Then she connected with the Black Band but her first mixed album was released in 2009 with the title ‘Amar Prithibi’.In 2009, her mixed album ‘Antohin’ was released and is well discussed in the market. Elita Karim couldn’t release her single music album even after lots of time since her career began, finally in 2015; she released her first single album Elita 2015."
                    // val textView: ExpandableTextView? = itemView?.findViewById(R.id.tvDescription)
                    Log.e("TAG", "ARTIST: " + elitaBio)
                    textView?.text = elitaBio
                    val cardBiography: CardView = itemView.findViewById(R.id.cardBiography)
                    cardBiography.visibility = VISIBLE
                } else {
                    val cardBiography: CardView = itemView.findViewById(R.id.cardBiography)
                    val updatedbio = Html.fromHtml(bio).toString()
                    if (updatedbio.length > 25) {
                        cardBiography.visibility = VISIBLE
                    }
                    textView?.setText(updatedbio)
                    if (homePatchDetail.artistName.equals(
                            "Scarecrow",
                            true
                        ) || homePatchDetail.artistName.equals(
                            "Mizan",
                            true
                        ) || homePatchDetail.artistName.equals(
                            "Feedback",
                            true
                        ) || homePatchDetail.artistName.equals(
                            "Vibe",
                            true
                        ) || homePatchDetail.artistName.equals("Kona", true)
                        || homePatchDetail.artistName.equals(
                            "Balam",
                            true
                        ) || homePatchDetail.artistName.equals(
                            "Nancy",
                            true
                        ) || homePatchDetail.artistName.equals("Liza", true)
                    ) {
                        cardBiography.visibility = GONE
                    }
                }
//            val updatedbio = Html.fromHtml(bio).toString()
//            val cardBiography: CardView = itemView.findViewById(R.id.cardBiography)
//            if (updatedbio.length > 25) {
//                cardBiography.visibility = VISIBLE
//            }
//            // textView?.text = bio?.artist?.bio?.summary
//            textView?.text = updatedbio
                val tvName: TextView = itemView.findViewById(R.id.tvName)!!
                tvName.text = homePatchDetail.artistName + "'s"
                val imageArtist: ImageView = itemView.findViewById(R.id.imageArtist)
                if (banner?.image?.isEmpty() == false) {
                    val cardListen: CardView = parentView!!.findViewById(R.id.cardListen)
                    cardListen.visibility = VISIBLE
                    if (itemView.context != null) {
                        Glide.with(itemView.context)
                            .load(banner?.image)
                            .into(imageArtist)
                    }
                }
                //  textView?.setText(Html.fromHtml(CharParser.replaceMultipleSpaces(bio?.artist?.bio?.summary)))
                val moreText: TextView? = itemView.findViewById(R.id.tvReadMore)

                moreText?.setOnClickListener {
                    if (textView!!.isExpanded) {
                        textView.collapse()
                        moreText.text = "Read More"
                    } else {
                        textView.expand()
                        moreText.text = "Less"
                    }
                }
                Glide.with(context)
                    .load(UtilHelper.getImageUrlSize300(homePatchDetail.imageUrl ?: ""))
                    .into(imageView)
                var isFav = false
                val isAddedToFav =
                    cacheRepository?.getFavoriteById(homePatchDetail.artist_Id.toString())
                if (isAddedToFav?.artist_Id != null) {
                    ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                    isFav = true
                } else {
                    ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                    isFav = false
                }

                ivFavorite?.setOnClickListener {
                    if (isFav.equals(true)) {
                        favViewModel?.deleteFavContent(
                            homePatchDetail.artist_Id.toString(),
                            "A" ?: ""
                        )
                        cacheRepository?.deleteFavoriteById(homePatchDetail.artist_Id.toString())
                        Toast.makeText(context, "Removed from favorite", Toast.LENGTH_LONG).show()
                        ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_favorite_border)
                        isFav = false
                    } else {
                        favViewModel?.addFavContent(
                            homePatchDetail.artist_Id.toString(),
                            "A" ?: ""
                        )

                        ivFavorite?.setImageResource(R.drawable.my_bl_sdk_ic_filled_favorite)
                        cacheRepository?.insertFavSingleContent(
                            FavDataModel().apply {
                                content_Id = homePatchDetail.artist_Id.toString()
                                album_Id = homePatchDetail.album_Id
                                albumImage = homePatchDetail.imageUrl
                                artistName = homePatchDetail.artistName
                                artist_Id = homePatchDetail.artist_Id
                                clientValue = 2
                                content_Type = "A"
                                fav = "1"
                                imageUrl = homePatchDetail.imageUrl
                                playingUrl = homePatchDetail.playingUrl
                                rootContentId = homePatchDetail.rootContentId
                                rootContentId = homePatchDetail.rootContentType
                                titleName = homePatchDetail.artistName
                                createDate = DateTime

                            }
                        )
                        isFav = true
                        Toast.makeText(context, "Added to favorite", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    companion object {
        const val VIEW_TYPE = 1
    }
}