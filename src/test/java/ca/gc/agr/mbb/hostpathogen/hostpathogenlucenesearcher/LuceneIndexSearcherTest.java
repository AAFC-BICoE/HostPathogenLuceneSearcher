package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


@RunWith(JUnit4.class)
public class LuceneIndexSearcherTest{
    private final static Logger LOG = Logger.getLogger(LuceneIndexSearcherTest.class.getName()); 

    // FAIL tests
    @Test(expected=InitializationException.class)
    public void nullLuceneDir() throws InitializationException{
	LuceneIndexSearcher lis = new LuceneIndexSearcher();
	PathogenPopulator pap = new PathogenPopulator();

	lis.init(null, pap);
    }


    @Test(expected=InitializationException.class)
    public void nullPopulator() throws InitializationException{
	LuceneIndexSearcher lis = new LuceneIndexSearcher();
	lis.init(HPSearcherTest.GOOD_LUCENE_DIR, null);
    }

    // SUCCESS tests
    @Test
    public void goodLuceneDirAndGoodPopulator() throws InitializationException{
	LuceneIndexSearcher lis = new LuceneIndexSearcher();
	PathogenPopulator pap = new PathogenPopulator();
	lis.init(HPSearcherTest.GOOD_LUCENE_DIR + "/" + LuceneFields.INDEX_PATHOGEN, pap);
    }
}
