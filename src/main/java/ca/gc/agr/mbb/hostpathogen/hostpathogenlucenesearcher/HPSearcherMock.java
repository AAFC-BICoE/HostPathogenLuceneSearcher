package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

public class HPSearcherMock implements Searcher{
    public static final int NUM_IDS = 100;

    // 2000-2099
    static List<Long>pathogenId = null;

    // 100-199
    static List<Long>hostId = null;

    static{
	hostId = new ArrayList<Long>(NUM_IDS);
	pathogenId = new ArrayList<Long>(NUM_IDS);
	for(int i=0; i<NUM_IDS; i++){
	    hostId.add(new Long((long)(100+i)));
	    pathogenId.add(new Long((long)(2000+i)));
	}
    }

/** 
  * Initialize Searcher with Properties.
  *  Must be run before any other methods.
  * 
  * 
  * @param java.util.Properties - contains init information
  * @return Searcher 
  * @exception InitializationException
  */ 
    public Searcher init(Properties p) throws InitializationException{
	// get 
	return this;
    }


/** 
  * Get Pathogens by ID
  * 
  * @param ids         List of ids wanted
  * @return List<Long> List of ids found
  * @exception IllegalArgumentException
  */ 
    public List<Long>getPathogens(final List<Long> ids)
	throws IllegalArgumentException{
	
	Util.checkList(ids, NUM_IDS);
	return pathogenId.subList(0, NUM_IDS);
    }


    public List<Long>getAllPathogens(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return Util.sliceList(pathogenId, offset, limit);
    }

    public List<Long>searchPathogens(List<String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return Util.sliceList(pathogenId, offset, limit);
    }


    public List<Long>getHosts(List<Long> ids) throws IllegalArgumentException{
	Util.checkList(ids, NUM_IDS);
	return hostId.subList(0, ids.size());
    }
    public List<Long>getAllHosts(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return Util.sliceList(hostId, offset, limit);

    }
    public List<Long>searchHosts(List<String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return Util.sliceList(hostId, offset, limit);
    }


}
