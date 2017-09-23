package pl.rmakowiecki.simplemusicplayer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListSectionHeader(
        val headerext: String
) : DataModel, Parcelable