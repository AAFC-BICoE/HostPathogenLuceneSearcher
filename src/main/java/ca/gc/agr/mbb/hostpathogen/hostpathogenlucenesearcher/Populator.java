package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;

public interface Populator<T>{
    public T populate(Document d);
}
