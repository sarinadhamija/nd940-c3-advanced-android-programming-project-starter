package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

private val NOTIFICATION_ID = 0
private val REQUEST_CODE = 0

fun NotificationManager.sendNotification(applicationContext : Context, channelId : String, downloadStatus: DownloadStatus?){
    val intent = Intent(applicationContext, DetailActivity::class.java)
    intent.putExtra("downloadStatus", downloadStatus)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat
        .Builder(applicationContext,channelId)
        .setSmallIcon( R.drawable.ic_assistant_black_24dp)
        .setContentTitle("Udacity : Android Kotlin Nanodegree")
        .setContentText("The Project 3 repository is downloaded")
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(0, "Check the status", contentPendingIntent).setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotification(){
    cancelAll()
}