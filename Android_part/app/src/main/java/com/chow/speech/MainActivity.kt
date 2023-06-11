package com.chow.speech

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.gesture.GestureLibraries
import android.gesture.GestureLibrary
import android.gesture.GestureOverlayView
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.baidu.aip.asrwakeup3.core.inputstream.InFileStream
import com.baidu.aip.asrwakeup3.core.util.AuthUtil
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.example.cameraalbumtest.R
import com.example.cameraalbumtest.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() , EventListener {
    private val createGesture = 1
    private var tts: TextToSpeech? = null
    private lateinit var gestureLibrary: GestureLibrary
    private lateinit var gestureView: GestureOverlayView

    //CameraX
    private lateinit var viewBinding: ActivityMainBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private var wakeup: EventManager? = null
    private var asr: EventManager? = null

    companion object {
        private const val TAG = "CameraXApp"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
//                Manifest.permission.RECORD_AUDIO
//                Manifest.permission.READ_EXTERNAL_STORAGE
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        // Hide the status bar.
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        actionBar?.hide()

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val sharedpreferences =
            getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        if (!sharedpreferences.getBoolean("gestureCreated", false)) {
            val editor = sharedpreferences.edit()
            editor.putBoolean("gestureCreated", true)
            editor.apply()

            val intentGesture = Intent(this, GestureActivity::class.java)
            startActivityForResult(intentGesture, createGesture)
            tts = TextToSpeech(this, TextToSpeech.OnInitListener { status: Int ->
                if (status == TextToSpeech.SUCCESS) {
                    val result = tts?.setLanguage(Locale.CHINESE)
                    if (result == TextToSpeech.LANG_AVAILABLE) {
                        tts?.speak(
                            "首次使用该应用时，请打开文件访问权限，以支持手势识别。请在屏幕上绘制手势。",
                            TextToSpeech.QUEUE_ADD,
                            null
                        )
                    }
                }
            })
        }

        //第一次以后访问
        if (allPermissionsGranted()) {
            startCamera()
            loadGestureLib()
            viewBinding.takePhotoBtn.setOnClickListener {
                if(tts?.isSpeaking==true){
                    tts?.stop()
                }
                // Get a stable reference of the modifiable image capture use case

                // Create time stamped name and MediaStore entry.
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, "temp")
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
                    }
                }

                // Create output options object which contains file + metadata
                val outputOptions = ImageCapture.OutputFileOptions
                    .Builder(contentResolver,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
                    .build()

                // Set up image capture listener, which is triggered after photo has
                // been taken
                imageCapture!!.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(this),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(exception: ImageCaptureException) {
                            TODO("Not yet implemented")
                        }
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().path+"/Pictures/CameraX-Image/temp.jpg")
                            val recognizer =
                                TextRecognition.getClient(
                                    ChineseTextRecognizerOptions.Builder().build()
                                )
                            val image = InputImage.fromBitmap(bitmap!!, 0)
                            val result = recognizer.process(image)
                                .addOnSuccessListener { visionText ->
//                                    viewBinding.ocrResult.text = visionText.text
//                                    viewBinding.ocrResult.movementMethod = ScrollingMovementMethod.getInstance()

                                    //文本转语音
                                    viewBinding.stopBtn.setOnClickListener {
                                        if (tts == null) {
                                            tts = TextToSpeech(
                                                this@MainActivity,
                                                TextToSpeech.OnInitListener { status: Int ->
                                                    if (status === TextToSpeech.SUCCESS) {
                                                        val result = tts?.setLanguage(Locale.CHINESE)
                                                        if (result == TextToSpeech.LANG_AVAILABLE) {
                                                            tts?.speak(
                                                                visionText.text,
                                                                TextToSpeech.QUEUE_ADD,
                                                                null
                                                            )
                                                        }
                                                    }
                                                })
                                        } else {
                                            if (tts!!.isSpeaking) {
                                                tts?.stop()
                                            } else {
                                                tts?.speak(
                                                    visionText.text,
                                                    TextToSpeech.QUEUE_ADD,
                                                    null
                                                )
                                            }
                                        }
                                    }
                                }
                                .addOnFailureListener { e ->
                                    println("ocr fail!")
                                }
                            val fdelete = File(Environment.getExternalStorageDirectory().path+"/Pictures/CameraX-Image/temp.jpg")
                            if (fdelete.exists()) {
                                if (fdelete.delete()) {
                                    println("file Deleted ")
                                } else {
                                    println("file not Deleted ")
                                }
                            }
                        }
                    }
                )
            }
            cameraExecutor = Executors.newSingleThreadExecutor()
        }

        initPermission()
        // 基于SDK唤醒词集成1.1 初始化EventManager
        wakeup = EventManagerFactory.create(this, "wp")
        // 基于SDK唤醒词集成1.3 注册输出事件
        wakeup?.registerListener(this) //  EventListener 中 onEvent方法
        //初始化EventManager对象
        asr = EventManagerFactory.create(this, "asr")
        //注册自己的输出事件类
        asr?.registerListener(this) //  EventListener 中 onEvent方法
        asr_start()
        wakeup_start()

    }

    /**
     * 测试参数填在这里
     * 基于SDK唤醒词集成第2.1 设置唤醒的输入参数
     */
    private fun wakeup_start() {
//        txtLog.setText("")
        // 基于SDK唤醒词集成第2.1 设置唤醒的输入参数
        val params = AuthUtil.getParam()
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        params[SpeechConstant.WP_WORDS_FILE] = "assets:///WakeUp.bin"
        // "assets:///WakeUp.bin" 表示WakeUp.bin文件定义在assets目录下
        InFileStream.setContext(this)
        var json: String? = null // 这里可以替换成你需要测试的json
        json = JSONObject(params).toString()
        wakeup!!.send(SpeechConstant.WAKEUP_START, json, null, 0, 0)
//        printLog("输入参数：$json")
    }

    // 基于SDK唤醒词集成第4.1 发送停止事件
    private fun wakeup_stop() {
        wakeup!!.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0) //
    }

    private fun asr_start() {
        val params = AuthUtil.getParam()
        var event: String? = null
        event = SpeechConstant.ASR_START // 替换成测试的event
        params[SpeechConstant.DECODER] = 2
        // 基于SDK集成2.1 设置识别参数
        params[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        var json: String? = null // 可以替换成自己的json
        json = JSONObject(params).toString() // 这里可以替换成你需要测试的json
        asr!!.send(event, json, null, 0, 0)
    }

    // 基于SDK唤醒词集成第4.1 发送停止事件
    private fun asr_stop() {
        asr?.send(SpeechConstant.ASR_STOP, "{}", null, 0, 0);
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.imageView.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()
                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)
            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == createGesture){
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
                loadGestureLib()
                viewBinding.takePhotoBtn.setOnClickListener {
                    if(tts?.isSpeaking==true){
                        tts?.stop()
                    }
                    // Get a stable reference of the modifiable image capture use case

                    // Create time stamped name and MediaStore entry.
                    val contentValues = ContentValues().apply {
                        put(MediaStore.MediaColumns.DISPLAY_NAME, "temp")
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
                        }
                    }

                    // Create output options object which contains file + metadata
                    val outputOptions = ImageCapture.OutputFileOptions
                        .Builder(contentResolver,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)
                        .build()

                    // Set up image capture listener, which is triggered after photo has
                    // been taken
                    imageCapture!!.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(this),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onError(exception: ImageCaptureException) {
                                TODO("Not yet implemented")
                            }
                            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                val bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().path+"/Pictures/CameraX-Image/temp.jpg")
                                val recognizer =
                                    TextRecognition.getClient(
                                        ChineseTextRecognizerOptions.Builder().build()
                                    )
                                val image = InputImage.fromBitmap(bitmap!!, 0)
                                val result = recognizer.process(image)
                                    .addOnSuccessListener { visionText ->
//                                        viewBinding.ocrResult.text = visionText.text
//                                        viewBinding.ocrResult.movementMethod = ScrollingMovementMethod.getInstance()

                                        //文本转语音
                                        viewBinding.stopBtn.setOnClickListener {
                                            if (tts == null) {
                                                tts = TextToSpeech(
                                                    this@MainActivity,
                                                    TextToSpeech.OnInitListener { status: Int ->
                                                        if (status === TextToSpeech.SUCCESS) {
                                                            val result = tts?.setLanguage(Locale.CHINESE)
                                                            if (result == TextToSpeech.LANG_AVAILABLE) {
                                                                tts?.speak(
                                                                    visionText.text,
                                                                    TextToSpeech.QUEUE_ADD,
                                                                    null
                                                                )
                                                            }
                                                        }
                                                    })
                                            } else {
                                                if (tts!!.isSpeaking) {
                                                    tts?.stop()
                                                } else {
                                                    tts?.speak(
                                                        visionText.text,
                                                        TextToSpeech.QUEUE_ADD,
                                                        null
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        println("ocr fail!")
                                    }
                                val fdelete = File(Environment.getExternalStorageDirectory().path+"/Pictures/CameraX-Image/temp.jpg")
                                if (fdelete.exists()) {
                                    if (fdelete.delete()) {
                                        println("file Deleted ")
                                    } else {
                                        println("file not Deleted ")
                                    }
                                }
                            }
                        }
                    )
                }
                cameraExecutor = Executors.newSingleThreadExecutor()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun loadGestureLib(){
        gestureLibrary =
            GestureLibraries.fromFile(Environment.getExternalStorageDirectory().absolutePath + "/mygestures")
        if (gestureLibrary.load()) {
            Toast.makeText(this@MainActivity, "手势文件装载成功！", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "手势文件装载失败！", Toast.LENGTH_SHORT).show()
        }
        gestureView = findViewById(R.id.gestureRecognize)
        gestureView.isGestureVisible = false
        gestureView.addOnGesturePerformedListener { _, gesture ->
            val predictions = gestureLibrary.recognize(gesture)
            val result = ArrayList<String>()
            predictions.sortByDescending { it.score }
            val pred = predictions[0]
            if (pred.score > 2.0) {
                when (pred.name) {
                    "1" -> viewBinding.takePhotoBtn.performClick()
                    "2" -> viewBinding.stopBtn.performClick()
                    "3" -> {}
                    else -> {}
                }
            } else {
                Toast.makeText(this, "无法找到能匹配的手势！", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        tts?.stop()
        tts?.shutdown()
        wakeup!!.send(SpeechConstant.WAKEUP_STOP, "{}", null, 0, 0)
        // 基于SDK集成4.2 发送取消事件
        asr?.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        // 基于SDK集成5.2 退出事件管理器
        // 必须与registerListener成对出现，否则可能造成内存泄露
        asr?.unregisterListener(this);
    }

    //  基于SDK唤醒词集成1.2 自定义输出事件类 EventListener  回调方法
    // 基于SDK唤醒3.1 开始回调事件
    override fun onEvent(name: String, params: String?, data: ByteArray?, offset: Int, length: Int) {
//        Log.d(TAG, String.format("event: name=%s, params=%s", name, params))
        //唤醒事件
        if (name == "wp.data") {
            try {
                val json = JSONObject(params)
                val errorCode = json.getInt("errorCode")
                if (errorCode == 0) {
                    //唤醒成功
                    Toast.makeText(this@MainActivity,"唤醒成功",Toast.LENGTH_SHORT).show()
                    tts?.stop()
                    asr!!.send(SpeechConstant.ASR_START, "{}", null, 0, 0)
                } else {
                    //唤醒失败
                    Toast.makeText(this@MainActivity,"唤醒失败",Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        } else if ("wp.exit" == name) {
            //唤醒已停止
//            Toast.makeText(this@MainActivity,"唤醒停止",Toast.LENGTH_SHORT).show()
        }else if(name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)){
            // 一句话的临时结果，最终结果及语义结果
            if (params!!.contains("\"final_result\"")) {

                val gson = Gson()
                val asRresponse = gson.fromJson(params, ASRresponse::class.java)
                    ?: return //数据解析转实体bean

                Toast.makeText(this@MainActivity,asRresponse.best_result,Toast.LENGTH_SHORT).show()
                val res = asRresponse.best_result;
                if(res!!.contains("拍照")){
                    viewBinding.takePhotoBtn.performClick()
                }else if(res!!.contains("播放")||res!!.contains("朗读")||res!!.contains("暂停")||res!!.contains("停止")){
                    viewBinding.stopBtn.performClick()
                }else if(res!!.contains("围城")) {
                    viewBinding.stopBtn.setOnClickListener {
                        if (tts == null) {
                            tts = TextToSpeech(
                                this@MainActivity,
                                TextToSpeech.OnInitListener { status: Int ->
                                    if (status === TextToSpeech.SUCCESS) {
                                        val result = tts?.setLanguage(Locale.CHINESE)
                                        if (result == TextToSpeech.LANG_AVAILABLE) {
                                            tts?.speak(
                                                "《围城》全集\n" +
                                                        "\n" +
                                                        "作者：钱钟书\n" +
                                                        "\n" +
                                                        "序\n" +
                                                        "\n" +
                                                        "在这本书里，我想写现代中国某一部分社会、某一类人物。写这类人，我没\n" +
                                                        "\n" +
                                                        "忘记他们是人类，只是人类，具有无毛两足动物的基本根性。角色当然是虚构的\n" +
                                                        "\n" +
                                                        "，但是有考据癖的人也当然不肯错过索隐的杨会、放弃附会的权利的。\n" +
                                                        "\n" +
                                                        "这本书整整写了两年。两年里忧世伤生，屡想中止。由于杨绛女士不断的督\n" +
                                                        "\n" +
                                                        "促，替我挡了许多事，省出时间来，得以锱铢积累地写完。照例这本书该献给她\n" +
                                                        "\n" +
                                                        "。不过，近来觉得献书也像“致身于国”、“还政于民”等等佳话，只是语言幻\n" +
                                                        "\n" +
                                                        "成的空花泡影，名说交付出去，其实只仿佛魔术家玩的飞刀，放手而并没有脱手\n" +
                                                        "\n" +
                                                        "。随你怎样把作品奉献给人，作品总是作者自已的。大不了一本书，还不值得这\n" +
                                                        "\n" +
                                                        "样精巧地不老实，因此罢了。",
                                                TextToSpeech.QUEUE_ADD,
                                                null
                                            )
                                        }
                                    }
                                })
                        } else {
                            if (tts!!.isSpeaking) {
                                tts?.stop()
                            } else {
                                tts?.speak(
                                    "《围城》全集\n" +
                                            "\n" +
                                            "作者：钱钟书\n" +
                                            "\n" +
                                            "序\n" +
                                            "\n" +
                                            "在这本书里，我想写现代中国某一部分社会、某一类人物。写这类人，我没\n" +
                                            "\n" +
                                            "忘记他们是人类，只是人类，具有无毛两足动物的基本根性。角色当然是虚构的\n" +
                                            "\n" +
                                            "，但是有考据癖的人也当然不肯错过索隐的杨会、放弃附会的权利的。\n" +
                                            "\n" +
                                            "这本书整整写了两年。两年里忧世伤生，屡想中止。由于杨绛女士不断的督\n" +
                                            "\n" +
                                            "促，替我挡了许多事，省出时间来，得以锱铢积累地写完。照例这本书该献给她\n" +
                                            "\n" +
                                            "。不过，近来觉得献书也像“致身于国”、“还政于民”等等佳话，只是语言幻\n" +
                                            "\n" +
                                            "成的空花泡影，名说交付出去，其实只仿佛魔术家玩的飞刀，放手而并没有脱手\n" +
                                            "\n" +
                                            "。随你怎样把作品奉献给人，作品总是作者自已的。大不了一本书，还不值得这\n" +
                                            "\n" +
                                            "样精巧地不老实，因此罢了。",
                                    TextToSpeech.QUEUE_ADD,
                                    null
                                )
                            }
                        }
                    }
                    viewBinding.stopBtn.performClick()
                }else if (res!!.contains("活着")){
                    viewBinding.stopBtn.setOnClickListener {
                        if (tts == null) {
                            tts = TextToSpeech(
                                this@MainActivity,
                                TextToSpeech.OnInitListener { status: Int ->
                                    if (status === TextToSpeech.SUCCESS) {
                                        val result = tts?.setLanguage(Locale.CHINESE)
                                        if (result == TextToSpeech.LANG_AVAILABLE) {
                                            println(res.toString())
                                            tts?.speak(
                                                "《活着》\n" +
                                                        "\n" +
                                                        "\n" +
                                                        " 前言\n" +
                                                        "\n" +
                                                        "    一位真正的作家永远只为内心写作，只有内心才会真实地告诉他，他的自私、他的高尚是多么突出。内心让他真实地了解自己，一旦了解了自己也就了解了世界。很多年前我就明白了这个原则，可是要捍卫这个原则必须付出艰辛的劳动和长时期的痛苦，因为内心并非时时刻刻都是敞开的，它更多的时候倒是封闭起来，于是只有写作，不停地写作才能使内心敞开，才能使自己置身于发现之中，就像日出的光芒照亮了黑暗，灵感这时候才会突然来到。\n" +
                                                        "\n" +
                                                        "    长期以来，我的作品都是源出于和现实的那一层紧张关系。我沉湎于想象之中，又被现实紧紧控制，我明确感受着自我的分裂，我无法使自己变得纯粹，我曾经希望自己成为一位童话作家，要不就是一位实实在在作品的拥有者，如果我能够成为这两者中的任何一个，我想我内心的痛苦将会轻微得多，可是与此同时我的力量也会削弱很多。\n" +
                                                        "\n" +
                                                        "    事实上我只能成为现在这样的作家，我始终为内心的需要而写作，理智代替不了我的写作，正因为此，我在很长一段时间是一个愤怒和冷漠的作家。\n" +
                                                        "\n" +
                                                        "    这不只是我个人面临的困难，几乎所有优秀的作家都处于和现实的紧张关系中，在他们笔下，只有当现实处于遥远状态时，他们作品中的现实才会闪闪发亮。应该看到，这过去的现实虽然充满魅力，可它已经蒙上了一层虚幻的色彩，那里面塞满了个人想象和个人理解。真正的现实，也就是作家生活中的现实，是令人费解和难以相处的。\n" +
                                                        "\n" +
                                                        "    作家要表达与之朝夕相处的现实，他常常会感到难以承受，蜂拥而来的真实几乎都在诉说着丑恶和阴险，怪就怪在这里，为什么丑恶的事物总是在身边，而美好的事物却远在海角。换句话说，人的友爱和同情往往只是作为情绪来到，而相反的事实则是伸手便可触及。正像一位诗人所表达的：人类无法忍受太多的真实。也有这样的作家，一生都在解决自我和现实的紧张关系，福克纳是最为成功的例子，他找到了一条温和的途径，他描写中间状态的事物，同时包容了美好与丑恶，他将美国南方的现实放到了历史和人文精神之中，这是真正意义上的文学现实，因为它连接着过去和将来。\n" +
                                                        "\n" +
                                                        "    一些不成功的作家也在描写现实，可他们笔下的现实说穿了只是一个环境，是固定的，死去的现实，他们看不到人是怎样走过来的，也看不到怎样走去。当他们在描写斤斤计较的人物时，我们会感到作家本人也在斤斤计较，这样的作家是在写实在的作品，而不是现实的作品。\n" +
                                                        "\n" +
                                                        "    前面已经说过，我和现实关系紧张，说得严重一些，我一直是以敌对的态度看待现实。随着时间的推移，我内心的愤怒渐渐平息，我开始意识到一位真正的作家所寻找的是真理，是一种排斥道德判断的真理。作家的使命不是发泄，不是控诉或者揭露，他应该向人们展示高尚。这里所说的高尚不是那种单纯的美好，而是对一切事物理解之后的超然，对善与恶一视同仁，用同情的目光看待世界。\n" +
                                                        "\n" +
                                                        "    正是在这样的心态下，我听到了一首美国民歌《老黑奴》，歌中那位老黑奴经历了一生的苦难，家人都先他而去，而他依然友好地对待世界，没有一句抱怨的话。这首歌深深打动了我，我决定写下一篇这样的小说，就是这篇《活着》，写人对苦难的承受能力，对世界乐观的态度。写作过程让我明白，人是为活着本身而活着的，而不是为活着之外的任何事物所活着。我感到自己写下了高尚的作品。\n",
                                                TextToSpeech.QUEUE_ADD,
                                                null
                                            )
                                        }
                                    }
                                })
                        } else {
                            if (tts!!.isSpeaking) {
                                tts?.stop()
                            } else {
                                tts?.speak(
                                    "《活着》\n" +
                                            "\n" +
                                            "\n" +
                                            " 前言\n" +
                                            "\n" +
                                            "    一位真正的作家永远只为内心写作，只有内心才会真实地告诉他，他的自私、他的高尚是多么突出。内心让他真实地了解自己，一旦了解了自己也就了解了世界。很多年前我就明白了这个原则，可是要捍卫这个原则必须付出艰辛的劳动和长时期的痛苦，因为内心并非时时刻刻都是敞开的，它更多的时候倒是封闭起来，于是只有写作，不停地写作才能使内心敞开，才能使自己置身于发现之中，就像日出的光芒照亮了黑暗，灵感这时候才会突然来到。\n" +
                                            "\n" +
                                            "    长期以来，我的作品都是源出于和现实的那一层紧张关系。我沉湎于想象之中，又被现实紧紧控制，我明确感受着自我的分裂，我无法使自己变得纯粹，我曾经希望自己成为一位童话作家，要不就是一位实实在在作品的拥有者，如果我能够成为这两者中的任何一个，我想我内心的痛苦将会轻微得多，可是与此同时我的力量也会削弱很多。\n" +
                                            "\n" +
                                            "    事实上我只能成为现在这样的作家，我始终为内心的需要而写作，理智代替不了我的写作，正因为此，我在很长一段时间是一个愤怒和冷漠的作家。\n" +
                                            "\n" +
                                            "    这不只是我个人面临的困难，几乎所有优秀的作家都处于和现实的紧张关系中，在他们笔下，只有当现实处于遥远状态时，他们作品中的现实才会闪闪发亮。应该看到，这过去的现实虽然充满魅力，可它已经蒙上了一层虚幻的色彩，那里面塞满了个人想象和个人理解。真正的现实，也就是作家生活中的现实，是令人费解和难以相处的。\n" +
                                            "\n" +
                                            "    作家要表达与之朝夕相处的现实，他常常会感到难以承受，蜂拥而来的真实几乎都在诉说着丑恶和阴险，怪就怪在这里，为什么丑恶的事物总是在身边，而美好的事物却远在海角。换句话说，人的友爱和同情往往只是作为情绪来到，而相反的事实则是伸手便可触及。正像一位诗人所表达的：人类无法忍受太多的真实。也有这样的作家，一生都在解决自我和现实的紧张关系，福克纳是最为成功的例子，他找到了一条温和的途径，他描写中间状态的事物，同时包容了美好与丑恶，他将美国南方的现实放到了历史和人文精神之中，这是真正意义上的文学现实，因为它连接着过去和将来。\n" +
                                            "\n" +
                                            "    一些不成功的作家也在描写现实，可他们笔下的现实说穿了只是一个环境，是固定的，死去的现实，他们看不到人是怎样走过来的，也看不到怎样走去。当他们在描写斤斤计较的人物时，我们会感到作家本人也在斤斤计较，这样的作家是在写实在的作品，而不是现实的作品。\n" +
                                            "\n" +
                                            "    前面已经说过，我和现实关系紧张，说得严重一些，我一直是以敌对的态度看待现实。随着时间的推移，我内心的愤怒渐渐平息，我开始意识到一位真正的作家所寻找的是真理，是一种排斥道德判断的真理。作家的使命不是发泄，不是控诉或者揭露，他应该向人们展示高尚。这里所说的高尚不是那种单纯的美好，而是对一切事物理解之后的超然，对善与恶一视同仁，用同情的目光看待世界。\n" +
                                            "\n" +
                                            "    正是在这样的心态下，我听到了一首美国民歌《老黑奴》，歌中那位老黑奴经历了一生的苦难，家人都先他而去，而他依然友好地对待世界，没有一句抱怨的话。这首歌深深打动了我，我决定写下一篇这样的小说，就是这篇《活着》，写人对苦难的承受能力，对世界乐观的态度。写作过程让我明白，人是为活着本身而活着的，而不是为活着之外的任何事物所活着。我感到自己写下了高尚的作品。\n",
                                    TextToSpeech.QUEUE_ADD,
                                    null
                                )
                            }
                        }
                    }
                    viewBinding.stopBtn.performClick()
                }
            }
        }
    }


    /**
     * android 6.0 以上需要动态申请权限
     */
    private fun initPermission() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val toApplyList = ArrayList<String>()
        for (perm in permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                    this,
                    perm
                )
            ) {
                toApplyList.add(perm)
                // 进入到这里代表没有权限.
            }
        }
        val tmpList = arrayOfNulls<String>(toApplyList.size)
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123)
        }
    }
}




