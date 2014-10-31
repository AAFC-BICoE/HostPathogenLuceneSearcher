package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;


import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Host;
import ca.gc.agr.mbb.hostpathogen.nouns.HostPathogen;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface Searcher2<T>{

     /**
     * Initialize the Searcher. Must be run before any other methods.
     *
     * @param p Initialization properties (like Search.MOCK_PROPERTY)
     * @return Searcher
     * @throws InitializationException <<Description>>
     */
    public void init(LuceneConfig lc) throws InitializationException;

    public List<Long>getAll(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException;
    public List<Long>search(Map<String,List<String>>queryParameters, List<String> sortFields, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException;
    public List<T>get(final List<Long> ids) throws IllegalArgumentException, IndexFailureException;
    public T get(final Long id) throws IllegalArgumentException, IndexFailureException;
    public long getAllCount() throws IndexFailureException;
    public long searchCount(Map<String,List<String>>queryParameters) throws IllegalArgumentException, IndexFailureException;

    public static final String LUCENE_INDICES_BASE_DIR = "lucene_indices_base_dir";

    public static final int MAX_REQUESTED_IDS = 100;
    public static final int MAX_REQUESTED_OBJECTS = 20;
}
