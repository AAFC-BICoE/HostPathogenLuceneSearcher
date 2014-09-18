package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.List;
import java.io.File;

import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;

import org.apache.lucene.util.BytesRef;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.TopDocs;

public class LuceneIndexSearcher<T>{

    IndexSearcher searcher = null;

    public void init(final String indexDir) throws InitializationException{
	try{
	    searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexDir))));
	}catch(Throwable t){
	    throw new InitializationException(t);
	}
    }

    final static String pkField = "pk_pathogen_id";
    public List<T>get(final List<Long> ids){
	BooleanQuery query = new  BooleanQuery();

	for(Long id: ids){
	    Query longQuery = NumericRangeQuery.newLongRange(pkField, id, id, true,true);
	    BooleanClause bc = new BooleanClause(longQuery, BooleanClause.Occur.SHOULD);
	    query.add(bc);
	}

	try{
	    TopDocs td = searcher.search(query, 99999999);
	}catch(Exception e){
	    e.printStackTrace();
	}

	//BytesRef ref = new BytesRef();    
	//NumericUtils.longToPrefixCoded( 12L, 0, ref );

	// BooleanQuery q = new BooleanQuery();

	// for(String id: ids){
	//     TermQuery tq = new TermQuery(new Term(pkFields, id));
	//     BooleanClause bc = new BooleanClause(tq, 

	return null;
    }

    public List<Long>getAll(final long offset, final int limit){
	return null;
    }

    public List<Long>search(List<String>queryPrameters, final long offset, final int limit){
	return null;
    }
    


}




