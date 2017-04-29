

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Link
 */
public class Link extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Link() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		try {
			HttpSession session = request.getSession();

			User user = (User) session.getAttribute("user");
			
			System.out.println("------------- Link.doGet ------------- "+user.getUser());
			
			response.getWriter().print("<a href=\"UserList\">UserList</a><br>");
			response.getWriter().print("<a href=\"/simpleServlet/Connect\">Connect</a><br><br>");
			response.getWriter().print("<a href=\"/simpleServlet/LogOut\">LogOut</a>");
			response.getWriter().print("<hr/>");
			
		} catch (NullPointerException e) {
			// TODO: handle exception
			new Index().doGet(request, response);
		}
	}
}
