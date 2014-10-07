package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import java.util.Set;
import java.util.HashSet;


abstract public class BasePopulator<T> implements Populator{
    protected Set<String> validSortField = new HashSet<String>();
    protected String defaultSortField = null;
    protected String primaryKeyField = PK;
    protected String recordType = null;

    public void setDefaultSortField(final String defaultString){
	this.defaultSortField = defaultSortField;
    }
    public String getDefaultSortField(){
	return defaultSortField;
    }

    public String getPrimaryKeyField(){
	return primaryKeyField;
    }

    public void addSortField(final String s){
	validSortField.add(s);
    }

    public boolean isValidSortField(String s){
	return validSortField.contains(s);
    }

    static final long longValue(Document doc, String fieldName) throws FailedPopulateException{
	return longValue(doc, fieldName, false);
    }

    static final long longValue(Document doc, String fieldName, boolean mustExist) throws FailedPopulateException
    {
	checkDocFieldName(doc, fieldName);

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

    static final String stringValue(Document doc, String fieldName) throws FailedPopulateException{
	return stringValue(doc, fieldName, false);
    }

    static final String stringValue(Document doc, String fieldName, boolean mustExist) throws FailedPopulateException
    {
	checkDocFieldName(doc, fieldName);

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

    static final void checkDocFieldName(Document doc, String fieldName){
	if (doc == null){
	    throw new FailedPopulateException("Document is null");
	}

	if (fieldName == null || fieldName.length() == 0){
	    throw new FailedPopulateException("Fieldname is null or zero length");
	}
    }


}
