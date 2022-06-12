package com.udacity

import java.io.Serializable

data class DownloadStatus(val title : String, val isSuccessful : Boolean) : Serializable
