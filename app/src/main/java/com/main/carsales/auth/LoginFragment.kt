package com.main.carsales.auth

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.main.carsales.R
import com.main.carsales.databinding.FragmentLoginBinding
import com.main.carsales.main.MainActivity


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.loginButton.setOnClickListener {
            val email = binding.emailLogin.text.toString()
            val password = binding.passwordLogin.text.toString()
            signIn(email, password)
        }

        binding.goToRegister.setOnClickListener {
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.registerFragment)
        }

        auth = Firebase.auth
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            reload();
        }
    }

    private fun reload() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = binding.emailLogin.text.toString()
        if (TextUtils.isEmpty(email)) {
            binding.emailLogin.error = "Required."
            valid = false
        } else {
            binding.emailLogin.error = null
        }

        val password = binding.passwordLogin.text.toString()
        if (TextUtils.isEmpty(password)) {
            binding.passwordLogin.error = "Required."
            valid = false
        } else {
            binding.passwordLogin.error = null
        }

        return valid
    }

    private fun signIn(email: String, password: String) {
        Log.d(ContentValues.TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(
                        context, "Успешный вход.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(user)
                    val navController = NavHostFragment.findNavController(this)
                    navController.navigate(R.id.action_loginFragment_to_mainActivity)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Ошибка авторизации.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
    }
}