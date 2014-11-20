package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import ca.gc.agr.mbb.hostpathogen.nouns.Host;
import ca.gc.agr.mbb.hostpathogen.nouns.HostPathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Reference;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

public class HPSearcher<T> implements SearcherDao<T>, LuceneFields{
    private final static Logger LOG = Logger.getLogger(HPSearcher.class.getName()); 
    public final static int MAX_IDS = 50;

    private LuceneConfig luceneConfig = null;
    public static int LIMIT_MAX = 50;
    
    private String luceneDir = null;
    private boolean initted=false;

    private final ReentrantLock lock = new ReentrantLock();
    private Class genericClass;
    
    private HPSearcher(){

    }

    public HPSearcher(Class genericClass){
	this.genericClass = genericClass;
    }


    

    @Override
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

	    Util.checkPopulator(lc.populator, genericClass);

	    this.luceneConfig = lc;
	    initted = true;
	}catch(Exception e){
	    throw new InitializationException(e);
	}finally {
	    lock.unlock();
	}
    }

    private void checkInit() throws InitializationException{
	if(!initted){
	    throw new InitializationException("init() not called");
	}
    }

    @Override
    public List<Long>getAll(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException, InitializationException{
	checkInit();
	Util.checkOffsetAndLimit(offset, limit);
	return UtilLucene.runQueryForIds(null, offset, limit, luceneConfig);
    }

    @Override
    public List<Long>search(Map<String,List<String>>queryParameters, List<String> sortFields, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException,InitializationException{
	checkInit();
	Util.checkQueryParameters(queryParameters, luceneConfig.populator.getValidSearchFieldSet());
	Util.checkSortFields(sortFields, luceneConfig.populator.getValidSortFieldSet());
	Util.checkOffsetAndLimit(offset, limit);
	Util.checkOffsetAndLimit(offset, limit);
	
	return UtilLucene.runQueryForIds(queryParameters, offset, limit, luceneConfig);
    }

    @Override
    public List<T>get(final List<Long> ids) throws IllegalArgumentException, IndexFailureException,InitializationException{
	checkInit();
	Util.checkIds(ids);
	LOG.info("Getting by id: " + ids);
	Util.checkIds(ids);

	if(ids.size() > MAX_IDS){
	    throw new TooManyIdsException("Too many ids: " + ids.size() +"; larger than max=" + MAX_IDS);
	}

	String queryString = UtilLucene.buildQuery(UtilLucene.makeIdsQueryMap(luceneConfig.populator.getPrimaryKeyField(), ids), luceneConfig.populator.getRecordType());
	LOG.info("**** query=" + queryString);
	List<T>nouns = null;
	try{
	    TopDocs td = UtilLucene.runQuery(queryString, luceneConfig.populator.getDefaultSortFields(), luceneConfig.analyzer, luceneConfig.searcher, false);
	    LOG.info("**** Totalhits=" + td.totalHits);
	    nouns = new ArrayList<T>(td.totalHits);
	    for(ScoreDoc sd: td.scoreDocs){
		Document doc = luceneConfig.searcher.doc(sd.doc);
		nouns.add((T)luceneConfig.populator.populate(doc));
		LOG.info(sd.doc + ":" + doc);
	    }
	}catch(Exception e){
	    e.printStackTrace();
	    throw new IndexFailureException(e);
	}
	return nouns;
    }

    @Override
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

    @Override
    public long getAllCount() throws IndexFailureException,InitializationException{
	checkInit();
	return (long)UtilLucene.all(luceneConfig.populator.getRecordType(), luceneConfig.populator.getDefaultSortFields(), luceneConfig.analyzer, luceneConfig.searcher).scoreDocs.length;
    }

    @Override
    public long searchCount(Map<String,List<String>>queryParameters) throws IllegalArgumentException, IndexFailureException,InitializationException{
	checkInit();
	return UtilLucene.runQuery(UtilLucene.buildQuery(queryParameters, luceneConfig.populator.getRecordType()), luceneConfig.populator.getDefaultSortFields(), luceneConfig.analyzer, luceneConfig.searcher).totalHits;
    }

    @Override
    public List<Long> getBy(Class type, long foreignKeyId, final long offset, final int limit) throws IllegalArgumentException, IndexFailureException, IllegalOffsetLimitException,InitializationException{
	checkInit();
	Util.checkOffsetAndLimit(offset, limit);
	Util.isNull(type);

	if(!luceneConfig.populator.isValidRelation(type)){
	    throw new IllegalArgumentException("Not valid relation class: " + type.getName());
	}

	Map<String,List<String>>queryParameters = new HashMap<String,List<String>>();
	List<String> fieldQuery = new ArrayList<String>();
	fieldQuery.add(Long.toString(foreignKeyId));

	queryParameters.put(luceneConfig.populator.getRelationField(type), fieldQuery);

	return UtilLucene.runQueryForIds(queryParameters, offset, limit, luceneConfig);
    }

    // This is just temp: supposed to read this from Lucene index
    @Override
    public Date getTimeStamp() throws IndexFailureException{
	Calendar cal = Calendar.getInstance();
	cal.roll(Calendar.MONTH, -1);
	return cal.getTime();
    }

}

