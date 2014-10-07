package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;


import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;

public class LuceneIndexSearcher<T> implements LuceneFields{
    private final static Logger LOG = Logger.getLogger(LuceneIndexSearcher.class.getName()); 
    private final static int MAX_IDS = 50;

    private IndexSearcher searcher = null;
    private Analyzer analyzer = null;
    private Populator populator = null;

    public void init(final String indexDir, final Populator populator) throws InitializationException{
	try{
	    Util.isNullOrZero(indexDir);
	    Util.isNull(populator);
	}catch(IllegalArgumentException e){
	    throw new InitializationException(e);
	}

	if(populator.getDefaultSortFields() == null || populator.getDefaultSortFields().size() == 0){
	    throw new InitializationException("Populator (" + populator.getClass().getName() + ") has null or zero length default sort field");
	}

	if(populator.getValidSortFieldSet() == null || populator.getValidSortFieldSet().size() == 0){
	    throw new InitializationException("Populator has null or zero length default sort field map");
	}

	if(!Util.listInSet(populator.getDefaultSortFields(), populator.getValidSortFieldSet())){
	    throw new InitializationException("Default sort fields not in valid sort fields");
	}

	this.populator = populator;
	LOG.info("Opening Lucene index for directory: " + indexDir);
	try{
	    IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(indexDir)));
	    analyzer = new StandardAnalyzer(Version.LUCENE_4_10_0); // thread safe
	    LOG.info("*** Num docs in " + indexDir + "=" + reader.maxDoc());
	    searcher = new IndexSearcher(reader);
	}catch(Throwable t){
	    throw new InitializationException(t);
	}
    }


    public List<T>get(final List<Long> ids)throws TooManyIdsException, IllegalArgumentException, IndexFailureException{
	Util.checkIds(ids);
	if(ids.size() > MAX_IDS){
	    throw new TooManyIdsException("Too many ids: " + ids.size() +"; larger than max=" + MAX_IDS);
	}

	String queryString = UtilLucene.buildQuery(UtilLucene.makeIdsQueryMap(populator.getPrimaryKeyField(), ids));
	List<T>pathogens = null;
	try{
	    LOG.info("**** query=" + queryString);
	    TopDocs td = UtilLucene.runQuery(queryString, populator.getDefaultSortFields(), analyzer, searcher, false);
	    LOG.info("**** Totalhits=" + td.totalHits);
	    pathogens = new ArrayList<T>(td.totalHits);
	    for(ScoreDoc sd: td.scoreDocs){
		Document doc = searcher.doc(sd.doc);
		pathogens.add((T)populator.populate(doc));
		LOG.info(sd.doc + ":" + doc);
	    }
	}catch(Exception e){
	    e.printStackTrace();
	    throw new IndexFailureException(e);
	}
	return pathogens;
    }



    public List<Long>getAll(final long offset, final int limit) throws IndexFailureException{
	return UtilLucene.topDocsToIds(UtilLucene.all(populator.getDefaultSortFields(), searcher), searcher, populator.getPrimaryKeyField(), offset, limit);
    }


    public long countAll()throws IndexFailureException{
	return (long)UtilLucene.all(populator.getDefaultSortFields(), searcher).scoreDocs.length;
    }

    public long countSearch(final Map<String,List<String>>queryParameters) throws IndexFailureException{
	Util.checkQueryParameters(queryParameters);
	//return (long)all().totalHits;
	return UtilLucene.runQuery(UtilLucene.buildQuery(queryParameters), populator.getDefaultSortFields(), analyzer, searcher).totalHits;
    }



    public List<Long> search(final Map<String,List<String>>queryParameters, final long offset, final int limit) throws IndexFailureException, IllegalOffsetLimitException, IllegalArgumentException{
	Util.checkOffsetAndLimit(offset, limit);
	Util.checkQueryParameters(queryParameters);
	return UtilLucene.topDocsToIds(UtilLucene.runQuery(UtilLucene.buildQuery(queryParameters), populator.getDefaultSortFields(), analyzer, searcher), searcher, populator.getPrimaryKeyField(), offset, limit);
    }




    


}




