package com.br.file;

import com.br.lucene.LuceneManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Classe criada para ações dos documentos, como salvar, indexar, etc...
public class FileManager {


    List stringsToIndex = new ArrayList();
    // Objeto que controla as ações do Lucene
    LuceneManager luceneManager = new LuceneManager();
    // Pasta raiz do projeto
    String rootProjectFolder = System.getProperty("user.dir");
    // Tag que guarda a informação do número do arquivo
    String registerNumber = "RN";

    // Tags que deverão ser indexadas
    List acceptedTerms = new ArrayList<String>() {{
            add("AU");
            add("TI");
            add("SO");
            add("MJ");
            add("MN");
            add("AB");
            add("EX");
        }
    };
    // Tags que deverão ser ignoradas
    List rejectedTerms = new ArrayList<String>() {{
        add("PN");
        add("RN");
        add("AN");
        add("RF");
        add("CT");
    }
    };

    // Converte todos os arquivos em determinada pasta (sourceFolder) para arquivos txt, e os salva em outra pasta (destinationFolder)
    public void convertAllFilesToTxt(String sourceFolder,String destinationFolder) throws IOException {
        File sourcePath = new File(sourceFolder);
        File destinationPath = new File(destinationFolder);
        if (!destinationPath.exists()){
            destinationPath.mkdir();
        }
        File [] files = sourcePath.listFiles();
        for (int i = 0; i < files.length; i++){
            if (files[i].isFile()){
                String outputFileName = destinationFolder + "/" + String.format("%s.txt", files[i].getName().replace('.', '_'));
                File newFile = new File(outputFileName);
                if(!newFile.exists()){
                    newFile.createNewFile();
                    addFileToIndex(files[i].getAbsolutePath());
                    saveFile(files[i],outputFileName);
                }
            }
        }
    }

    // Função que salva o arquivo
    private void saveFile(File file,String destination){
        String fileName = file.getAbsolutePath();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName));
             PrintWriter pw = new PrintWriter(new FileWriter(destination))) {
            int count = 1;
            String line;
            while ((line = br.readLine()) != null) {
                pw.printf("%s%n", preProcessData(line));
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Função que salva o arquivo no index do Lucene
    private  void addFileToIndex(String origin) throws IOException {
        Path path = Paths.get(origin);
        File file = path.toFile();
        getTermAndFileNumberToIndex(file);
    }

    // Função parser que tem lê o arquivo e prepara uma estrutura para ser indexado
    private void getTermAndFileNumberToIndex(File file){
        StringBuffer terms = new StringBuffer("");
        String fileNumber = "";
        Boolean isAcceptedTerm = false;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String term = line.substring(0, Math.min(2,line.length()));
                if(term.equals(registerNumber)){
                    String[] splittedTag = line.split("\\s+");
                    if((splittedTag[1] != fileNumber) && !fileNumber.isEmpty()){
                        System.out.println("Indexing file :" + splittedTag[1]);
                        luceneManager.indexFile(terms.toString(),splittedTag[1]);
                        terms = new StringBuffer();
                    }
                    fileNumber = splittedTag[1];
                }
                if(acceptedTerms.contains(term)){
                    isAcceptedTerm = true;
                }else if (rejectedTerms.contains(term)){
                    isAcceptedTerm = false;
                }
                if(isAcceptedTerm){
                    terms.append(line);
                }
            }
        } catch (Exception e) {
            System.out.println("Termos " + terms);
            System.out.println("Erro: "+ e.getCause());
        }
    }

     public String preProcessData(String text){
        String processedText = text.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
        return processedText;
    }
}
