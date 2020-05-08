package com.example.dietkuy;

public class ProductModel {

    private String Makanan;
    private long Berat;
    private long Kalori;

    public ProductModel() {}

    public ProductModel(String Makanan, long Berat, long Kalori) {
        this.Makanan = Makanan;
        this.Berat = Berat;
        this.Kalori = Kalori;
    }

    public String getMakanan() {
        return Makanan;
    }

    public void setMakanan(String makanan) {
        Makanan = makanan;
    }

    public long getBerat() {
        return Berat;
    }

    public void setBerat(long berat) {
        Berat = berat;
    }

    public long getKalori() {
        return Kalori;
    }

    public void setKalori(long kalori) {
        Kalori = kalori;
    }
}
