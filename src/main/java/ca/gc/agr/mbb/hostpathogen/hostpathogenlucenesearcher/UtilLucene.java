package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.Sort;

public class UtilLucene{
    private final static Logger LOG = Logger.getLogger(UtilLucene.class.getName()); 

    public static final String makeLuceneQueryPair(final String key, final String value){
	return key + ":" + value.toLowerCase();
    }

    public static final Map<String, List<String>> makeIdQueryMap(final String fieldName, final Long id){
	List<Long>ids = new ArrayList<Long>(1);
	ids.add(id);
	return makeIdsQueryMap(fieldName, ids);
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
		ids.add(new Long(doc.getValues(storedName(primaryKeyField))[0]));
	    }
	}
	return ids;
    }

    public static final String storedName(String name){
	return name + LuceneFields.STORED_SUFFIX;
    }


    protected final static TopDocs all(final List<String> sortFields, final IndexSearcher searcher) throws IndexFailureException{
	Query allQuery = new MatchAllDocsQuery();
	return runQuery(allQuery, sortFields, searcher);
    }

    protected final static String buildQuery(final Map<String,List<String>>queryParameters){
	if(queryParameters == null || queryParameters.size() == 0){
	    return "";
	}
	
	StringBuilder sb = new StringBuilder();
	boolean first = true;
	for(String key:queryParameters.keySet()){
	    if(key.equals(LuceneFields.SORT_FIELD)){
		continue;
	    }

	    if (first){
		first = false;
	    }else{
		sb.append(" ");
	    }
	    List<String>values = queryParameters.get(key);
	    for(String value: values){
		sb.append(UtilLucene.makeLuceneQueryPair(key, value));
		sb.append(" ");
	    }
	}
	LOG.info("Lucene QueryParemeters: " + queryParameters);
	LOG.info("Lucene Query: " + sb.toString());
	return sb.toString();
    }

    protected final static TopDocs runQuery(final Query query, final List<String> sortFields, final IndexSearcher searcher) throws IndexFailureException{
	Sort sort = new Sort();
	SortField[] sFields =new SortField[sortFields.size()];
	
	for(int i=0; i<sortFields.size(); i++){
	    sFields[i] = new SortField(sortFields.get(i), SortField.Type.STRING_VAL);
	}
	sort.setSort(sFields);
	try{
	    LOG.info("Lucene query run: " + query);
	    return searcher.search(query, Integer.MAX_VALUE, sort);
	}catch(java.io.IOException e){
	    e.printStackTrace();
	    throw new IndexFailureException(e);
	}
    }

    // Allow leadingWildCard by default
    protected final static TopDocs runQuery(final String queryString, final List<String> sortFields, final Analyzer analyzer, final IndexSearcher searcher) throws IndexFailureException{
	return runQuery(queryString, sortFields, analyzer, searcher, true);
    }
    
    protected final static TopDocs runQuery(final String queryString, final List<String> sortFields, final Analyzer analyzer, final IndexSearcher searcher, boolean allowLeadingWildcard) throws IndexFailureException{
	QueryParser queryParser = new QueryParser("tmp", analyzer); // not thread safe// "tmp" as there should be no unattached-to-field queries...
	queryParser.setAllowLeadingWildcard(allowLeadingWildcard);

	// see https://stackoverflow.com/questions/5527868/exact-phrase-search-using-lucene
	//queryParser.setDefaultOperator(QueryParser.Operator.AND);
	//queryParser.setPhraseSlop(0);
	Query query = null;
	try{
	    query = queryParser.parse(queryString);
	}catch(org.apache.lucene.queryparser.classic.ParseException e){
	    e.printStackTrace();
	    throw new IndexFailureException(e);
	}
	return UtilLucene.runQuery(query, sortFields, searcher);
    }



}
