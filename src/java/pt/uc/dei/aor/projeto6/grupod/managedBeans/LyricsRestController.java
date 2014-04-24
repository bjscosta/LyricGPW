

package pt.uc.dei.aor.projeto6.grupod.managedBeans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import pt.uc.dei.aor.projeto6.grupod.entities.Lyric;



@Named
@RequestScoped
public class LyricsRestController {
    private Lyric lyric;
    
    public void findlyric(){
        
        Client client = ClientBuilder.newClient();
        String server = client.target("http://lyrics.wikia.com/api.php")
        
            .queryParam("artist", "Breaking_Benjamin")
            .queryParam("song", "Until_The_End")
            .queryParam("func", "getSong")
            .queryParam("fmt", "text")
                   
           .request(MediaType.TEXT_PLAIN)
           .get(String.class);
    
    }
    
}
