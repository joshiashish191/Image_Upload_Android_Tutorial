package net.softglobe.imageuploadandroidtutorial

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    lateinit var chooseImgBtn : Button
    lateinit var uploadImgBtn : Button
    lateinit var imageView: ImageView
    lateinit var imageUri: Uri

    val contract = registerForActivityResult(ActivityResultContracts.GetContent()) {
        imageUri = it!!
        imageView.setImageURI(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chooseImgBtn = findViewById(R.id.btn_chose_img)
        uploadImgBtn = findViewById(R.id.btn_upload_img)
        imageView = findViewById(R.id.imageView)

        chooseImgBtn.setOnClickListener {
            contract.launch("image/*")
        }

        uploadImgBtn.setOnClickListener {
            val filesDir = applicationContext.filesDir
            val file = File(filesDir, "image.png")
            val inputStream = contentResolver.openInputStream(imageUri)
            val outputStream = FileOutputStream(file)
            inputStream!!.copyTo(outputStream)

            val requestBody = RequestBody.create(MediaType.parse("image/*"), file)
            val part = MultipartBody.Part.createFormData("pic", file.name, requestBody)

            lifecycleScope.launch(Dispatchers.IO) {
                val result = RetrofitInstance.api.uploadImage(part)
                withContext(Dispatchers.Main) {
                    if (!result.error)
                        Toast.makeText(this@MainActivity, result.message, Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this@MainActivity, "Failed to upload image", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}