package com.br.evaluation;

import java.util.List;

public class PrecisionRecallModel {
    private List<Double> precisionArray;
    private List<Double> recallArray;

    public List<Double> getPrecisionArray() {
        return precisionArray;
    }

    public void setPrecisionArray(List<Double> precisionArray) {
        this.precisionArray = precisionArray;
    }

    public List<Double> getRecallArray() {
        return recallArray;
    }

    public void setRecallArray(List<Double> recallArray) {
        this.recallArray = recallArray;
    }
}
