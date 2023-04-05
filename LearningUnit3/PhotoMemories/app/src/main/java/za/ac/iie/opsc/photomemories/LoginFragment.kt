package za.ac.iie.opsc.photomemories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import za.ac.iie.opsc.photomemories.databinding.FragmentLocalImagesStoreBinding
import za.ac.iie.opsc.photomemories.databinding.FragmentLoginBinding


/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    lateinit var binding: FragmentLoginBinding
    lateinit var loginListener: OnCompleteListener<AuthResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)
        binding.btnRegister.setOnClickListener {
            val email = binding.txtUName.text.toString()
            val password = binding.txtPword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { if (it.isSuccessful) {
                        Log.d("Login", "createUserWithEmail:success")
                        val user = auth.currentUser
                    } else {
                        Log.w("Login", "createUserWithEmail:failure", it.exception)
                        Toast.makeText(context, "Unable to register",
                            Toast.LENGTH_SHORT).show()
                    } }
            }
            else {
                Toast.makeText(context, "Username and password cannot be blank",
                    Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.txtUName.text.toString()
            val password = binding.txtPword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(loginListener)
            }
            else {
                Toast.makeText(context, "Username and password cannot" +
                        "be blank",
                    Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
}