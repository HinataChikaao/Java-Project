package lethbridge.core;


import util.R;

import javax.ejb.LocalBean;
import javax.ejb.Stateful;
import java.io.Serializable;

public class ExceptionPack implements Serializable {

    private String className;
    private String methodName;
    private String errorMessage;
    private String trace;

    {
        className = R.PublicText.EMPTY;
        methodName = R.PublicText.EMPTY;
        errorMessage = R.PublicText.EMPTY;
        trace = R.PublicText.EMPTY;
    }

    public ExceptionPack() {
    }

    public ExceptionPack(String className,
                         String methodName,
                         String errorMessage,
                         String trace) {
        this.className = className;
        this.methodName = methodName;
        this.errorMessage = errorMessage;
        this.trace = trace;
    }

    public ExceptionPack(String className, String methodName, String errorMessage) {
        this.className = className;
        this.methodName = methodName;
        this.errorMessage = errorMessage;
    }

    public ExceptionPack(String className, String methodName) {
        this.className = className;
        this.methodName = methodName;
    }

    public ExceptionPack(String className) {
        this.className = className;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public boolean hasErrorMessage() {
        return this.errorMessage.trim().length() > 0;
    }

    public boolean hasTrace() {
        return this.trace.trim().length() > 0;
    }

    public boolean hasMethodName() {
        return this.methodName.trim().length() > 0;
    }

    public boolean hasClassName() {
        return this.className.trim().length() > 0;
    }

    @Override
    public String toString() {
        return "className: '" + className + '\'' +
                ", methodName: '" + methodName + '\'' +
                ", errorMessage: '" + errorMessage + '\'' +
                ", trace: '" + trace + '\'';
    }
}
