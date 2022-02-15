package core;

import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

@LocalBean
@Stateless
public class Validation {

    public String validator(Object object) {

        StringBuffer sb = new StringBuffer("");
        Validator validator = new Validator();
        List<ConstraintViolation> violations = validator.validate(object);

        if (violations.size() > 0) {
            for (ConstraintViolation violation : violations) {
                sb.append(violation.getMessage()).append("\n");
            }
        }


        return sb.toString();
    }

}
