package com.example.rapidrescue.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.rapidrescue.R
import com.example.rapidrescue.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var navController: NavController
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentProfileBinding.inflate(inflater,container,false)
        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        navController=Navigation.findNavController(view)
        binding.btnAccessAfterProfile.setOnClickListener {
            navController.navigate(R.id.action_navigation_notifications_to_afterProfileFragment)

        }
        binding.btnFetchData.setOnClickListener {
            navController.navigate(R.id.action_navigation_notifications_to_fetchUserDataFragment)

        }
        binding.developersBtn.setOnClickListener {
            navController.navigate(R.id.action_navigation_notifications_to_developers_btn)
        }
    }
}