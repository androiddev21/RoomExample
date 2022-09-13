package com.example.room.fragments.add

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.room.R
import com.example.room.data.model.Address
import com.example.room.data.model.User
import com.example.room.fragments.viewmodel.UserViewModel
import com.example.room.databinding.FragmentAddBinding
import com.example.room.utils.generateProfilePhoto
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddFragment : Fragment() {

    private lateinit var binding: FragmentAddBinding

    private lateinit var userViewModel: UserViewModel

    private lateinit var photoBitmap: Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(
            inflater,
            container,
            false
        )

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            photoBitmap = requireContext().generateProfilePhoto()
            binding.ivPhoto.load(photoBitmap)
        }

        binding.ivPhoto.setOnClickListener{
            lifecycleScope.launch {
                photoBitmap = requireContext().generateProfilePhoto()
                binding.ivPhoto.load(photoBitmap)
            }
        }

        binding.bAdd.setOnClickListener {
            insertDataToDB()
        }
    }

    private fun insertDataToDB() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val age = binding.etAge.text.toString()
        val city = binding.etCity.text.toString()
        val street = binding.etStreet.text.toString()

        if (inputCheck(firstName, lastName, age, city, street, photoBitmap)) {
            userViewModel.addUser(
                User(
                    firstName = firstName,
                    lastName = lastName,
                    age = age.toInt(),
                    address = Address(city = city, street = street),
                    profilePhoto = photoBitmap
                )
            )
            Toast.makeText(
                requireContext(),
                getString(R.string.successfully_added),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.fill_all_data),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun inputCheck(
        firstName: String?,
        lastName: String?,
        age: String?,
        city: String?,
        street: String?,
        bitmap: Bitmap?
    ) =
        !firstName.isNullOrBlank() && !lastName.isNullOrBlank() && !age.isNullOrBlank()
                && !city.isNullOrBlank() && !street.isNullOrBlank() && bitmap != null
}