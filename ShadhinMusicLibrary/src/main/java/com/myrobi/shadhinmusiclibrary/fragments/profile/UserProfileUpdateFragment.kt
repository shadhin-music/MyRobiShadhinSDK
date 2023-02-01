package com.myrobi.shadhinmusiclibrary.fragments.profile

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

import com.myrobi.shadhinmusiclibrary.R
import com.myrobi.shadhinmusiclibrary.fragments.base.BaseFragment
import com.myrobi.shadhinmusiclibrary.utils.CircleImageView
import com.myrobi.shadhinmusiclibrary.utils.Status
import com.yalantis.ucrop.UCrop
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


internal class UserProfileUpdateFragment : BaseFragment(){
    private lateinit var viewModel: UserProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(
                this,
                injector.factoryUserProfileVM
            )[UserProfileViewModel::class.java]
        viewModel.getUserInfo()
        val profilepic = requireView().findViewById<CircleImageView>(R.id.profile_pic)
        profilepic.setImageResource(R.drawable.my_bl_sdk_ic_artist)
        setupUI()

    }
    private fun setupUI() {
        val backButton = requireView().findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener { requireActivity().onBackPressed() }
        val selectBirthday = requireView().findViewById<TextView>(R.id.select_birthday)
      selectBirthday.setOnClickListener {
            DatePickerDialog(requireActivity(), datePickerListener, 2000, 1, 1)
                .show()
        }
//        val countryCodePicker: CountryCodePicker = requireView().findViewById(R.id.countryCodePicker)
//        val countryCode:TextView = requireView().findViewById(R.id.country_code)
//        val countryNum:TextView = requireView().findViewById(R.id.country_num)
//       countryCodePicker.setOnCountryChangeListener {
//           countryCode.text = countryCodePicker.selectedCountryNameCode
//          countryNum.text = countryCodePicker.selectedCountryCode
//        }
        val selectCountry = requireView().findViewById<LinearLayout>(R.id.select_country)
        val updateProfileBtn = requireView().findViewById<TextView>(R.id.update_profile_btn)
//        selectCountry.setOnClickListener { countryCodePicker.performClick() }

       updateProfileBtn.setOnClickListener {
            updateUserProfile()
        }
        val takeImage:ImageButton = requireView().findViewById(R.id.take_image)
        takeImage.setOnClickListener {

            selectImageFromGalleryResult.launch("image/*")
        }
        var genderGroup = requireView().findViewById<RadioGroup>(R.id.gender_group)


        val  phone_number = requireView().findViewById<TextView>(R.id.phone_number)
        val etName = requireView().findViewById<EditText>(R.id.et_name)
        val profilepic = requireView().findViewById<CircleImageView>(R.id.profile_pic)
        val radio_male:RadioButton = requireView().findViewById(R.id.radio_male)
        val radio_female:RadioButton = requireView().findViewById(R.id.radio_female)
        val progressBar:ProgressBar = requireView().findViewById(R.id.progressBar)
        val name = etName.text.trim().toString()
        //val genderGroup = requireView().findViewById<RadioGroup>(R.id.gender_group)
            viewModel.getProfile.observe(viewLifecycleOwner){
             if(it?.status == Status.SUCCESS){
                 progressBar.visibility = GONE
                   phone_number.text = it.data?.data?.phoneNumber
                 if (it.data?.data?.userFullName?.toString().isNullOrBlank()== true){
                       etName.setText("")
                 }else{
                     Log.e("TAG", "onActivityResultuserFullName: "+ it.data?.data?.userFullName)
                     etName.setText(it.data?.data?.userFullName.toString())
                 }
                 if (it.data?.data?.birthDate.toString().isNullOrEmpty()){
                     selectBirthday.setText(R.string.choose_date_of_birth)
                 }else{
                     val dateString = kotlin.runCatching {
                         val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                         val date: Date? = it.data?.data?.birthDate?.let { format.parse(it.toString()) }
                         val dateFormat1 = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
                         date?.let { dateFormat1.format(it) }
                     }
                         .getOrNull()
                     if(dateString !=null){
                         selectBirthday.text = dateString
                     }else{
                         selectBirthday.setText(R.string.choose_date_of_birth)
                     }
                     //selectBirthday.text = it.data?.data?.birthDate.toString()
                 }
                 if(it?.data?.data?.userPic?.toString().isNullOrEmpty()){
                       profilepic.setImageResource(R.drawable.my_bl_sdk_ic_artist)
                 }else {
                        Log.e("TAG","PROPIC: "+it.data?.data?.userPic)
                        Glide.with(requireContext()).load("https://api.shadhinmusic.com/userpic/"+it.data?.data?.userPic).into(profilepic)

                 }

//
                     val gender = it?.data?.data?.gender
//
                 if(gender?.toString().equals("male",true) ){
                     genderGroup.check(R.id.radio_male)
                 }else if (gender?.toString().equals("female",true)){
                     genderGroup.check(R.id.radio_female)
                 }
                 Log.e("TAG", "onActivityResult: "+ it.data?.data?.phoneNumber)
                 Log.e("TAG", "onActivityResultuserPic: "+ it?.data?.data?.userPic)
                 Log.e("TAG", "onActivityResultuserFullName: "+ it.data?.data?.userFullName)
                 Log.e("TAG", "onActivityResultbirthDate: "+ it.data?.data?.birthDate)
                 Log.e("TAG", "onActivityResultbirthDate: "+ it?.data?.data?.gender)
                // val genderGroup.text = kotlin.runCatching {  requireView().findViewById<RadioButton>(it.data.gender)


             }
         }





    }
    private fun updateUserProfile() {
        val etName = requireView().findViewById<EditText>(R.id.et_name)
        val selectBirthday = requireView().findViewById<TextView>(R.id.select_birthday)
        val name = etName.text.trim().toString()
        val dateFormat1 = SimpleDateFormat("dd MMM, yyyy")

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val birthday = kotlin.runCatching {
            dateFormat1.parse(selectBirthday.text.trim().toString())
                ?.let { dateFormat.format(it) }
        }.getOrNull()
         val genderGroup = requireView().findViewById<RadioGroup>(R.id.gender_group)
        val gender = kotlin.runCatching {  requireView().findViewById<RadioButton>(genderGroup.checkedRadioButtonId)
            .text
            .toString()
            .lowercase()}.getOrNull()
        Log.e("TAG", "onActivityResult: "+ name)
        Log.e("TAG", "onActivityResult: "+ birthday.toString())
        Log.e("TAG", "onActivityResult: "+ gender.toString())
      viewModel.updateProfile(name, birthday.toString(), gender.toString())
        viewModel.updateProfile.observe(viewLifecycleOwner){
            if(it?.status == Status.SUCCESS){
                Toast.makeText(requireActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(requireActivity(), it?.message, Toast.LENGTH_SHORT).show()

            }
        }



    }
    private val selectImageFromGalleryResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->

            uri?.let {

                val outputFile = File("${requireContext().cacheDir}${File.separator}temp_image_${System.currentTimeMillis()}.png")
                val options = UCrop.Options().apply {
                    setToolbarColor(ContextCompat.getColor(requireContext(),R.color.my_sdk_color_primary))
                    setStatusBarColor(ContextCompat.getColor(requireContext(),R.color.my_sdk_black1))
                    setToolbarWidgetColor(ContextCompat.getColor(requireContext(),R.color.my_sdk_color_white))
                    setActiveControlsWidgetColor(ContextCompat.getColor(requireContext(),R.color.my_sdk_color_primary))
                }
                UCrop.of(uri, Uri.fromFile(outputFile))
                    .withAspectRatio(1f, 1f)
                    .withOptions(options)
                    .withMaxResultSize(400, 400)
                    .start(requireContext(),this,UCrop.REQUEST_CROP)

            }


        }

    private val datePickerListener =
        DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
            val selectBirthday = requireView().findViewById<TextView>(R.id.select_birthday)
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR,selectedYear)
            calendar.set(Calendar.MONTH,selectedMonth)
            calendar.set(Calendar.DAY_OF_MONTH,selectedDay)

            val dateFormat = SimpleDateFormat("dd MMM, yyyy")
            val dateString = dateFormat.format(calendar.time)

          selectBirthday.text = dateString

        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("AuthRepository", "onActivityResult")
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
 val profile_pic = requireView().findViewById<CircleImageView>(R.id.profile_pic)
            val resultUri = data?.let { UCrop.getOutput(it) };
            val finalFile = resultUri?.path?.let { File(it) }

            Glide.with(this)
                .load(resultUri)
                .placeholder(R.drawable.my_bl_sdk_ic_artist)
                .into(profile_pic)
            Log.e("TAG", "onActivityResult: "+ resultUri)
            Log.e("TAG", "onActivityResult: "+ finalFile)
            viewModel.profileImageFile = finalFile
        }
    }

}