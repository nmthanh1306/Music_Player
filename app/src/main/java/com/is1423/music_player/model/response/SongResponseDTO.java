package com.is1423.music_player.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SongResponseDTO implements Parcelable { // gui du lieu object hoac mang object

    private Long songId;

    private String albumId;

    private String typeId;

    private String playlistId;

    private String songImage;

    private String songName;

    private String singer;

    private String linkSong;

    private int favourite;

    protected SongResponseDTO(Parcel in) {
        if (in.readByte() == 0) {
            songId = null;
        } else {
            songId = in.readLong();
        }
        albumId = in.readString();
        typeId = in.readString();
        playlistId = in.readString();
        songImage = in.readString();
        songName = in.readString();
        singer = in.readString();
        linkSong = in.readString();
        favourite = in.readInt();
    }

    public static final Creator<SongResponseDTO> CREATOR = new Creator<SongResponseDTO>() {
        @Override
        public SongResponseDTO createFromParcel(Parcel in) {
            return new SongResponseDTO(in);
        }

        @Override
        public SongResponseDTO[] newArray(int size) {
            return new SongResponseDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (songId == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(songId);
        }
        parcel.writeString(albumId);
        parcel.writeString(typeId);
        parcel.writeString(playlistId);
        parcel.writeString(songImage);
        parcel.writeString(songName);
        parcel.writeString(singer);
        parcel.writeString(linkSong);
        parcel.writeInt(favourite);
    }
}
