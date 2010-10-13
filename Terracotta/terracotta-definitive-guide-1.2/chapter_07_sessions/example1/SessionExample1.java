import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class SessionExample1 extends HttpServlet {
    protected void service(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {
        res.setContentType("text/html;charset=UTF-8");
        PrintWriter out = res.getWriter();
        HttpSession session = req.getSession();
        Integer count = (Integer)session.getAttribute("count");
        if (null == count) {
            count = 0;
        }
        count = count + 1;
        session.setAttribute("count", count);
        try {
            out.println("<html>");
            out.println("<head><title>Session Example 1</title></head>");
            out.println("<body>");
            out.println("<p>Current count "+count+"</p>");
            out.println("</body>");
            out.println("</html>");
        } finally { 
            out.close();
        }
        if (count >= 10) {
            session.invalidate();
        }
    } 
}

