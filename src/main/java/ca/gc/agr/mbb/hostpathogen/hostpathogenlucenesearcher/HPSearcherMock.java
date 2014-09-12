package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

public class HPSearcherMock implements Searcher{
    public static final int NUM_IDS = 100;

    // 2000-2199
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

    public Searcher init(Properties p) throws InitializationException{
	return this;
    }

    public List<Long>getPathogens(final List<Long> ids, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return pathogenId.subList((int)offset, (int)(offset+limit));
    }
    public List<Long>getAllPathogens(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return pathogenId.subList((int)offset, (int)(offset+limit));
    }

    public List<Long>searchPathogens(List<String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return pathogenId.subList((int)offset, (int)(offset+limit));
    }


    public List<Long>getHosts(List<Long> ids, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return hostId.subList((int)offset, (int)(offset+limit));
    }
    public List<Long>getAllHosts(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return hostId.subList((int)offset, (int)(offset+limit));

    }
    public List<Long>searchHosts(List<String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return hostId.subList((int)offset, (int)(offset+limit));
    }


}
