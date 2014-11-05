package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import ca.gc.agr.mbb.hostpathogen.nouns.HostPathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Host;
import ca.gc.agr.mbb.hostpathogen.nouns.Reference;

public class HostPathogenPopulator<T> extends BasePopulator{
    
    public HostPathogenPopulator(){
	recordType = HOST_PATHOGEN_TYPE;
	classType = HostPathogen.class;
	addSortFields(PATHOGEN_GENUS, PATHOGEN_SPECIES, HOST_GENUS, HOST_SPECIES, HOST_SUBSPECIFIC_TAXA);
	addSortFields(HOST_GENUS, HOST_SPECIES);
	addSearchFields(HOST_GENUS, HOST_SPECIES);
	addDefaultSortFields(HOST_GENUS, HOST_SPECIES, HOST_SUBSPECIFIC_TAXA);

	addRelation(Host.class, FK_HOST_ID);
	addRelation(Pathogen.class, FK_PATHOGEN_ID);
	addRelation(Reference.class, FK_REFERENCE_ID);
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
