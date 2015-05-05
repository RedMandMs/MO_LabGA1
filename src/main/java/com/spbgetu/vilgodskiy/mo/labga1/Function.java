package com.spbgetu.vilgodskiy.mo.labga1;

/**
 * Created by Vred.L.Hom on 04.05.2015.
 */
public class Function {

    public static double valFunc(Individul ind){
        double result;
        double x1 = ind.getVals()[0];
        double x2 = ind.getVals()[1];
        result = 8 * Math.pow(x1,2) + 4*x1*x2 + 5 * Math.pow(x2,2);
        return result;
    }
}
