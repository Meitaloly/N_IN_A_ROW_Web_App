package Servlets;

import GamesManager.GameInList;
import GamesManager.GameManager;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PopOutDiskServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GameManager gameManager = utils.ServletUtils.getGameManager(request.getServletContext());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        String gameName = request.getParameter("gameName");
        String playerName = request.getParameter("playerName");
        Integer col = Integer.valueOf(request.getParameter("col"));
        GameInList game = gameManager.getGameInListByName(gameName);
        int [][] gameBoard = game.popOutDisk(playerName, col);
        game.setNextPlayerName();/////////////////////////////////////////////////////////////////////////
        String json = new Gson().toJson(gameBoard);
        response.getWriter().write(json);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
