package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

abstract public class BasePopulator<T> implements Populator{
    private final static Logger LOG = Logger.getLogger(BasePopulator.class.getName()); 

    protected Set<String> validSortFieldSet = new HashSet<String>();
    protected List<String> defaultSortFields = new ArrayList<String>();
    protected String primaryKeyField = PK;
    protected String recordType = null;


    public void addDefaultSortField(final String field){
	defaultSortFields.add(field);
    }

    public List<String> getDefaultSortFields(){
	return defaultSortFields;
    }

    public String getPrimaryKeyField(){
	return primaryKeyField;
    }

    public Set<String>getValidSortFieldSet(){
	return validSortFieldSet;
    }

    public void addSortField(final String s){
	validSortFieldSet.add(s);
    }

    public boolean isValidSortField(String s){
	return validSortFieldSet.contains(s);
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
