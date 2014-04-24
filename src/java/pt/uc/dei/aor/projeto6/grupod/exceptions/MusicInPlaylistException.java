

package pt.uc.dei.aor.projeto6.grupod.exceptions;


public class MusicInPlaylistException extends Exception{
    
    public MusicInPlaylistException(){
        super("This music is already inside the playlist");
    }
}
