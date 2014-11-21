package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Anamorph;

public class AnamorphPopulator<T> extends BasePopulator{

    public AnamorphPopulator(){
	recordType = ANAMORPH_TYPE;

	//addSortFields(HOST_GENUS, HOST_SPECIES);

	//addDefaultSortFields(HOST_GENUS, HOST_SPECIES, HOST_SUBSPECIFIC_TAXA);
    }

    @Override
    public final T populate(Document doc) throws FailedPopulateException{
	super.populate(doc);
	throw new FailedPopulateException();

	// Anamorph h = new Anamorph();
	// // obligatory
	// h.setId(longValue(doc, primaryKeyField+STORED_SUFFIX, true));

	// return (T)h;
    }



}
