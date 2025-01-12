/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MAIN;

/**
 *
 * @author Lenovo
 */
// Class for diem
public class Diem {
    private String monHoc;
    private int tinChi;
    private String maSV;
    private float diem;

    // Constructor
    public Diem(String monHoc, int tinChi, String maSV, float diem) {
        this.monHoc = monHoc;
        this.tinChi = tinChi;
        this.maSV = maSV;
        this.diem = diem;
    }

    // Getters and Setters
    public String getMonHoc() {
        return monHoc;
    }

    public void setMonHoc(String monHoc) {
        this.monHoc = monHoc;
    }

    public int getTinChi() {
        return tinChi;
    }

    public void setTinChi(int tinChi) {
        this.tinChi = tinChi;
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public float getDiem() {
        return diem;
    }

    public void setDiem(float diem) {
        this.diem = diem;
    }
}


