import com.spbgetu.vilgodskiy.mo.labga1.GenAlg;
import com.spbgetu.vilgodskiy.mo.labga1.Individul;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

/**
 * Created by ������ on 02.05.2015.
 */
public class TestingIndividual {

    //����������� �����������
    private int m;
    //�������� ���������� ���������������
    private double[] vals;
    //�������������� �������� - ������
    private List<int[]> codVals;

    @Before
    public void testStartMethod(){
        this.m = 4;
        this.vals = new double[2];
        this.vals[0] = 1.3275;
        this.vals[1] = 1.8667;
        this.codVals = new ArrayList<int[]>(2);
        int[] codVal0 = new int[]{0,1,0,0};
        int[] codVal1 = new int[]{1,1,0,1};
        this.codVals.add(0, codVal0);
        this.codVals.add(1, codVal1);
    }

    @Test
    public void testCoding(){
        Individul individul = new Individul(m);
        double[] minMax = new double[]{1,2};
        List<double[]> listMinMax = new ArrayList<double[]>();
        listMinMax.add(minMax);
        listMinMax.add(minMax.clone());
        individul.setValues(this.vals, listMinMax);
        Assert.assertArrayEquals("������������ �� �������� ��� 1", codVals.get(0), individul.getGen().get(0));
        Assert.assertArrayEquals("������������ �� �������� ��� 2", codVals.get(1), individul.getGen().get(1));
    }

    @Test
    public void testDecoding(){
        Individul individul = new Individul(m);
        double[] minMax = new double[]{1,2};
        List<double[]> listMinMax = new ArrayList<double[]>();
        listMinMax.add(minMax);
        listMinMax.add(minMax.clone());
        //�����������
        double p = (minMax[1] - minMax[0])/(Math.pow(2,m) - 1);
        individul.setValues(this.codVals, listMinMax);
        Assert.assertEquals("�������������� �� �������� ��� 1", vals[1], individul.getVals()[1], p);
        Assert.assertEquals("�������������� �� �������� ��� 2", vals[0], individul.getVals()[0], p);
    }

    @Test
    public void  testGenereteG(){
        List<double[]> minMax = new ArrayList<double[]>();
        double[] minMax1 = new double[]{1, 9};
        double[] minMax2 = new double[]{2, 13};
        minMax.add(minMax1);
        minMax.add(minMax2);
        //GenAlg genAlg = new GenAlg(7, 2, 10, minMax);
        System.out.println();
    }

    @Test
    public void testComparable(){
        List<double[]> minMax = new ArrayList<double[]>();
        double[] minMax1 = new double[]{1, 9};
        double[] minMax2 = new double[]{2, 13};
        minMax.add(minMax1);
        minMax.add(minMax2);
        //GenAlg genAlg = new GenAlg(7, 2, 10, minMax);
        //genAlg.sortPopulation();
        System.out.println();
    }

    /**
     * Проверяем не генерируюстся ли случайные числа одинаковыми стопками
     */
    @Test
    public void testRandom(){
        List<Integer> listInteger = new ArrayList<Integer>();
        Random rnd = new Random();
        for (int j = 0; j < 100; j++) {
            int num1 = rnd.nextInt(30);
            int num2 = rnd.nextInt(30);
            listInteger.add(num1);
            listInteger.add(num2);
        }
    }

    @Test
    public void testSelection(){
        List<double[]> minMax = new ArrayList<double[]>();
        double[] minMax1 = new double[]{1, 9};
        double[] minMax2 = new double[]{2, 13};
        minMax.add(minMax1);
        minMax.add(minMax2);
        Map<String,Object> info = new HashMap<String, Object>();
        info.put("listMinMax", minMax);
        info.put("m", 4);
        info.put("amountPar", 2);
        info.put("sizePop", 10);
        info.put("eps", 0.01);
        info.put("kTour", 4);
        info.put("pCross", 0.9);
        info.put("pMut", 0.05);
        GenAlg genAlg = new GenAlg(info);
        //genAlg.sortPopulation();
        //genAlg.selection();
        System.out.println();
    }

}
