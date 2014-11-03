package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.lucene.search.IndexSearcher;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Host;
import ca.gc.agr.mbb.hostpathogen.nouns.HostPathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Reference;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.document.Document;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class HPSearcher<T> implements Searcher<T>, LuceneFields{
    private final static Logger LOG = Logger.getLogger(HPSearcher.class.getName()); 
    public final static int MAX_IDS = 50;

    private IndexSearcher searcher = null;
    private Analyzer analyzer = null;
    private Populator populator = null;

    public static int LIMIT_MAX = 50;
    
    private String luceneDir = null;
    private Class type;

    private boolean initted=false;

    private final ReentrantLock lock = new ReentrantLock();
    
    public HPSearcher(Class type){
	this.type = type;
    }


    public void init(LuceneConfig lc) throws InitializationException{
	lock.lock();
	try{
	    if (initted == true){
		throw new InitializationException("init() already called!");
	    }

	    try{
		Util.isNull(lc);
		Util.isNull(lc.searcher);
		Util.isNull(lc.analyzer);
		Util.isNull(lc.populator);
	    }catch(IllegalArgumentException e){
		throw new InitializationException(e);
	    }

	    Util.checkPopulator(lc.populator, type);

	    this.populator = lc.populator;
	    this.searcher = lc.searcher;
	    this.analyzer = lc.analyzer;
	    initted = true;
	}
	finally {
	    lock.unlock();
	}
    }

    public void checkInit() throws InitializationException{
	if(!initted){
	    throw new InitializationException("init() not called");
	}
    }

    public List<Long>getAll(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException, InitializationException{
	checkInit();
	Util.checkOffsetAndLimit(offset, limit );
	return UtilLucene.topDocsToIds(UtilLucene.all(populator.getRecordType(), populator.getDefaultSortFields(), analyzer, searcher), searcher, populator.getPrimaryKeyField(), offset, limit);
    }

    @Override
	public List<Long>search(Map<String,List<String>>queryParameters, List<String> sortFields, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException,InitializationException{
	checkInit();
	Util.checkQueryParameters(queryParameters, populator.getValidSearchFieldSet());
	Util.checkSortFields(sortFields, populator.getValidSortFieldSet());
	Util.checkOffsetAndLimit(offset, limit);
	Util.checkOffsetAndLimit(offset, limit);
	
	List<String> recordType = new ArrayList<String>(1);
	recordType.add(populator.getRecordType());

	return UtilLucene.topDocsToIds(UtilLucene.runQuery(UtilLucene.buildQuery(queryParameters, populator.getRecordType()), populator.getDefaultSortFields(), analyzer, searcher), searcher, populator.getPrimaryKeyField(), offset, limit);

    }

    public List<T>get(final List<Long> ids) throws IllegalArgumentException, IndexFailureException,InitializationException{
	checkInit();
	Util.checkIds(ids);
	LOG.info("Getting by id: " + ids);
	Util.checkIds(ids);

	if(ids.size() > MAX_IDS){
	    throw new TooManyIdsException("Too many ids: " + ids.size() +"; larger than max=" + MAX_IDS);
	}

	String queryString = UtilLucene.buildQuery(UtilLucene.makeIdsQueryMap(populator.getPrimaryKeyField(), ids), populator.getRecordType());
	LOG.info("**** query=" + queryString);
	List<T>nouns = null;
	try{
	    TopDocs td = UtilLucene.runQuery(queryString, populator.getDefaultSortFields(), analyzer, searcher, false);
	    LOG.info("**** Totalhits=" + td.totalHits);
	    nouns = new ArrayList<T>(td.totalHits);
	    for(ScoreDoc sd: td.scoreDocs){
		Document doc = searcher.doc(sd.doc);
		nouns.add((T)populator.populate(doc));
		LOG.info(sd.doc + ":" + doc);
	    }
	}catch(Exception e){
	    e.printStackTrace();
	    throw new IndexFailureException(e);
	}
	return nouns;
    }

    public T get(final Long id) throws IllegalArgumentException, IndexFailureException,InitializationException{
	checkInit();
	List<Long>ids = new ArrayList<Long>(1);
	ids.add(id);
	List<T> listOfNouns = get(ids);
	if(listOfNouns == null || listOfNouns.size() == 0){
	    return null;
	}
	if(listOfNouns.size() != 1){
	    throw new IndexFailureException("Finding multiple items: should only be finding one for a unique key");
	}
	return listOfNouns.get(0);
    }

    public long getAllCount() throws IndexFailureException,InitializationException{
	checkInit();
	return (long)UtilLucene.all(populator.getRecordType(), populator.getDefaultSortFields(), analyzer, searcher).scoreDocs.length;
    }

    public long searchCount(Map<String,List<String>>queryParameters) throws IllegalArgumentException, IndexFailureException,InitializationException{
	checkInit();
	return UtilLucene.runQuery(UtilLucene.buildQuery(queryParameters, populator.getRecordType()), populator.getDefaultSortFields(), analyzer, searcher).totalHits;
    }

}
