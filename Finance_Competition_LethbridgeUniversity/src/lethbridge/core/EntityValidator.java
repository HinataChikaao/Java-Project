package lethbridge.core;


import util.ConstantText;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Stateless
@LocalBean
public class EntityValidator implements Serializable {

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static <T> String validate(T object) {
        StringBuilder sb = new StringBuilder();
        Set<String> checkConstraintMessages = new HashSet<>();

        Set<ConstraintViolation<T>> validateResult = validator.validate(object);
        sb.append(ConstantText.NO_MESSAGE);

        for(ConstraintViolation<T> check : validateResult){
            checkConstraintMessages.add(check.getMessage());
        }

        for (String msg : checkConstraintMessages) {
            sb.append(msg).append(", ");
        }
        if (validateResult.size() > 0) {
            sb.append("the end.");
        }
        return sb.toString();
    }
}
