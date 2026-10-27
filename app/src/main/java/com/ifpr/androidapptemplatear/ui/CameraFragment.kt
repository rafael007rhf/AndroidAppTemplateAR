package com.ifpr.androidapptemplatear.ui

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.ar.core.Config
import com.ifpr.androidapptemplatear.R
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.math.Position

class CameraFragment : Fragment() {
    private lateinit var sceneView: ArSceneView
    private var modelNode: ArModelNode? = null
    private lateinit var placeButton: Button
    private lateinit var collectButton: Button
    private lateinit var scoreView: TextView
    private var currentModel = "cristal_azul.glb"
    private var score = 0
    private var placed = false
    private var music: MediaPlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_camera, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sceneView = view.findViewById<ArSceneView>(R.id.sceneView).apply {
            lightEstimationMode = Config.LightEstimationMode.DISABLED
        }
        placeButton = view.findViewById(R.id.place)
        collectButton = view.findViewById(R.id.collect)
        scoreView = view.findViewById(R.id.score)
        score = savedInstanceState?.getInt("score") ?: 0
        scoreView.text = "Pontos: $score"
        placeButton.isEnabled = false
        collectButton.isEnabled = false
        placeButton.setOnClickListener {
            val node = modelNode ?: return@setOnClickListener
            node.anchor()
            node.isEditable = true
            node.isScaleEditable = false
            placed = true
            placeButton.isEnabled = false
            collectButton.isEnabled = true
            sceneView.planeRenderer.isVisible = false
        }
        collectButton.setOnClickListener {
            if (placed) {
                score += 10
                scoreView.text = "Pontos: $score"
                Toast.makeText(requireContext(), "Cristal coletado! +10 pontos", Toast.LENGTH_SHORT).show()
                loadModel(currentModel)
            }
        }
        val vm = ViewModelProvider(requireActivity())[SharedModelViewModel::class.java]
        vm.selectedModel.observe(viewLifecycleOwner) { file ->
            currentModel = file
            if (hasCameraPermission()) loadModel(file)
        }
        if (hasCameraPermission()) loadModel(currentModel)
        else requestPermissions(arrayOf(Manifest.permission.CAMERA), 1001)
    }

    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) loadModel(currentModel)
        else if (requestCode == 1001) Toast.makeText(requireContext(), "Permita o acesso à câmera para jogar.", Toast.LENGTH_LONG).show()
    }

    private fun loadModel(fileName: String) {
        modelNode?.let { sceneView.removeChild(it) }
        placed = false
        placeButton.isEnabled = false
        collectButton.isEnabled = false
        val node = ArModelNode(sceneView.engine, PlacementMode.INSTANT).apply {
            loadModelGlbAsync(glbFileLocation = fileName, centerOrigin = Position(0f, 0f, 0f)) {
                if (isAdded && modelNode === this) {
                    sceneView.planeRenderer.isVisible = true
                    placeButton.isEnabled = true
                }
            }
            isEditable = true
            isScaleEditable = false
        }
        modelNode = node
        sceneView.addChild(node)
    }

    override fun onResume() {
        super.onResume()
        music = MediaPlayer.create(requireContext(), R.raw.ambiente)?.apply { isLooping = true; setVolume(0.25f, 0.25f); start() }
    }

    override fun onPause() {
        music?.release()
        music = null
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("score", score)
        super.onSaveInstanceState(outState)
    }
}
