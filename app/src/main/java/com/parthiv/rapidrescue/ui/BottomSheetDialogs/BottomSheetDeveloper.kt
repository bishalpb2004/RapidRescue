package com.parthiv.rapidrescue.ui.BottomSheetDialogs

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.parthiv.rapidrescue.databinding.FragmentBottomSheetDeveloperBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDeveloper : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetDeveloperBinding

    companion object {
        private const val ARG_GITHUB = "github"
        private const val ARG_LINKEDIN = "linkedin"
        private const val ARG_INSTAGRAM = "instagram"
        private const val ARG_FACEBOOK = "facebook"

        fun newInstance(github: String, linkedin: String, instagram: String, facebook: String): BottomSheetDeveloper {
            val fragment = BottomSheetDeveloper()
            val args = Bundle().apply {
                putString(ARG_GITHUB, github)
                putString(ARG_LINKEDIN, linkedin)
                putString(ARG_INSTAGRAM, instagram)
                putString(ARG_FACEBOOK, facebook)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetDeveloperBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val githubUrl = arguments?.getString(ARG_GITHUB)
        val linkedinUrl = arguments?.getString(ARG_LINKEDIN)
        val instagramUrl = arguments?.getString(ARG_INSTAGRAM)
        val facebookUrl = arguments?.getString(ARG_FACEBOOK)

        binding.gitIcon.setOnClickListener {
            openUrl(githubUrl)
        }

        binding.linkedInIcon.setOnClickListener {
            openUrl(linkedinUrl)
        }

        binding.instaIcon.setOnClickListener {
            openUrl(instagramUrl)
        }

        binding.fbIcon.setOnClickListener {
            openUrl(facebookUrl)
        }
    }

    private fun openUrl(url: String?) {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(it)
            startActivity(intent)
        }
    }
}
