package pl.rmakowiecki.simplemusicplayer.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Song(
        val id: Long,
        val title: String,
        val artist: String,
        val albumName: String,
        val albumCoverUri: Uri,
        val durationMillis: Int
) : DataModel, Parcelable