package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HPSearcherTest{
    private final static Logger LOGGER = Logger.getLogger("test"); 

    private final String BAD_LUCENE_DIR="/88888/444/bhgjrueww/../22/%^$";
    private final String TMP_DIR="./testDir_" + System.nanoTime();
    private final String TMP_FILE="./testFile_" + System.nanoTime();

    //@BeforeClass
    //    public static void init()
    // {
    // 	tmpDir = File.createTempFile("temp", Long.toString(System.nanoTime()));
	
    // }


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
}
