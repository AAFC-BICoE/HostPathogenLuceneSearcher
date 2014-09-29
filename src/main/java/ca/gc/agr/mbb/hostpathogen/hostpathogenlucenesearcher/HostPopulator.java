package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Host;

public class HostPopulator<T> extends BasePopulator{

    public HostPopulator(){
	primaryKeyField = PK;
    }

    public final T populate(Document doc) throws FailedPopulateException{
	Host h = new Host();
	// obligatory
	//h.setId(longValue(doc, PK_HOST_ID, true));
	h.setId(longValue(doc, PK, true));
	h.setGenus(stringValue(doc, HOST_GENUS, true));
	h.setSpecies(stringValue(doc, HOST_SPECIES, true));
	//
	h.setHigherTaxaId(longValue(doc, FK_HIGHER_TAXA_ID));
	h.setIdAccepted(longValue(doc, FK_HOST_ID_ACCEPTED));
	h.setCultivar(stringValue(doc, CULTIVAR));
	h.setAuthor(stringValue(doc, HOST_AUTHOR));
	return (T)h;
    }



}
