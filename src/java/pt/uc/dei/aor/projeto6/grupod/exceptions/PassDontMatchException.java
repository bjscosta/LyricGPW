

package pt.uc.dei.aor.projeto6.grupod.exceptions;


public class PassDontMatchException extends Exception {
    
    public PassDontMatchException(){
        super("The passwords dont match");
    }
    
}
