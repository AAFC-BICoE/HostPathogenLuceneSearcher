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
import ca.gc.agr.mbb.hostpathogen.nouns.Host;

@RunWith(JUnit4.class)
public class HPSearcherTest{
    private final static Logger LOG = Logger.getLogger(HPSearcherTest.class.getName()); 

    protected static final String GOOD_LUCENE_DIR="./luceneIndexes";
    protected static final String BAD_LUCENE_DIR="/88888/444/bhgjrueww/../22/%^$";
    protected static final String TMP_DIR="./testDir_" + System.nanoTime();
    protected static final String TMP_FILE="./testFile_" + System.nanoTime();

    protected static final String GOOD_HOST_GENUS="Abies";
    protected static final String GOOD_PATHOGEN_GENUS="Basidiodendron";

    protected static final Long HUGE_ID = new Long(99999999999999l);


    // init
    // FAIL tests
    @Test(expected=InitializationException.class)
    public void missingLuceneDirProperty() throws InitializationException{
	Properties p = new Properties();
	Searcher s = HPSearcher.newSearcher(p);
    }

    @Test(expected=InitializationException.class)
    public void luceneDirDoesNotExist() throws InitializationException{
	Properties p = new Properties();
	p.setProperty(Searcher.LUCENE_INDICES_BASE_DIR, BAD_LUCENE_DIR);
	Searcher s = HPSearcher.newSearcher(p);
    }

    @Test(expected=InitializationException.class)
    public void luceneDirUnreadable() throws InitializationException{
	File newDir = null;
	try{
	    newDir = File.createTempFile("temp", Long.toString(System.nanoTime()));
	}catch(java.io.IOException e){
	    return; // This should not happen, but if does returning now will cause the test to fail...
	}
	newDir.setReadable(false);
	Properties p = new Properties();
	p.setProperty(Searcher.LUCENE_INDICES_BASE_DIR, BAD_LUCENE_DIR);
	Searcher s = HPSearcher.newSearcher(p);
    }

    @Test(expected=InitializationException.class)
    public void luceneDirNotDir() throws InitializationException{
	File tmpDir = new File(TMP_DIR);
	tmpDir.deleteOnExit();
	if(!tmpDir.mkdir()){
	    return;
	}
	File tmpFile = new File(tmpDir, TMP_FILE);
	
	try{
	    Util.touch(tmpFile);
	}catch(Exception e){
	    return; // this will cause the test to fail
	}
	tmpFile.deleteOnExit();
	Properties p = new Properties();
	p.setProperty(Searcher.LUCENE_INDICES_BASE_DIR, tmpFile.getAbsolutePath());
	Searcher s = HPSearcher.newSearcher(p);
    }


    @Test(expected=TooManyIdsException.class)
    public void requestTooManyPathogenIds() throws InitializationException{
	Properties p = new Properties();
	p.setProperty(Searcher.LUCENE_INDICES_BASE_DIR, "./luceneIndexes");
	Searcher s = HPSearcher.newSearcher(p);
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

    @Test(expected=TooManyIdsException.class)
    public void requestTooManyHostIds() throws InitializationException{
	Properties p = new Properties();
	p.setProperty(Searcher.LUCENE_INDICES_BASE_DIR, "./luceneIndexes");
	Searcher s = HPSearcher.newSearcher(p);
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
    public void getPathogensdByIdSuccessfully() throws InitializationException{
	Properties p = new Properties();
	p.setProperty(Searcher.LUCENE_INDICES_BASE_DIR, GOOD_LUCENE_DIR);
	Searcher s = HPSearcher.newSearcher(p);
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
	//Assert.assertTrue(results.size() > 0);
    }

    @Test
    public void getHostByIdSuccessfully() throws InitializationException{
	Properties p = new Properties();
	p.setProperty(Searcher.LUCENE_INDICES_BASE_DIR, GOOD_LUCENE_DIR);
	Searcher s = HPSearcher.newSearcher(p);
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
	Assert.assertTrue(results.size() > 0);
    }


    @Test
    public void searchHostSuccessfully() throws InitializationException{
	Properties p = new Properties();
	p.setProperty(Searcher.LUCENE_INDICES_BASE_DIR, GOOD_LUCENE_DIR);
	Searcher s = HPSearcher.newSearcher(p);

	List<String> values = new ArrayList<String>();
	values.add("Abies");
	Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
	queryParameters.put(LuceneFields.HOST_GENUS, values);

	List<Long>results = null;
	try{
	    results = s.searchHosts(queryParameters, null, 0, 20);
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}catch(IllegalOffsetLimitException e){
	    // Not supposed to happen
	    e.printStackTrace();
	    throw new NullPointerException();
	}
	Assert.assertTrue(results != null);
	Assert.assertTrue(results.size() >= 0);
    }


    protected static final  Map<String,List<String>>makeParameters(final String fieldName, final String parameterValue){
	Map<String,List<String>>queryParameters = new HashMap<String, List<String>>();
	List<String>queryParameter = new ArrayList<String>();
	queryParameter.add(parameterValue);
	queryParameters.put(fieldName, queryParameter);

	return queryParameters;
    }

    protected static Searcher goodSearcher() throws InitializationException{
	Properties p = new Properties();
	p.setProperty(Searcher.LUCENE_INDICES_BASE_DIR, HPSearcherTest.GOOD_LUCENE_DIR);
	return HPSearcher.newSearcher(p);
    }

}
