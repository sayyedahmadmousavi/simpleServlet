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
		String userNmae = (String) session.getAttribute("user");
		if (nowChat(userNmae,session)) {
			request.getRequestDispatcher("Chat.html").forward(request,
					response);
		} else {

			request.getRequestDispatcher("Connect.html").forward(request,
					response);
		}
	}

	private boolean nowChat(String userNmae,HttpSession httpSession) {
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
				if (user.getUser().equals(userNmae)) {
					
					if (user.getState().equals("OnChat")) {
						return true;

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

		return res;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String user = (String) session.getAttribute("user");
		try {
			int number = Integer.parseInt(request.getParameter("number"));
		} catch (Exception e) {
			doGet(request, response);
		}
		try {
			String parameter = request.getParameter("Users");
			String[] split = parameter.split(" ");
			ArrayList<String> users = check(split, user ,session);
			boolean state = changeState(users,session);
			if (state) {
				
				response.sendRedirect("/simpleServlet/Chat");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private boolean changeState(ArrayList<String> users,HttpSession httpSession) {
		// TODO Auto-generated method stub
		boolean res = true;
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
//				httpSession.setAttribute("state", "OnChat");
				for (String userName : users) {
					if (user.getUser().equals(userName)) {
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
		return res;
	}

	private ArrayList<String> check(String[] split, String userName,HttpSession httpSession) {
		ArrayList<String> res = new ArrayList<String>();

		SessionFactory sessionFactory;
		sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		Transaction tx = null;

		try {
			for (String string : split) {
				tx = session.beginTransaction();
				List userList = session.createQuery("FROM User").list();
				ff: for (Iterator iterator = userList.iterator(); iterator
						.hasNext();) {

					User user = (User) iterator.next();
					if (userName.equals(user.getUser())) {
						if (user.getUser().equals(string)
								&& user.getState().equals("online")) {
							res.add(user.getUser());

						}

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
		res.add(userName);

		return res;
	}
}
