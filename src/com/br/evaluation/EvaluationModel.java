package com.br.evaluation;

import java.util.List;

// Classe feita para encapsular os arquivos do documento cfQuery em uma estrutura manipulável
public class EvaluationModel {
    private String query;
    List<Integer> docNumbers;

    public EvaluationModel(String query,List<Integer> docNumbers){
        this.setDocNumbers(docNumbers);
        this.setQuery(query);
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Integer> getDocNumbers() {
        return docNumbers;
    }

    public void setDocNumbers(List<Integer> docNumbers) {
        this.docNumbers = docNumbers;
    }
}
