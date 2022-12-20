package com.fileprovider.photodemo

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fileprovider.photodemo.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    // VIEW BINDING
    private lateinit var binding: ActivityMainBinding

    // URI OF IMAGE TO SHARE, PICK TO GALLERY
    private var imageUri: Uri? = null

    // TEXT TO SHARE, FROM  EDIT TEXT
    private var textToShare = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // handle click, pick image
        binding.imageIv.setOnClickListener{

        }

        // handle click, share text
        binding.shareTextBtn.setOnClickListener{

        }
        // handle click, share image & text
        binding.shareImgBtn.setOnClickListener{

        }
    }
}