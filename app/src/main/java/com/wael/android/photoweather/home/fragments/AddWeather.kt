package com.wael.android.photoweather.home.fragments

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.wael.android.photoweather.R
import com.wael.android.photoweather.databinding.FragmentAddWeatherBinding
import com.wael.android.photoweather.home.ViewModels.AddWeatherViewModel
import com.wael.android.photoweather.home.ViewModels.AddWeatherViewModelFactory
import com.wael.android.photoweather.home.data.Weather
import com.wael.android.photoweather.home.database.WeatherDatabase
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class AddWeather : Fragment() {
    val REQUEST_TAKE_PHOTO = 1
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1
    private val IMAGE_DIRECTORY_NAME = "VLEMONN"
    internal var imagePath: String? = ""
    lateinit var binding: FragmentAddWeatherBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val args=AddWeatherArgs.fromBundle(arguments!!)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_weather, container, false)
        val weather = args.weather
        binding.weather=weather

        binding.uploadImage.setOnClickListener { dispatchTakePictureIntent2() }

            val application = requireNotNull(this.activity).application
            val dataSource = WeatherDatabase.getInstance(application).weatherDatabaseDao
        val viewModelFactory = AddWeatherViewModelFactory(dataSource, application)
        val addWeatherViewModel =
            ViewModelProvider(this, viewModelFactory).get(AddWeatherViewModel::class.java)

        //addWeatherViewModel.insert()



        binding.save.setOnClickListener {
           weather.imagePath=currentPhotoPath
            addWeatherViewModel.insert(weather)
            findNavController().navigate(R.id.action_addWeather_to_init_fragment)

        }

        return binding.root
    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name

        val storageDir: File = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "myPic", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath


        }
    }


    private fun dispatchTakePictureIntent2() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            context?.packageManager?.let {
                takePictureIntent.resolveActivity(it)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File


                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            context!!,
                            "com.example.android.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                    }
                }
            }
        }
    }


    private fun bitmapToFile(bitmap: Bitmap): Uri {
        // Get the context wrapper
        val wrapper = ContextWrapper(context)

        // Initialize a new file instance to save bitmap object
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return the saved bitmap uri
        return Uri.parse(file.absolutePath)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            binding.weatherImage.setImageURI(Uri.parse(currentPhotoPath))
            shareImage()
            binding.uploadImage.visibility=View.INVISIBLE
            binding.save.visibility=View.VISIBLE
            galleryAddPic()
         //  var bitmap: Bitmap  = BitmapFactory.decodeFile(Uri.parse(currentPhotoPath).toString());
            //createImageFile4()
        }
    }

    private fun shareImage() {
        val share = Intent(Intent.ACTION_SEND)

        share.type = "image/*"

        share.putExtra(Intent.EXTRA_STREAM, Uri.parse(currentPhotoPath))
        startActivity(Intent.createChooser(share, "Share Image!"))
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)

            context?.sendBroadcast(mediaScanIntent)
            Log.i("nana","foa")
        }
    }



    fun convert(): ByteArray {
        val bitmap = (binding.weatherImage.getDrawable() as BitmapDrawable).getBitmap()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        return image

    }

    private fun saveImage(finalBitmap: Bitmap) {


        val root =   Environment.getExternalStorageState().toString()
        val myDir = File(root + "/capture_photo")
        myDir.mkdirs()
        val generator = Random()
        var n = 10000
        n = generator.nextInt(n)
        val OutletFname = "Image-$n.jpg"
        val file = File(myDir, OutletFname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            imagePath = file.absolutePath
            out.flush()
            out.close()


        } catch (e: Exception) {
            e.printStackTrace()

        }

    }


    private fun createImageFile4() {
        val resolver = context?.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "CuteKitten001")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/*")
            put(MediaStore.MediaColumns.RELATIVE_PATH, currentPhotoPath)
        }

        val uri = resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            resolver?.openOutputStream(uri).use {
                // TODO something with the stream

            }
        }

    }
}