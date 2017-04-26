import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

/**
 * Servlet implementation class Servlet
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Login() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// String firstname = request.getParameter("firstname");
		// String lastname = request.getParameter("lastname");
		// String password = request.getParameter("password");
		//
		// if (firstname == null || lastname == null || firstname == null
		// || lastname.length() < 1 || password.length() < 1) {
		// String message = "Hello World";
		// request.setAttribute("message", message);
		//
		// request.getRequestDispatcher("hello.jsp")
		// .forward(request, response);
		//
		// request.getRequestDispatcher("Login.html")
		// .forward(request, response);
		//
		// } else {
		// String message = firstname + "  " + lastname;
		//
		// PrintWriter out = response.getWriter();
		// out.println(message);
		//
		// }

		response.setContentType("text/html");
		// HttpSession session = request.getSession();
		//
		//
		//
		//
		// if (session.getAttribute("login") == Boolean.TRUE) {
		// response.sendRedirect("/simpleServlet/Link");
		// }else{
		// request.getRequestDispatcher("/Login.html").forward(request,
		// response);
		// }

		Check.checkState(request, response);
		request.getRequestDispatcher("/Login.html").forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String password = request.getParameter("password");
		String username = request.getParameter("username");

		boolean checkField = false;
		boolean checkPass = false;
		boolean exist = false;
		boolean error = true;

		checkField = Check.checkField(username, password);
		checkPass = Check.checkUserPass(username, password);
		exist = Check.isExist(username);
		error = sendError(checkField, checkPass, exist, request, response);
		// response.getWriter().print(checkField+" "+checkPass+" "+exist+" "+error);;
		HttpSession session = request.getSession();
//		if (session.getAttribute("login") == Boolean.TRUE) {
//			response.sendRedirect("/simpleServlet/Link");
//		} else {
			if (!error) {
				if (checkField) {
					if (exist) {
						if (checkPass) {
							// response.sendRedirect("/simpleServlet/UserList");
							// response.sendRedirect("/simpleServlet/Link");

							session.setAttribute("user", username);

							session.setAttribute("state", "online");

							response.sendRedirect("/simpleServlet/Link");
							// request.getRequestDispatcher("/Link").forward(request,
							// response);
						}
					}

				}

//			}

		}
	}

	private boolean sendError(boolean checkField, boolean checkPass,
			boolean exist, HttpServletRequest request,
			HttpServletResponse response) {
		String error = "";
		if (!checkField) {
			error += "Please carefully fill field<br>";
		}
		if (!exist) {
			error += "This This User name does not exist<br>";
		}
		if (!checkPass) {
			error += "Password wrong<br>";
		}

		try {
			if (error.length() > 5) {

				response.getWriter().print(error);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (error.length() > 5) {
			return true;
		} else {
			return false;

		}

	}

}