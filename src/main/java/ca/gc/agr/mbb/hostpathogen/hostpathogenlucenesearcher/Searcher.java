package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;


import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;
import ca.gc.agr.mbb.hostpathogen.nouns.Host;
import ca.gc.agr.mbb.hostpathogen.nouns.HostPathogen;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public interface Searcher{

     /**
     * Initialize the Searcher. Must be run before any other methods.
     *
     * @param p Initialization properties (like Search.MOCK_PROPERTY)
     * @return Searcher
     * @throws InitializationException <<Description>>
     */
    public Searcher init(Properties p) throws InitializationException;

    // PATHOGEN
    public List<Long>getAllPathogens(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException;
    public List<Long>searchPathogens(Map<String,List<String>>queryParameters, List<String> sortFields, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException;
    public List<Pathogen>getPathogens(final List<Long> ids) throws IllegalArgumentException, IndexFailureException;
    public Pathogen getPathogen(final Long id) throws IllegalArgumentException, IndexFailureException;
    public long getAllPathogensCount() throws IndexFailureException;
    public long searchPathogensCount(Map<String,List<String>>queryParameters) throws IllegalArgumentException, IndexFailureException;


    // HOST
    public List<Host>getHosts(List<Long> ids) throws IllegalArgumentException, IndexFailureException;
    public List<Long>getAllHosts(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException;
    public List<Long>searchHosts(Map<String,List<String>>queryPrameters, List<String> sortFields, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException;
    public long getAllHostsCount() throws IndexFailureException;
    public long searchHostsCount(Map<String,List<String>>queryParameters) throws IllegalArgumentException, IndexFailureException;


    
    // HOST-PATHOGEN
    public List<Long>getAllHostPathogens(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException;
    public long getAllHostPathogensCount() throws IndexFailureException;
    public List<Long>searchHostPathogens(Map<String,List<String>>queryParameters, List<String> sortFields, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException;
    public long searchHostPathogensCount(Map<String,List<String>>queryParameters) throws IllegalArgumentException, IndexFailureException;
    public List<HostPathogen>getHostPathogens(final List<Long> ids) throws IllegalArgumentException, IndexFailureException;
    public HostPathogen getHostPathogen(final Long id) throws IllegalArgumentException, IndexFailureException;
    // // Relations
    public List<Long>getHostByPathogen(long pathogenId, final long offset, final int limit) throws IllegalArgumentException, IndexFailureException, IllegalOffsetLimitException;
    public List<Long>getPathogenByHost(long hostId, final long offset, final int limit) throws IllegalArgumentException, IndexFailureException, IllegalOffsetLimitException;
    public List<Long>getLocationsByHostPathogen(long hostPathogenId, final long offset, final int limit) throws IllegalArgumentException, IndexFailureException, IllegalOffsetLimitException;


    // Reference


    // CONSTANTS1
    public static final String MOCK_PROPERTY = "mock";
    public static final String LUCENE_INDICES_BASE_DIR = "lucene_indices_base_dir";

    public static final int MAX_REQUESTED_IDS = 100;
    public static final int MAX_REQUESTED_OBJECTS = 20;
}
