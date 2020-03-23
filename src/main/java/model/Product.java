package model;

import data.Data;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;

public class Product {

    private Integer position;
    private String code;
    private String title;
    private String measures;
    private String OKEI;
    private Double cost;

    public static int counter = 1;

    private TreeMap <String, Pair <Integer, Double>> remains;
    private static HashMap <String, String> productCodes;
    private static HashMap <String, String> measuresCodes;

    static {
        Pair <HashMap <String, String>, HashMap <String, String>> pair = new Data().getCodes();
        productCodes = pair.getKey();
        measuresCodes = pair.getValue();
    }

    public Product(int position) {
        this.position = position;
        this.cost = 0.0;

        remains = new TreeMap <>();
    }

    public static HashMap <String, String> getProductCodes() {
        return productCodes;
    }

    public static HashMap <String, String> getMeasuresCodes() {
        return measuresCodes;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMeasures() {
        return measures;
    }

    public void setMeasures(String measures) {
        this.measures = measures;
    }

    public String getOKEI() {
        return OKEI;
    }

    public void setOKEI(String OKEI) {
        this.OKEI = OKEI;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public TreeMap <String, Pair <Integer, Double>> getRemains() {
        return remains;
    }

    public void setRemains(TreeMap <String, Pair <Integer, Double>> remains) {
        this.remains = remains;
    }

    public int getRemainsCount(String key) {
        return remains.get(key).getKey();
    }

    public void setRemainsCount(String key, int count) {
        remains.put(key, new Pair <> (count, getRemainsSum(key)));
    }

    public double getRemainsSum(String key) {
        return remains.get(key).getValue();
    }

    public void setRemainsOneSum(String key, double sum) {
        remains.put(key, new Pair<> (getRemainsCount(key), sum));
    }

    public void setRemainsSum(double sum) {

        for (Map.Entry <String, Pair <Integer, Double>> remain : remains.entrySet()) {
            String key = remain.getKey();
            int count = remain.getValue().getKey();
            remains.put(key, new Pair <>(count, sum * count));
        }
    }

}
