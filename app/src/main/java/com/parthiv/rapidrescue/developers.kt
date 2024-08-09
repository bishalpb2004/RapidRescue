package com.parthiv.rapidrescue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.parthiv.rapidrescue.databinding.FragmentDevelopersBinding
import com.parthiv.rapidrescue.ui.BottomSheetDialogs.BottomSheetDeveloper

class developers : Fragment() {
    private lateinit var binding: FragmentDevelopersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDevelopersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnParthiv.setOnClickListener {
            val bottomSheet = BottomSheetDeveloper.newInstance(
                "https://github.com/parthiv002",
                "https://www.linkedin.com/in/parthiv-kumar-das-9b5b87257/",
                "https://www.instagram.com/parthivkumardas/",
                "https://www.facebook.com/profile.php?id=100087913660839"
            )
            bottomSheet.show(childFragmentManager, "BottomSheetDialog")
        }

        binding.btnBishal.setOnClickListener {
            val bottomSheet = BottomSheetDeveloper.newInstance(
                "https://github.com/bishalpb2004",
                "https://www.linkedin.com/in/bishal-paul-biswas-592341257/",
                "https://www.instagram.com/bishalbiswas2004/",
                "https://www.facebook.com/profile.php?id=100087658158317"
            )
            bottomSheet.show(childFragmentManager, "BottomSheetDialog")
        }

        binding.btnTurin.setOnClickListener {
            val bottomSheet = BottomSheetDeveloper.newInstance(
                "https://github.com/Domestikus",
                "https://www.linkedin.com/in/turin-teron-369459259/",
                "https://www.instagram.com/turrrinn/",
                "https://www.facebook.com/profile.php?id=100087960463029"
            )
            bottomSheet.show(childFragmentManager, "BottomSheetDialog")
        }

        binding.btnRajdeep.setOnClickListener {
            val bottomSheet = BottomSheetDeveloper.newInstance(
                "https://github.com/Rajdeepsark",
                "https://www.linkedin.com/in/rajdeep-sarkar-00b80a244/",
                "https://www.instagram.com/rajdeeps_arkar/",
                "https://www.facebook.com/freaky.Rajdeep"
            )
            bottomSheet.show(childFragmentManager, "BottomSheetDialog")
        }
    }
}
