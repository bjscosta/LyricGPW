
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

@Named
@ViewScoped
public class LyricsRestController {

    @Inject
    private UserLogedEJB userLoged;

    @Inject
    private LyricFacade lyricFacade;

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

            if (haveLyric(m)) {
                Lyric l = lyricFacade.findLyricBySongIdAndUserId(m, userLoged.getUser());
                l.setLyricText(server);
                lyricFacade.edit(l);
            } else {
                lyric.setLyricText(server);
                lyric.setMusic(m);
                lyric.setUser(userLoged.getUser());
                try {
                    lyricFacade.createLyric(lyric, m, userLoged.getUser());
                } catch (CreateLyricException ex) {
                    Logger.getLogger(LyricsRestController.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("o bruno é.....");

                }
            }
        } else {
            lyricFromDB = "Lyric Not Found";
        }

    }

    public void showLyric(Music m) {
        if (haveLyric(m)) {
            lyricFromDB = lyricFacade.findLyricBySongIdAndUserId(m, userLoged.getUser()).getLyricText();
            music = m;

        }
    }

    public void lyricGetAndShow(Music m) {
        lyricFromDB = "";
        findlyric(m);
        showLyric(m);

    }

    public boolean haveLyric(Music m) {

        return (lyricFacade.findLyricBySongIdAndUserId(m, userLoged.getUser()) != null);

    }

    public void editLyric() {
        Lyric l = lyricFacade.findLyricBySongIdAndUserId(music, userLoged.getUser());
        l.setLyricText(lyricFromDB);
        lyricFacade.edit(l);

    }

}
