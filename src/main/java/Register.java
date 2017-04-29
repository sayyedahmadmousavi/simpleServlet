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
		HttpSession session = request.getSession();
		User user = null;
		try {
			user = (User) session.getAttribute("user");
			String state = user.getState();
			System.out
					.println("------------------ Register.doGet -------------- "
							+ user.getUser());

			if (state.equals("online")) {
				System.out.println("------------------ online -------------- ");

				// request.getRequestDispatcher("/Link")
				// .forward(request, response);
				Link link = new Link();
				link.doGet(request, response);
			} else if (state.equals("onChat")) {
				System.out.println("------------------ onchat -------------- ");

				// request.getRequestDispatcher("/Chat")
				// .forward(request, response);
				Chat chat = new Chat();
				chat.doGet(request, response);

				// }
				// else if (state.equals("ofline")) {
				// System.out.println("------------------ ofline -------------- ");
				//
				// request.getRequestDispatcher("/").forward(request, response);
			} else {

				// Register register = new Register();
				// register.d
				request.getRequestDispatcher("Register.html").forward(request,
						response);

			}
		} catch (NullPointerException e) {
			// TODO: handle exception
			request.getRequestDispatcher("Register.html").forward(request,
					response);

		}

		// Check.checkState(request, response);

		// String state = Check.checkState(request, response);

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		HttpSession session = request.getSession();
		try {
			try {
				User user = (User) session.getAttribute("user");
				System.out
						.println("------------- Register.doPost -------------"
								+ user.getUser());

			} catch (Exception e) {
				// TODO: handle exception
				System.out
						.println("------------- Register.doPost -------------");

			}

			String firstName = request.getParameter("firstname");
			String lastName = request.getParameter("lastname");
			String userName = request.getParameter("username");
			String password = request.getParameter("password");
			String rePassword = request.getParameter("re-password");

			boolean checkField = true;
			if ((!Check.checkField(firstName)) || (!Check.checkField(lastName))
					|| (!Check.checkField(password))
					|| (!Check.checkField(rePassword))
					|| (!Check.checkField(userName))
					|| (!Check.checkUserName(userName))) {
				checkField = false;
			}

			// boolean check = check(request, response, firstName, lastName,
			// userName,
			// password, rePassword);

			// HttpSession session = request.getSession();
			boolean exist = false;
			if (checkField) {

				exist = Check.isExist(userName);
				System.out.println(checkField + " d " + exist + " dd ");
			}

			System.out.println("9999999999999995555555555555");

			if (checkField) {

				sendError(checkField, exist, request, response);

				if (!exist) {
					String state = "online";

					boolean add = addUser(firstName, lastName, userName,
							password, state);
					if (add) {

						session.setAttribute("user", new User(firstName,
								lastName, userName, password, state));
						// session.setAttribute("state", "online");

						response.sendRedirect("/simpleServlet/Link");

					}

				}

			} else {
				sendError(checkField, exist, request, response);
			}

		} catch (Exception e) {
			// TODO: handle exception
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
			e.printStackTrace();
		}

	}

	private boolean addUser(String firstname, String lastname, String username,
			String password, String state) {
		boolean res = false;

		SessionFactory sessionFactory;
		Configuration configuration = new Configuration();
		Configuration configure = configuration.configure();
		sessionFactory = configure.buildSessionFactory();

		Session session = sessionFactory.openSession();

		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			User user = new User(firstname, lastname, username, password, state);

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

		System.out.println("added ");
		return res;

	}

}