package com.br.evaluation;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import com.br.chart.ChartManager;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


/**
 * Classe que contém os métodos referentes a avaliação da consulta
 */
public class EvaluationManager {

    /**
     * queries: Objeto que armazenará as consultas pré definidas, bem como os
     * documentos considerados relevantes para cada uma delas
     * queryTagInFile : Tag no arquivo texto que indica uma query
     * relevantFilesTag : Tag no arquivo texto que indica os arquivos relevantes de cada query
     */
    private List<EvaluationModel> queries = new ArrayList<>();
    private String queryTagInFile = "QU";
    private String relevantFilesTag = "RD";
    private String firstFileTag = "QN";
    private String rootProjectFolder = System.getProperty("user.dir");

    /**
     * Lê a coleção de referência, e armazena numa estrutura de dados manipulável
     */
    public void readReferenceCollectionFile(){

        Path path = Paths.get(rootProjectFolder + "/cfc/cfquery");
        File file = path.toFile();

        Boolean isReadingFileNumbers = false;
        Boolean isReadingQuery = false;
        Boolean isFirstDocument = true;
        String currentTag;
        StringBuffer query = new StringBuffer();
        List<Integer> fileNumbers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                currentTag = line.substring(0, Math.min(2,line.length()));

                if(!isFirstDocument && currentTag.equals(firstFileTag)){
                    queries.add(new EvaluationModel(query.toString(),fileNumbers));
                    query = new StringBuffer();
                    fileNumbers = new ArrayList<>();
                }
                isFirstDocument = false;

                if(currentTag.equals(relevantFilesTag)){
                    isReadingFileNumbers = true;
                }else if(!currentTag.trim().isEmpty()){
                    isReadingFileNumbers = false;
                }

                if(isReadingFileNumbers){
                    String[] splited = line.split("\\s+");
                    for (int i = 0;i < splited.length; i++){
                        if(i % 2 != 0){
                            fileNumbers.add(Integer.parseInt(splited[i]));
                        }
                    }
                }

                if(currentTag.equals(queryTagInFile)){
                    isReadingQuery = true;
                }else if(!currentTag.trim().isEmpty()){
                    isReadingQuery = false;
                }

                if (isReadingQuery) {
                    String splited = line.substring(2,line.length());
                    query.append(splited.toLowerCase());
                }
            }

            if(!isFirstDocument){
                queries.add(new EvaluationModel(query.toString(),fileNumbers));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Cria um Objeto com 11 valores de Precisão, e 11 valores de revocação
     * @param hits Objetos retornados
     * @param searcher Objeto do Lucene específico para busca
     * @param eM
     * @return
     * @throws IOException
     */
    public PrecisionRecallModel createPrecisionRecallArray(ScoreDoc[] hits, IndexSearcher searcher, EvaluationModel eM) throws IOException {
        List<String> docsRetrievedByQuery = new ArrayList<>();
        List<Integer> documentsThatMatchQuery = eM.docNumbers;
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            String docNumber = d.get("docNumber");
            docsRetrievedByQuery.add(docNumber);
        }
        Double currentPrecision = 100.0;
        int numberOfDocs = 0;
        int numberOfRelevantDocs = 0;
        Double currentRecall = 0.0;
        List<Double> precisionVector = new ArrayList<>(Collections.nCopies(docsRetrievedByQuery.size(), 0.0));
        List<Double> recallVector = new ArrayList<>();


        for (String docNumber: docsRetrievedByQuery) {
            numberOfDocs += 1;
            if(documentsThatMatchQuery.contains(Integer.parseInt(docNumber))){
                numberOfRelevantDocs += 1;
                currentRecall = currentRecall + 10;
                precisionVector.set(numberOfDocs - 1,currentPrecision);
            }else{
                currentPrecision = (((double)numberOfRelevantDocs/(double)numberOfDocs));
            }
            recallVector.add(currentRecall);
        }

        List<Double> convertedPrecision = new ArrayList<>(Collections.nCopies(11, 0.0));
        currentPrecision = 100.0;
        numberOfDocs = 0;
        for (int i = 0; i < precisionVector.size(); i++) {
            numberOfDocs += 1;
            if(precisionVector.get(i) > 0.0){
                if(numberOfDocs > 1.0) {
                    currentPrecision = (((double) numberOfRelevantDocs / numberOfDocs));
                }
                float value = ((float) i) / precisionVector.size() * 10;
                convertedPrecision.set((int)Math.round(value),currentPrecision * 100);
            }
        }

        double currentValue = 0.0;
        for (int i = convertedPrecision.size() -1; i >= 0; i--) {
            if(convertedPrecision.get(i) != 0.0){
                currentValue = convertedPrecision.get(i);
            }
            if(currentValue != 0.0){
                convertedPrecision.set(i,currentValue);
            }
        }
        convertedPrecision.set(0,100.0);
        List<Double> recall = new ArrayList<>();
        recall.add(0.0);
        recall.add(1.0);
        recall.add(2.0);
        recall.add(3.0);
        recall.add(4.0);
        recall.add(5.0);
        recall.add(6.0);
        recall.add(7.0);
        recall.add(8.0);
        recall.add(9.0);
        recall.add(10.0);
        PrecisionRecallModel model = new PrecisionRecallModel();
        model.setPrecisionArray(convertedPrecision);
        model.setRecallArray(recall);
        return model;
    }

    /**
     * Cria um gráfico a partir de um objeto de precisão e revocação
     * @param model
     */
    public void createGraph(PrecisionRecallModel model){
        SwingUtilities.invokeLater(() -> {
            ChartManager chart = new ChartManager("Tabela Precisão X Revocação",model.getPrecisionArray(),model.getRecallArray());
            chart.setSize(800, 400);
            chart.setLocationRelativeTo(null);
            chart.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            chart.setVisible(true);
        });
    }

    /**
     * Retorna o objeto com as queries e documentos relevantes
     * @return
     */
    public List<EvaluationModel> getQueries(){
        return this.queries;
    }
}
