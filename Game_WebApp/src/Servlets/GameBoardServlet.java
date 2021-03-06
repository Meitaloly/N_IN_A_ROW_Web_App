package Servlets;

import GamesManager.BoardAndWinners;
import GamesManager.GameInList;
import GamesManager.GameManager;
import com.google.gson.Gson;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GameBoardServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GameManager gameManager = ServletUtils.getGameManager(request.getServletContext());
        String gameName = request.getParameter("gameName");
        GameInList game = gameManager.getGameInListByName(gameName);
        List<String> winners = game.checkAnyPlayerWins();
        int [][] gameBoard = game.getCurrGameManager().getGameBoard().getBoard();
        BoardAndWinners res = new BoardAndWinners();
        res.setGameBoard(gameBoard);
        res.setWinners(winners);
        if(!winners.isEmpty())
        {
            game.setStatus("Not Active");
        }
        String json = new Gson().toJson(res);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

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
