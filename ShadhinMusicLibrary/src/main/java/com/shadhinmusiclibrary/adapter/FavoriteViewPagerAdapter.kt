package com.shadhinmusiclibrary.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.shadhinmusiclibrary.fragments.fav.*
import com.shadhinmusiclibrary.fragments.fav.ArtistFavFragment
import com.shadhinmusiclibrary.fragments.fav.PodcastFavFragment
import com.shadhinmusiclibrary.fragments.fav.SongsFavoriteFragment
import com.shadhinmusiclibrary.fragments.fav.VideosFavFragment


@Suppress("DEPRECATION")
internal class FavoriteViewPagerAdapter(
    var context: Context,
    fm: FragmentManager,
    var totalTabs: Int,
) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
//        return  SearchFragment()
        return when (position) {

            0 -> {
              AllFavoriteDetailsFragment()
            }
           1-> {
               SongsFavoriteFragment()


            }
            2-> {
                VideosFavFragment()


            }

            3-> {
                PodcastFavFragment()


            }
            4-> {
                ArtistFavFragment()


            }
            5-> {
                AlbumsFavFragment()


            }
            6->{
                PlaylistFavFragment()
            }
            else -> getItem(position)
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}
