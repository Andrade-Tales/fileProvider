package com.fileprovider.photodemo

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.fileprovider.photodemo.databinding.ActivityMainBinding
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {

    // VIEW BINDING
    private lateinit var binding: ActivityMainBinding

    // URI DA IMAGEM PARA COMPARTILHAR, ESCOLHA NA GALERIA
    private var imageUri: Uri? = null

    // TEXTO PARA COMPARTILHAR
    private var textToShare = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // manipula o clique, escolhe a imagem
        binding.imageIv.setOnClickListener {
            pickImage()
        }

        // manipula o clique, compartilha o texto
        binding.shareTextBtn.setOnClickListener {

            // obtém o texto do texto de edição
            textToShare = binding.textEt.text.toString().trim()

            // verifica se o texto está vazio ou não
            if (textToShare.isEmpty()) {

                showToast("Entre com o texto...")
            } else {
                shareText()
            }
        }
        // lida com clique, compartilha imagem e texto
        binding.shareImgBtn.setOnClickListener {
            // check image is picked or not
            if (imageUri == null) {
                showToast("Escolha a imagem...")
            } else {
                shareImage()
            }

            // obtém o texto do texto de edição
            textToShare = binding.textEt.text.toString().trim()


            // verifica se o texto está vazio ou não
            if (textToShare.isEmpty()) {

                showToast("Entre com o texto...")
            } else if (imageUri == null) {

                showToast("Escolha a imagem")
            } else {

                shareImageText()
            }
        }
    }

    private fun pickImage() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private var galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->

            // manipula o resultado da seleção da imagem em ambos os casos; escolhido ou não
            if (result.resultCode == Activity.RESULT_OK) {


                // imagem escolhida
                showToast("Imagem escolhida da galeria")
                val intent = result.data
                imageUri = intent!!.data


                // configura a imagem para imageView
                binding.imageIv.setImageURI(imageUri)
            } else {

                // cancelled
                showToast("Cancelar")
            }
        }
    )

    private fun showToast(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun shareText() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "Texto/simples"
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            "Escreva aqui"
        ) // se compartilhar por e-mail, pode ser útil
        intent.putExtra(Intent.EXTRA_TEXT, textToShare)
        startActivity(Intent.createChooser(intent, "Compatilhar"))
    }

    private fun shareImage() {
        val contentUri = getContentUri()
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "Imagem/png"
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            "Escreva aqui"
        ) // se compartilhar por e-mail, pode ser útil
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, "Compatilhar"))

    }

    private fun shareImageText() {
        val contentUri = getContentUri()
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "Imagem/png"
        intent.putExtra(
            Intent.EXTRA_SUBJECT,
            "Escreva aqui"
        ) // se compartilhar por e-mail, pode ser útil
        intent.putExtra(Intent.EXTRA_TEXT, textToShare)
        intent.putExtra(Intent.EXTRA_STREAM, contentUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent, "Compatilhar"))

    }

    private fun getContentUri(): Uri? {
        val bitmap: Bitmap
        // obtém bitmap de uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(contentResolver, imageUri!!)
            bitmap = ImageDecoder.decodeBitmap(source)
        } else {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        }

        // se você deseja obter bitmap de imageview em vez de uri
        /* val bitmapDrawable = binding.imageIv.drawable as BitmapDrawable
        * bitmap = bitmapDrawable.bitmap */

        val imagesFolder = File(cacheDir, "images")
        var contentUri: Uri? = null
        try {

            imagesFolder.mkdirs()   // criar pasta se não existir
            val file = File(imagesFolder, "shared_image_png")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream)
            stream.flush()
            stream.close()
            contentUri = FileProvider.getUriForFile(this, "com.fileprovider.photodemo", file)
        } catch (e: java.lang.Exception) {

            showToast("${e.message}")
        }
        return contentUri
    }

}