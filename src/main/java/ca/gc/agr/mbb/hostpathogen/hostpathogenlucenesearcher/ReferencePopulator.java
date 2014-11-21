package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Reference;


public class ReferencePopulator<T> extends BasePopulator{
    
    public ReferencePopulator(){
	recordType = REFERENCE_TYPE;

	addSortFields(REFERENCE_YEAR, REFERENCE_AUTHORS);
	addDefaultSortFields(REFERENCE_AUTHORS, REFERENCE_YEAR);
	addSearchFields(REFERENCE_AUTHORS, REFERENCE_YEAR);
    }

    @Override
    public final T populate(Document doc) throws FailedPopulateException{
	super.populate(doc);
	Reference ref = new Reference();
	// obligatory
	ref.setId(longValue(doc, stored(primaryKeyField), true));
	ref.setSourceId(longValue(doc, stored(FK_REF_SOURCE_ID), true));
	ref.setAuthor(stringValue(doc, stored(REFERENCE_AUTHORS), true));
	ref.setYear(stringValue(doc, stored(REFERENCE_YEAR), true));
	ref.setTitle(stringValue(doc, stored(CHAPTER_ARTICLE_TITLE), true));
	//
	ref.setVolume(stringValue(doc, stored(VOLUME)));
	ref.setPage(stringValue(doc, stored(PAGES)));

	return (T)ref;
    }



}
