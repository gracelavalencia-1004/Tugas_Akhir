package com.example.dietkuy;

public class ProductModel2 {

    private String Kalori;
    private String Makanan;

    private ProductModel2() {}

    ProductModel2(String kalori, String makanan) {
        this.Kalori = kalori;
        this.Makanan = makanan;
    }

    public String getKalori() {
        return Kalori;
    }

    public void setKalori(String kalori) {
        this.Kalori = kalori;
    }

    public String getMakanan() {
        return Makanan;
    }

    public void setMakanan(String makanan) {
        this.Makanan = makanan;
    }
}
