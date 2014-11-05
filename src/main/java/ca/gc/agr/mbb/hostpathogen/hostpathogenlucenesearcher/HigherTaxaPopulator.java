package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.HigherTaxa;

public class HigherTaxaPopulator<T> extends BasePopulator{

    public HigherTaxaPopulator(){
	recordType = HIGHER_TAXA_TYPE;

	//addSortFields(HOST_GENUS, HOST_SPECIES);

	//addDefaultSortFields(HOST_GENUS, HOST_SPECIES, HOST_SUBSPECIFIC_TAXA);
    }

    public final T populate(Document doc) throws FailedPopulateException{
	throw new FailedPopulateException();

	// HigherTaxa h = new HigherTaxa();
	// // obligatory
	// h.setId(longValue(doc, primaryKeyField+STORED_SUFFIX, true));

	// return (T)h;
    }



}
