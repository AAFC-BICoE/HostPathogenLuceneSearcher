package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.io.File;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;

public class LuceneIndexSearcher<T> implements LuceneFields{
    private final static Logger LOG = Logger.getLogger(LuceneIndexSearcher.class.getName()); 
    private final static int MAX_IDS = 50;

    private IndexSearcher searcher = null;
    private Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_10_0);
    private Populator populator = null;

    public void init(final String indexDir, final Populator populator) throws InitializationException{
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


    public List<T>get(final List<Long> ids)throws TooManyIdsException, IllegalArgumentException{
	Util.checkIds(ids);
	if(ids.size() > MAX_IDS){
	    throw new TooManyIdsException("Too many ids: " + ids.size() +"; larger than max=" + MAX_IDS);
	}

	QueryParser queryParser = new QueryParser("tmp", analyzer); // not thread safe
	TermQuery tq = null;
	StringBuilder sb = new StringBuilder(ids.size()*16);
	boolean first = true;
	for(Long id: ids){
	    if (first){
		first = false;
	    }else{
		sb.append(" ");
	    }
	    sb.append(populator.getPrimaryKeyField() + ":" + id);
	}

	Query query = null;
	try{
	    query = queryParser.parse(sb.toString());
	}catch(org.apache.lucene.queryparser.classic.ParseException p){
	    p.printStackTrace();
	    return null;
	}
	List<T>pathogens = null;
	try{
	    LOG.info("**** query=" + query);
	    TopDocs td = runQuery(query);
	    LOG.info("**** Totalhits=" + td.totalHits);
	    pathogens = new ArrayList<T>(td.totalHits);
	    for(ScoreDoc sd: td.scoreDocs){
		Document doc = searcher.doc(sd.doc);
		pathogens.add((T)populator.populate(doc));
		LOG.info(sd.doc + ":" + doc);
	    }
	}catch(Exception e){
	    e.printStackTrace();
	    throw new IllegalArgumentException(e);
	}
	return pathogens;
    }


    TopDocs runQuery(Query query) throws java.io.IOException{
	return searcher.search(query, MAX_IDS);
    }

    public List<Long>getAll(final long offset, final int limit) throws IndexFailureException{
	List<Long> ids = new ArrayList<Long>();
	Query query = new MatchAllDocsQuery();
	TopDocs td = null;
	try{
	    td = runQuery(query);
	}catch(Exception e){
	    e.printStackTrace();
	    throw new IndexFailureException(e);
	}
	ScoreDoc[] scoreDocs = td.scoreDocs;
	if (offset <= scoreDocs.length){
	    for(int i=(int)offset; i<limit && i<scoreDocs.length; i++){
		Document doc = null;
		try{
		    doc  = searcher.doc(scoreDocs[i].doc);
		}catch(java.io.IOException e){
		    e.printStackTrace();
		    throw new IndexFailureException(e);
		}
		String[] id = doc.getValues(populator.getPrimaryKeyField());
		//String value = doc.getValues(populator.getPrimaryKeyField())[0];
		ids.add(new Long(doc.getValues(populator.getPrimaryKeyField())[0]));
	    }
	}
	return ids;
    }

    public long countAll(){
	return 0l;
    }

    public long countSearch(Map<String,String>queryPrameters){
	return 0l;
    }


    public List<Long>search(Map<String,String>queryPrameters, final long offset, final int limit){
	return null;
    }
    


}




