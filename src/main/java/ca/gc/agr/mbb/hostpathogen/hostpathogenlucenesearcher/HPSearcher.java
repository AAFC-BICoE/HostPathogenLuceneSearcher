package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Host;
import ca.gc.agr.mbb.hostpathogen.nouns.Reference;


public class HPSearcher implements Searcher{
    private final static Logger LOG = Logger.getLogger(HPSearcher.class.getName()); 
    public static int LIMIT_MAX = 50;
    
    private String luceneDir = null;

    private LuceneIndexSearcher<Pathogen> pathogenLis = null;
    private LuceneIndexSearcher<Host> hostLis = null;
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
	    throw new InitializationException("Missing LUCENE_INDICES_BASE_DIR property for location of Lucene indices");
	}
	luceneDir = prop.getProperty(LUCENE_INDICES_BASE_DIR);
	LOG.info("Lucene directory=" + luceneDir);

	String errorString = Util.existsIsDirIsReadable(luceneDir);
	if(errorString != null){
	    throw new InitializationException(errorString);
	}

	pathogenLis = new LuceneIndexSearcher<Pathogen>();
	pathogenLis.init(luceneDir + "/luceneIndex.pathogens", new PathogenPopulator<Pathogen>());
	hostLis = new LuceneIndexSearcher<Host>();
	hostLis.init(luceneDir + "/luceneIndex.hosts", new HostPopulator<Host>());
	referenceLis = new LuceneIndexSearcher<Reference>();
	referenceLis.init(luceneDir + "/luceneIndex.references", new ReferencePopulator<Reference>());
	return this;
    }


    ///// Pathogens

    public List<Pathogen>getPathogens(final List<Long> ids) throws IllegalArgumentException, IndexFailureException{
	Util.checkIds(ids);
	return pathogenLis.get(ids);
    }

    public List<Pathogen>getPathogenByHost(long hostId) throws IllegalArgumentException, IndexFailureException{
	if(true){
	    throw new NullPointerException();
	}
	return null;
    }

    public List<Long>getAllPathogens(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	Util.checkOffsetAndLimit(offset, limit);
	return pathogenLis.getAll(offset, limit);
    }

    public long getAllPathogensCount() throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	return pathogenLis.countAll();
    }

    public List<Long>searchPathogens(Map<String,List<String>>queryParameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	Util.checkOffsetAndLimit(offset, limit);
	return pathogenLis.search(queryParameters, offset, limit);
    }

    public long searchPathogensCount(Map<String,List<String>>queryParameters) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	return pathogenLis.countSearch(queryParameters);
    }

    public long searchHostsCount(Map<String,List<String>>queryParameters) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	return hostLis.countSearch(queryParameters);
    }


    ///// Hosts
    public List<Host>getHosts(List<Long> ids) throws IllegalArgumentException, IndexFailureException{
	Util.checkIds(ids);
	LOG.info("Getting hosts by id: " + ids);
	return hostLis.get(ids);
    }

    public List<Host>getHostByPathogen(long pathogenId) throws IllegalArgumentException, IndexFailureException{
	if(true){
	    throw new NullPointerException();
	}
	return null;
    }

    public List<Long>getAllHosts(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	Util.checkOffsetAndLimit(offset, limit);
	return hostLis.getAll(offset, limit);
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
	return hostLis.search(queryParameters, offset, limit);
    }


    



}
