import com.br.evaluation.EvaluationManager;
import com.br.evaluation.EvaluationModel;
import com.br.evaluation.PrecisionRecallModel;
import com.br.file.FileManager;
import com.br.lucene.LuceneManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    /**
     * EvaluationManager : Classe que cuida das operações de avaliação
     */
    static EvaluationManager evaluationManager = new EvaluationManager();
    static String rootProjectFolder = System.getProperty("user.dir");
    static Analyzer analyzer;

    /**
     * Inicialização do objeto Analyzer, com as mesmas especificações
     * de indexação
     */
    static {
        try {
            analyzer = CustomAnalyzer.builder().withTokenizer("standard")
                        .addTokenFilter("lowercase")
                        .addTokenFilter("stop")
                        .addTokenFilter("capitalization")
                        .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception
    {
        evaluationManager.readReferenceCollectionFile();

        String filesProjectFolder = rootProjectFolder + "/cfcConverted/";
        FileManager fM = new FileManager();
        fM.convertAllFilesToTxt(rootProjectFolder +"/cfc",filesProjectFolder);

        PrecisionRecallModel meanModel = getMeanArrayForAllQueries(evaluationManager.getQueries());
        evaluationManager.createGraph(meanModel);
    }

    /**
     * Método responsável por fazer as consultas e construir o array com as médias de precisão
     * e revocação para todas as consultas
     * @param queryList : O objeto com a lista de consultas que serão usadas para avaliação e pesquisa
     * @return Retorna um objeto com um array de precisão e revocação
     * @throws Exception
     */
    private static PrecisionRecallModel getMeanArrayForAllQueries(List<EvaluationModel> queryList) throws Exception {
        List<PrecisionRecallModel> models = new ArrayList<>();
        String indexDir = rootProjectFolder + "/indexDir/";
        Directory indexDirectory = FSDirectory.open(Paths.get(indexDir));

        /**
         * Faz a pesquisa para cada consulta no objeto de queries, e salva numa lista
         */
        for (EvaluationModel eM: queryList) {
            PrecisionRecallModel pM = LuceneManager.searchAndReturnPrecisionRecall(indexDirectory,analyzer,eM);
            models.add(pM);
        }

        /**
         * Após a pesquisa, calcula a média de todas as pesquisas, e salva numa lista
         */
        PrecisionRecallModel modelWithMean = new PrecisionRecallModel();
        List<Double> meanPrecisionVector = new ArrayList<>(Collections.nCopies(models.get(0).getPrecisionArray().size(), 0.0));
        for (int i = 0; i < models.size(); i++) {
            for (int j = 0; j < models.get(0).getPrecisionArray().size(); j++) {
                meanPrecisionVector.set(j,meanPrecisionVector.get(j) + models.get(i).getPrecisionArray().get(j));
            }
        }
        for (int i = 0; i < meanPrecisionVector.size(); i++) {
            meanPrecisionVector.set(i, meanPrecisionVector.get(i) / models.size());
        }
        modelWithMean.setPrecisionArray(meanPrecisionVector);
        modelWithMean.setRecallArray(models.get(0).getRecallArray());

        /**
         * Retorna a lista de médias
         */
        return modelWithMean;
    }
}