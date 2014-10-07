package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;

public class PathogenPopulator<T> extends BasePopulator{
    
    public PathogenPopulator(){
	recordType = PATHOGEN_TYPE;
	addSortField(PATHOGEN_GENUS);
	addSortField(PATHOGEN_SPECIES);
	setDefaultSortField(PATHOGEN_GENUS);
    }

    public final T populate(Document doc) throws FailedPopulateException{
	Pathogen p = new Pathogen();
	// obligatory
	//	p.setId(longValue(doc, PK_PATHOGEN_ID, true));
	p.setId(longValue(doc, primaryKeyField+STORED_SUFFIX, true));
	p.setGenus(stringValue(doc, PATHOGEN_GENUS+STORED_SUFFIX, true));
	p.setSpecies(stringValue(doc, PATHOGEN_SPECIES+STORED_SUFFIX, true));
	//
	p.setHigherTaxaId(longValue(doc, FK_HIGHER_TAXA_ID+STORED_SUFFIX));
	p.setIdAccepted(longValue(doc, FK_PATHOGEN_ID_ACCEPTED+STORED_SUFFIX));
	p.setAnamorphId(longValue(doc, FK_ANAMORPH_ID+STORED_SUFFIX));
	p.setFungalState(stringValue(doc, FUNGAL_STATE+STORED_SUFFIX));
	p.setAuthor(stringValue(doc, PATHOGEN_AUTHOR+STORED_SUFFIX));
	p.setSubSpecificTaxa(stringValue(doc, PATHOGEN_SUBSPECIFIC_TAXA+STORED_SUFFIX));
	p.setVirusMPLO(stringValue(doc, VIRUS_MPLO_NAMES+STORED_SUFFIX));
	return (T)p;
    }



}
