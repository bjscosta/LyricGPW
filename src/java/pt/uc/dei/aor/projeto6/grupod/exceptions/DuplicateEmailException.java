

package pt.uc.dei.aor.projeto6.grupod.exceptions;


public class DuplicateEmailException extends Exception {
    
    public DuplicateEmailException(){
        super("E-mail already taken");
    }
}
