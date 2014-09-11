package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UtilTest{
    private final static Logger LOGGER = Logger.getLogger("test"); 


    @Test(expected=IllegalOffsetLimitException.class)
    public void negativeOffset() throws IllegalOffsetLimitException
    {
	Util.checkOffset(-1, 2);
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
