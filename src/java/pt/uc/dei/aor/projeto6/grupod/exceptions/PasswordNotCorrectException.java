

package pt.uc.dei.aor.projeto6.grupod.exceptions;


public class PasswordNotCorrectException extends Exception {
    
    
    public PasswordNotCorrectException(){
        super("The password is not correct");
    }
}
