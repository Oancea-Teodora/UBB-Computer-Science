<%@ page import="java.util.List" %>
<%--print la toate--%>


<%
    List<String> random = (List<String>) request.getSession().getAttribute("shopList");
    System.out.println(random);
%>