package com.spbgetu.vilgodskiy.mo.labga1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created by Vred.L.Hom on 03.05.2015.
 */
public class GUI {

    private JRadioButton f1RadioButton, f2RadioButton;
    private ButtonGroup chooseFunctionGroup = new ButtonGroup();
    private JTable table;
    JTextField sizePopTF;
    JTextField epsTF;
    JTextField capacityTF;
    JTextField x1MinTF;
    JTextField x1MaxTF;
    JTextField x2MinTF;
    JTextField x2MaxTF;
    JTextField pCTF;
    JTextField pMTF;
    JTextField kTourTF;
    JPanel resultPanel;
    JPanel mainPanel;

    GenAlg genAlg;

    String[] namesColumnTable = new String[]{"Закодированное x1", "x1", "Закодированное x2", "x2", "F(x1,x2)"};

    public void toDesign(){

        /** Главное окно */
        JFrame mainFrame = new JFrame("Лабораторная работа № 1 - Простой генетический алгоритм");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /** Главная панель */
        mainPanel = new JPanel(new BorderLayout());
        mainFrame.add(mainPanel);


        /**
         *  Панель с функцией для оптимизации и параметрами
         */
        JPanel functionChoosePanel = new JPanel(new FlowLayout());
        mainPanel.add(functionChoosePanel, BorderLayout.NORTH);
        ImageIcon imageIconFun = new ImageIcon("Function.jpg");
        JLabel jLabelFun = new JLabel(imageIconFun);
        functionChoosePanel.add(jLabelFun);

        //Популяция
        JLabel popLable = new JLabel("  Размер популяции =");
        functionChoosePanel.add(popLable);
        sizePopTF = new JTextField("30");
        functionChoosePanel.add(sizePopTF);
        //eps
        JLabel epsLable = new JLabel("  eps = ");
        functionChoosePanel.add(epsLable);
        epsTF = new JTextField("0.01");
        functionChoosePanel.add(epsTF);
        //разрядность
        JLabel capacityLable = new JLabel(";   разрядность =");
        functionChoosePanel.add(capacityLable);
        capacityTF = new JTextField("6");
        functionChoosePanel.add(capacityTF);

        //Ограничение по значениям переменных
        //x1
        JLabel x1Label = new JLabel(";     X1 от:");
        functionChoosePanel.add(x1Label);
        x1MinTF = new JTextField("-5.12");
        JLabel untilLabl1 = new JLabel("до:");
        x1MaxTF = new JTextField("5.12");
        functionChoosePanel.add(x1MinTF);
        functionChoosePanel.add(untilLabl1);
        functionChoosePanel.add(x1MaxTF);
        //x2
        JLabel x2Label = new JLabel(";     X2 от:");
        functionChoosePanel.add(x2Label);
        x2MinTF = new JTextField("-5.12");
        JLabel untilLabl2 = new JLabel("до:");
        x2MaxTF = new JTextField("5.12");
        functionChoosePanel.add(x2MinTF);
        functionChoosePanel.add(untilLabl2);
        functionChoosePanel.add(x2MaxTF);

        //Установка значений вероятности
        //Скрещивание
        JLabel pCrosLabel = new JLabel(";      pC=");
        functionChoosePanel.add(pCrosLabel);
        pCTF = new JTextField("0.9");
        functionChoosePanel.add(pCTF);
        //Мутация
        JLabel pMutLabel = new JLabel(";  pM=");
        pMTF = new JTextField("0.05");
        functionChoosePanel.add(pMutLabel);
        functionChoosePanel.add(pMTF);

        //Турнирная система
        JLabel kTourLabel = new JLabel(";    kTour=");
        functionChoosePanel.add(kTourLabel);
        kTourTF = new JTextField("4");
        functionChoosePanel.add(kTourTF);


        /**
         *  Панель для вывода информации о результах оптимизации
         */
        resultPanel = new JPanel(new BorderLayout());
        mainPanel.remove(resultPanel);

        /**
         *  Панель для установления параметров оптимизации
         */
        JPanel parametersChoosePanel = new JPanel();
        parametersChoosePanel.setLayout(new BoxLayout(parametersChoosePanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(parametersChoosePanel, BorderLayout.WEST);



        /**
         *  Панель для кнопок управления процессом
         */
        JPanel buttonPanel = new JPanel(new FlowLayout());
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        //Создать популяцию
        JButton createButton = new JButton("Создать популяцию");
        createButton.addActionListener(new CreatePopBtnListener());
        buttonPanel.add(createButton);

        //Сделать 1 шаг
        JButton oneStepButton = new JButton("Сделать один шаг алгоритма");
        oneStepButton.addActionListener(new OneStepBtnListener());
        buttonPanel.add(oneStepButton);

        //Проиграть весь алгоритм до конца
        JButton allAlgButton = new JButton("Выполнить весь алгоритм");
        allAlgButton.addActionListener(new AllAlgBtnListener());
        buttonPanel.add(allAlgButton);

        /**
         * Чек-боксы для настройки вывода
         */
        /*withGS = new JCheckBox("С выводом результатов метода золотого сечения");
        withSven = new JCheckBox("С выводом результатов метода Свенна");
        buttonPanel.add(withGS);
        buttonPanel.add(withSven);*/

        /**
         * Размер и видимость окна
         */
        mainFrame.setSize(1000, 700);
        mainFrame.setVisible(true);
    }

    private class CreatePopBtnListener implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            Map<String,Object> info = new HashMap<String, Object>();
            info.put("m", Integer.valueOf(capacityTF.getText()));
            info.put("sizePop", Integer.valueOf(sizePopTF.getText()));
            ArrayList<double[]> listMinMax = new ArrayList<double[]>();
            double[] x1MinMax = new double[]{Double.valueOf(x1MinTF.getText()), Double.valueOf(x1MaxTF.getText())};
            listMinMax.add(x1MinMax);
            double[] x2MinMax = new double[]{Double.valueOf(x2MinTF.getText()), Double.valueOf(x2MaxTF.getText())};
            listMinMax.add(x2MinMax);
            info.put("listMinMax", listMinMax);
            info.put("eps", Double.valueOf(epsTF.getText()));
            info.put("kTour", Integer.valueOf(kTourTF.getText()));
            info.put("pCross", Double.valueOf(pCTF.getText()));
            info.put("pMut", Double.valueOf(pMTF.getText()));
            info.put("amountPar", 2);
            genAlg = new GenAlg(info);

            showResult();
        }
    }

    private class OneStepBtnListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            genAlg.activeAlg(false);
            showResult();
        }
    }

    private class AllAlgBtnListener implements ActionListener{

        public void actionPerformed(ActionEvent e) {
            genAlg.activeAlg(true);
            showResult();
        }

    }

    /**
     * Показать промежуточный результат вычислений
     */
    public void showResult(){
        /**
         *  Панель для вывода информации о результах оптимизации
         */
        mainPanel.remove(resultPanel);
        resultPanel = new JPanel(new BorderLayout());
        table = new JTable(genAlg.getData(), namesColumnTable);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainPanel.remove(resultPanel);
        resultPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(resultPanel);
        mainPanel.updateUI();
    }

    /**
     * Показать лучшую особь
     */
    public void showBest(){

    }


}
