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
import ca.gc.agr.mbb.hostpathogen.nouns.Host;

@RunWith(JUnit4.class)
public class HPSearcherHostTest{
    private final Logger LOG = Logger.getLogger(this.getClass().getName()); 

    @Test
    public void requestMissingHostIds() throws InitializationException{
	LOG.info("Start requestMissingHostIds");
	Searcher s = HPSearcherTest.goodSearcher();
	List<Long> ids = new ArrayList<Long>();
	ids.add(HPSearcherTest.HUGE_ID);
	List<Host>results = null;
	try{
	    results = s.getHosts(ids);
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
	Assert.assertTrue(results.size() == 0);
    }

    @Test(expected=TooManyIdsException.class)
    public void requestTooManyHostIds() throws InitializationException{
	LOG.info("Start requestTooManyHostIds");
	Searcher s = HPSearcherTest.goodSearcher();
	List<Long> ids = new ArrayList<Long>();
	for(int i=21; i<1000; i++){
	    ids.add(new Long(i));
	}
	try{
	    s.getHosts(ids);
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
    }

    @Test
    public void getHostsByIdSuccessfully() throws InitializationException{
	LOG.info("Start getHostsByIdSuccessfully");
	Searcher s = HPSearcherTest.goodSearcher();

	List<Long> ids = new ArrayList<Long>();
	for(int i=21; i<40; i++){
	    ids.add(new Long(i));
	}
	List<Host>results = null;
	try{
	    results = s.getHosts(ids);
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
	LOG.info("Num hosts in search: " +results.size());
	Assert.assertTrue(results.size() > 0);
    }

    @Test
    public void searchHostsGenusSuccessfully() throws InitializationException{
	LOG.info("Start searchHostsGenusSuccessfully");
	Searcher s = HPSearcherTest.goodSearcher();

	List<Long>results = null;
	try{
	    results = s.searchHosts(goodHostGenusParameters(), null, 0, 20);
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
    public void searchHostsGenusUnSuccessfully() throws InitializationException{
	LOG.info("Start searchHostsGenusUnSuccessfully");
	Searcher s = HPSearcherTest.goodSearcher();

	List<Long>results = null;
	try{
	    results = s.searchHosts(HPSearcherTest.makeParameters(LuceneFields.HOST_GENUS, HPSearcherTest.GOOD_HOST_GENUS + "ZZZZZZZZZZz"), null, 0, 20);
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


    private Map<String,List<String>>goodHostGenusParameters(){
	return HPSearcherTest.makeParameters(LuceneFields.HOST_GENUS, HPSearcherTest.GOOD_HOST_GENUS);
    }


}
