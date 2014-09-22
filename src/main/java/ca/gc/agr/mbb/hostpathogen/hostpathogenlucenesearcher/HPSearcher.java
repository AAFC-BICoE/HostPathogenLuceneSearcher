package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.List;
import java.util.Map;

import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Host;


public class HPSearcher implements Searcher{
    public static int LIMIT_MAX = 50;
    
    private String luceneDir = null;

    private LuceneIndexSearcher<Pathogen> lis = null;

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

	String errorString = Util.existsIsDirIsReadable(luceneDir);
	if(errorString != null){
	    throw new InitializationException(errorString);
	}

	lis = new LuceneIndexSearcher<Pathogen>();
	lis.init("/home/newtong/work/HostPathogenLuceneSearcher/luceneIndexes/luceneIndex.pathogens", new PathogenPopulator<Pathogen>());

	return this;
    }


    ///// Pathogens

    public List<Pathogen>getPathogens(final List<Long> ids) throws IllegalArgumentException{
	Util.checkIds(ids);
	return lis.get(ids);
    }

    public List<Long>getAllPathogens(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffsetAndLimit(offset, limit);
	return null;
    }

    public List<Long>searchPathogens(Map<String,String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffsetAndLimit(offset, limit);
	return null;
    }


    ///// Hosts
    public List<Host>getHosts(List<Long> ids) throws IllegalArgumentException{
	Util.checkIds(ids);
	return null;
    }
    public List<Long>getAllHosts(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffsetAndLimit(offset, limit);
	return null;
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
    public List<Long>searchHosts(Map<String,String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffsetAndLimit(offset, limit);
	return null;
    }


    



}
