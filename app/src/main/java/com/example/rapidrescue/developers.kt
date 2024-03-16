package com.example.rapidrescue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rapidrescue.databinding.FragmentDevelopersBinding
import com.example.rapidrescue.ui.BottomSheetDialogs.BottomSheet_Bishal
import com.example.rapidrescue.ui.BottomSheetDialogs.BottomSheet_Parthiv
import com.example.rapidrescue.ui.BottomSheetDialogs.BottomSheet_Rajdeep
import com.example.rapidrescue.ui.BottomSheetDialogs.BottomSheet_Turin


class developers : Fragment() {
    private lateinit var binding:FragmentDevelopersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentDevelopersBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetParthiv = BottomSheet_Parthiv()
        val bottomsheetBishal=BottomSheet_Bishal()
        val bottomsheetTurin=BottomSheet_Turin()
        val bottomsheetRajdeep=BottomSheet_Rajdeep()

        binding.btnParthiv.setOnClickListener {
            bottomSheetParthiv.show(childFragmentManager,"BottomSheetDialog")
        }

        binding.btnBishal.setOnClickListener {
            bottomsheetBishal.show(childFragmentManager,"BottomSheetDialog")
        }

        binding.btnTurin.setOnClickListener {
            bottomsheetTurin.show(childFragmentManager,"BottomSheetDialog")
        }

        binding.btnRajdeep.setOnClickListener {
            bottomsheetRajdeep.show(childFragmentManager,"BottomSheetDialog")
        }
    }
}