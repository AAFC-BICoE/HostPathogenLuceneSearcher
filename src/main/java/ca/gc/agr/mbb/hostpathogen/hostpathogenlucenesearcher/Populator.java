package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Set;
import java.util.List;
import org.apache.lucene.document.Document;

public interface Populator<T> extends LuceneFields{
    public T populate(Document d);
    public String getRecordType();
    public String getPrimaryKeyField();

    public void addSortField(String s);
    public boolean isValidSortField(String s);
    public Set<String>getValidSortFieldSet();

    public void addDefaultSortField(String s);
    public List<String> getDefaultSortFields();
}
