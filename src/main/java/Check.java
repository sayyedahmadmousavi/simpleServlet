import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Check {

	static boolean checkField(String username, String password) {
		boolean res = false;

		if (username.length() > 0 || password.length() > 0) {
			return true;

		}

		return res;
	}

	static boolean isExist(String username) {
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

	static boolean checkUserPass(String username, String password) {
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
				if (user.getUser().equals(username)
						&& user.getPass().equals(password)) {
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

		return res;
	}

	static boolean checkState(HttpServletRequest request,
			HttpServletResponse response) {

		boolean res = false;

		try {

			HttpSession httpSession = request.getSession();
			String state = (String) httpSession.getAttribute("state");
			String userName = (String) httpSession.getAttribute("user");

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
					if (user.getUser().equals(userName)) {

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

			Enumeration<String> attributeNames = httpSession
					.getAttributeNames();
			System.out
					.println("---------------------- check State ---------------------");
			while (attributeNames.hasMoreElements()) {
				String nextElement = attributeNames.nextElement();

				System.out.println(nextElement + "   "
						+ httpSession.getAttribute(nextElement));
			}

			if (state == null) {

				httpSession.setAttribute("state", "");

			} else

			if (state.equals("online")) {
				System.out.println("online");
				res = true;
				request.getRequestDispatcher("/Link")
						.forward(request, response);

			} else if (state.equals("onChat")) {
				System.out.println("onChat");
				res = true;
				request.getRequestDispatcher("/Chat")
						.forward(request, response);
			}

		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

}
