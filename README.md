## Metodologia

A ferramenta de recuperação de documentos utilizada para a execução deste trabalho foi
o Lucene, que consiste em uma biblioteca de mecanismo de busca e, de software livre a
partir do Apache Software Foundation. As APIs do Lucene focam principalmente na indexação e na procura de texto e, dentre os diferentes recursos que disponibiliza temos: algoritmos de procura em documentos de texto; retorno de documentos relevantes a consulta realizada pelo usuário
através da aplicação de um cálculo de pontuação para cada documento da coleção; procura e a indexação simultaneamente, entre outros. A Figura 1 mostra o funcionamento da máquina de busca configurada no presente
trabalho, utilizando o Lucene para indexação e pesquisa dos documentos e das consultas.

1. A ferramenta inicia lendo um arquivo chamado ”cfquery”, onde estão todas as
informações sobre as consultas e documentos relevantes de cada uma, e armazenando os dados em uma estrutura manipul ́avel. As queries são utilizadas posteriormente para fazer as consultas na ferramenta e, os documentos para avaliar os a
precisãoo e revocaçãoo dos resultados.

2. Posteriormente, o programa verifica se os documentos já foram devidamente lidos e indexados, caso a resposta seja negativa, todo o processo de Indexação, pré-processamento dos documentos e conversão para arquivo .txt  é feita.

3. A próxima etapa é realizar as consultas. O programa utiliza as consultas do arquivo ”cfquery”que contendo 100 pesquisas distintas, para recuperar os resultados de cada uma.

4. Após a pesquisa, os resultados obtidos s ̃ao comparados com os documentos relevantes.

5. Por fim, são calculadas as métricas de precisão e revocação para cada uma das pesquisas, gerando um único array com a média aritmética das pesquisas.

6. O programa finaliza sua execução mostrando um gráfico com a curva de precisão versus revocação.


![fluxo](https://github.com/programmer2710/lucene_searcher/assets/15629444/e32c0709-9ec6-4c7e-90ec-2157d9429958)

## Descrição dos dados utilizados

Para realização	implementação e	teste	dos classificadores	utilizouse a coleção	de	documentos ”CReuters-21578”, disponível em http://disi.unitn.it/moschitti/corpora.html

## Indexação e Pesquisa
A biblioteca Lucene é composta por duas etapas principais: indexaçãoo e pesquisa e, os documentos precisam ser previamente indexados antes de se realizar a consulta.
Neste trabalho antes do Lucene indexar os termos dos documentos, foi necessário primeiramente programar um código em java para ler e guardar automaticamente os campos que continham o texto 
e o número de identificaçãoo de cada documento. Para isso utilizou-se uma estrutura de repetiçãoo que acessa cada arquivo, verifica e guarda os campos que contém apenas as informaçõees relevantes 
para as especificaçõees propostas ao desenvolvimento do trabalho.
Os códigos a seguir trazem o procedimento principal para a criação da indexação dos documentos e a estrutura de pesquisa (método chamado Indexer), que permite a realizaçãoo da consulta na coleção para recuperação dos documentos relevantes, 
de acordo com a entrada (consulta) informada pelo usuário.

```
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
```
```
public void indexFile(String text,String fileNumber){
  try {
    Document document = getDocument(text,fileNumber);
    writer.addDocument(document);
    writer.commit();
  }catch (IOException e){
    System.out.println("Problema : " + e.getMessage());
  }
}
```
```
//Estrutra de pequisa
Query query = new QueryBuilder(analyzer).
createMinShouldMatchQuery("termos", queryObject.
getQuery(), 0.1f);
// Abre o diret rio com o index do Lucene
IndexReader reader = DirectoryReader.open(index);
IndexSearcher searcher = new IndexSearcher(reader);
TopScoreDocCollector collector = TopScoreDocCollector.
create(10000,10000);
searcher.setSimilarity(similarityType);
searcher.search(query,collector);
ScoreDoc[] hits = collector.topDocs().scoreDocs;
```

## Resultados

Os grafos apresentados nas Figuras 3 e 4, correspondem aos resultados da precisao interpolada em 11 níveis de revocação obtidos. A interpolação mostra em intervalos de dez e dez (eixo Revocação) 
quantos documentos recuperados sãoo realmente relevantes (eixo Precisao).

A Figura 2 traz os resultados obtidos pelos dois modelos para a seguinte consulta ”what are the effects of calcium on the physical properties of mucus from cf patients?”.

![testee](https://github.com/programmer2710/lucene_searcher/assets/15629444/fb8a1568-099d-478e-9603-1a00dc024352)

![vetorial](https://github.com/programmer2710/lucene_searcher/assets/15629444/944216c4-5443-42ac-9d7f-53e56464cb03)

## Conclusão
O objetivo do presente trabalho foi utilizar um mecanismo de recuperac¸ao de informação como o Lucene, bem como realizar consultas atraves deste mecanismo utilizando diferentes modelos de RI a fim de, 
analisar a qualidade de recuperação destes modelos. Através dos resultados obtidos foi possível constatar que ambos os modelos apresentaram desempenho semelhante, porem o modelo BM25 se saiu ligeiramente melhor.
Além disso, a pesquisa entrega documentos relevantes logo nas primeiras amostras, o que pode ser relevante para alguns casos.


