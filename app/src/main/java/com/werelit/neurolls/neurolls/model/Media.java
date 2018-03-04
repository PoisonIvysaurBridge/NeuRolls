package com.werelit.neurolls.neurolls.model;

public class Media {
    /** name of the media */
    private String mMediaName;

    /** the genre of the media */
    private String mMediaGenre;

    /** year released or published of the media */
    private int mMediaYear = 9999;

    /** if the media was archived or not */
    private boolean isArchived = false;

    public Media(){

    }

    public Media(String mediaName, String mediaGenre, int releaseYear) {
        mMediaName = mediaName;
        mMediaGenre = mediaGenre;
        mMediaYear = releaseYear;
    }

    public String getmMediaName() {
        return mMediaName;
    }

    public void setmMediaName(String mMediaName) {
        this.mMediaName = mMediaName;
    }

    public String getmMediaGenre() {
        return mMediaGenre;
    }

    public void setmMediaGenre(String mMediaGenre) {
        this.mMediaGenre = mMediaGenre;
    }

    public int getmMediaYear() {
        return mMediaYear;
    }

    public void setmMediaYear(int mMediaYear) {
        this.mMediaYear = mMediaYear;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
