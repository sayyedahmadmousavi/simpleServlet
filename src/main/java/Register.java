import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

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

@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Register() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		
		System.out.println(" ------------------  Register.java doget --------------");

		Check.checkState(request, response);
		
		
		System.out.println(" ------------------  Register.java doget --------------");
		
		
		
		request.getRequestDispatcher("/Register.html")
		.forward(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");

		String firstname = request.getParameter("firstname");
		String lastname = request.getParameter("lastname");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String rePassword = request.getParameter("re-password");

		boolean check = check(firstname, lastname, username, password,
				rePassword);
		boolean exist = isExist(username);

		HttpSession session = request.getSession();

		sendError(check, exist, request, response);

		if (check) {
			if (!exist) {

				session.setAttribute("user", username);
				boolean add = addUser(firstname, lastname, username, password,
						rePassword);
				if (add) {
					session.setAttribute("state", "online");
					session.setAttribute("user", username);

					response.sendRedirect("/simpleServlet/Link");


				}

			}

		}
	}

	private void sendError(boolean check, boolean exist,
			HttpServletRequest request, HttpServletResponse response) {
		String error = "";
		if (exist) {
			error = "This User name is already in use";
		}
		if (!check && exist) {
			error += "<br>" + "Please carefully fill field";
		} else if (!check) {
			error += "Please carefully fill field";
		}
		request.setAttribute("error", error);

		try {
			if (error.length() > 5) {

				response.getWriter().print(error);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private boolean isExist(String username) {
		boolean res = false;
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
				if (user.getUser().equals(username)) {
					res = true;

					tx.commit();

					// session.close();

					return res;
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

		return false;
	}

	private boolean addUser(String firstname, String lastname, String username,
			String password, String rePassword) {
		boolean res = false;

		System.out.println("adding user was started");
		SessionFactory sessionFactory;
		Configuration configuration = new Configuration();
		Configuration configure = configuration.configure();
		sessionFactory = configure.buildSessionFactory();

		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			User user = new User(firstname, lastname, username, password,
					"online");
			session.save(user);
			tx.commit();
			res = true;
		} catch (Exception e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}

		System.out.println("user added");
		return res;

	}

	private boolean check(String firstname, String lastname, String username,
			String password, String rePassword) {
		boolean res = true;
		if (firstname.length() < 1 || firstname == null
				|| firstname.length() < 1 || firstname == null
				|| lastname.length() < 1 || lastname == null
				|| username.length() < 1 || username == null
				|| username.contains(" ") || password.length() < 1
				|| password == null || rePassword.length() < 1
				|| rePassword == null || (!password.equals(rePassword))) {
			res = false;
		}
		return res;
	}
}
