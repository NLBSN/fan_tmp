package com.http.day13_JSP_EL_JSTL.StuManager.src.com.itheima.dao;

/**
 * 该dao定义了对用户表的访问规则
 */
public interface UserDao {

	/**
	 * 这里简单就返回一个Boolean类型， 成功或者失败即可。
	 * 
	 * 但是开发的时候，登录的方法，一旦成功。这里应该返回该用户的个人信息
	 * @param userName 
	 * @param password
	 * 
	 * @return true : 登录成功， false : 登录失败。
	 */
	boolean login(String userName, String password);
}
