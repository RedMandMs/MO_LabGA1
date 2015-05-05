package com.spbgetu.vilgodskiy.mo.labga1;

import javax.swing.table.TableModel;
import java.util.*;

/**
 * Created by ������ on 01.05.2015.
 */
public class GenAlg {

    //GUI
    GUI gui;

    //����������� �����������
    private int m;
    //����� ������
    private List<Individul> population;
    //������ ���������
    private int sizePop;
    //xMin � xMax ��� ������� ���������
    private List<double[]> listMinMax;
    //������� ������� ����������������� ������ � ��������� ����� (��� ������� ��������)
    private double eps = 0.01;
    //����� ������ ���������� � �����������
    private List<Individul> selectPopul;
    //����������� ������ ������������ � �������
    private int kTour = 4;
    //����� ���������
    private List<Individul> newPopulation;
    //����������� �����������
    private double pCross = 0.9;
    //����������� �������
    private double pMut = 0.05;
    //���������� ���������� �������
    private int amountPar;

    /**
     * �����������
     * @param population - ������ ������
     * @param m - ����������� �����������
     */
    public GenAlg(List<Individul> population, int m) {
        this.population = population;
        this.m = m;
    }

    /**
     * �����������
     * @param info - ��� �������  � �����������:
     *             "m" - int - �����������;
     *             "sizePop" - int - ������ ����������� ���������;
     *             "listMinMax" - List<double[]> - ������ ����������� � ������������ �������� ���-��;
     *             "eps" - double - ����������� ��� �������� ��������
     *             "kTour" - int - ���-�� ������ ��� ���������� ������
     *             "pCross" -  double - ����������� �����������
     *             "pMut" - double - ����������� �������
     *             "amountPar" - int - ����� ���������� �������
     */
    public GenAlg(Map<String, Object> info) {
        this.m = (Integer) info.get("m");
        this.sizePop = (Integer) info.get("sizePop");
        this.listMinMax = (List<double[]>) info.get("listMinMax");
        this.eps = (Double) info.get("eps");
        this.kTour = (Integer) info.get("kTour");
        this.pCross = (Double) info.get("pCross");
        this.pMut = (Double) info.get("pMut");
        this.amountPar = (Integer) info.get("amountPar");
        generateG(amountPar);
    }

    /*
    ������ ��� ������������!!!
     */
    public GenAlg(int m, int amountPar, List<double[]> minMax) {
        this.m = m;
        this.listMinMax = minMax;
        generateG(amountPar);

    }

