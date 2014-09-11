package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.util.Properties;
import java.util.List;

public class HPSearcher{
    public static int OFFSET_MAX = 50;

    public void init(Properties properties){

    }


    ///// Pathogens
    public List<Long>getPathogens(List<Long> ids, final long offset, final int limit) throws IllegalOffsetLimitException {
	Util.checkOffset(offset, limit);
	return null;
    }

    public List<Long>getAllPathogens(final long offset, final int limit) throws IllegalOffsetLimitException {
	Util.checkOffset(offset, limit);
	return null;
    }

    public List<Long>searchPathogens(List<String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException {
	Util.checkOffset(offset, limit);
	return null;
    }


    ///// Hosts
    public List<Long>getHosts(List<Long> ids, final long offset, final int limit) throws IllegalOffsetLimitException {
	Util.checkOffset(offset, limit);
	return null;
    }
    public List<Long>getAllHosts(final long offset, final int limit) throws IllegalOffsetLimitException {
	Util.checkOffset(offset, limit);
	return null;
    }

    public List<Long>searchHosts(List<String>queryPrameters, final long offset, final int limit) throws IllegalOffsetLimitException {
	Util.checkOffset(offset, limit);
	return null;
    }


    



}
