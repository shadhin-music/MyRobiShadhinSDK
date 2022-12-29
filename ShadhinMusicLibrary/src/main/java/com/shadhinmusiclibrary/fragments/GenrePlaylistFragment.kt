package com.shadhinmusiclibrary.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.data.model.HomePatchDetailModel
import com.shadhinmusiclibrary.data.model.HomePatchItemModel

internal class GenrePlaylistFragment : Fragment() {

    var homePatchItem: HomePatchItemModel? = null
    var homePatchDetail: HomePatchDetailModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.my_bl_sdk_common_rv_pb_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val dataAdapter = GenrePlaylistAdapter(this)
//        // dataAdapter.setData(getMockData())
//        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
//        recyclerView.layoutManager =
//            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//        recyclerView.adapter = dataAdapter
//        val button: AppCompatImageView = view.findViewById(R.id.imageBack)
//        val manager: FragmentManager = (context as AppCompatActivity).supportFragmentManager
//        button.setOnClickListener {
//            manager.popBackStack("GenrePlaylistFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE)
//            // Toast.makeText(requireActivity(),"click",Toast.LENGTH_LONG).show()
//        }
    }
}