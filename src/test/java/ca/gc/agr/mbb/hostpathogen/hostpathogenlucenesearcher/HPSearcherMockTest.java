package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.Assert;

@RunWith(JUnit4.class)
public class HPSearcherMockTest{

    private final static Logger LOGGER = Logger.getLogger("test"); 
    private static Properties p;
    static{
	p = new Properties();
	p.setProperty(Searcher.MOCK_PROPERTY, "true");
    }


    // Pathogen tests///////////////////

    // Simple
    @Test(expected=IllegalOffsetLimitException.class)
    public void negativeOffset() throws IllegalOffsetLimitException
    {
	try{
	    Searcher s = HPSearcher.newInstance(p);
	    s.getAllPathogens((long)-1, 2);
	}catch(InitializationException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}
    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void limitLessThanOne() throws IllegalOffsetLimitException
    {
	try{
	    Searcher s = HPSearcher.newInstance(p);
	    s.getAllPathogens(100, 0);
	}catch(InitializationException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}

    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void limitTooLarge() throws IllegalOffsetLimitException
    {
	try{
	    Searcher s = HPSearcher.newInstance(p);
	    s.getAllPathogens(100, HPSearcher.OFFSET_MAX+6);
	}catch(InitializationException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}
    }

    // getAllPathogens
    @Test
    public void successfullyGetAllPathogensWithPaging(){
	Searcher s = null;
	try{
	    s = HPSearcher.newInstance(p);
	}catch(InitializationException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}
	List<Long>pathogens = new ArrayList<Long>();

	long offset = 0l;
	int limit = 20;
	
	try{
	    while(pathogens.addAll(s.getAllPathogens(offset, limit))){
		offset += limit;
	    }
	}catch(IllegalOffsetLimitException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}
	Assert.assertEquals(pathogens.size(), HPSearcherMock.NUM_IDS);

    }

}
