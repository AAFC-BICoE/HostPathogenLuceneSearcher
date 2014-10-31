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
public class HPSearcher2Test{
    private final static Logger LOG = Logger.getLogger(HPSearcher2Test.class.getName()); 

    protected static final String GOOD_LUCENE_DIR="./luceneIndexes";
    protected static final String BAD_LUCENE_DIR="/88888/444/bhgjrueww/../22/%^$";
    protected static final String TMP_DIR="./testDir_" + System.nanoTime();
    protected static final String TMP_FILE="./testFile_" + System.nanoTime();

    protected static final String GOOD_HOST_GENUS="Abies";
    protected static final String GOOD_PATHOGEN_GENUS="Basidiodendron";

    protected static final Long HUGE_ID = new Long(99999999999999l);

    static LuceneConfig pathogenConfig;
    static LuceneConfig hostConfig;
    static{
	try{
	    pathogenConfig = UtilLucene.luceneConfig(LuceneFields.PATHOGEN_TYPE, UtilTest.goodProperties);
	    hostConfig = UtilLucene.luceneConfig(LuceneFields.HOST_TYPE, UtilTest.goodProperties);
	}catch(InitializationException e){
	    e.printStackTrace();
	}
    }

    @Test
    public void shouldConstructOK() throws InitializationException{
	HPSearcher2<Pathogen> hps = new HPSearcher2<Pathogen>(Pathogen.class);
	Assert.assertTrue(hps != null);
    }

   @Test
    public void shouldInitOKWithGoodConfig() throws InitializationException{
       HPSearcher2<Pathogen> hps = new HPSearcher2<Pathogen>(Pathogen.class);
       hps.init(pathogenConfig);
	
    }

