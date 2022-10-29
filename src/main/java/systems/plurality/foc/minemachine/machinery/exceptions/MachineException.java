package systems.plurality.foc.minemachine.machinery.exceptions;

import systems.plurality.foc.minemachine.machinery.MachineErrors;

public class MachineException extends Exception {
    private MachineErrors errorType;

    public MachineException(MachineErrors errorType, String message) {
        super(message);
        this.errorType = errorType;
    }

    public MachineException(MachineErrors errorType, Throwable cause) {
        super(cause);
        this.errorType = errorType;
    }

    public MachineErrors getErrorType() {
        return errorType;
    }
}
