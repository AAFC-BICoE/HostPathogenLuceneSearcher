package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Location;

public class LocationPopulator<T> extends BasePopulator{

    public LocationPopulator(){
	recordType = HOST_TYPE;

	addSortField(HOST_GENUS);
	addSortField(HOST_SPECIES);

	addDefaultSortField(HOST_GENUS);
	addDefaultSortField(HOST_SPECIES);
	addDefaultSortField(HOST_SUBSPECIFIC_TAXA);
    }

    public final T populate(Document doc) throws FailedPopulateException{
	Location h = new Location();
	// obligatory
	h.setId(longValue(doc, primaryKeyField+STORED_SUFFIX, true));

	return (T)h;
    }



}
