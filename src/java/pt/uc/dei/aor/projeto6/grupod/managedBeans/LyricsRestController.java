package pt.uc.dei.aor.projeto6.grupod.managedBeans;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import pt.uc.dei.aor.projeto6.grupod.ejb.UserLogedEJB;
import pt.uc.dei.aor.projeto6.grupod.entities.Lyric;
import pt.uc.dei.aor.projeto6.grupod.entities.Music;
import pt.uc.dei.aor.projeto6.grupod.exceptions.CreateLyricException;
import pt.uc.dei.aor.projeto6.grupod.facades.LyricFacade;
import pt.uc.dei.aor.projeto6.grupod.facades.MusicFacade;

@Named
@ViewScoped
public class LyricsRestController {

    @Inject
    private UserLogedEJB userLoged;

    @Inject
    private LyricFacade lyricFacade;
    
    @Inject
    private MusicFacade musicFacade;

    private Music music;
    private Lyric lyric;
    private String lyricFromDB;

    public Lyric getLyric() {
        return lyric;
    }

    public void setLyric(Lyric lyric) {
        this.lyric = lyric;
    }

    public String getLyricFromDB() {

        return lyricFromDB;
    }

    public void setLyricFromDB(String lyricFromDB) {
        this.lyricFromDB = lyricFromDB;
    }

    public void findlyric(Music m) {

        Client client = ClientBuilder.newClient();
        String server = client.target("http://lyrics.wikia.com/api.php")
                .queryParam("artist", m.getArtist())
                .queryParam("song", m.getTitle())
                .queryParam("func", "getSong")
                .queryParam("fmt", "text")
                .request(MediaType.TEXT_PLAIN)
                .get(String.class);
        if (!server.equals("Not found")) {
            lyric = new Lyric();
            m.setLyricOriginal(server);
            musicFacade.edit(m);
            

            if (haveLyricEdited(m)) {
                Lyric l = lyricFacade.findLyricBySongIdAndUserId(m, userLoged.getUser());
                l.setLyricText(server);
                lyricFacade.edit(l);
            } else {
                
            }
        } else {
            lyricFromDB = "Lyric Not Found";
        }

    }

    public void createLyricVersion(Music m) {
        lyric.setLyricText(lyricFromDB);
        lyric.setMusic(m);
        lyric.setUser(userLoged.getUser());
        try {
            lyricFacade.createLyric(lyric, m, userLoged.getUser());
        } catch (CreateLyricException ex) {
            Logger.getLogger(LyricsRestController.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    public void showLyric(Music m) {
        if (haveLyricEdited(m)) {
            lyricFromDB = lyricFacade.findLyricBySongIdAndUserId(m, userLoged.getUser()).getLyricText();
            

        }
        else{
            lyricFromDB = m.getLyricOriginal();
        }
        music = m;
    }

    public void lyricGetAndShow(Music m) {
        lyricFromDB = "";
        findlyric(m);
        showOriginalLyric(m);

    }

    public boolean haveLyricEdited(Music m) {

        return (lyricFacade.findLyricBySongIdAndUserId(m, userLoged.getUser()) != null);

    }
    
    public boolean haveLyricOriginal(Music m){
        return(m.getLyricOriginal() != null);
    }
    
    
    public void showOriginalLyric(Music m){
        lyricFromDB = m.getLyricOriginal();
    }
    
    

    public void editLyric() {
        Lyric l = lyricFacade.findLyricBySongIdAndUserId(music, userLoged.getUser());
        
        if(l == null){
            createLyricVersion(music);
        }
        else{
            l.setLyricText(lyricFromDB);
            lyricFacade.edit(l);
        }
        

    }

}
