package com.shadhinmusiclibrary.fragments.create_playlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.ShadhinMusicSdkCore
import com.shadhinmusiclibrary.activities.SDKMainActivity
import com.shadhinmusiclibrary.adapter.HomeFooterAdapter
import com.shadhinmusiclibrary.adapter.MyPlaylistAdapter
import com.shadhinmusiclibrary.data.model.HomePatchItemModel
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.utils.AppConstantUtils
import com.shadhinmusiclibrary.utils.AppConstantUtils.PlaylistId
import com.shadhinmusiclibrary.utils.AppConstantUtils.PlaylistName
import com.shadhinmusiclibrary.utils.textColor
import java.io.Serializable


internal class MyPlaylistFragment : BaseFragment(),
    OnItemClickCallBack {

    private lateinit var viewModel: CreateplaylistViewModel
    private lateinit var footerAdapter: HomeFooterAdapter
    private lateinit var myPlaylistAdapter: MyPlaylistAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        return inflater.inflate(R.layout.my_bl_sdk_fragment_my_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        setupViewModel()
        val verticalSpanCount = 1
        val horizontalSpanCount = 2


        val textTitle: TextView = requireView().findViewById(R.id.tvTitle)
        textTitle.text = "My Playlist"
        imageBackBtn.setOnClickListener {

            requireActivity().onBackPressed()
        }
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        viewModel.getuserPlaylist()
        viewModel.getUserPlaylist.observe(viewLifecycleOwner) { res ->
            if (res != null) {
                if (res.data != null) {
                    if (res.data?.size?.equals(0) == true) {
                        val layout: ConstraintLayout? = view.findViewById(R.id.createPlaylist)
                        layout?.visibility = VISIBLE
                        recyclerView.visibility = GONE
                        val btnCreatePlaylist: AppCompatButton =
                            requireView().findViewById(R.id.btnCreatePlaylist)
                        btnCreatePlaylist.setOnClickListener {
                            openCreatePlaylist()
                        }
                    } else {
                        val layout: ConstraintLayout? = view.findViewById(R.id.createPlaylist)
                        layout?.visibility = GONE
                        recyclerView.visibility = VISIBLE
                        footerAdapter = HomeFooterAdapter()
                        myPlaylistAdapter = MyPlaylistAdapter(res.data, this)

                        val config = ConcatAdapter.Config.Builder()
                            .setIsolateViewTypes(false)
                            .build()
                        val concatAdapter = ConcatAdapter(config, myPlaylistAdapter)
                        val layoutManager = GridLayoutManager(context, horizontalSpanCount)
                        val onSpanSizeLookup: GridLayoutManager.SpanSizeLookup =
                            object : GridLayoutManager.SpanSizeLookup() {
                                override fun getSpanSize(position: Int): Int {
                                    return if (concatAdapter.getItemViewType(position) == HomeFooterAdapter.VIEW_TYPE) horizontalSpanCount else verticalSpanCount
                                }
                            }
                        recyclerView.layoutManager = layoutManager
                        layoutManager.setSpanSizeLookup(onSpanSizeLookup)
                        recyclerView.adapter = concatAdapter
                        concatAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
        val addPlaylist: AppCompatImageView = requireView().findViewById(R.id.imageAddPlaylist)

        addPlaylist.setOnClickListener {
            argHomePatchItem?.let { it1 -> clickOnAddPlaylist(it1) }
        }
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                injector.factoryCreatePlaylistVM
            )[CreateplaylistViewModel::class.java]
    }

    fun clickOnAddPlaylist(selectedHomePatchItem: HomePatchItemModel) {
//        ShadhinMusicSdkCore.pressCountIncrement()
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        findNavController().navigate(R.id.to_create_playlist,data)
        /*startActivity(Intent(requireActivity(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_CreatePlaylist
                )
                putExtra(AppConstantUtils.PatchItem, data)
            })*/
    }

    override fun onResume() {
        super.onResume()
        viewModel.getuserPlaylist()
    }

    override fun GotoPlaylistDetails(userPlaylistData: String?, name: String?, gradientResId: Int) {
        argHomePatchItem?.let {
            clickOnPlaylistItem(
                it,
                userPlaylistData.toString(),
                name.toString(),
                gradientResId
            )
        }
    }

    fun clickOnPlaylistItem(
        selectedHomePatchItem: HomePatchItemModel,
        id: String,
        name: String,
        gradientResId: Int
    ) {
//        ShadhinMusicSdkCore.pressCountIncrement()
        val data = Bundle()
        data.putSerializable(
            AppConstantUtils.PatchItem,
            selectedHomePatchItem as Serializable
        )
        data.putSerializable(PlaylistId, id)
        data.putSerializable(PlaylistName, name)
        startActivity(Intent(requireActivity(), SDKMainActivity::class.java)
            .apply {
                putExtra(
                    AppConstantUtils.UI_Request_Type,
                    AppConstantUtils.Requester_Name_CreatedPlaylistDetails
                )
                putExtra(AppConstantUtils.PatchItem, data)
                putExtra(PlaylistId, data)
                putExtra(PlaylistName, data)
                putExtra(AppConstantUtils.PlaylistGradientId, gradientResId)
            })
    }

    fun openCreatePlaylist() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)

        val contentView =
            View.inflate(context, R.layout.my_bl_sdk_bottomsheet_create_new_playlist, null)
        bottomSheetDialog.setContentView(contentView)
        bottomSheetDialog.show()
        val closeButton: ImageView? = bottomSheetDialog.findViewById(R.id.closeButton)
        closeButton?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        val etCreatePlaylist: EditText? = bottomSheetDialog.findViewById(R.id.etCreatePlaylist)
        var savePlaylist: AppCompatButton? = bottomSheetDialog.findViewById(R.id.btnSavePlaylist)
        etCreatePlaylist?.setOnFocusChangeListener { view, focused ->
            val keyboard: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (focused) keyboard.showSoftInput(
                etCreatePlaylist,
                0
            ) else keyboard.hideSoftInputFromWindow(
                etCreatePlaylist.getWindowToken(),
                0
            )
        }
        etCreatePlaylist?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val name: String = etCreatePlaylist.getText().toString()
                savePlaylist?.setBackgroundResource(R.drawable.my_bl_sdk_rounded_button_red)
                savePlaylist?.isEnabled = true
                savePlaylist?.textColor(R.color.my_sdk_color_white)
                savePlaylist?.setOnClickListener {

                    viewModel.createPlaylist(name)
                    //requireActivity().onBackPressed()
                    requireActivity().onBackPressed()
                    bottomSheetDialog.dismiss()

                }
                if (etCreatePlaylist.text.isNullOrEmpty()) {
                    savePlaylist?.setBackgroundResource(R.drawable.my_bl_sdk_rounded_button_gray)
                    savePlaylist?.isEnabled = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        etCreatePlaylist?.requestFocus()
        viewModel.createPlaylist.observe(viewLifecycleOwner) { res ->
            Log.e("TAG", "RESULT: " + res.status)
            if (res.status == "successful") {
                viewModel.getuserPlaylist()
            }
        }
    }
}