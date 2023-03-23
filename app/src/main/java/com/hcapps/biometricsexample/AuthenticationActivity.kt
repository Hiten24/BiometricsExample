package com.hcapps.biometricsexample

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.hcapps.biometricsexample.databinding.ActivityAuthenticationBinding

class AuthenticationActivity: FragmentActivity() {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var binding: ActivityAuthenticationBinding

    private val lottieSuccessAnimationListener = object: Animator.AnimatorListener {

        var intent: Intent? = null

        override fun onAnimationStart(animation: Animator) {
            intent = Intent(this@AuthenticationActivity, MainActivity::class.java)
        }

        override fun onAnimationEnd(animation: Animator) {
            startActivity(intent)
            binding.animationView.removeAllAnimatorListeners()
            finish()
        }

        override fun onAnimationCancel(animation: Animator) {}

        override fun onAnimationRepeat(animation: Animator) {}
    }

    private val biometricCallback = object: BiometricPrompt.AuthenticationCallback() {

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            binding.animationView.setAnimation(R.raw.wrong_fingerprint)
            binding.animationView.playAnimation()
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            binding.animationView.setAnimation(R.raw.fingerprint_success)
            binding.animationView.playAnimation()
            binding.animationView.addAnimatorListener(lottieSuccessAnimationListener)
        }

        override fun onAuthenticationFailed() {
            binding.animationView.setAnimation(R.raw.wrong_fingerprint)
            binding.animationView.playAnimation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        biometricPrompt = BiometricPrompt(this, biometricCallback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(getString(R.string.biometric_prompt_title))
            .setSubtitle(getString(R.string.biometric_prompt_description))
            .setNegativeButtonText(getString(R.string.cancel))
            .build()

        biometricPrompt.authenticate(promptInfo)

    }

}