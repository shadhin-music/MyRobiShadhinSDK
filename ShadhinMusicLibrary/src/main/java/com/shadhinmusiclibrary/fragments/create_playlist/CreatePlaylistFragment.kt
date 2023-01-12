package com.shadhinmusiclibrary.fragments.create_playlist

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shadhinmusiclibrary.R
import com.shadhinmusiclibrary.R.drawable.my_bl_sdk_rounded_button_red
import com.shadhinmusiclibrary.callBackService.CreatePlaylistBottomsheetCallback
import com.shadhinmusiclibrary.fragments.base.BaseFragment
import com.shadhinmusiclibrary.data.model.PlaylistBody
import com.shadhinmusiclibrary.utils.textColor
import org.json.JSONObject

internal class CreatePlaylistFragment : BaseFragment(),
    CreatePlaylistBottomsheetCallback {

    private lateinit var viewModel: CreateplaylistViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.my_bl_sdk_fragment_create_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
//        Log.e("TAG","PLAYLIST:"+ )
        val imageBackBtn: AppCompatImageView = view.findViewById(R.id.imageBack)
        imageBackBtn.setOnClickListener {
            /* if (ShadhinMusicSdkCore.pressCountDecrement() == 0) {
                 requireActivity().finish()
             }*/
            requireActivity().onBackPressed()
        }
        viewModel.createPlaylist.observe(viewLifecycleOwner) { res ->

            res?.status?.let {
                Toast.makeText(requireContext(),it , Toast.LENGTH_LONG).show()
            }

        }
        val btnCreatePlaylist: AppCompatButton = requireView().findViewById(R.id.btnCreatePlaylist)
        btnCreatePlaylist.setOnClickListener {
            createPlaylist()
        }
    }

    override fun createPlaylist() {
        openCreatePlaylist()
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(
                this,
                injector.factoryCreatePlaylistVM
            )[CreateplaylistViewModel::class.java]
    }

    @SuppressLint("ResourceAsColor")
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
                Log.e("TAG", "NAME: " + name)
                savePlaylist?.setBackgroundResource(my_bl_sdk_rounded_button_red)
                savePlaylist?.textColor(R.color.my_sdk_color_white)
                savePlaylist?.isEnabled = true
                savePlaylist?.setOnClickListener {
                    viewModel.createPlaylist(name)
                    // requireActivity().onBackPressed()
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
    }
}