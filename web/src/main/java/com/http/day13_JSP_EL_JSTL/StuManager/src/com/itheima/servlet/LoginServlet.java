package com.http.day13_JSP_EL_JSTL.StuManager.src.com.itheima.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itheima.dao.StuDao;
import com.itheima.dao.UserDao;
import com.itheima.dao.impl.StuDaoImpl;
import com.itheima.dao.impl.UserDaoImpl;
import com.itheima.domain.Student;

/**
 * 这是用于处理登录的servlet
 */
public class LoginServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//提交的数据有可能有中文， 怎么处理。
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");
		
		//1. 获取客户端提交的信息
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		
		//2. 去访问dao ， 看看是否满足登录。
		UserDao dao = new UserDaoImpl();
		boolean isSuccess = dao.login(userName, password);
		
		//3. 针对dao的返回结果，做出响应
		if(isSuccess){
			//response.getWriter().write("登录成功.");
			
			//1. 查询出来所有的学生信息。
			StuDao stuDao = new StuDaoImpl();
			List<Student> list = stuDao.findAll();
			
			//2. 先把这个集合存到作用域中。
			request.getSession().setAttribute("list", list);
			
			//2. 重定向
			response.sendRedirect("stu_list.jsp");
			
		}else{
			response.getWriter().write("用户名或者密码错误！");
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
