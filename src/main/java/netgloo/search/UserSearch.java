package netgloo.search;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import netgloo.models.Document;

import org.apache.commons.lang.ObjectUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;
import org.apdplat.word.WordSegmenter;
import org.apdplat.word.lucene.ChineseWordAnalyzer;
import org.apdplat.word.segmentation.SegmentationAlgorithm;
import org.apdplat.word.segmentation.Word;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import static java.awt.Color.red;
import static org.apdplat.word.segmentation.SegmentationAlgorithm.MaxNgramScore;

/**
 * Search methods for the entity User using Hibernate search.
 * The Transactional annotation ensure that transactions will be opened and
 * closed at the beginning and at the end of each method.
 */
@Repository
@Transactional
public class UserSearch {

  // Spring will inject here the entity manager object
  @PersistenceContext
  private EntityManager entityManager;


  /**
   * A basic search for the entity User. The search is done by exact match per
   * keywords on fields name, city and email.
   *
   * @param text The query text.
   */
  public List<Document> search(String text) {//User // get the full text entity manager
    FullTextEntityManager fullTextEntityManager =
            org.hibernate.search.jpa.Search.
                    getFullTextEntityManager(entityManager);

    // create the query using Hibernate Search query DSL
    QueryBuilder queryBuilder =
            fullTextEntityManager.getSearchFactory()
                    .buildQueryBuilder().forEntity(Document.class).get();//User

    // a very basic query by keywords
    org.apache.lucene.search.Query query =
            queryBuilder
                    .keyword()
                    .onFields("title", "desp", "source", "docurl")
                    .matching(text)
                    .createQuery();
    System.out.println("query is :" + text);
    // wrap Lucene query in an Hibernate Query object
    org.hibernate.search.jpa.FullTextQuery jpaQuery =
            fullTextEntityManager.createFullTextQuery(query, Document.class);//User

    // execute search and return results (sorted by relevance as default)
    jpaQuery.setFirstResult(0);
    jpaQuery.setMaxResults(10);
    List<Document> results = jpaQuery.getResultList();//User
//    for(int i=0;i<results.size();i++){
//      Document doc=results.get(i);
//      System.out.println("doc title is :"+doc.getTitle());
//    }
    return results;
  } // method search

  public List<Document> Search(String queryString,Pagenation pagenation) throws IOException {
    FullTextEntityManager fullTextEntityManager =
            org.hibernate.search.jpa.Search.
                    getFullTextEntityManager(entityManager);
    QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().
            buildQueryBuilder().forEntity(Document.class).get();
    //在这里修改要查询的字段

    queryString=queryFilter(queryString);
    System.out.println(queryString);
    org.apache.lucene.search.Query luceneQuery = queryBuilder.keyword().
            onFields("title", "desp", "source", "docurl").matching(queryString).createQuery();
    org.hibernate.search.jpa.FullTextQuery
            fullTextQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Document.class);
    pagenation.setRowCount(fullTextQuery.getResultSize());
    fullTextQuery.setFirstResult(pagenation.getStartRow());
    fullTextQuery.setMaxResults(10);
    List<Document> results = fullTextQuery.getResultList();
//    for(int i=0;i<results.size();i++){
//      Document doc=results.get(i);
//      System.out.println("doc title is :"+doc.getTitle());
//    }
    //上面是搜索功能的代码，下面是结果集关键字高亮的实现代码
    SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("r", "r");
    QueryScorer queryScorer = new QueryScorer(luceneQuery);
    Highlighter highlighter = new Highlighter(formatter, queryScorer);
    Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_36);
    //Analyzer analyzer = new ChineseWordAnalyzer();

    String[] fieldNames = {"title", "desp"};
    for (Document doc : results) {
      for (String fieldName : fieldNames) {
        //运用反射得到具体的标题内容
        Object fieldValue = ReflectionUtils.
                invokeMethod(BeanUtils.getPropertyDescriptor(Document.class, fieldName).getReadMethod(), doc);
        String hightLightFieldValue = null;
        if (fieldValue instanceof String) {

          try {
            //获得高亮关键字
            hightLightFieldValue = highlighter.
                    getBestFragment(analyzer, fieldName, ObjectUtils.toString(fieldValue, null));
          } catch (IOException e1) {
            e1.printStackTrace();
          } catch (InvalidTokenOffsetsException e1) {
            e1.printStackTrace();
          }
          //这个判断很关键，否则如果标题或内容中没有关键字的话，就会出现不显示的问题。
          if (hightLightFieldValue != null) {
            //运用反射设置结果集中的关键字高亮
            ReflectionUtils.invokeMethod(BeanUtils.
                    getPropertyDescriptor(Document.class, fieldName).getWriteMethod(), doc, hightLightFieldValue);
          }
        }
      }
  }
//    for(int i=0;i<results.size();i++){
//      Document doc=results.get(i);
//      System.out.println("doc title is :"+doc.getTitle());
//    }
    return results;
}

  public String queryFilter(String query){
    String newquery="";
    List<Word> words = WordSegmenter.seg(query,MaxNgramScore);
    for(int i=0;i<words.size();i++){
      newquery=newquery+words.get(i).toString()+" ";
      //System.out.println(words.get(i));
    }
    return newquery;
  }
  public static void main(String args[]){
    ArrayList queries=new ArrayList();
    queries.add(0,"华为");
    queries.add(1,"我是陈刚保，一个帅气的骚年");
    queries.add(2,",.;'/?");
    String query="华为";
    UserSearch us=new UserSearch();
    List<Document> searchResults = null;
    Pagenation pagenation=new Pagenation();
    try {
      searchResults = us.Search(query,pagenation);
    }
    catch (Exception ex) {
    }
    System.out.println(searchResults.size());
//    for(int i=0;i<10;i++){
//      //System.out.println(searchResults.get(i).getTitle());
//
//    }
//    long start=System.currentTimeMillis();
//    for(int i=0;i<queries.size();i++){
//      long end=System.currentTimeMillis();
//      String newquery=us.queryFilter(queries.get(i).toString());
//      System.out.println(newquery);
//      long cost=end-start;
//      System.out.println("cost time is : ");
//      System.out.println(cost);

//      Analyzer analyzer=new ChineseWordAnalyzer();
//      QueryParser queryParser = new QueryParser(Version.LUCENE_36,newquery,analyzer);
//      try{
//      Query query = queryParser.parse(newquery);
//      System.out.println(query);
//      }
//      catch(ParseException e){
//      }
//    }
  }
}

