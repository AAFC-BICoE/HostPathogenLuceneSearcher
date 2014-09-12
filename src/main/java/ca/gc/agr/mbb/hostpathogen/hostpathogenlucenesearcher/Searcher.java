package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.List;

public interface Searcher{
    public Searcher init(Properties p) throws InitializationException;

    //public List<Pathogen>getPathogens(final List<Long> ids, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;
    public List<Long>getPathogens(final List<Long> ids, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;
    public List<Long>getAllPathogens(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;
    public List<Long>searchPathogens(List<String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;



    //public List<Host>getHosts(List<Long> ids, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;
    public List<Long>getHosts(List<Long> ids, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;
    public List<Long>getAllHosts(final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;
    public List<Long>searchHosts(List<String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException, IllegalArgumentException;

    public static final String OFFSET_MAX_PROPERTY = "offsetMax";
    public static final String MOCK_PROPERTY = "mock";
}