    @Test(expected=InitializationException.class)
   public void shouldFailWithNullConfig() throws InitializationException, IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
       HPSearcher2<Host> hps = new HPSearcher2<Host>(Host.class);
       hps.init(null);
    }

   @Test
   public void shouldGetAllPathogens() throws InitializationException, IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
       HPSearcher2<Pathogen> hps = new HPSearcher2<Pathogen>(Pathogen.class);
       hps.init(pathogenConfig);
       List<Long> all = hps.getAll(1,10);	   
       Assert.assertTrue(all != null && all.size() >0);
    }

   @Test
   public void shouldGetAllHosts() throws InitializationException, IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
       HPSearcher2<Host> hps = new HPSearcher2<Host>(Host.class);
       hps.init(hostConfig);
       List<Long> all = hps.getAll(1,10);	   
       Assert.assertTrue(all != null && all.size() >0);
    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void shouldFailWithNegativeOffset() throws InitializationException, IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
       HPSearcher2<Host> hps = new HPSearcher2<Host>(Host.class);
       hps.init(hostConfig);
       List<Long> all = hps.getAll(-1,10);	   
    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void shouldFailWithBadLimit() throws InitializationException, IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
       HPSearcher2<Host> hps = new HPSearcher2<Host>(Host.class);
       hps.init(hostConfig);
       List<Long> all = hps.getAll(10,-1);	   
    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void shouldFailWithTooLargeOffset() throws InitializationException, IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
       HPSearcher2<Host> hps = new HPSearcher2<Host>(Host.class);
       hps.init(hostConfig);
       List<Long> all = hps.getAll(10,1000);	   
    }



   @Test
   public void shouldGetAllHostsCount() throws InitializationException, IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
       HPSearcher2<Host> hps = new HPSearcher2<Host>(Host.class);
       hps.init(hostConfig);
       long allCount = hps.getAllCount();	   
       Assert.assertTrue(allCount >0);
    }

    @Test(expected=InitializationException.class)
   public void shouldFailGetAllWithMismatcheNouns() throws InitializationException, IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
       HPSearcher2<Host> hps = new HPSearcher2<Host>(Host.class);
       hps.init(pathogenConfig);
       List<Long> all = hps.getAll(1,10);	   
       Assert.assertTrue(all != null && all.size() >0);
    }

   @Test
   public void shouldGetSinglePathogenById() throws InitializationException, IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
       HPSearcher2<Pathogen> hps = new HPSearcher2<Pathogen>(Pathogen.class);
       hps.init(pathogenConfig);
       List<Long> all = hps.getAll(1,10);	

       Pathogen pathogen = hps.get(all.get(0));
       Assert.assertTrue(pathogen != null);
    }

   @Test
   public void shouldGetSingleHostById() throws InitializationException, IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
       HPSearcher2<Host> hps = new HPSearcher2<Host>(Host.class);
       hps.init(hostConfig);
       List<Long> all = hps.getAll(1,10);	

       Host host = hps.get(all.get(0));
       Assert.assertTrue(host != null);
    }


    @Test
    public void searchHostSuccessfully() throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException, InitializationException{
	Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
	HPSearcher2<Host> hps = goodHostSearcher(queryParameters);
	List<Long> results = hps.search(queryParameters, null, 1,3);
	Assert.assertTrue(results != null && results.size()>0);
    }

    @Test
    public void searchHostCountSuccessfully() throws IllegalArgumentException, IndexFailureException, InitializationException{
	Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
	HPSearcher2<Host> hps = goodHostSearcher(queryParameters);
	long results = hps.searchCount(queryParameters);
	Assert.assertTrue(results>0l);
    }

    @Test
    public void searchHostSuccessfullyWithSorting() throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException, InitializationException{
	Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
	HPSearcher2<Host> hps = goodHostSearcher(queryParameters);
	List<Long> results = hps.search(queryParameters, goodHostSortFields(), 1,20);
	Assert.assertTrue(results != null && results.size()>0);
    }

    @Test
    public void searchHostSuccessfullyWithSortingValidated() throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException, InitializationException{
	Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
	HPSearcher2<Host> hps = goodHostSearcher(queryParameters);

	queryParameters.remove(LuceneFields.HOST_GENUS);
	List<String> values = new ArrayList<String>();
	values.add("a*");
	queryParameters.put(LuceneFields.HOST_GENUS, values);

	List<Long> results = hps.search(queryParameters, goodHostSortFields(), 1,3);
	Assert.assertTrue(results != null && results.size()>1 );
    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void searchShouldFailWithBadOffset() throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException, InitializationException{
	Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
	HPSearcher2<Host> hps = goodHostSearcher(queryParameters);
	List<Long> results = hps.search(queryParameters, null, -1,3);
    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void searchShouldFailWithBadLimit() throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException, InitializationException{
	Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
	HPSearcher2<Host> hps = goodHostSearcher(queryParameters);
	List<Long> results = hps.search(queryParameters, null, 10,-3);
    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void searchShouldFailWithTooBigLimit() throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException, InitializationException{
	Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
	HPSearcher2<Host> hps = goodHostSearcher(queryParameters);
	List<Long> results = hps.search(queryParameters, null, 10,9999);
    }


    @Test(expected=IllegalArgumentException.class)
    public void searchShouldFailwithBadSearchFields() throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException, InitializationException{
	Map<String, List<String>> queryParameters = new HashMap<String, List<String>>();
	queryParameters.put(LuceneFields.PATHOGEN_GENUS, null);
	HPSearcher2<Host> hps = goodHostSearcher(queryParameters);
	List<Long> results = hps.search(queryParameters, null, 1,3);
    }


    private HPSearcher2<Host> goodHostSearcher(Map<String, List<String>> queryParameters) throws InitializationException{
       HPSearcher2<Host> hps = new HPSearcher2<Host>(Host.class);
       hps.init(hostConfig);
       List<String> values = new ArrayList<String>();
       values.add("Abies");
       queryParameters.put(LuceneFields.HOST_GENUS, values);
       return hps;
    }

    private List<String> goodHostSortFields(){
	List<String> sf = new ArrayList<String>();
	sf.add(LuceneFields.HOST_GENUS);
	return sf;
    }
}
