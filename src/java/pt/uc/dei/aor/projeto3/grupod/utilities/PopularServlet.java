package pt.uc.dei.aor.projeto3.grupod.utilities;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import static java.lang.System.out;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pt.uc.dei.aor.projeto3.grupod.entities.Music;
import pt.uc.dei.aor.projeto3.grupod.facades.MusicFacade;

@WebServlet(name = "popularServlet", urlPatterns = {"/popularServlet"})
public class PopularServlet extends HttpServlet {

    @Inject
    private MusicFacade musicFacade;

    private List<Music> popular;
    private List<Music> topTen;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try (PrintWriter out = response.getWriter()) {

            String top = request.getParameter("top");

            if (top.equals("todas")) {
                response.setContentType("application/json;charset=UTF-8");

                //musicFacade.setDadosJson(jsonData);
                //Lista de Musicas que quero mostrar
                //ejbMusic é um EJB que me faz a Logica de Negocio de "escolher" as Muscias
                List<Music> musicsList = musicFacade.findPopularMusics();
                //Criar Um Array Json para colocar todas as Musicas
                JsonArrayBuilder array = Json.createArrayBuilder();
                //Colocar a informacao que quero em JSON
                for (Music m : musicsList) {
                    array.add(Json.createObjectBuilder()
                            .add("Title", m.getTitle())
                            .add("Artist", m.getAlbum())
                            .add("Author", m.getAlbum())
                            .add("YearofRelease", m.getReleaseYEAR())
                            .add("TimesSelected", m.getTimesSelected()));
                }
                //Construir o array
                array.build();

                //Construir o Objecto JSON
                JsonObject obj = Json.createObjectBuilder()
                        .add("Music", array).build();
                /* Write JSON Output */
                StringWriter stWriter = new StringWriter();
                try (JsonWriter jsonWriter = Json.createWriter(stWriter)) {
                    jsonWriter.writeObject(obj);
                }
                String documentJson = stWriter.toString();
                /* Write formatted JSON Output */
                Map<String, String> config = new HashMap<>();
                config.put(JsonGenerator.PRETTY_PRINTING, "");
                JsonWriterFactory factory = Json.createWriterFactory(config);
                StringWriter stWriterF = new StringWriter();
                try (JsonWriter jsonWriterF = factory.createWriter(stWriterF)) {
                    jsonWriterF.writeObject(obj);
                }
                String documentJsonFormatted = stWriterF.toString();
                out.print(documentJsonFormatted);
            } else {
			
			response.setContentType("text/html;charset=UTF-8");

                topTen = musicFacade.TopTen();

                out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\"><table>");

                out.println("<table>");
                out.println("<thead>");
                out.println("<tr>");
                out.println("<th>Title</th>");
                out.println("<th>Artist</th>");
                out.println("<th>Times Selected</th>");
                out.println("</tr>");
                out.println("</thead>");

                for (Music m : topTen) {

                    out.println("<tr>");
                    out.println("<td>" + m.getTitle() + "</td>");
                    out.println("<td>" + m.getArtist() + "</td>");
                    out.println("<td>" + m.getTimesSelected() + "</td>");
                    out.println("</tr>");
                }
                out.println("</table></html>");

            }

        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);

        popular = musicFacade.findPopularMusics();
        out.println("<table>");
        out.println("<thead>");
        out.println("<tr>");
        out.println("<th>Title</th>");
        out.println("<th>Artist</th>");
        out.println("<th>Album</th>");
        out.println("<th>Release Year</th>");
        out.println("<th>Times Selected</th>");
        out.println("</tr>");
        out.println("</thead>");

        for (Music m : popular) {
            out.println("<tr>");
            out.println("<td>" + m.getTitle() + "</td>");
            out.println("<td>" + m.getArtist() + "</td>");
            out.println("<td>" + m.getAlbum() + "</td>");
            out.println("<td>" + m.getReleaseYEAR() + "</td>");
            out.println("<td>" + m.getTimesSelected() + "</td>");
            out.println("</tr>");
        }

        out.println("</table>");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
