import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Chat
 */
@WebServlet("/Chat")
public class Chat extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Chat() {
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

		session.setAttribute("state", "onChat");
		String userName = (String) session.getAttribute("user");

		request.getParameterNames();
		String text = "";
		String text2 = "";
		if (request.getParameter("Text") == null) {
			// System.out.println(" null is here");
		} else {

			text2 = userName + " : " + request.getParameter("Text") + "\n";
		}
		// System.out.println(text2);

		// System.out.println("--------------------------");
		try {
//			String file1 = "E:\\ahmad\\lunas\\simpleServlet\\src\\main\\webapp\\text.txt";
//			FileInputStream inf = new FileInputStream(file1);
//			FileOutputStream onf = new FileOutputStream(file1, true);
//
//			RandomAccessFile randomAccessFile = new RandomAccessFile(file1,
//					"rw");
//
			// byte readByte = 0;
			// int c = 0;

//			onf.write(text2.getBytes());
			
			
			

//			int d = (int) randomAccessFile.length();
//			for (int i = 0; i < d; i++) {
//				text += (char) inf.read();
//			}
			// byte[] bytes = text2.getBytes();
			// onf.write(bytes);
			// System.out.println("text2 : {" + text2 + "}");
			// randomAccessFile.
			// randomAccessFile.write(text2.getBytes());
			// randomAccessFile.writeBytes(text2);
			// inf.read()
			// String readLine = randomAccessFile.readLine();

			// System.out.println("readLine : { "+readLine+" }");
			// text += readLine;

			// for (int i = 0; i < randomAccessFile.length(); i++) {
			// byte read = (byte) randomAccessFile.read();
			// text += "" + read;
			//
			// }

			// System.out.println(text);

			try {
//				randomAccessFile.close();
//				onf.close();
//				inf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		text += text2;
		// // int i = 0;
		// // while(text.contains("\n")){
		// // i++;
		// // }
		//
		String htmlFile = creatHTMLFile(text);
		// // System.out.println("text : {" + text + "}");
		checkRepeat(htmlFile);
		//
		// // request.setAttribute("message", htmlFile);
		// // request.getRequestDispatcher("Chat.jsp").forward(request,
		// response);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		Enumeration e = request.getParameterNames();
		ArrayList<String> fieldNameList = new ArrayList<String>();

		while (e.hasMoreElements()) {
			Object obj = e.nextElement();
			String fieldName = (String) obj;
			String fieldValue = request.getParameter(fieldName);
			System.out.println(fieldName + " : " + fieldValue);
			fieldNameList.add(fieldName);

		}
		if (fieldNameList.contains("logOutButton")) {
			session.setAttribute("state", "ofline");
			request.getRequestDispatcher("/").forward(request, response);
		}else if(fieldNameList.contains("leaveChaatButton")){
			session.setAttribute("state", "online");
			request.getRequestDispatcher("/Link").forward(request, response);
		}
		
		System.out.println(fieldNameList.size());
		response.getWriter().print(htmlFile);
		// out.close();
		// }
	}

	private String creatHTMLFile(String text) {
		// TODO Auto-generated method stub
		String res = "<!DOCTYPE html>" + "<html>" + " <head>"
				+ " <link rel=\"stylesheet\" href=\"style.css\">" + " </head>"
				+ " <body>"
				// + "<div "/*id=\"single\"*/+" dir=\"rtl\">"
				// + "    <div class=\"common\">Single</div>"
				// + "</div>"
				// + "<div id=\"both\" dir=\"ltr\">    "
				// + "<div class=\"common\">Both</div>"
				// + "</div>"
				+ " <div		style=\"overflow-y: scroll; width: 500px; height: 500px; line-height: 1em; overflow: auto; padding: 5px;\">";
		String tableRow = "   <table> ";
		String[] split = text.split("\n");
		for (String string : split) {
			if (!string.contains("null")) {
				int lineLenght = 60;
				int stringLength = string.length();

				int a = stringLength;
				int len = lineLenght;

				int mod = stringLength / lineLenght;

				if (mod > 0) {
					for (int i = 0; i <= mod; i++) {
						if ((i + 1) * len <= a) {
							tableRow += "   <tr>" + "    <td>"
									+ string.substring(len * i, len * (i + 1))
									+ "</td>" + "   </tr>";

						} else {
							// System.out.println((i) * len + ":" + a);
							tableRow += "   <tr>" + "    <td>"
									+ string.substring(len * i) + "</td>"
									+ "   </tr>";

						}
					}

				} else {
					tableRow += "   <tr>" + "    <td>" + string + "</td>"
							+ "   </tr>";

				}

			}

		}

		tableRow += "  </table> </div>  ";
		// tableRow = checkRepeat(tableRow);

		String formChat = "<form action=\"/simpleServlet/Chat\" method=\"GET\">"
				+ "Your Text:<br> <input type=\"text\" name=\"Text\" autofocus=\"autofocus\"> "
				+ "<input	type=\"submit\" name =\"sendButton\"value=\"send...\"  >"
				+ "<input type=\"submit\" name=\"logOutButton\" value=\"Log Out\">"
				+ "<input type=\"submit\" name=\"leaveChaatButton\" value=\"Leave Chaat\">"
				+ "</form>";
		// String leaveGroupForm =
		// "<form action=\"/simpleServlet/Chat\" method=\"DELETE\">"
		// + "<input	type=\"submit\" value=\"Log Out\"  >"
		// + "</form>";
		// String logOutForm=
		// "<form action=\"/simpleServlet/Chat\" method=\"OPTIONS\">"
		// + "<input	type=\"submit\" value=\"leave group\"  >" + "</form>";

		res += tableRow;
		res += formChat;
		// res += leaveGroupForm;
		// res += logOutForm;
		res += "</body></html>";

		return res;

	}

	private String checkRepeat(String tableRow) {
		String res = "";
		String[] split = tableRow.split("   <tr>" + "    <td>");
		for (int i = 0; i < split.length - 1; i++) {
			if (split[i].endsWith("</tr>")) {
				// System.out.println(split[i]);
			}

		}

		return tableRow;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

}