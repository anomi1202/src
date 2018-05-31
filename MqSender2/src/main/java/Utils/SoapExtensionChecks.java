package Utils;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class SoapExtensionChecks implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (!value.endsWith(".xml")){
            throw new ParameterException("Incorrect extension of SOAP file. Need 'XML' extension of SOAP file");
        }
    }
}
