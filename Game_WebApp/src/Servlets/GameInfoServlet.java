package Servlets;

import GamesManager.GameInList;
import GamesManager.GameManager;
import UserAuthentication.User;
import UserAuthentication.UserManager;
import com.google.gson.Gson;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class GameInfoServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GameManager gameManager = ServletUtils.getGameManager(getServletContext());
        String gameName = request.getParameter("gameName");
        GameInList currGame = gameManager.getGameInListByName(gameName);
            if(currGame.isActive() && !currGame.isArrayByOrder())
            {
                UserManager userManager = utils.ServletUtils.getUserManager(getServletContext());
                Map<String, User> players = userManager.getUsersOfCurrGame(gameName);
                System.out.println("create array in GameInfoServlet");
                currGame.addPlayersWithColors(players);
            }
        String json = new Gson().toJson(currGame);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
//
//    protected void processRequestGet(HttpServletRequest request, HttpServletResponse response) {
//
//    }


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
