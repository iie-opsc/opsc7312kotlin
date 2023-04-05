package za.ac.iie.opsc.photomemories

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult


class MainActivity : AppCompatActivity(), OnCompleteListener<AuthResult> {
    override fun onCreate(savedInstanceState: Bundle?) {
        supportFragmentManager.addFragmentOnAttachListener {
                fragmentManager, fragment ->
            if (fragment is LoginFragment) {
                fragment.loginListener = this
            }
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onComplete(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            Toast.makeText(this, "You have signed in " +
                    task.result.user?.email,
                Toast.LENGTH_SHORT).show();
            loadFragment();
        } else {
            Toast.makeText(this,
                "Boo Boo Happened when logging in",
                Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Switch the fragment on successful login.
     */
    fun loadFragment() {
        val manager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.replace(R.id.layout_fragment, MainChoiceFragment())
        transaction.commit()
    }
}