package Servlets;

import GamesManager.GameInList;
import GamesManager.GameManager;
import GamesManager.gameBoardInfo;
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

public class isActiveGameServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        GameManager gameManager = ServletUtils.getGameManager(request.getServletContext());
        UserManager userManager = ServletUtils.getUserManager(request.getServletContext());
        String gameName = request.getParameter("gameName");
        GameInList game = gameManager.getGameInListByName(gameName);
        boolean active = game.isActive();
        gameBoardInfo res = new gameBoardInfo();
        res.setActive(active);
        res.setGameType(game.getVariant());
        if(active)
        {
            int[][] gameBoard = game.getCurrGameManager().getGameBoard().getBoard();
            res.setGameBoard(gameBoard);
        }

        String json = new Gson().toJson(res);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
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
