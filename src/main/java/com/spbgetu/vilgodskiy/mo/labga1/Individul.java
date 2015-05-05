package com.spbgetu.vilgodskiy.mo.labga1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ������ on 01.05.2015.
 */
public class Individul implements Comparable<Individul> {

    /**
    ����������� ��� ��������, ������ � ������������
     */
    public Individul(int m) {
        this.m = m;
    }

    /*
    ����������� � �������������� ��������� � ������������
     */
    public Individul(int m, List<int[]> gen, List<double[]> listMinMax) {
        this.m = m;
        this.setValues(gen, listMinMax);
    }

    /*
    ����������� � ��������������� ��������� � ������������
     */
    public Individul(int m, double[] vals, List<double[]> listMinMax) {
        this.m = m;
        setValues(vals, listMinMax);
    }

    //������ �����, � ������� � �������� ������������ ��������
    private List<int[]> gen;

    //������ �������� (���������������)
    private double[] vals;

    //����������� ����
    private int m;

    //�������� ������ �������
    private double fitnesVal;


    /*
    ������������ �������� ����������, �� ���� ������� ������ ����� ��������(��������������)
     */
    public void setValues(double [] valsDec, List<double[]> listMinMax){
        this.vals = valsDec.clone();
        gen = new ArrayList<int[]>();
        for (int i = 0; i < valsDec.length; i++) {
            double xMin = listMinMax.get(i)[0];
            double xMax = listMinMax.get(i)[1];
            gen.add(i, coding(valsDec[i], xMin, xMax));
        }
        this.fitnesVal = fitnesFun();
    }

    /*
    ������������ �������� ����������, �� ���� ������� ������ ����� ��������(��������������)
     */
    public void setValues(List<int[]> valsCod, List<double[]> listMinMax){

        this.gen = new ArrayList<int[]>();
        //�������� �������������� ��������
        for (int i = 0; i < valsCod.size(); i++) {
            this.gen.add(i, valsCod.get(i).clone());
        }

        this.vals = new double[valsCod.size()];
        //���������� ��������������� ��������
        for (int i = 0; i < valsCod.size(); i++) {
            double xMin = listMinMax.get(i)[0];
            double xMax = listMinMax.get(i)[1];
            this.vals[i] = decoding(valsCod.get(i), xMin, xMax);
        }
        this.fitnesVal = fitnesFun();
    }

    /*
    ���������� �������� ������ �������
     */
    public double fitnesFun(){
        return Function.valFunc(this);
    }

    /*
    ����� �������������� ��������
     */
    private double decoding(int [] codVal, double xMin, double xMax){
        int [] twoV = codVal.clone();

        //������� � ���������� ��������
        double tenV = 0;
        for (int i = 0; i < m; i++) {
            tenV += (twoV[m - i - 1] * Math.pow(2, i));
        }

        double result = (tenV * (xMax - xMin))/(Math.pow(2, m) - 1) + xMin;

        return result;
    }

    /*
    ����� ������������� ��������
     */
    private int[] coding(double value, double xMin, double xMax){

        int tenV = (int) ((int) ( (value - xMin) * (Math.pow(2,m) - 1) )/(xMax - xMin));

        int [] codVal = new int[m];
        //������� �� ����������� � �������� (��������������) ��������
        for (int i =0; i < m; i++) {
            if((tenV%2)!=0){
                codVal[m - i - 1] = 1;
            }else{
                codVal[m - i - 1] = 0;
            }
            tenV = tenV / 2;
        }
        return  codVal;
    }

    public int compareTo(Individul other) {
        if(this.fitnesFun()>other.fitnesFun()){
            return 1;
        }else{
            if(this.fitnesFun() == other.fitnesFun()){
                return 0;
            }else {
                return -1;
            }
        }
    }

    public List<int[]> getGen() {
        return gen;
    }

    public double[] getVals() {
        return vals;
    }

    public double getFitnesVal() {
        return fitnesVal;
    }

    public String getCodParam(int index){
        String result = "";
        for (int i = 0; i < gen.get(index).length; i++) {
            result = result + gen.get(index)[i];
        }
        return result;
    }
}
