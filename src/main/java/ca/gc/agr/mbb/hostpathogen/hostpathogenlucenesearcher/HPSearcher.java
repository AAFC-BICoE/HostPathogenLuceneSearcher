package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.List;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Host;


public class HPSearcher implements Searcher{
    public static int LIMIT_MAX = 50;

    public static final Searcher newSearcher(final Properties p) throws InitializationException{
	Searcher searcher = null;

	if(p.containsKey(MOCK_PROPERTY)){
	    searcher = new HPSearcherMock();
	}else{
	    searcher = new HPSearcher();
	}
	return searcher.init(p);
    }


    /**
     * Initialize the Searcher. Must be run before any other methods.
     *
     * @param p Initialization properties (like Search.MOCK_PROPERTY)
     * @return Searcher
     * @throws InitializationException <<Description>>
     */
    public Searcher init(Properties prop) throws InitializationException{
	// itialize all Lucene indices 
	return this;
    }

    ///// Pathogens

    public List<Pathogen>getPathogens(final List<Long> ids) throws IllegalArgumentException{
	Util.checkIds(ids);
	return null;
    }

    public List<Long>getAllPathogens(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return null;
    }

    public List<Long>searchPathogens(List<String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return null;
    }


    ///// Hosts
    public List<Host>getHosts(List<Long> ids) throws IllegalArgumentException{
	Util.checkIds(ids);
	return null;
    }
    public List<Long>getAllHosts(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return null;
    }

    public List<Long>searchHosts(List<String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffset(offset, limit);
	return null;
    }


    



}
