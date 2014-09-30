package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.IndexSearcher;

public class UtilLucene{

    public static final String makeLuceneQueryPair(final String key, final String value){
	return key + ":" + value;
    }


    public static final Map<String, List<String>> makeIdsQueryMap(final String fieldName, final List<Long> ids){
	Map<String, List<String>> queryMap = new HashMap<String, List<String>>();
	List<String> values = new ArrayList<String>();
	for(Long id: ids){
	    values.add(id.toString());
	}
	queryMap.put(fieldName, values);
	return queryMap;
    }

    public static final List<Long> topDocsToIds(final TopDocs td, final IndexSearcher searcher, final String primaryKeyField, final long offset, final int limit) throws IndexFailureException{
	List<Long> ids = new ArrayList<Long>();
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
		ids.add(new Long(doc.getValues(primaryKeyField)[0]));
	    }
	}
	return ids;
    }



}
