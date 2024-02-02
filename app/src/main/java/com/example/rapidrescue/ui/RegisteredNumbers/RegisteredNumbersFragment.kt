package com.example.rapidrescue.ui.RegisteredNumbers

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rapidrescue.R

class RegisteredNumbersFragment : Fragment() {

    companion object {
        fun newInstance() = RegisteredNumbersFragment()
    }

    private lateinit var viewModel: RegisteredNumbersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registered_numbers, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisteredNumbersViewModel::class.java)
        // TODO: Use the ViewModel
    }

}