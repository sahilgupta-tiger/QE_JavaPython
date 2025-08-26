package listeners;

import helperfunctions.ExcelDataProvider;
import reports.ReportManager;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * To set the data provider, execute flag and priority to all the test cases
 *
 * @author gayathri
 */
public class TestNgIATListener extends ReportManager implements IAnnotationTransformer {

    @SuppressWarnings("rawtypes")
    @Override
    public void transform(ITestAnnotation annotation, Class cls, Constructor cnst, Method method) {

        try {
            annotation.setDataProviderClass(ExcelDataProvider.class);
            annotation.setDataProvider("getExcelData");
        } catch (NullPointerException e) {
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
