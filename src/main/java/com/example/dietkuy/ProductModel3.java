package com.example.dietkuy;

public class ProductModel3 {

    private String DocID;
    private String Makanan;
    private String Kalori;
    private String Berat;

    private ProductModel3() {}

    public ProductModel3(String makanan, String kalori, String berat, String docID) {
        Makanan = makanan;
        Kalori = kalori;
        Berat = berat;
        DocID = docID;
    }

    public String getDocID() {
        return DocID;
    }

    public void setDocID(String docID) {
        DocID = docID;
    }

    public String getMakanan() {
        return Makanan;
    }

    public void setMakanan(String makanan) {
        Makanan = makanan;
    }

    public String getKalori() {
        return Kalori;
    }

    public void setKalori(String kalori) {
        Kalori = kalori;
    }

    public String getBerat() {
        return Berat;
    }

    public void setBerat(String berat) {
        Berat = berat;
    }
}
