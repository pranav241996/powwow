/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.questionanswercontroller;

import dao.questionanswerdao.QuestionDao;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.StudentModel;
import model.questionanswermodel.QuestionModel;
import model.questionanswermodel.RelatedQuestionModel;

/**
 *
 * @author Lenovo
 */
public class ReportQuestion extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
           
            HttpSession session=request.getSession();
           ServletContext context=getServletContext();
           
          int qid=Integer.parseInt(request.getParameter("qid"));
          String type=request.getParameter("type");
          int index=Integer.parseInt(request.getParameter("index"));
          
          QuestionModel qm=new QuestionModel();
          qm.setQid(qid);
          
          QuestionDao qd=new QuestionDao();
            
          
          if(type.equals("faculty"))
          {
            qd.deleteQuestion(qm,context); 
            RelatedQuestionModel rqm=(RelatedQuestionModel)session.getAttribute("relques");
            ArrayList<QuestionModel> alqm=rqm.getRelatedque();
            alqm.remove(index);
            rqm.setRelatedque(alqm);
            session.setAttribute("relques",rqm);
            out.println("DELETED...");
          }
          else if(type.equals("student"))
          {
            StudentModel sm=(StudentModel)session.getAttribute("userModel");
            int chk=qd.incReportAbuseCount(qm,context,sm.getCcode());
            if(chk==1)
                out.println("Reported Successfully..");
            else if(chk==0)
                out.println("Already Reported...");
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
