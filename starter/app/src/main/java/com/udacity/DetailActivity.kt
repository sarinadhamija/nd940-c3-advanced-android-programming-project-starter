package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        setSupportActionBar(binding.toolbar)

        val downloadStatus: DownloadStatus =
            intent.extras?.getSerializable("downloadStatus") as DownloadStatus
        downloadStatus.let {
            binding.contentDetail.optionStatusValue.text =
                if (it.isSuccessful) getString(R.string.download_status_success) else getString(R.string.download_status_fail)
            binding.contentDetail.optionTypeValue.text = it.title
        }

        binding.contentDetail.detailBtnOk.setOnClickListener { finish() }
    }
}
