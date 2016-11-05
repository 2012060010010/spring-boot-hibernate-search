package netgloo.models;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by chengangbao on 2016/11/4.
 */
/**
 * An entity User composed by four fields (id, email, name, city).
 * The annotation Indexed marks User as an entity which needs to be indexed by
 * Hibernate Search.
 */
@Entity
@Indexed
@Table(name = "Document")
public class Document {

    // ------------------------
    // PRIVATE FIELDS
    // ------------------------

    // Hibernate Search needs to store the entity identifier in the index for
    // each entity. By default, it will use for this purpose the field marked
    // with Id.
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    // You have to mark the fields you want to make searchable annotating them
    // with Field.
    // The parameter Store.NO ensures that the actual data will not be stored in
    // the index (mantaining the ability to search for it): Hibernate Search
    // will execute a Lucene query in order to find the database identifiers of
    // the entities matching the query and use these identifiers to retrieve
    // managed objects from the database.
 /* 参数Store.NO确保实际数据不会存储在索引中（保留搜索它的能力）：Hibernate
  Search将执行Lucene查询，以便找到匹配查询的实体的数据库标识符，
  并使用这些查询标识符以从数据库检索受管对象。
  */
    @Field(store = Store.NO)
    @NotNull
    private String docurl;

    // store=Store.NO is the default values and could be omitted.
    @Field
    @NotNull
    private String desp;

    @Field
    @NotNull
    private String source;
    @Field
    @NotNull
    private String title;

    // ------------------------
    // PUBLIC METHODS
    // ------------------------

    public Document() { }

    public Document(long id) {
        this.id = id;
    }

    // Getter and setter methods

    public Document(String title, String desp,String source,String docurl) {
        this.title = title;
        this.desp =desp;
        this.source=source;
        this.docurl=docurl;
    }

    public long getId() {
        return id;
    }

    public void setId(long value) {
        this.id = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String value) {
        this.desp = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String value) {
        this.source = value;
    }

    public String  getDocurl(){return docurl;}

    public void setDocurl(String value){this.docurl=value;}


} // class

