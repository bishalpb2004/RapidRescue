import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.rapidrescue.R
import com.example.rapidrescue.ui.add.AddDataModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var nameEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var uploadButton: Button

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

    private fun uploadData() {
        val name = nameEditText.text.toString().trim()
        val phoneNumber = phoneNumberEditText.text.toString().trim()

        // Check if the fields are not empty
        if (name.isNotEmpty() && phoneNumber.isNotEmpty()) {
            // Create a unique key for each entry
            val entryKey = databaseReference.push().key

            // Create a data object
            val dataModel = AddDataModel(name, phoneNumber)

            // Upload the data to Firebase Database
            entryKey?.let {
                databaseReference.child(it).setValue(dataModel)
            }

            // Optionally, you can clear the fields after uploading
            nameEditText.text.clear()
            phoneNumberEditText.text.clear()

            navigateToYourNextFragment()
        }
    }

    private fun navigateToYourNextFragment() {
        // Use NavController to navigate to the next fragment
        findNavController().navigate(R.id.action_addFragment_to_registeredNumbersFragment2)
    }
}

