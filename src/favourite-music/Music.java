/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FavouriteMusic;

import java.io.Serializable;

/**
 *
 * @author sd
 */
public class Music implements Serializable {
    public String Song_Name;
    public String Artist;
    public String Album;
    public int Year_Released;
 
    Music(String Song_Name,String Artist, String Album, int Year_Released ){
        this.Song_Name = Song_Name;
        this.Artist = Artist;
        this.Album = Album;
        this.Year_Released = Year_Released;
    }

    public String getSongName() {
        return Song_Name;
    }
    
    public String getArtist() {
        return Artist;
    }
    
    public String getAlbum() {
        return Album;
    }
    public String getYearReleased() {
        String String_Year_Released = String.valueOf(Year_Released);
        return String_Year_Released;
    }
    
    @Override
    public String toString() {
        return this.Song_Name + " " + this.Artist + this.Album + this.Year_Released; 
    }
}

