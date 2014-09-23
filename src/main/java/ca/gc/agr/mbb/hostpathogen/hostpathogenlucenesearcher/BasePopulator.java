package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;

abstract public class BasePopulator<T> implements Populator{

    static final long longValue(Document doc, String fieldName, boolean mustExist) throws FailedPopulateException
    {
	if (doc == null){
	    throw new FailedPopulateException(new NullPointerException("Document is null"));
	}
	String[] values = doc.getValues(fieldName);
	if(values == null || values.length == 0){
	    if(mustExist){
		throw new NullPointerException("Field " + fieldName + " cannot be empty/null");
	    }
	    return 0l;
	}
	try{
	    return (new Long(values[0])).longValue();
	}catch(NumberFormatException e){
	    throw new FailedPopulateException(e);
	}
    }

    static final String stringValue(Document doc, String fieldName, boolean mustExist) throws FailedPopulateException
    {
	String[] values = doc.getValues(fieldName);
	if(values == null || values.length == 0){
	    if(mustExist){
		throw new NullPointerException("Field " + fieldName + " cannot be empty/null");
	    }
	    return null;
	}
	try{
	    return values[0];
	}catch(NumberFormatException e){
	    throw new FailedPopulateException(e);
	}
    }




}
