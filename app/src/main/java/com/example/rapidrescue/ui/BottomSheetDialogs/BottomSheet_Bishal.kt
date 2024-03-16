package com.example.rapidrescue.ui.BottomSheetDialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rapidrescue.R
import com.example.rapidrescue.databinding.FragmentBottomSheetBishalBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheet_Bishal : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentBottomSheetBishalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentBottomSheetBishalBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gitIcon.setOnClickListener {
            val intent= Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://github.com/bishalpb2004")
            startActivity(intent)
        }

        binding.linkedInIcon.setOnClickListener {
            val intent= Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://www.linkedin.com/in/bishal-paul-biswas-592341257/")
            startActivity(intent)
        }

        binding.instaIcon.setOnClickListener {
            val intent= Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://www.instagram.com/bishalbiswas2004/")
            startActivity(intent)
        }

        binding.fbIcon.setOnClickListener {
            val intent= Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://www.facebook.com/profile.php?id=100087658158317")
            startActivity(intent)
        }
    }
}