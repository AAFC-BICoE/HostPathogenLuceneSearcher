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

public class HPSearcher2<T> implements Searcher2<T>, LuceneFields{
    private final static Logger LOG = Logger.getLogger(HPSearcher2.class.getName()); 
    public final static int MAX_IDS = 50;

    private IndexSearcher searcher = null;
    private Analyzer analyzer = null;
    private Populator populator = null;

    public static int LIMIT_MAX = 50;
    
    private String luceneDir = null;
    private Class type;
    
    public HPSearcher2(Class type){
	this.type = type;
    }


    public void init(LuceneConfig lc) throws InitializationException{
	try{
	    Util.isNull(lc);
	    Util.isNull(lc.searcher);
	    Util.isNull(lc.analyzer);
	    Util.isNull(lc.populator);
	}catch(IllegalArgumentException e){
	    throw new InitializationException(e);
	}

	if (type != lc.populator.getProductClass()){
	    throw new InitializationException("Search type does not match Populator class: type=" + type.getName() + "  productClass=" + lc.populator.getProductClass().getName());
	}


	if(lc.populator.getDefaultSortFields() == null || lc.populator.getDefaultSortFields().size() == 0){
	    throw new InitializationException("Populator (" + lc.populator.getClass().getName() + ") has null or zero length default sort field");
	}

	if(lc.populator.getValidSortFieldSet() == null || lc.populator.getValidSortFieldSet().size() == 0){
	    throw new InitializationException("Populator has null or zero length default sort field map");
	}

	if(!Util.listInSet(lc.populator.getDefaultSortFields(), lc.populator.getValidSortFieldSet())){
	    throw new InitializationException("Default sort fields not in valid sort fields");
	}

	this.populator = lc.populator;
	this.searcher = lc.searcher;
	this.analyzer = lc.analyzer;
    }

    public List<Long>getAll(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	Util.checkOffsetAndLimit(offset, limit );
	return UtilLucene.topDocsToIds(UtilLucene.all(populator.getRecordType(), populator.getDefaultSortFields(), analyzer, searcher), searcher, populator.getPrimaryKeyField(), offset, limit);
    }

    @Override
    public List<Long>search(Map<String,List<String>>queryParameters, List<String> sortFields, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
	Util.checkQueryParameters(queryParameters, populator.getValidSearchFieldSet());
	Util.checkOffsetAndLimit(offset, limit);
	Util.checkOffsetAndLimit(offset, limit);
	Util.checkQueryParameters(queryParameters);
	
	List<String> recordType = new ArrayList<String>(1);
	recordType.add(populator.getRecordType());

	return UtilLucene.topDocsToIds(UtilLucene.runQuery(UtilLucene.buildQuery(queryParameters, populator.getRecordType()), populator.getDefaultSortFields(), analyzer, searcher), searcher, populator.getPrimaryKeyField(), offset, limit);

    }

    public List<T>get(final List<Long> ids) throws IllegalArgumentException, IndexFailureException{
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

    public T get(final Long id) throws IllegalArgumentException, IndexFailureException{
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

    public long getAllCount() throws IndexFailureException{
	return (long)UtilLucene.all(populator.getRecordType(), populator.getDefaultSortFields(), analyzer, searcher).scoreDocs.length;
    }

    public long searchCount(Map<String,List<String>>queryParameters) throws IllegalArgumentException, IndexFailureException{
	return UtilLucene.runQuery(UtilLucene.buildQuery(queryParameters, populator.getRecordType()), populator.getDefaultSortFields(), analyzer, searcher).totalHits;
    }

}
