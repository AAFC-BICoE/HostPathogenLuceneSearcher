package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;

public class Util{

    // Make sure offset and limits are sane and within constraints.
    public static final void checkOffset(final long offset, final int limit) throws IllegalOffsetLimitException{
	StringBuilder s = new StringBuilder();
	if (offset <0){
	    s.append("Offset is < 0: " + offset + "||");
	}
	if (limit<1){
	    s.append("Limit is < 1: " + limit);
	}

	if (limit>HPSearcher.OFFSET_MAX){
	    s.append("Limit is > max limit value:" + HPSearcher.OFFSET_MAX + " :" + limit); 
	}

	if(s.length() > 0){
	    throw new IllegalOffsetLimitException(s.toString());
	}

    }
    

}
