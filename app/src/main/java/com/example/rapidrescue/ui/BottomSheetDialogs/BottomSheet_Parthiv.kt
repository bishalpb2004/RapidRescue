package com.example.rapidrescue.ui.BottomSheetDialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rapidrescue.databinding.FragmentBottomSheetParthivBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheet_Parthiv : BottomSheetDialogFragment() {

    private lateinit var binding:FragmentBottomSheetParthivBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentBottomSheetParthivBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.gitIcon.setOnClickListener {
            val intent= Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://github.com/parthiv002")
            startActivity(intent)
        }

        binding.linkedInIcon.setOnClickListener {
            val intent= Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://www.linkedin.com/in/parthiv-kumar-das-9b5b87257/")
            startActivity(intent)
        }

        binding.instaIcon.setOnClickListener {
            val intent= Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://www.instagram.com/parthivkumardas/")
            startActivity(intent)
        }

        binding.fbIcon.setOnClickListener {
            val intent= Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://www.facebook.com/profile.php?id=100087913660839")
            startActivity(intent)
        }

    }
}