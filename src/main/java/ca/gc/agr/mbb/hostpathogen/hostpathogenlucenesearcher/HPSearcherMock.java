package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Host;

public class HPSearcherMock implements Searcher{
    public static final int NUM_IDS = 100;

    // 2000-2099
    static List<Long>pathogenIds = null;
    static List<Pathogen>pathogens = null;

    // 100-199
    static List<Long>hostIds = null;
    static List<Host>hosts = null;

    static{
	pathogenIds = new ArrayList<Long>(NUM_IDS);
	pathogens = new ArrayList<Pathogen>(NUM_IDS);

	hostIds = new ArrayList<Long>(NUM_IDS);
	hosts = new ArrayList<Host>(NUM_IDS);
	for(int i=0; i<NUM_IDS; i++){
	    long hid = (long)(100+i);
	    long pid = (long)(2000+i);

	    hostIds.add(new Long(hid));
	    pathogenIds.add(new Long(pid));

	    Pathogen pathogen = new Pathogen();
	    pathogen.setId(pid);
	    pathogen.setGenus("genus" + pid);
	    pathogen.setSpecies("species" + pid);

	    pathogens.add(pathogen);

	    Host host = new Host();
	    host.setId(pid);
	    host.setGenus("genus" + pid);
	    host.setSpecies("species" + pid);
	    hosts.add(host);
	}
    }

    protected HPSearcherMock(){
	
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
    public List<Pathogen>getPathogens(final List<Long> ids)
	throws IllegalArgumentException{
	
	Util.checkList(ids, NUM_IDS);
	return pathogens.subList(0, NUM_IDS);
    }

    public Pathogen getPathogen(final Long id) throws IllegalArgumentException, IndexFailureException{
	List<Long>ids = new ArrayList<Long>(1);
	ids.add(id);
	return getPathogens(ids).get(0);
    }

    public List<Long>getPathogenByHost(long hostId, final long offset, final int limit) throws IllegalArgumentException, IndexFailureException{
	if(true){
	    throw new NullPointerException();
	}
	return null;
    }

    public List<Long>getAllPathogens(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffsetAndLimit(offset, limit);
	return Util.sliceList(pathogenIds, offset, limit);
    }

    public long getAllPathogensCount() throws IllegalOffsetLimitException, IllegalArgumentException{
	return pathogenIds.size();
    }


    public List<Long>searchPathogens(Map<String,List<String>>queryParameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkQueryParameters(queryParameters);
	Util.checkOffsetAndLimit(offset, limit);
	return Util.sliceList(pathogenIds, offset, limit);
    }

    public long searchPathogensCount(Map<String,List<String>>queryParmeters) throws IllegalOffsetLimitException, IllegalArgumentException{
	if(true){
	    throw new NullPointerException();
	}
	return 17l;
    }

    public long getAllHostsCount() throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	return 100l;
    }

    public long searchHostsCount(Map<String,List<String>>queryParmeters) throws IllegalOffsetLimitException, IllegalArgumentException{
	if(true){
	    throw new NullPointerException();
	}
	return 17l;
    }

    public List<Host>getHosts(List<Long> ids) throws IllegalArgumentException{
	Util.checkList(ids, NUM_IDS);
	return hosts.subList(0, ids.size());
    }

    public List<Long>getHostByPathogen(long pathogenId, final long offset, final int limit) throws IllegalArgumentException, IndexFailureException{
	if(true){
	    throw new NullPointerException();
	}
	return null;
    }

    public List<Long>getAllHosts(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffsetAndLimit(offset, limit);
	return Util.sliceList(hostIds, offset, limit);

    }
    public List<Long>searchHosts(final Map<String,List<String>>queryParameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkQueryParameters(queryParameters);
	Util.checkOffsetAndLimit(offset, limit);
	return Util.sliceList(hostIds, offset, limit);
    }


}
