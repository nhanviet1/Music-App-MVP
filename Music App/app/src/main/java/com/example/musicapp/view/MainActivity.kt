package com.example.musicapp.view

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.R
import com.example.musicapp.data.model.Song
import com.example.musicapp.data.repository.SongRepository
import com.example.musicapp.data.source.local.MusicLocalDataSource
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.service.BoundService
import com.example.musicapp.view.adapter.SongAdapter
import com.example.musicapp.view.presenter.SongPresenter
import com.example.mvpmusicapp.ui.ServiceInterface
import com.example.mvpmusicapp.ui.SongInterface
import java.util.*

class MainActivity : AppCompatActivity(), SongInterface.View, ServiceInterface {
    private var list = mutableListOf<Song>()
    private lateinit var songAdapter: SongAdapter
    private var songPresenter: SongInterface.Presenter? = null
    private var isServiceConnected = false
    private lateinit var mService: BoundService
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
//    private val receiver by lazy { Receiver() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initService()
        setup()
    }


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as BoundService.MyBinder
            mService = binder.getBoundService()
            mService.setProgressInterface(this@MainActivity)
            isServiceConnected = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isServiceConnected = false
        }
    }

    private fun setup() {
        songAdapter = SongAdapter(this@MainActivity, ::onClickItem)
        binding.rvMusicList.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvMusicList.adapter = songAdapter
        songPresenter = SongPresenter(
            SongRepository.getInstance(MusicLocalDataSource.getInstance(this@MainActivity)),
            this@MainActivity
        )
        storagePermission()
        bindingButton()
    }

    private fun onClickItem(position: Int) {
        if (isServiceConnected){
            if (mService.songList.isEmpty()){
                mService.setData(list)
                mService.playSong(position)

            } else {
                mService.playSong(position)
            }
        }
        setProgressBar()
        updateProgress()
        checkingButtonState()
    }

    override fun getAllSongList(songList: List<Song>?) {
        songAdapter.setData(songList!!)
        list = songList.toMutableList()
    }

    override fun showErrorMessage() {
        Toast.makeText(this, R.string.text_load_song_failed, Toast.LENGTH_SHORT).show()
    }

    override fun setPlayButton() {
        if (isServiceConnected){
            if (mService.songList.isEmpty()){
                mService.setData(list)
                mService.playSong(mService.songHolder)
            } else {
                mService.playSong(mService.songHolder)
            }
        }
        setProgressBar()
        updateProgress()
        binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_24)
    }

    override fun setPauseButton() {
        mService.pauseMusic()
        binding.playBtn.setImageResource(R.drawable.ic_baseline_play_circle_24)
    }

    private fun storagePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val requestPermissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted ->
                if (isGranted) {
                    songPresenter?.getLocalSongs()
                }
            }
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }


    private fun bindingButton() {
        binding.playBtn.setOnClickListener {
            if (!mService.isPlaying){
                songPresenter?.playMusic(true)
            } else {
                songPresenter?.playMusic(false)
            }
        }

        binding.previousBtn.setOnClickListener {
            if (isServiceConnected){
                if (mService.songList.isEmpty()){
                    mService.setData(list)
                    mService.switchSong("previous")
                } else {
                    mService.switchSong("previous")
                }
            }
            binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_24)
        }
        binding.nextBtn.setOnClickListener {
            if (isServiceConnected){
                if (mService.songList.isEmpty()){
                    mService.setData(list)
                    mService.switchSong("next")
                } else {
                    mService.switchSong("next")
                }
            }
            binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_24)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isServiceConnected) {
            unbindService(connection)
            isServiceConnected = false
        }
    }

    fun checkingButtonState(){
        when(mService.isPlaying){
            true -> binding.playBtn.setImageResource(R.drawable.ic_baseline_pause_circle_24)
            false -> binding.playBtn.setImageResource(R.drawable.ic_baseline_play_circle_24)
        }
    }

    private fun initService() {
        val intent = Intent(this, BoundService::class.java)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    override fun updateProgress(position: Int?) {
        Thread { binding.progressMusic.progress = position ?: 0 }.start()

        if (mService.currentPosition()!! < mService.getDuration()!!) {
            binding.progressMusic.progress =
                mService.currentPosition() ?: 0
        }
    }

    private fun setProgressBar() {
        binding.progressMusic.max = mService.getDuration() ?: 0
        binding.progressMusic.progress = 0
    }

    private fun updateProgress() {
        val musicTime = Timer()
        musicTime.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    if (mService.currentPosition()!! < mService.getDuration()!!) {
                        binding.progressMusic.progress =
                            mService.currentPosition() ?: 0
                    } else {
                        binding.progressMusic.progress = 0
                        musicTime.cancel()
                        musicTime.purge()
                    }
                }
            }

        }, TIME_DELAY, TIME_PERIOD)
    }
    override fun onCheckButtonState() {
        checkingButtonState()
    }

}