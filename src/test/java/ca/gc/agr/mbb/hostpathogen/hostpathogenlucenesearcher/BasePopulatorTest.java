package ca.gc.agr.mbb.hostpathogen.hostpathogenlucenesearcher;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.Field;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.logging.Level;
import java.util.logging.Logger;

@RunWith(JUnit4.class)
public class BasePopulatorTest{
    private final static Logger LOGGER = Logger.getLogger("test"); 
    static Document doc = null;
    static final String fieldName1 = "f1";
    static final String fieldValue1String = "string1";
    static final String fieldName2 = "f2";
    static final long fieldValue2Long = 129877l;

    static{
	doc = new Document();
	doc.add(new StringField(fieldName1, fieldValue1String, Field.Store.YES));
	doc.add(new LongField(fieldName2, fieldValue2Long, Field.Store.YES));
    }


    // longValue
    @Test(expected=FailedPopulateException.class)
    public void nullDocLong(){
	BasePopulator.longValue(null, "fieldName", true);
    }

    // longValue
    @Test(expected=FailedPopulateException.class)
    public void nullFieldLong(){
	BasePopulator.longValue(doc, null, true);
    }

    // longValue
    @Test(expected=FailedPopulateException.class)
    public void zeroLengthFieldLong(){
	BasePopulator.longValue(doc, "", true);
    }

    



}
