package com.example.tasks.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.R
import com.example.tasks.service.helper.FingerprintHelper
import com.example.tasks.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*
import java.util.concurrent.Executor

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        // Inicializa eventos
        setListeners()
        observe()

        // Verifica se usuário está logado
        verifyLoggedUser()
    }

    private fun showAuthentication() {
        if (!FingerprintHelper.isAuthenticationAvailable(this)) {
            return
        }

        //Executor
        val executor: Executor = ContextCompat.getMainExecutor(this)

        //BiometricPrompt
        val biometricPrompt = BiometricPrompt(
            this@LoginActivity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        //BiometricPrompt INFO
        val info: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Titulo")
            .setSubtitle("Subti")
            .setDescription("descri")
            .setNegativeButtonText("Cancelar")
            .build()


        biometricPrompt.authenticate(info)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_login) {
            handleLogin()
        } else if (v.id == R.id.text_register) {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    /**
     * Inicializa os eventos de click
     */
    private fun setListeners() {
        button_login.setOnClickListener(this)
        text_register.setOnClickListener(this)
    }

    /**
     * Verifica se usuário está logado
     */
    private fun verifyLoggedUser() {
        mViewModel.verifyLoggedUser()
    }

    /**
     * Observa ViewModel
     */
    private fun observe() {
        mViewModel.login.observe(this, Observer {
            if (it.success()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, it.failure(), Toast.LENGTH_LONG).show()
            }
        })

        mViewModel.loggedUser.observe(this, Observer { logged ->
            if (logged) {
                showAuthentication()
            }
        })
    }

    /**
     * Autentica usuário
     */
    private fun handleLogin() {
        val email = edit_email.text.toString()
        val password = edit_password.text.toString()

        mViewModel.doLogin(email, password)
    }
}
