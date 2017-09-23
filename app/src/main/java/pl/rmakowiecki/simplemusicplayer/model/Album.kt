package pl.rmakowiecki.simplemusicplayer.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Album(
        val id: Long,
        val name: String,
        val artist: String,
        val songs: MutableList<Song>,
        val albumCoverUri: Uri
) : DataModel, Parcelable

