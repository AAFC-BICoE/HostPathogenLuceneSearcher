package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Host;
import ca.gc.agr.mbb.hostpathogen.nouns.HostPathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Reference;


public class HPSearcher implements Searcher, LuceneFields{
    private final static Logger LOG = Logger.getLogger(HPSearcher.class.getName()); 
    public static int LIMIT_MAX = 50;
    
    private String luceneDir = null;

    private LuceneIndexSearcher<Pathogen> pathogenLis = null;
    private LuceneIndexSearcher<Host> hostLis = null;
    private LuceneIndexSearcher<HostPathogen> hostPathogenLis = null;
    private LuceneIndexSearcher<Reference> referenceLis = null;

    public static final Searcher newSearcher(final Properties p) throws InitializationException{
	if (p==null){
	    throw new InitializationException("Properties are null");
	}
	Searcher searcher = null;

	if(p.containsKey(MOCK_PROPERTY)){
	    searcher = new HPSearcherMock();
	}else{
	    searcher = new HPSearcher();
	}
	return searcher.init(p);
    }


    private HPSearcher(){

    }


    /**
     * Initialize the Searcher. Must be run before any other methods.
     *
     * @param p Initialization properties (like Search.MOCK_PROPERTY)
     * @return Searcher
     * @throws InitializationException <<Description>>
     */
    public Searcher init(Properties prop) throws InitializationException{
	if(!prop.containsKey(LUCENE_INDICES_BASE_DIR)){
	    throw new InitializationException("Missing Searcher.LUCENE_INDICES_BASE_DIR property for location of Lucene indices");
	}
	luceneDir = prop.getProperty(LUCENE_INDICES_BASE_DIR);
	LOG.info("Lucene directory=" + luceneDir);

	String errorString = Util.existsIsDirIsReadable(luceneDir);
	if(errorString != null){
	    throw new InitializationException(errorString);
	}

	pathogenLis = new LuceneIndexSearcher<Pathogen>();
	pathogenLis.init(luceneDir + "/" + INDEX_PATHOGEN, new PathogenPopulator<Pathogen>());

	hostLis = new LuceneIndexSearcher<Host>();
	hostLis.init(luceneDir + "/" + INDEX_HOST, new HostPopulator<Host>());

	referenceLis = new LuceneIndexSearcher<Reference>();
	referenceLis.init(luceneDir + "/" + INDEX_REFERENCE, new ReferencePopulator<Reference>());

	hostPathogenLis = new LuceneIndexSearcher<HostPathogen>();
	hostPathogenLis.init(luceneDir + "/" + INDEX_HOST_PATHOGEN, new HostPathogenPopulator<HostPathogen>());

	return this;
    }


    private void initializeAllowableSearchFields(){
	pathogenSearchFields = Util.strings2Set(pathogenSearchFieldsList);
	hostSearchFields = Util.strings2Set(hostSearchFieldsList);
    }

    //////////// Pathogens  /////////////////

    Set<String> pathogenSearchFields;

    public static String[] pathogenSearchFieldsList = {
	FUNGAL_STATE,
	PATHOGEN_AUTHOR,
	PATHOGEN_GENUS,
	PATHOGEN_SPECIES,
	PATHOGEN_SUBSPECIFIC_TAXA,
	PK_PATHOGEN_ID,
	VIRUS_MPLO_NAMES,
    };


    public List<Pathogen>getPathogens(final List<Long> ids) throws IllegalArgumentException, IndexFailureException{
	Util.checkIds(ids);
	return pathogenLis.get(ids);
    }



    public List<Long>getAllPathogens(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	Util.checkOffsetAndLimit(offset, limit );
	return pathogenLis.getAll(offset, limit);
    }

    public long getAllPathogensCount() throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	return pathogenLis.countAll();
    }

    

    public List<Long>searchPathogens(Map<String,List<String>>queryParameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	Util.checkQueryParameters(queryParameters, pathogenSearchFields);
	Util.checkOffsetAndLimit(offset, limit);
	return pathogenLis.search(queryParameters, offset, limit);
    }

    public long searchPathogensCount(Map<String,List<String>>queryParameters) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	Util.checkQueryParameters(queryParameters, pathogenSearchFields);
	return pathogenLis.countSearch(queryParameters);
    }


    ///// Hosts////////////
    Set<String> hostSearchFields;
    public static String[] hostSearchFieldsList = {
	CULTIVAR,
	HOST_AUTHOR,
	HOST_GENUS,
	HOST_SPECIES,
    };
    public List<Host>getHosts(List<Long> ids) throws IllegalArgumentException, IndexFailureException{
	Util.checkIds(ids);
	LOG.info("Getting hosts by id: " + ids);
	return hostLis.get(ids);
    }



    public List<Long>getAllHosts(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	Util.checkOffsetAndLimit(offset, limit);
	return hostLis.getAll(offset, limit);
    }

    public long getAllHostsCount() throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	return hostLis.countAll();
    }


    public long searchHostsCount(Map<String,List<String>>queryParameters) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	Util.checkQueryParameters(queryParameters, hostSearchFields);
	return hostLis.countSearch(queryParameters);
    }


    // country=x*
    // provState=y
    // enumerate=true
    // hostFamily=foobar
    // hostGenus=foobar
    // hostSpecies=foobar    
    // pathogenVirus=x
    // pathogenGenus=x
    // pathogenSpecies=x
    public List<Long>searchHosts(final Map<String,List<String>>queryParameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	Util.checkOffsetAndLimit(offset, limit);
	Util.checkQueryParameters(queryParameters, hostSearchFields);
	return hostLis.search(queryParameters, offset, limit);
    }


    // Host-Pathogen
    public List<Long>getPathogenByHost(long hostId, final long offset, final int limit) throws IllegalArgumentException, IndexFailureException, IllegalOffsetLimitException{
	Util.checkId(hostId);
	Map<String, List<String>> query = UtilLucene.makeIdQueryMap(FK_HOST_ID, hostId);
	return hostPathogenLis.search(query, offset, limit);
    }

    public List<Long>getHostByPathogen(long pathogenId, final long offset, final int limit) throws IllegalArgumentException, IndexFailureException, IllegalOffsetLimitException{
	Util.checkId(pathogenId);
	Map<String, List<String>> query = UtilLucene.makeIdQueryMap(FK_PATHOGEN_ID, pathogenId);
	return hostPathogenLis.search(query, offset, limit);
    }



}
