package com.br.file;

// Classe criada para encapsular os dados que serão indexados pelo Lucene
public class FileIndexFields {
    private String terms;
    private String fileNumber;

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }
}
