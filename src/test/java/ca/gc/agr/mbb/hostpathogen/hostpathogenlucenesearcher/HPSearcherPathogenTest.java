package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher.PathogenPopulator;

@RunWith(JUnit4.class)
public class HPSearcherPathogenTest{
    private final Logger LOG = Logger.getLogger(this.getClass().getName()); 

    @Test
    public void verifyNotNullDefaultSortField() throws InitializationException{
	PathogenPopulator<Pathogen> pathogenPopulator = new PathogenPopulator<Pathogen>();
    }

    @Test(expected=TooManyIdsException.class)
    public void requestTooManyPathogenIds() throws InitializationException{
	Searcher s = HPSearcherTest.goodSearcher();
	List<Long> ids = new ArrayList<Long>();
	for(int i=21; i<1000; i++){
	    ids.add(new Long(i));
	}
	try{
	    s.getPathogens(ids);
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
    }

    @Test
    public void requestMissingPathogenIds() throws InitializationException{
	Searcher s = HPSearcherTest.goodSearcher();
	List<Long> ids = new ArrayList<Long>();
	ids.add(HPSearcherTest.HUGE_ID);
	List<Pathogen>results = null;
	try{
	    results = s.getPathogens(ids);
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
	Assert.assertTrue(results.size() == 0);
    }


    @Test
    public void getPathogensByIdSuccessfully() throws InitializationException{
	Searcher s = HPSearcherTest.goodSearcher();

	List<Long> ids = new ArrayList<Long>();
	for(int i=21; i<40; i++){
	    ids.add(new Long(i));
	}
	List<Pathogen>results = null;
	try{
	    results = s.getPathogens(ids);
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
	LOG.info("Num pathogens in search: " +results.size());
	Assert.assertTrue(results.size() > 0);
    }

    @Test
    public void searchPathogensGenusSuccessfully() throws InitializationException{
	Searcher s = HPSearcherTest.goodSearcher();

	List<Long>results = null;
	try{
	    results = s.searchPathogens(goodPathogenGenusParameters(), 0, 20);
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}catch(IllegalOffsetLimitException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
	LOG.info("Num pathogens in search: " +results.size());
	Assert.assertTrue(results.size() > 0);
    }

    @Test
    public void searchPathogensGenusAndPageSuccessfully() throws InitializationException{
	Searcher s = HPSearcherTest.goodSearcher();

	List<Long>results = null;
	try{
	    int numHits = (int)s.searchPathogensCount(allAsPathogenGenusParameters());
	    LOG.info("Num results: " + numHits);
	    for(int i=0; i<numHits; i+=LuceneIndexSearcher.MAX_IDS){
		results = s.searchPathogens(allAsPathogenGenusParameters(), i, LuceneIndexSearcher.MAX_IDS);
	    }
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}catch(IllegalOffsetLimitException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
    }

    @Test
    public void verifySortOrderingIsWorking() throws InitializationException{
	Searcher s = HPSearcherTest.goodSearcher();

	List<Long>results = null;
	try{
	    int numHits = (int)s.getAllPathogensCount();
	    LOG.info("Num results: " + numHits);
	    for(int i=0; i<numHits; i+=LuceneIndexSearcher.MAX_IDS){
		results = s.getAllPathogens(i, LuceneIndexSearcher.MAX_IDS);
		Pathogen lastPathogen = null;
		for(Long id: results){
		    Pathogen thisPathogen = s.getPathogen(id);
		    if(lastPathogen != null){
			if(lastPathogen.getGenus() != null && thisPathogen.getGenus() != null){
			    Assert.assertTrue(lastPathogen.getGenus().compareTo(thisPathogen.getGenus())<=0);
			    if(lastPathogen.getGenus().equals(thisPathogen.getGenus()) && lastPathogen.getSpecies() != null && thisPathogen.getSpecies() != null){
				Assert.assertTrue(lastPathogen.getSpecies().compareTo(thisPathogen.getSpecies())<=0);
				if(lastPathogen.getSpecies().equals(thisPathogen.getSpecies()) && lastPathogen.getSubSpecificTaxa() != null && thisPathogen.getSubSpecificTaxa() != null){
				    Assert.assertTrue(lastPathogen.getSubSpecificTaxa().compareTo(thisPathogen.getSubSpecificTaxa())<=0);
				}
			    }
			}
		    }
		    lastPathogen = thisPathogen;
		}
	    }
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}catch(IllegalOffsetLimitException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
    }

    @Test
    public void searchPathogensGenusUnSuccessfully() throws InitializationException{
	Searcher s = HPSearcherTest.goodSearcher();

	List<Long>results = null;
	try{
	    results = s.searchPathogens(HPSearcherTest.makeParameters(LuceneFields.PATHOGEN_GENUS, HPSearcherTest.GOOD_PATHOGEN_GENUS + "ZZZZZZZZZZZz"), 0, 20);
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}catch(IllegalOffsetLimitException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
	LOG.info("Num pathogens in search: " +results.size());
	Assert.assertTrue(results.size() == 0);
    }


    private Map<String,List<String>>goodPathogenGenusParameters(){
	return HPSearcherTest.makeParameters(LuceneFields.PATHOGEN_GENUS, HPSearcherTest.GOOD_PATHOGEN_GENUS);
    }

    private Map<String,List<String>>allAsPathogenGenusParameters(){
	return HPSearcherTest.makeParameters(LuceneFields.PATHOGEN_GENUS, "a*");
    }


}
