package com.example.identifyme.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHost
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.identifyme.R
import com.example.identifyme.databinding.ActivityMainBinding
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Base64
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.security.DigestException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNavigation()
    }

    private fun initBottomNavigation(){
        val bottomNavigationView = binding.myBottonNav
        val navController = supportFragmentManager.findFragmentById(binding.myFragmentViewContainer.id) as? NavHostFragment
        bottomNavigationView.setupWithNavController(navController!!.navController)
    }
}