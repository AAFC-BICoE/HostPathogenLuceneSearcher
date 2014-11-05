package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Host;
import ca.gc.agr.mbb.hostpathogen.nouns.Reference;

public class HostPopulator<T> extends BasePopulator{

    public HostPopulator(){
	recordType = HOST_TYPE;
	classType = Host.class;
	addSortFields(HOST_GENUS, HOST_SPECIES);
	addDefaultSortFields(HOST_GENUS, HOST_SPECIES, HOST_SUBSPECIFIC_TAXA);
	addSearchFields(HOST_GENUS, HOST_SPECIES, HOST_SUBSPECIFIC_TAXA);


    }

    public final T populate(Document doc) throws FailedPopulateException{
	Host h = new Host();

	// obligatory
	h.setId(longValue(doc, primaryKeyField+STORED_SUFFIX, true));
	h.setGenus(stringValue(doc, HOST_GENUS+STORED_SUFFIX, true));
	//
	h.setSpecies(stringValue(doc, HOST_SPECIES+STORED_SUFFIX));
	h.setHigherTaxaId(longValue(doc, FK_HIGHER_TAXA_ID+STORED_SUFFIX));
	h.setIdAccepted(longValue(doc, FK_HOST_ID_ACCEPTED+STORED_SUFFIX));
	h.setCultivar(stringValue(doc, CULTIVAR+STORED_SUFFIX));
	h.setAuthor(stringValue(doc, HOST_AUTHOR+STORED_SUFFIX));
	return (T)h;
    }



}
