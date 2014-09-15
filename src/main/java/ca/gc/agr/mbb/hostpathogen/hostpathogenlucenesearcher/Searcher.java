package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.List;

public interface Searcher{

     /**
     * Initialize the Searcher. Must be run before any other methods.
     *
     * @param p Initialization properties (like Search.MOCK_PROPERTY)
     * @return Searcher
     * @throws InitializationException <<Description>>
     */
    public Searcher init(Properties p) throws InitializationException;


    public List<Long>getPathogens(final List<Long> ids) throws IllegalArgumentException;
    public List<Long>getAllPathogens(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;
    public List<Long>searchPathogens(List<String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;



    //public List<Host>getHosts(List<Long> ids, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;
    public List<Long>getHosts(List<Long> ids) throws IllegalArgumentException;
    public List<Long>getAllHosts(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;
    public List<Long>searchHosts(List<String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;

    public static final String MOCK_PROPERTY = "mock";
    public static final String LUCENE_INDICES_BASE_DIR = "lucene_indices_base_dir";

    public static final int MAX_REQUESTED_IDS = 100;
    public static final int MAX_REQUESTED_OBJECTS = 20;
}
