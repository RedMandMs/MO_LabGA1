package com.spbgetu.vilgodskiy.mo.labga1;

import javax.swing.table.TableModel;
import java.util.*;

/**
 * Created by Сергей on 01.05.2015.
 */
public class GenAlg {

    //GUI
    GUI gui;

    //Разрядность кодирования
    private int m;
    //Набор особей
    private List<Individul> population;
    //Размер популяции
    private int sizePop;
    //xMin и xMax для каждого параметра
    private List<double[]> listMinMax;
    //Разница функции преспособленности лидера и наихудшей особи (для условия останова)
    private double eps = 0.01;
    //Набор особей допущенных к размножению
    private List<Individul> selectPopul;
    //Колличество особей учавствующих в турнире
    private int kTour = 4;
    //Новое поколение
    private List<Individul> newPopulation;
    //Вероятность скрещивания
    private double pCross = 0.9;
    //Вероятность мутации
    private double pMut = 0.05;
    //Количество параметров функции
    private int amountPar;

    /**
     * Конструктор
     * @param population - список особей
     * @param m - разрядность кодирования
     */
    public GenAlg(List<Individul> population, int m) {
        this.population = population;
        this.m = m;
    }

    /**
     * Конструктор
     * @param info - Хеш таблица  с параметрами:
     *             "m" - int - разрядность;
     *             "sizePop" - int - размер создаваемой популяции;
     *             "listMinMax" - List<double[]> - список минимальных и максимальных значений пар-ов;
     *             "eps" - double - погрешность для критерия останова
     *             "kTour" - int - кол-во особей для турнирного отбора
     *             "pCross" -  double - вероятность скрещивания
     *             "pMut" - double - вероятность мутации
     *             "amountPar" - int - число параметров функции
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
    ТОЛЬКО ДЛЯ ТЕСТИРОВАНИЯ!!!
     */
    public GenAlg(int m, int amountPar, List<double[]> minMax) {
        this.m = m;
        this.listMinMax = minMax;
        generateG(amountPar);

    }

    /**
     * Активировать алгоритм
     * @param isFull - выполнить алгоритм полностью(true) или только один шаг(false)
     * @return - найдена ли лучшая особь
     */
    public boolean activeAlg(boolean isFull){
        if(goAlgorithm(isFull)){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Запуск генетического алгоритма
     * @param isFull - выполнить полностью(true) или только один шаг(false)
     * @return - лучшая особь
     */
    private boolean goAlgorithm(boolean isFull){
        do{
            // Делаем один шаг олгаритма и если
            // он оказывается заключительным
            // заканчиваем цикл и выдаём результат
            if(goOneStepAlgorithm()){
                return true;
            }
        }while (isFull);
        return false;
    }

    /**
     * Выполнить один шаг алгоритма
     * @return - закончен ли алгоритм
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
     * Сгенерировать первоначальную популяцию
     */
    private void generateG(int amountPar){
        Random rnd = new Random();

        population = new ArrayList<Individul>();

        //Заполение популяции
        for (int i = 0; i < sizePop; i++) {
            double[] vals = new double[amountPar];
            //Заполнение параметров
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
     * Функция сортировки популяции
     */
    private void sortPopulation(){
        Collections.sort(population);
    }

    /**
     * Условие останова
     * @return - выполняется ли?
     */
    private boolean stopConditions(){
        if((population.get(sizePop-1).fitnesFun() - population.get(0).fitnesFun())<eps){
            return true;
        }else return false;
    }

    /**
     * Секция (турнирный отбор)
     */
    private void selection(){
        this.selectPopul = new ArrayList<Individul>();
        Random rnd = new Random();

        //Выбираем N особей допущенных до скрещивания
        for (int i = 0; i < sizePop; i++) {
            ArrayList<Individul> turnList = new ArrayList<Individul>();
            //выбор особей для турнира из kTour особей
            for (int j = 0; j < kTour; j++) {
                int num = rnd.nextInt(sizePop);
                turnList.add(population.get(num));
            }
            //Сортируем турнир по возрастанию фитнес функции
            Collections.sort(turnList);
            // и выбираем лучшую особь, которую добавляем в список для скрещивания
            this.selectPopul.add(turnList.get(0));
        }
    }

    /**
     * Функция для выполнения скрещивания и мутации
     */
    private void crossPlusMut(){
        this.newPopulation = new ArrayList<Individul>();
        Random rnd = new Random();

        //Формирование нового поколения
        for (int i = 0; i < sizePop/2; i++) {
            Individul ind1 = selectPopul.remove(rnd.nextInt(selectPopul.size()));
            Individul ind2 = selectPopul.remove(rnd.nextInt(selectPopul.size()));

            //Скрещивать?
            if(rnd.nextDouble()<pCross){
                List<Individul> newIndivs = cross(ind1, ind2);
                newPopulation.add(newIndivs.get(0));
                newPopulation.add(newIndivs.get(1));
                //Мутация
                newPopulation.add(mutation(ind1));
                newPopulation.add(mutation(ind2));
            }
            //Добавляем получившихся особей в новую популяцию
            this.newPopulation.add(ind1);
            this.newPopulation.add(ind2);
        }
    }

    /**
     * Однородное скрещивание двух особей
     * @param ind1 - Особь 1
     * @param ind2 - Особь 2
     * @return - список с новыми особями
     */
    private List<Individul> cross(Individul ind1, Individul ind2){
        Random rnd = new Random();
        Individul newInd1 = new Individul(m);
        List<int[]> codVal1 = new ArrayList<int[]>();
        Individul newInd2 = new Individul(m);
        List<int[]> codVal2 = new ArrayList<int[]>();

        int countPar = ind1.getGen().size();
        //Перебор по параметрам(переменным функции)
        for (int i = 0; i < countPar; i++) {
            int[] hromosoma1 = new int[m];
            int[] hromosoma2 = new int[m];
            //Перебор по генам каждой хромосомы
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
     * Мутация особи
     * @param ind - Мутирующая особь
     * @return - новая, смутирововшая особь
     */
    private Individul mutation(Individul ind){
        Random rnd = new Random();
        int countPar = ind.getGen().size();

        Individul newInd = new Individul(m);
        List<int[]> codVal = new ArrayList<int[]>();
        //Перебор по параметрам(переменным функции)
        for (int i = 0; i < countPar; i++) {
            int[] hromosoma = new int[m];
            //Перебор по генам каждой хромосомы
            for (int j = 0; j < m; j++) {
                if(rnd.nextDouble() >= pMut) {
                    //Инвертируем
                    if(ind.getGen().get(i)[j] == 0) {
                        hromosoma[j] = 1;
                    }else {
                        hromosoma[j] = 0;
                    }
                }else {
                    //Не инвертируем
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
        //Заполняем строки
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
