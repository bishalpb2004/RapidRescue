import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.rapidrescue.R
import com.google.rapidrescue.databinding.FragmentAddBinding
import com.google.rapidrescue.ui.add.AddDataModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var nameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var uploadButton: Button
    private var addData:AddDataModel?=null
    private lateinit var binding:FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        nameEditText = view.findViewById(R.id.name)
        phoneNumberEditText = view.findViewById(R.id.phone_number)
        uploadButton = view.findViewById(R.id.uploadButton)

        databaseReference = FirebaseDatabase.getInstance().getReference("Name & Number")

        uploadButton.setOnClickListener {
            uploadData()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments!=null){
            addData=AddDataModel(
                arguments?.getString("name").toString(),
                arguments?.getString("phoneNumber").toString()
            )
            binding.name.setText(addData?.name)

        }
        registerEvents()

    }

    private fun registerEvents() {


    }

    private fun uploadData() {
        val name = nameEditText.text.toString().trim()
        val phoneNumber = phoneNumberEditText.text.toString().trim()

        // Check if the fields are not empty
        if (name.isNotEmpty() && phoneNumber.length == 10) {
            // Create a unique key for each entry
            val entryKey = databaseReference.push().key

            // Create a data object
            val dataModel = AddDataModel(name, phoneNumber)

            // Upload the data to Firebase Database
            entryKey?.let {
                databaseReference.child(it).setValue(dataModel)
                    .addOnSuccessListener {
                        // Data upload successful
                        showToast("Number added successfully")
                    }
                    .addOnFailureListener {
                        // Data upload unsuccessful
                        showToast("Failed to upload Number")
                    }
            }
            //clear the fields after uploading
            nameEditText.text.clear()
            phoneNumberEditText.text.clear()

            navigateToYourNextFragment()
        }
        else if(name.isEmpty()){
            showToast("Enter a Name")
        }
        else if(phoneNumber.length != 10){
            showToast("Enter a valid Phone number")
        }
    }

    private fun navigateToYourNextFragment() {
        // Use NavController to navigate to the next fragment
        findNavController().navigate(R.id.action_addFragment_to_registeredNumbersFragment2)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

