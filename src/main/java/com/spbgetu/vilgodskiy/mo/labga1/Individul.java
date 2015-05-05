package com.spbgetu.vilgodskiy.mo.labga1;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Сергей on 01.05.2015.
 */
public class Individul implements Comparable<Individul> {

    /**
    Конструктор без значений, только с разрядностью
     */
    public Individul(int m) {
        this.m = m;
    }

    /*
    Конструктор с закодированным значением и разрядностью
     */
    public Individul(int m, List<int[]> gen, List<double[]> listMinMax) {
        this.m = m;
        this.setValues(gen, listMinMax);
    }

    /*
    Конструктор с раскодированным значением и разрядностью
     */
    public Individul(int m, double[] vals, List<double[]> listMinMax) {
        this.m = m;
        setValues(vals, listMinMax);
    }

    //Список генов, в которых в массивах закодированы значения
    private List<int[]> gen;

    //Массив значений (раскодированных)
    private double[] vals;

    //разрядность гена
    private int m;

    //значение фитнес функции
    private double fitnesVal;


    /*
    Установление значения параметров, на вход подаётся массив новых значений(декодированных)
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
    Установление значения параметров, на вход подаётся массив новых значений(закодированных)
     */
    public void setValues(List<int[]> valsCod, List<double[]> listMinMax){

        this.gen = new ArrayList<int[]>();
        //Копируем закодированные значения
        for (int i = 0; i < valsCod.size(); i++) {
            this.gen.add(i, valsCod.get(i).clone());
        }

        this.vals = new double[valsCod.size()];
        //Генерируем раскодированные значения
        for (int i = 0; i < valsCod.size(); i++) {
            double xMin = listMinMax.get(i)[0];
            double xMax = listMinMax.get(i)[1];
            this.vals[i] = decoding(valsCod.get(i), xMin, xMax);
        }
        this.fitnesVal = fitnesFun();
    }

    /*
    Вычисление значения фитнес функции
     */
    public double fitnesFun(){
        return Function.valFunc(this);
    }

    /*
    Метод раскодирования значения
     */
    private double decoding(int [] codVal, double xMin, double xMax){
        int [] twoV = codVal.clone();

        //Перевод в десятичное значение
        double tenV = 0;
        for (int i = 0; i < m; i++) {
            tenV += (twoV[m - i - 1] * Math.pow(2, i));
        }

        double result = (tenV * (xMax - xMin))/(Math.pow(2, m) - 1) + xMin;

        return result;
    }

    /*
    Метод закодирования значения
     */
    private int[] coding(double value, double xMin, double xMax){

        int tenV = (int) ((int) ( (value - xMin) * (Math.pow(2,m) - 1) )/(xMax - xMin));

        int [] codVal = new int[m];
        //Перевод из десятичного в двоичное (закодированное) значение
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
