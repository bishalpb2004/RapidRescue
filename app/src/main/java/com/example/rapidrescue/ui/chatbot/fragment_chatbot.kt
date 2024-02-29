package com.example.rapidrescue.ui.chatbot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.rapidrescue.R
import com.example.rapidrescue.databinding.FragmentProfileBinding

class fragment_chatbot : Fragment() {

    companion object {
        fun newInstance() = fragment_chatbot()
    }

    private lateinit var viewModel: FragmentChatbotViewModel
    private var _binding: FragmentProfileBinding? = null
    private lateinit var navController: NavController
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chatbot, container, false)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController= Navigation.findNavController(view)
        navController.navigate(R.id.chatbotFragment)
    }

}