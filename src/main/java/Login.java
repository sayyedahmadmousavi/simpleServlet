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

		response.setContentType("text/html");
		HttpSession session = request.getSession();
		User user = null;
		String state = Check.checkState(request, response);

		try {
			user = (User) session.getAttribute("user");
			System.out.println("------------------ login.doGet -------------- "
					+ user.getUser());

			if (state.equals("online")) {
				System.out.println("------------------ online -------------- ");

				Link link = new Link();
				link.doGet(request, response);
			} else if (state.equals("onChat")) {
				System.out.println("------------------ onchat -------------- ");

				Chat chat = new Chat();
				chat.doGet(request, response);
			} else {

				request.getRequestDispatcher("Login.html").forward(request,
						response);

			}
		} catch (NullPointerException e) {
			request.getRequestDispatcher("Login.html").forward(request,
					response);

		}
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		try {
			try {
				User user1 = (User) session.getAttribute("user");
				System.out.println("------------- Login.doPost -------------"
						+ user1.getUser());

			} catch (NullPointerException e) {
				// TODO: handle exception
				System.out.println("NullPointerException");
				System.out.println("------------- Login.doPost -------------");

			}

			String password = request.getParameter("password");
			String userName = request.getParameter("username");

			boolean checkField = true;
			boolean checkPass = false;
			boolean exist = false;

			if ((!Check.checkField(userName))
					|| (!Check.checkUserName(userName))
					|| (!Check.checkField(password))) {
				checkField = false;
			}

			exist = Check.isExist(userName);
			if (exist) {
				System.out.println(" >>>>> " + exist);
				checkPass = Check.checkUserPass(userName, password);
			}

			System.out.println(checkPass);

			if (checkField) {
				if (exist) {
					if (checkPass) {

						session.setAttribute("user", getUser(userName));

						response.sendRedirect("/simpleServlet/Link");

					}
				}

			}
			sendError(checkField, checkPass, exist, request, response);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private User getUser(String userName) {
		User res = new User();

		SessionFactory sessionFactory;
		sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List userList = session.createQuery("FROM User").list();
			ff: for (Iterator iterator = userList.iterator(); iterator
					.hasNext();) {

				User user = (User) iterator.next();
				if (user.getUser().equals(userName)) {

					res.setFirstName(user.getFirstName());
					res.setLastName(user.getLastName());
					res.setPass(user.getPass());
					user.setState("online");
					res.setState(user.getState());
					
					session.update(user);
					
					res=user;

				}
			}
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		return res;
	}

	private void sendError(boolean checkField, boolean checkPass,
			boolean exist, HttpServletRequest request,
			HttpServletResponse response) {
		String error = "";
		if (!checkField) {
			error += "Please carefully fill field\n";
		}
		if (!exist) {
			error += "This This User name does not exist\n";
		}else
		if (!checkPass) {
			error += "Password wrong\n";
		}

		try {
			if (error.length() > 5) {

				response.getWriter().print(error);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}