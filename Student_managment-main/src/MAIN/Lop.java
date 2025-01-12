/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package MAIN;

/**
 *
 * @author Lenovo
 */
// Class for lop
public class Lop {
    private String maLop;
    private String tenLop;
    private String khoa;
    private String chuyenNganh;

    // Constructor
    public Lop(String maLop, String tenLop, String khoa, String chuyenNganh) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.khoa = khoa;
        this.chuyenNganh = chuyenNganh;
    }

    // Getters and Setters
    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getTenLop() {
        return tenLop;
    }

    public void setTenLop(String tenLop) {
        this.tenLop = tenLop;
    }

    public String getKhoa() {
        return khoa;
    }

    public void setKhoa(String khoa) {
        this.khoa = khoa;
    }

    public String getChuyenNganh() {
        return chuyenNganh;
    }

    public void setChuyenNganh(String chuyenNganh) {
        this.chuyenNganh = chuyenNganh;
    }
}
