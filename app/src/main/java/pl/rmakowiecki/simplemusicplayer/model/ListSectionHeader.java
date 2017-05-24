package pl.rmakowiecki.simplemusicplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

public final class ListSectionHeader extends DataModel implements Parcelable {
    private final String headerText;

    public String getHeaderext() {
        return headerText;
    }

    public ListSectionHeader(String headerText) {
        this.headerText = headerText;
    }

    protected ListSectionHeader(Parcel in) {
        headerText = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headerText);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ListSectionHeader> CREATOR = new Creator<ListSectionHeader>() {
        @Override
        public ListSectionHeader createFromParcel(Parcel in) {
            return new ListSectionHeader(in);
        }

        @Override
        public ListSectionHeader[] newArray(int size) {
            return new ListSectionHeader[size];
        }
    };
}
