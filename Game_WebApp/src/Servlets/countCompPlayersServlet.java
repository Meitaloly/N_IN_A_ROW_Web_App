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

public class countCompPlayersServlet extends HttpServlet {

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GameManager gameManager = utils.ServletUtils.getGameManager(getServletContext());
        UserManager userManager = utils.ServletUtils.getUserManager(getServletContext());
        Map<String, User> players = userManager.getUsers();
        String userNameFromParam = request.getParameter("userName");
        String gameNameFRomParam = request.getParameter("gameName");

        userNameFromParam = userNameFromParam.trim();
        gameNameFRomParam = gameNameFRomParam.trim();


        GameInList currGame = gameManager.getGameInListByName(gameNameFRomParam);
        User CurrUser = players.get(userNameFromParam);
        response.setContentType("text/html;charset=UTF-8");
        final PrintWriter out;
            if(CurrUser.getType().equals("Computer"))
            {
                if (currGame.incNumOfCompPlayers()) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    out = response.getWriter();
                    out.print("success");
                    out.close();
                }
                else{
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "only computer players");
                    out = response.getWriter();
                    out.print("error");
                    out.close();
                }
            }
            else
            {
                response.setStatus(HttpServletResponse.SC_OK);
                out = response.getWriter();
                out.print("success");
                out.close();
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

