package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;

public class PathogenPopulator<T> extends BasePopulator{

    public final T populate(Document doc) throws FailedPopulateException{
	Pathogen p = new Pathogen();
	p.setId(longValue(doc, PK_PATHOGEN_ID, true));
	p.setHigherTaxaId(longValue(doc, FK_HIGHER_TAXA_ID, false));
	p.setIdAccepted(longValue(doc, FK_PATHOGEN_ID_ACCEPTED, false));
	p.setAnamorphId(longValue(doc, FK_ANAMORPH_ID, false));
	p.setGenus(stringValue(doc, PATHOGEN_GENUS, true));
	p.setSpecies(stringValue(doc, PATHOGEN_SPECIES, true));
	/*
	FUNGAL_STATE,
	PATHOGEN_AUTHOR,
	PATHOGEN_SUBSPECIFIC_TAXA,
	VIRUS_MPLO_NAMES
	*/
	return (T)p;
    }



}
