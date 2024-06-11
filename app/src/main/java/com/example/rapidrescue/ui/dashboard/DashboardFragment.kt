package com.example.rapidrescue.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.rapidrescue.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {

    private lateinit var navController: NavController
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController=Navigation.findNavController(view)


        binding.btnHygieneIssues.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://www.psychologytoday.com/intl/blog/crucial-conversations/201510/6-ways-tactfully-bring-personal-hygiene-issues")
            startActivity(intent)
        }
        binding.btnStudyMaterials.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://drive.google.com/drive/folders/13G6OLqUbEll52fvOdCy8Fc38M_0mgrRJ")
            startActivity(intent)
        }
        binding.btnHealthIssues.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://www.betterhealth.vic.gov.au/health/servicesandsupport/illness-tips-to-help-you-recover")
            startActivity(intent)
        }
        binding.btnCareerIssues.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            intent.data= Uri.parse("https://in.indeed.com/career-advice/career-development/how-to-build-career")
            startActivity(intent)
        }
    }


}