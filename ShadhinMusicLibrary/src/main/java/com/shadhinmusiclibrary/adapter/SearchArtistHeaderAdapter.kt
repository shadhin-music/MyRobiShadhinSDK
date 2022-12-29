package com.shadhinmusiclibrary.adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.callBackService.CommonPlayControlCallback
import com.shadhinmusiclibrary.data.IMusicModel
import com.shadhinmusiclibrary.data.model.ArtistBannerModel
import com.shadhinmusiclibrary.data.model.lastfm.LastFmResult
import com.shadhinmusiclibrary.data.model.search.SearchArtistDataModel
import com.shadhinmusiclibrary.utils.ExpandableTextView

internal class SearchArtistHeaderAdapter(
    var homePatchDetail: SearchArtistDataModel?,
    private val itemClickCB: CommonPlayControlCallback
) : RecyclerView.Adapter<SearchArtistHeaderAdapter.ArtistHeaderVH>() {

    private var dataSongDetail: MutableList<IMusicModel> = mutableListOf()
    var bio: LastFmResult? = null
    var banner: ArtistBannerModel? = null
    private var parentView: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistHeaderVH {
        parentView = LayoutInflater.from(parent.context)
            .inflate(R.layout.my_bl_sdk_artist_details_header, parent, false)
        return ArtistHeaderVH(parentView!!)
    }


    override fun onBindViewHolder(holder: ArtistHeaderVH, position: Int) {
        holder.bindItems(homePatchDetail!!)
        itemClickCB.getCurrentVH(holder, dataSongDetail)
        holder.ivPlayBtn?.setOnClickListener {
            itemClickCB.onRootClickItem(dataSongDetail, position)
        }
    }

    override fun getItemViewType(position: Int) = VIEW_TYPE

    override fun getItemCount(): Int {
        return 1
    }

    fun setData(homePatchDetail: SearchArtistDataModel) {
        this.homePatchDetail = homePatchDetail
        notifyDataSetChanged()
    }

    fun artistBio(bio: LastFmResult?) {
        this.bio = bio
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
    }

    inner class ArtistHeaderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val context = itemView.getContext()
        var ivPlayBtn: ImageView? = null

        fun bindItems(homePatchDetail: SearchArtistDataModel) {
            val imageView: ImageView = itemView.findViewById(R.id.thumb)
            ivPlayBtn = itemView.findViewById(R.id.iv_play_btn)

            val url: String = homePatchDetail.getImageUrl300Size()
            val textArtist: TextView = itemView.findViewById(R.id.name)
            textArtist.setText(homePatchDetail.Artist)
            val textView: ExpandableTextView? = itemView?.findViewById(R.id.tvDescription)
            val bio: String = bio?.artist?.bio?.summary.toString()
            val updatedbio = Html.fromHtml(bio).toString()
            val cardBiography: CardView = itemView.findViewById(R.id.cardBiography)
            if (updatedbio.length > 25) {
                cardBiography.visibility = VISIBLE
            }
            // textView?.text = bio?.artist?.bio?.summary
            textView?.setText(updatedbio)
            val tvName: TextView = itemView?.findViewById(R.id.tvName)!!
            tvName.text = homePatchDetail.Artist + "'s"
            val imageArtist: ImageView = itemView!!.findViewById(R.id.imageArtist)
            if (banner?.image?.isEmpty() == false) {
                val cardListen: CardView = parentView!!.findViewById(R.id.cardListen)
                cardListen.visibility = VISIBLE
                if (context != null) {
                    Glide.with(context)
                        .load(banner?.image)
                        .into(imageArtist)
                }
            }
            //  textView?.setText(Html.fromHtml(CharParser.replaceMultipleSpaces(bio?.artist?.bio?.summary)))
            val moreText: TextView? = itemView?.findViewById(R.id.tvReadMore)

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
                .load(url)
                .into(imageView)
        }
    }

    companion object {
        const val VIEW_TYPE = 1
    }
}