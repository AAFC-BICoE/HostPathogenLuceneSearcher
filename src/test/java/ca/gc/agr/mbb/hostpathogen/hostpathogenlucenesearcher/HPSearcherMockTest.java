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

    private final static Logger LOG = Logger.getLogger("test"); 
    private static Properties p;
    static{
	p = new Properties();
	p.setProperty(Searcher.MOCK_PROPERTY, "true");
    }


    // Pathogen tests///////////////////

    // FAIL tests
    @Test(expected=IllegalOffsetLimitException.class)
    public void negativeOffset() throws IllegalOffsetLimitException
    {
	try{
	    Searcher s = HPSearcher.newSearcher(p);
	    s.getAllPathogens((long)-1, 2);
	}catch(InitializationException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}
    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void limitLessThanOne() throws IllegalOffsetLimitException
    {
	try{
	    Searcher s = HPSearcher.newSearcher(p);
	    s.getAllPathogens(100, 0);
	}catch(InitializationException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}

    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void limitTooLarge() throws IllegalOffsetLimitException
    {
	try{
	    Searcher s = HPSearcher.newSearcher(p);
	    s.getAllPathogens(100, HPSearcher.LIMIT_MAX+6);
	}catch(InitializationException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}
    }

    // getAllPathogens
    // Success tests
    @Test
    public void successfullyGetAllPathogensFirstPage(){
	Searcher s = null;
	try{
	    s = HPSearcher.newSearcher(p);
	}catch(InitializationException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}

	long offset = 0l;
	int limit = 20;

	List<Long>pathogens = new ArrayList<Long>(limit);
	
	try{
	    pathogens = s.getAllPathogens(offset, limit);
	}catch(IllegalOffsetLimitException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}
	Assert.assertEquals(pathogens.size(), limit);
    }

    public void successfullyGetAllPathogensOddOffset(){
	Searcher s = null;
	try{
	    s = HPSearcher.newSearcher(p);
	}catch(InitializationException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}

	// odd offset
	long offset = 37l;
	int limit = 20;
	List<Long>pathogens = new ArrayList<Long>(limit);
	
	try{
	    pathogens = s.getAllPathogens(offset, limit);
	}catch(IllegalOffsetLimitException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}
	Assert.assertEquals(pathogens.size(), limit);
    }

    public void successfullyGetAllPathogensMaxOffset(){
	Searcher s = null;
	try{
	    s = HPSearcher.newSearcher(p);
	}catch(InitializationException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}


	// max offset
	long offset = HPSearcher.LIMIT_MAX;
	int limit = 20;
	List<Long>pathogens = new ArrayList<Long>(limit);
	
	try{
	    pathogens = s.getAllPathogens(offset, limit);
	}catch(IllegalOffsetLimitException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}
	Assert.assertEquals(pathogens.size(), limit);
    }




    @Test
    public void successfullyGetAllPathogensWithPaging(){
	Searcher s = null;
	try{
	    s = HPSearcher.newSearcher(p);
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
	}catch(IndexFailureException e){
	    // Not supposed to happen
	    throw new NullPointerException();
	}
	Assert.assertEquals(pathogens.size(), HPSearcherMock.NUM_IDS);
    }

}
