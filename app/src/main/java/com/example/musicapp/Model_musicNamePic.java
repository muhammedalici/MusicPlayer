package com.example.musicapp;

public class Model_musicNamePic {
    String isim;
    int resim;

    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public int getResim() {
        return resim;
    }

    public void setResim(int resim) {
        this.resim = resim;
    }

    public Model_musicNamePic() {
    }


    public Model_musicNamePic(String isim, int resim) {
        this.isim = isim;
        this.resim = resim;
    }
}
