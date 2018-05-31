package Utils;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class JsonExtensionChecks implements IParameterValidator {
    @Override
    public void validate(String name, String value) throws ParameterException {
        if (!value.endsWith(".json")){
            throw new ParameterException("Incorrect extension of JSON file. Need 'JSON' extension of JSON file");
        }
    }
}
