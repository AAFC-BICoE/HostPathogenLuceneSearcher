package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class HPSearcherMockTest{

    private final static Logger LOGGER = Logger.getLogger("test"); 
    private static Properties p;
    static{
	p = new Properties();
	p.setProperty(Searcher.MOCK_PROPERTY, "true");
    }


    // Pathogen tests
    @Test(expected=IllegalOffsetLimitException.class)
    public void negativeOffset() throws IllegalOffsetLimitException
    {
	try{
	    Searcher s = HPSearcher.newInstance(p);
	    /s.getPathogens(null, 
	    Util.checkOffset(-1, 2);
	}catch(InitializationException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}
    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void limitLessThanOne() throws IllegalOffsetLimitException
    {
	Util.checkOffset(100, 0);
    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void limitTooLarge() throws IllegalOffsetLimitException
    {
	Util.checkOffset(100, HPSearcher.OFFSET_MAX+6);
    }


}
