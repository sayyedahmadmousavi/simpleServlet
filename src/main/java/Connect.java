import java.io.IOException;
import java.util.ArrayList;
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

/**
 * Servlet implementation class Connect
 */
@WebServlet("/Connect")
public class Connect extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Connect() {
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
		HttpSession session = request.getSession();
		User user = null;
		String state = Check.checkState(request, response);

		try {
			user = (User) session.getAttribute("user");
			System.out
					.println("------------------ Connect.doGet -------------- "
							+ user.getUser());

			if (state.equals("online")) {

				System.out.println("Connect.html");
				response.sendRedirect("Connect.html");

			} else if (state.equals("onChat")) {
				System.out.println("------------------ onchat -------------- ");

				Chat chat = new Chat();
				chat.doGet(request, response);
			}
		} catch (NullPointerException e) {

			new Index().doGet(request, response);

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		System.out.println("------------- Connect.doPost -----------  {"
				+ user.getUser() + "}");

		int number = 0;
		String parameter = "";
		try {
			number = Integer.parseInt(request.getParameter("number"));
			parameter = request.getParameter("Users");

		} catch (Exception exception) {
			System.out.println(" error 1 ");
			doGet(request, response);
		}

		boolean res = false;

		String[] split = parameter.split(" ");
		ff: for (String string : split) {

			if (string.equals(user.getUser())) {
				res = true;
				break ff;
			}

		}

		sendError(split, number, request, response);

		System.out.println(split.length + " : " + number);
		if (split.length == number/* &&/* !res */) {
			ArrayList<String> users = check(split);
			users.add(user.getUser());

			changeAllUserState(users);
			setFileName(users,user.getUser());
			System.out.println(user.getUser());
			// response.sendRedirect("/simpleServlet/Chat");
			Chat chat = new Chat(user.getUser());
			chat.doGet(request, response);

		} else {
			System.out.println(" error 2 ");
			doGet(request, response);

		}
	}

	private void setFileName(ArrayList<String> users, String fileName) {
		// TODO Auto-generated method stub
		SessionFactory sessionFactory;
		sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			List userList = session.createQuery("FROM User").list();

			for (String userName : users) {

				ff: for (Iterator iterator = userList.iterator(); iterator
						.hasNext();) {

					User user = (User) iterator.next();

					System.out.println(user.getUser() + ":" + user.getState());

					if (user.getUser().equals(userName)) {
						
						user.setFile(fileName);
						session.update(user);

					}

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
		
	}

	private void changeAllUserState(ArrayList<String> users) {
		// TODO Auto-generated method stub
		SessionFactory sessionFactory;
		sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();
			List userList = session.createQuery("FROM User").list();

			for (String userName : users) {

				ff: for (Iterator iterator = userList.iterator(); iterator
						.hasNext();) {

					User user = (User) iterator.next();

					System.out.println(user.getUser() + ":" + user.getState());

					if (user.getUser().equals(userName)
							&& user.getState().equals("online")) {
						
						user.setState("onChat");
						
						session.update(user);

					}

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

	}

	private void sendError(String[] split, int number,
			HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		String error = "";
		int a = 0;
		for (String string : split) {
			if (request.getSession().equals(string)) {
				error += "dont use your user name for userName(s)\n";
				a = 1;
			}

		}
		if ((split.length - a) != number) {
			error += "The number entered" + ", Enter the name";
			a += 2;
		}

		// request.setAttribute("error", error);
		try {
			if (a > 0) {

				System.out.println(error);
				response.getWriter().print(error);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ArrayList<String> check(String[] split) {
		ArrayList<String> res = new ArrayList<String>();

		SessionFactory sessionFactory;
		sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;

		for (String string : split) {
			System.out.println(string + "::::::::::: in split");
		}

		try {
			for (String string : split) {
				tx = session.beginTransaction();
				List userList = session.createQuery("FROM User").list();
				ff: for (Iterator iterator = userList.iterator(); iterator
						.hasNext();) {

					User user = (User) iterator.next();

					System.out.println(user.getUser() + ":" + user.getState());

					// if (userName.equals(user.getUser())) {
					if (user.getUser().equals(string)
							&& user.getState().equals("online")) {
						user.setState("onChat");
						res.add(user.getUser());
						session.update(user);
						System.out.println(user.getUser() + " --------- "
								+ user.getState());

					}

				}
				// }
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
}
