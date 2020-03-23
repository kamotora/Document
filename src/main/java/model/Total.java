package model;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Map;

public class Total {

    private static ArrayList <Double> sum;
    private static ArrayList <Double> sideSum;

    public Total () {
        sum = new ArrayList <>();
        sideSum = new ArrayList <>();
    }

    public ArrayList <Double> getSum() {
        return sum;
    }

    public double getOneSum(int index) {
        return sum.get(index);
    }

    public static void setSum(ArrayList <Double> sum) {
        Total.sum = sum;
    }

    public void setOneSum(double value, int index) {
        sum.set(index, value);
    }

    public void setSum(double value, ArrayList <Product> products, Product product) {
        Total.setSum(getSumArray(value, products, product));
    }

    public ArrayList <Double> getSideSum() {
        return sideSum;
    }

    public double getOneSideSum(int index) {
        return sideSum.get(index);
    }

    public static void setSideSum(ArrayList <Double> sideSum) {
        Total.sideSum = sideSum;
    }

    public void setOneSideSum(double value, int index) {
        sideSum.set(index, value);
    }

    public void setSideSum(double value, ArrayList <Product> products, Product product) {
        Total.setSideSum(getSumArray(value, products, product));
    }

    private ArrayList <Double> getSumArray(double value, ArrayList <Product> products, Product product) {
        ArrayList <Double> values = new ArrayList <>() {
            {
                while (size() != product.getRemains().size()) {
                    add(0.0);
                }
            }
        };

        for (Product item : products) {

            int index = 0;

            for (Map.Entry <String, Pair <Integer, Double>> remain : item.getRemains().entrySet()) {
                int count = remain.getValue().getKey();
                double cost = 0;

                if (count != 0) {
                    cost = remain.getValue().getValue() / count;
                }

                if (product == item) {
                    values.set(index, values.get(index) + (count * value));
                }

                else {
                    values.set(index, values.get(index) + (count * cost));
                }

                index++;
            }
        }

        return values;
    }
}
