package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;

public class PathogenPopulator<T> implements Populator{
    public T populate(Document d){
	return (T)new Pathogen();
    }
}
