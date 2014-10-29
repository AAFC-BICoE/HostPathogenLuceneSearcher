package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.HostPathogen;

public class HostPathogenPopulator<T> extends BasePopulator{
    
    public HostPathogenPopulator(){
	recordType = HOST_PATHOGEN_TYPE;

	addSortField(PATHOGEN_GENUS);
	addSortField(PATHOGEN_SPECIES);

	addSortField(HOST_GENUS);
	addSortField(HOST_SPECIES);

	addDefaultSortField(HOST_GENUS);
	addDefaultSortField(HOST_SPECIES);
	addDefaultSortField(HOST_SUBSPECIFIC_TAXA);
    }

    public final T populate(Document doc) throws FailedPopulateException{
	HostPathogen p = new HostPathogen();
	p.setId(longValue(doc, primaryKeyField+STORED_SUFFIX, true));

	p.setReferenceId(longValue(doc, FK_REFERENCE_ID + STORED_SUFFIX, true));
	p.setHostId(longValue(doc, FK_HOST_ID + STORED_SUFFIX, true));
	p.setPathogenId(longValue(doc, FK_PATHOGEN_ID + STORED_SUFFIX, true));
	//
	p.setRustState(stringValue(doc, RUST_STATE + STORED_SUFFIX));
	p.setPlantPart(stringValue(doc, PLANT_PART + STORED_SUFFIX));
	p.setSymptom(stringValue(doc, SYMPTOM + STORED_SUFFIX));
	p.setNotes(stringValue(doc,  NOTES + STORED_SUFFIX));
	return (T)p;
    }



}
