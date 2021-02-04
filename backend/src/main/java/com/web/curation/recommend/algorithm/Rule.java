package com.web.curation.recommend.algorithm;

public class Rule<T> {
    public T A;
    public T B;
    public double support;
    public double confidence;
    public double lift;

    public Rule(T a, T b, double support, double confidence, double lift) {
        A = a;
        B = b;
        this.support = support;
        this.confidence = confidence;
        this.lift = lift;
    }

    @Override
    public String toString() {
        return "Rule{" +
                "A=" + A +
                ", B=" + B +
                ", support=" + support +
                ", confidence=" + confidence +
                ", lift=" + lift +
                '}';
    }
}
