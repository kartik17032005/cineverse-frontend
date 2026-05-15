package com.example.cineversemovieapp.utils

import android.content.Context
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class MoodDetector(
    private val context: Context,
    private val onMoodDetected: (String) -> Unit
) {
    // ── ML Kit face detector setup ──
    // CLASSIFICATION_MODE_ALL = detects smiling + eye open probabilities
    private val faceDetector by lazy {
        val options = FaceDetectorOptions.Builder()
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setMinFaceSize(0.3f)  // detect faces that are at least 30% of frame
            .build()
        FaceDetection.getClient(options)
    }

    private var cameraProvider: ProcessCameraProvider? = null

    @OptIn(ExperimentalGetImage::class)
    fun startDetection(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView? = null  // null = no preview (silent mode)
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            // ── Image Analyzer ──
            // processes each frame from camera
            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysis ->
                    analysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context)
                    ) { imageProxy ->
                        val mediaImage = imageProxy.image
                        if (mediaImage != null) {
                            val image = InputImage.fromMediaImage(
                                mediaImage,
                                imageProxy.imageInfo.rotationDegrees
                            )

                            faceDetector.process(image)
                                .addOnSuccessListener { faces ->
                                    if (faces.isNotEmpty()) {
                                        val face = faces[0]

                                        // get values from ML Kit
                                        val smiling = face.smilingProbability ?: 0f
                                        val rightEye = face.rightEyeOpenProbability ?: 1f
                                        val leftEye = face.leftEyeOpenProbability ?: 1f

                                        // decide mood
                                        // smiling > 0.7 = very happy
                                        // smiling < 0.2 + eyes open = sad
                                        // both eyes < 0.3 = sleepy
                                        // smiling 0.4-0.7 = excited
                                        val mood = when {
                                            smiling > 0.7f -> "HAPPY"
                                            smiling < 0.2f &&
                                                    rightEye > 0.7f &&
                                                    leftEye > 0.7f -> "SAD"
                                            rightEye < 0.3f &&
                                                    leftEye < 0.3f -> "SLEEPY"
                                            smiling in 0.4f..0.7f -> "EXCITED"
                                            else -> "NEUTRAL"
                                        }

                                        onMoodDetected(mood)
                                    }
                                }
                                .addOnCompleteListener {
                                    imageProxy.close()  // always close!
                                }
                        } else {
                            imageProxy.close()
                        }
                    }
                }

            try {
                cameraProvider?.unbindAll()

                if (previewView != null) {
                    // with preview — user can see camera
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    cameraProvider?.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_FRONT_CAMERA,
                        preview,
                        imageAnalyzer
                    )
                } else {
                    // silent mode — no preview shown
                    cameraProvider?.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_FRONT_CAMERA,
                        imageAnalyzer
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }, ContextCompat.getMainExecutor(context))
    }

    // call this when screen is destroyed
    fun stopDetection() {
        cameraProvider?.unbindAll()
    }
}