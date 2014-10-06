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

@RunWith(JUnit4.class)
public class HPSearcherHostPathogenTest{
    private final Logger LOG = Logger.getLogger(this.getClass().getName()); 

    @Test
    public void getPathogenByHostIdSuccessfully() throws InitializationException{
	LOG.info("************************");
	Searcher s = HPSearcherTest.goodSearcher();
	Long hostId = 5157l;
	List<Long>results = null;
	try{
	    results = s.getPathogenByHost(hostId,0,40);
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}catch(IllegalOffsetLimitException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
	LOG.info("Num pathogens by hostid " + hostId + " in search: " +results.size());
	Assert.assertTrue(results.size() > 0);
    }

    @Test
    public void getHostByPathogenIdSuccessfully() throws InitializationException{
	LOG.info("************************");
	Searcher s = HPSearcherTest.goodSearcher();
	Long pathogenId = 74462l;
	List<Long>results = null;
	try{
	    results = s.getHostByPathogen(pathogenId, 0, 40);
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}catch(IllegalOffsetLimitException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
	LOG.info("Num hosts by pathogen " + pathogenId + " in search: " +results.size());
	Assert.assertTrue(results.size() > 0);
    }

}
