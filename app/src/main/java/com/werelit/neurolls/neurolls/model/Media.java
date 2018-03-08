package com.werelit.neurolls.neurolls.model;

public class Media {

    /** the ID of the media */
    private int mediaID;

    /** name of the media */
    private String mMediaName;

    /** the genre of the media */
    private String mMediaGenre;

    /** year released or published of the media */
    private String mMediaYear = "9999";

    /** if the media was archived or not */
    private boolean isArchived = false;


    public static final int CATEGORY_FILMS = 1;
    public static final int CATEGORY_BOOKS = 2;
    public static final int CATEGORY_GAMES = 3;

    public Media(){

    }

    public Media(String mediaName, String mediaGenre, String releaseYear) {
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

    public String getmMediaYear() {
        return mMediaYear;
    }

    public void setmMediaYear(String mMediaYear) {
        this.mMediaYear = mMediaYear;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }
}
