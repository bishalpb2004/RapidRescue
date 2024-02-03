package com.example.rapidrescue.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.rapidrescue.databinding.FragmentPopUpBinding
import com.example.rapidrescue.ui.add.AddDataModel

class PopUpFragment : DialogFragment() {
    private lateinit var binding: FragmentPopUpBinding
    private lateinit var listener: DialogNextBtnClickListener
    private var numberData: AddDataModel? = null

    fun setListener(listener: DialogNextBtnClickListener) {
        this.listener = listener
    }

    companion object {
        const val TAG = "PopUpFragment"

        @JvmStatic
        fun newInstance(name: String, phoneNumber: String) = PopUpFragment().apply {
            arguments = Bundle().apply {
                putString("name", name)
                putString("phoneNumber", phoneNumber)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPopUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null) {
            numberData = AddDataModel(
                arguments?.getString("name").toString(),
                arguments?.getString("phoneNumber").toString()
            )
            binding.name.setText(numberData?.name)
            binding.phoneNumber.setText(numberData?.phoneNumber)
        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.uploadButton.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val number = binding.phoneNumber.text.toString().trim()
            if (number.length==10 && name.isNotEmpty()) {
                if (numberData == null) {
                    listener.onSaveTask(name, number, binding.phoneNumber)
                } else {
                    numberData?.name = name
                    numberData?.phoneNumber = number
                }
            } else if (number.isEmpty()) {
                Toast.makeText(
                    context,
                    "Please enter a Name",
                    Toast.LENGTH_LONG
                ).show()
            }else if (number.length!=10){
                Toast.makeText(context,
                    "Enter a valid number",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    interface DialogNextBtnClickListener {
        fun onSaveTask(name: String, phoneNumber: String, phoneNumberEt: EditText)
    }
}
