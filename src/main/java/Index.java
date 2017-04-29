import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

import javassist.expr.NewArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Index
 */

public class Index extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Index() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("------------------- Index.doGet -----------------");
		HttpSession session = request.getSession();

		try {
			User user = (User) session.getAttribute("user");
			String state = user.getUser();

			if (state.equals("online")) {
				System.out.println("------------------ online -------------- ");

				Link link = new Link();
				link.doGet(request, response);
			} else if (state.equals("onChat")) {
				System.out.println("------------------ onchat -------------- ");

				Chat chat = new Chat();
				chat.doGet(request, response);
			} else {

				String html = createHtmlFile();
				response.getWriter().print(html);

			}
		} catch (NullPointerException e) {
			String html = createHtmlFile();
			response.getWriter().print(html);

		}

	}

	private String createHtmlFile() {
		String res = "";
		res += "<!DOCTYPE html> <html lang=\"en\"> <head> <title>Register</title> </head> <body> <body>	"
				+ "<form action=\"/simpleServlet/\" method=\"POST\">"
				+ "<input type=\"submit\" name=\"Login\" value=\"Login\">"
				+ "<input type=\"submit\" name=\"Register\" value=\"Register\">"
				+ "	</form>"

				+ "</body>" + "</html>";
		return res;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("------------- Index.doPost -------------");
		Enumeration e = request.getParameterNames();
		String res = "";
		ArrayList<String> arr = new ArrayList<String>();

		while (e.hasMoreElements()) {
			Object obj = e.nextElement();
			String fieldName = (String) obj;
//			String fieldValue = request.getParameter(fieldName);
			// System.out.println(fieldName + " : " + fieldValue);
			arr.add(fieldName);
		}
		if (arr.contains("Login")) {
			new Login().doGet(request, response);
			
		} else if (arr.contains("Register")) {
//			response.sendRedirect("/Register.html");
//			 request.getRequestDispatcher("/Register").forward(request,
//			 response);
			 new Register().doGet(request, response);		};
			

	}

}
