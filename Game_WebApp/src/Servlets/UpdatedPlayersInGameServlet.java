package Servlets;

import GamesManager.GameInList;
import GamesManager.GameManager;
import UserAuthentication.User;
import UserAuthentication.UserManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class UpdatedPlayersInGameServlet extends HttpServlet {

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GameManager gameManager = utils.ServletUtils.getGameManager(getServletContext());
        String gameName = request.getParameter("gameName");
        String action = request.getParameter("action");
        GameInList currGame = gameManager.getGameInListByName(gameName);
        response.setContentType("text/html;charset=UTF-8");
        final PrintWriter out;
        if(action.equals("add")){
            currGame.incNumOfSignedPlayers();
            if(currGame.isActive())
            {

                UserManager userManager = utils.ServletUtils.getUserManager(getServletContext());
                Map<String, User> players = userManager.getUsersOfCurrGame(gameName);
                System.out.println("create array in UpdatedPlayersInGameServlet ");
                currGame.addPlayersWithColors(players);
                response.setStatus(HttpServletResponse.SC_OK);
                out = response.getWriter();
                out.print("success");
                out.close();
            }
            else
            {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "game is not active");
                out = response.getWriter();
                out.print("error");
                out.close();
            }
        }
        else
        {
            currGame.decNumOfSignedPlayers();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "game is not active");
        }

        return;
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