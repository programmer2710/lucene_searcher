package com.br.lucene;

import com.br.evaluation.EvaluationManager;
import com.br.evaluation.EvaluationModel;
import com.br.evaluation.PrecisionRecallModel;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.QueryBuilder;

import java.io.IOException;
import java.nio.file.Paths;

// Classe criada para manipular ações do Lucene
public class LuceneManager {

    private static String rootProjectFolder = System.getProperty("user.dir");
    public static String indexDir = rootProjectFolder + "/indexDir/";
    private IndexWriter writer;
    Analyzer analyzer;
    private IndexWriterConfig config;

    public LuceneManager(){
        try {
            analyzer = CustomAnalyzer.builder()
                    .withTokenizer("standard")
                    .addTokenFilter("lowercase")
                    .addTokenFilter("stop")
                    .addTokenFilter("porterstem")
                    .addTokenFilter("capitalization")
                    .build();
            Indexer();
        }catch (Exception e){}
    }

    private void Indexer(){
        try {
            Directory indexDirectory = FSDirectory.open(Paths.get(indexDir));
            //create the indexer
            config = new IndexWriterConfig(analyzer);
            writer = new IndexWriter(indexDirectory, config);
        }catch (IOException e){
            System.out.println("Problema : " + e.getMessage());
        }
    }

    private Document getDocument(String text,String fileNumber) throws IOException {
        try {
            Document document = new Document();
            // List<String> termos = analyze(text);
            document.add(new TextField("termos", text, Field.Store.YES));
            document.add(new TextField("docNumber",fileNumber,Field.Store.YES));
            return document;
        }catch (Exception e){
            System.out.println("Problema : " + e.getMessage());
        }
        return new Document();
    }

    public void indexFile(String text,String fileNumber){
        try {
            Document document = getDocument(text,fileNumber);
            writer.addDocument(document);
            writer.commit();
        }catch (IOException e){
            System.out.println("Problema : " + e.getMessage());
        }
    }

    public static PrecisionRecallModel searchAndReturnPrecisionRecall(Directory index, Analyzer analyzer, EvaluationModel eM) throws Exception{
        EvaluationModel queryObject = eM;
        Query query = new QueryBuilder(analyzer).createMinShouldMatchQuery("termos", queryObject.getQuery(), 0.1f);
        // Abre o diretório com o index do Lucene
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopScoreDocCollector collector = TopScoreDocCollector.create(10000,10000);
        searcher.search(query,collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        EvaluationManager evaluationManager = new EvaluationManager();
        return evaluationManager.createPrecisionRecallArray(hits,searcher,eM);
    }

}

