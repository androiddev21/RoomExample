package com.example.room.fragments.update

import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.room.R
import com.example.room.data.model.Address
import com.example.room.data.model.User
import com.example.room.databinding.FragmentUpdateBinding
import com.example.room.fragments.viewmodel.UserViewModel
import com.example.room.utils.generateProfilePhoto
import kotlinx.coroutines.launch

class UpdateFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentUpdateBinding

    private lateinit var userViewModel: UserViewModel

    private var photoBitmap: Bitmap? = null

    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateBinding.inflate(
            inflater,
            container,
            false
        )

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.ivPhoto.setOnClickListener {
            lifecycleScope.launch {
                photoBitmap = requireContext().generateProfilePhoto()
                binding.ivPhoto.load(photoBitmap)
            }
        }
        with(args.currentUser) {
            binding.etFirstName.setText(firstName)
            binding.etLastName.setText(lastName)
            binding.etAge.setText(age.toString())
            binding.etCity.setText(address.city)
            binding.etStreet.setText(address.street)
            binding.ivPhoto.load(profilePhoto)
        }
        binding.bUpdate.setOnClickListener {
            updateUserData(args.currentUser.id)
        }
    }

    private fun updateUserData(userId: Int) {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val age = binding.etAge.text.toString()
        val city = binding.etCity.text.toString()
        val street = binding.etStreet.text.toString()

        val photo = photoBitmap ?: args.currentUser.profilePhoto
        if (inputCheck(firstName, lastName, age, city, street)) {
            userViewModel.updateUser(
                User(
                    id = userId,
                    firstName = firstName,
                    lastName = lastName,
                    age = age.toInt(),
                    address = Address(city = city, street = street),
                    profilePhoto = photo
                )
            )
            Toast.makeText(
                requireContext(),
                getString(R.string.successfully_updated),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().navigate(R.id.action_updateFragment_to_listFragment)
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
        street: String?
    ) =
        !firstName.isNullOrBlank() && !lastName.isNullOrBlank() && !age.isNullOrBlank()
                && !city.isNullOrBlank() && !street.isNullOrBlank()

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.delete_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.menu_delete -> {
                userViewModel.deleteUser(args.currentUser)
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
                true
            }
            else -> false
        }
    }
}