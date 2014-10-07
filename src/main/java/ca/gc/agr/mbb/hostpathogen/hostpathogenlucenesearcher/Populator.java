package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;

public interface Populator<T> extends LuceneFields{
    public T populate(Document d);
    public String getPrimaryKeyField();

    public void addSortField(String s);
    public boolean isValidSortField(String s);

    public void setDefaultSortField(String s);
    public String getDefaultSortField();
}
