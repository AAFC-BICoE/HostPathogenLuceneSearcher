package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;

public class PathogenPopulator<T> extends BasePopulator{

    public final T populate(Document doc) throws FailedPopulateException{
	Pathogen p = new Pathogen();
	p.setId(longValue(doc, PK_PATHOGEN_ID, true));
	p.setHigherTaxaId(longValue(doc, FK_HIGHER_TAXA_ID));
	p.setIdAccepted(longValue(doc, FK_PATHOGEN_ID_ACCEPTED));
	p.setAnamorphId(longValue(doc, FK_ANAMORPH_ID));
	p.setGenus(stringValue(doc, PATHOGEN_GENUS, true));
	p.setSpecies(stringValue(doc, PATHOGEN_SPECIES, true));
	p.setFungalState(stringValue(doc, FUNGAL_STATE));
	//p.setAuthor(stringValue(doc, PATHOGEN_AUTHOR, true));
	p.setAuthor(stringValue(doc, PATHOGEN_AUTHOR, false));
	p.setSubSpecificTaxa(stringValue(doc, PATHOGEN_SUBSPECIFIC_TAXA));
	p.setVirusMPLO(stringValue(doc, VIRUS_MPLO_NAMES));
	return (T)p;
    }



}
