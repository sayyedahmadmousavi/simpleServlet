import java.io.IOException;
import java.util.ArrayList;
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

	static boolean checkField(String field) {
		boolean res = false;

		try {
			if (field.length() > 0) {
				return true;
			}

		} catch (NullPointerException e) {
			return false;
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

	static boolean checkUserName(String userName) {
		// TODO Auto-generated method stub
		try {
			for (char c : userName.toCharArray()) {
				if ((c <= '9' && c >= '0') || (c >= 'A' && c <= 'Z')
						|| (c >= 'a' && c <= 'z')) {

				} else {
					return false;
				}

			}
		} catch (NullPointerException e) {
			// TODO: handle exception

		}

		return true;
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

					break ff;

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

	static void changeState(HttpServletRequest request,
			HttpServletResponse response, String state) {

		HttpSession httpSession = request.getSession();
		User user1 = (User) httpSession.getAttribute("user");

		SessionFactory sessionFactory = new Configuration().configure()
				.buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			List userList = session.createQuery("FROM User").list();
			ff: for (Iterator iterator = userList.iterator(); iterator
					.hasNext();) {

				User user = (User) iterator.next();
				if (user.getUser().equals(user1.getUser())) {
					user.setState(state);

					session.update(user);
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

	static String checkState(HttpServletRequest request,
			HttpServletResponse response) {

		String res = "";

		HttpSession httpSession = request.getSession();
		try {
			// String state = (String) httpSession.getAttribute("state");
			User user1 = (User) httpSession.getAttribute("user");

			SessionFactory sessionFactory = new Configuration().configure()
					.buildSessionFactory();

			Configuration configuration = new Configuration();
			configuration.configure();
			SessionFactory buildSessionFactory = configuration
					.buildSessionFactory();
			sessionFactory = buildSessionFactory;
			Session session = sessionFactory.openSession();

			Transaction tx = null;
			try {
				tx = session.beginTransaction();
				List userList = session.createQuery("FROM User").list();
				ff: for (Iterator iterator = userList.iterator(); iterator
						.hasNext();) {

					User user = (User) iterator.next();
					if (user.getUser().equals(user1.getUser())) {
						res = user.getState();

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
			// TODO: handle exception
			try {
				new Index().doGet(request, response);
			} catch (ServletException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return res;
	}
}
