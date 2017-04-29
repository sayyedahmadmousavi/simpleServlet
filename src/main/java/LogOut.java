
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
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
 * Servlet implementation class LogOut
 */
public class LogOut extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LogOut() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession httpSession = request.getSession();
		try {
			User user1 = (User) httpSession.getAttribute("user");
			
			String state = Check.checkState(request, response);
			user1.setState(state);
			

			SessionFactory sessionFactory;
			sessionFactory = new Configuration().configure()
					.buildSessionFactory();
			Session session = sessionFactory.openSession();
			Transaction tx = null;

			try {

				tx = session.beginTransaction();
				List userList = session.createQuery("FROM User").list();
				ff: for (Iterator iterator = userList.iterator(); iterator
						.hasNext();) {

					User user = (User) iterator.next();
					boolean res = user.getUser().equals(user1.getUser());

					if (res) {

						user.setState("ofline");
						Check.changeState(request, response, "ofline");
						httpSession.setAttribute(
								"user",
								new User(user.getFirstName(), user
										.getLastName(), user.getUser(), user
										.getPass(), user.getState()));

						System.out.println(((User) httpSession
								.getAttribute("User")).getState());

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

		} catch (NullPointerException e) {
			System.out.println("login5555");
			new Index().doGet(request, response);
		}
	}

}
