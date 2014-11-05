package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Reference;


public class RefSourcePopulator<T> extends BasePopulator{
    
    public RefSourcePopulator(){
	recordType = REF_SOURCES_TYPE;

	addSortFields(JOURNAL_TITLE, BOOK_YEAR, BOOK_AUTHOR, BOOK_TITLE);
	addDefaultSortFields(JOURNAL_TITLE);
	addSearchFields(JOURNAL_TITLE, BOOK_YEAR, BOOK_AUTHOR, BOOK_TITLE);
    }

    public final T populate(Document doc) throws FailedPopulateException{
	throw new FailedPopulateException();
    }



}