    /**
     * ������������ ��������
     * @param isFull - ��������� �������� ���������(true) ��� ������ ���� ���(false)
     * @return - ������� �� ������ �����
     */
    public boolean activeAlg(boolean isFull){
        if(goAlgorithm(isFull)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * ������ ������������� ���������
     * @param isFull - ��������� ���������(true) ��� ������ ���� ���(false)
     * @return - ������ �����
     */
    private boolean goAlgorithm(boolean isFull){
        do{
            // ������ ���� ��� ��������� � ����
            // �� ����������� ��������������
            // ����������� ���� � ����� ���������
            if(goOneStepAlgorithm()){
                return true;
            }
        }while (isFull);
        return false;
    }

    /**
     * ��������� ���� ��� ���������
     * @return - �������� �� ��������
     */
    private boolean goOneStepAlgorithm(){
        sortPopulation();
        if(stopConditions()){
            return true;
        }else{
            selection();
            genOperations();
            createNewPopulation();
            sortPopulation();
            return false;
        }
    }

    private void genOperations() {
        crossPlusMut();
    }

    /**
     * ������������� �������������� ���������
     */
    private void generateG(int amountPar){
        Random rnd = new Random();

        population = new ArrayList<Individul>();

        //��������� ���������
        for (int i = 0; i < sizePop; i++) {
            double[] vals = new double[amountPar];
            //���������� ����������
            for (int j = 0; j < amountPar; j++) {
                double xMin = listMinMax.get(j)[0];
                double xMax = listMinMax.get(j)[1];
                double val = rnd.nextDouble() * (xMax - xMin) + xMin;
                vals[j] = val;
            }
            Individul individul = new Individul(m, vals, listMinMax);
            population.add(individul);
        }
    }

    /**
     * ������� ���������� ���������
     */
    private void sortPopulation(){
        Collections.sort(population);
    }

    /**
     * ������� ��������
     * @return - ����������� ��?
     */
    private boolean stopConditions(){
        if((population.get(sizePop-1).fitnesFun() - population.get(0).fitnesFun())<eps){
            return true;
        }else return false;
    }

    /**
     * ������ (��������� �����)
     */
    private void selection(){
        this.selectPopul = new ArrayList<Individul>();
        Random rnd = new Random();

        //�������� N ������ ���������� �� �����������
        for (int i = 0; i < sizePop; i++) {
            ArrayList<Individul> turnList = new ArrayList<Individul>();
            //����� ������ ��� ������� �� kTour ������
            for (int j = 0; j < kTour; j++) {
                int num = rnd.nextInt(sizePop);
                turnList.add(population.get(num));
            }
            //��������� ������ �� ����������� ������ �������
            Collections.sort(turnList);
            // � �������� ������ �����, ������� ��������� � ������ ��� �����������
            this.selectPopul.add(turnList.get(0));
        }
    }

    /**
     * ������� ��� ���������� ����������� � �������
     */
    private void crossPlusMut(){
        this.newPopulation = new ArrayList<Individul>();
        Random rnd = new Random();

        //������������ ������ ���������
        for (int i = 0; i < sizePop/2; i++) {
            Individul ind1 = selectPopul.remove(rnd.nextInt(selectPopul.size()));
            Individul ind2 = selectPopul.remove(rnd.nextInt(selectPopul.size()));

            //����������?
            if(rnd.nextDouble()<pCross){
                List<Individul> newIndivs = cross(ind1, ind2);
                newPopulation.add(newIndivs.get(0));
                newPopulation.add(newIndivs.get(1));
                //�������
                newPopulation.add(mutation(ind1));
                newPopulation.add(mutation(ind2));
            }
            //��������� ������������ ������ � ����� ���������
            this.newPopulation.add(ind1);
            this.newPopulation.add(ind2);
        }
    }

    /**
     * ���������� ����������� ���� ������
     * @param ind1 - ����� 1
     * @param ind2 - ����� 2
     * @return - ������ � ������ �������
     */
    private List<Individul> cross(Individul ind1, Individul ind2){
        Random rnd = new Random();
        Individul newInd1 = new Individul(m);
        List<int[]> codVal1 = new ArrayList<int[]>();
        Individul newInd2 = new Individul(m);
        List<int[]> codVal2 = new ArrayList<int[]>();

        int countPar = ind1.getGen().size();
        //������� �� ����������(���������� �������)
        for (int i = 0; i < countPar; i++) {
            int[] hromosoma1 = new int[m];
            int[] hromosoma2 = new int[m];
            //������� �� ����� ������ ���������
            for (int j = 0; j < m; j++) {
                if(rnd.nextDouble()>=0.5){
                    hromosoma1[j] = ind1.getGen().get(i)[j];
                    hromosoma2[j] = ind2.getGen().get(i)[j];
                }else{
                    hromosoma1[j] = ind2.getGen().get(i)[j];
                    hromosoma2[j] = ind1.getGen().get(i)[j];
                }
            }
            codVal1.add(hromosoma1);
            codVal2.add(hromosoma2);
        }
        newInd1.setValues(codVal1, this.listMinMax);
        newInd2.setValues(codVal2, this.listMinMax);
        List<Individul> resultlist = new ArrayList<Individul>();
        resultlist.add(newInd1);
        resultlist.add(newInd2);
        return resultlist;
    }

    /**
     * ������� �����
     * @param ind - ���������� �����
     * @return - �����, ������������� �����
     */
    private Individul mutation(Individul ind){
        Random rnd = new Random();
        int countPar = ind.getGen().size();

        Individul newInd = new Individul(m);
        List<int[]> codVal = new ArrayList<int[]>();
        //������� �� ����������(���������� �������)
        for (int i = 0; i < countPar; i++) {
            int[] hromosoma = new int[m];
            //������� �� ����� ������ ���������
            for (int j = 0; j < m; j++) {
                if(rnd.nextDouble() >= pMut) {
                    //�����������
                    if(ind.getGen().get(i)[j] == 0) {
                        hromosoma[j] = 1;
                    }else {
                        hromosoma[j] = 0;
                    }
                }else {
                    //�� �����������
                    hromosoma[j] = ind.getGen().get(i)[j];
                }
            }
            codVal.add(hromosoma);
        }
        newInd.setValues(codVal, this.listMinMax);
        return newInd;
    }


    private void createNewPopulation() {
        this.population = newPopulation;
    }

    public Object[][] getData() {
        Object[][] data = new Object[sizePop][5];
        //��������� ������
        for (int i = 0; i < sizePop; i++) {
            data[i][0] = population.get(i).getCodParam(0);
            data[i][1] = population.get(i).getVals()[0];
            data[i][2] = population.get(i).getCodParam(1);
            data[i][3] = population.get(i).getVals()[1];
            data[i][4] = population.get(i).getFitnesVal();
        }
        return data;
    }
}
