package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Reference;


public class ReferencePopulator<T> extends BasePopulator{
    
    public ReferencePopulator(){
	recordType = REFERENCE_TYPE;
    }

    public final T populate(Document doc) throws FailedPopulateException{
	Reference ref = new Reference();
	// obligatory
	ref.setId(longValue(doc, primaryKeyField+STORED_SUFFIX, true));
	ref.setSourceId(longValue(doc, FK_REF_SOURCE_ID, true));
	ref.setAuthor(stringValue(doc, REFERENCE_AUTHORS, true));
	ref.setYear(stringValue(doc, REFERENCE_YEAR, true));
	ref.setTitle(stringValue(doc, CHAPTER_ARTICLE_TITLE, true));
	//
	ref.setVolume(stringValue(doc, VOLUME));
	ref.setPage(stringValue(doc, PAGES));

	return (T)ref;
    }



}
