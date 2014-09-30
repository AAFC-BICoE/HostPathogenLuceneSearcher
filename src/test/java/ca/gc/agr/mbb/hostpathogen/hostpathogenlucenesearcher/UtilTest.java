package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UtilTest{
    private final static Logger LOG = Logger.getLogger("test"); 

    // checkOffsetAndLimit
    @Test(expected=IllegalOffsetLimitException.class)
    public void negativeOffset() throws IllegalOffsetLimitException
    {
	Util.checkOffsetAndLimit(-1, 2);
    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void limitLessThanOne() throws IllegalOffsetLimitException
    {
	Util.checkOffsetAndLimit(100, 0);
    }

    @Test(expected=IllegalOffsetLimitException.class)
    public void limitTooLarge() throws IllegalOffsetLimitException
    {
	Util.checkOffsetAndLimit(100, HPSearcher.LIMIT_MAX+6);
    }

    @Test
    public void hasGoodOffsetAndLimit() throws IllegalOffsetLimitException
    {
	Util.checkOffsetAndLimit(100, 50);
    }



    //////////////////////////////
    // checkQueryParameters

    @Test(expected=IllegalArgumentException.class)
    public void queryIsNull() throws IllegalArgumentException
    {
	Util.checkQueryParameters(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void queryIsZeroLength() throws IllegalArgumentException{
	Util.checkQueryParameters(new HashMap<String,List<String>>());
    }

    @Test
    public void queryWithOneParameter(){
	Map<String, List<String>>queries = new HashMap<String,List<String>>();
	List<String>values = new ArrayList<String>();
	values.add("ali*");
	queries.put("genus", values);
	Util.checkQueryParameters(queries);
    }


    //////////////////////////////
    // sliceList

    @Test(expected=IllegalArgumentException.class)
    public void listIsNull() throws IllegalArgumentException
    {
	Util.sliceList(null, 0,10);
    }



}
