package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ca.gc.agr.mbb.hostpathogen.nouns.Pathogen;


@RunWith(JUnit4.class)
public class HPSearcher_PathogenTest implements HPSearch{
    private final static Logger LOG = Logger.getLogger(HPSearcher_PathogenTest.class.getName()); 

    static LuceneConfig pathogenConfig;

    static{
	try{
	    pathogenConfig = UtilLucene.luceneConfig(LuceneFields.PATHOGEN_TYPE, UtilTest.goodProperties);
	}catch(InitializationException e){
	    e.printStackTrace();
	}
    }

   @Test
   public void shouldGetAllPathogens() throws InitializationException, IllegalOffsetLimitException, IllegalArgumentException, IndexFailureException{
       SearcherDao<Pathogen> hps = new HPSearcher<Pathogen>(Pathogen.class);
       hps.init(pathogenConfig);
       List<Long> all = hps.getAll(1,10);	   
       Assert.assertTrue(all != null && all.size() >0);
    }

}
