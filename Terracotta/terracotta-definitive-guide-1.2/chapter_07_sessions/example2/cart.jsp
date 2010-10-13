<%@page import="java.util.Iterator,example.Item"%>
<html>
    <jsp:useBean id="cart" scope="session" class="example.Cart"/>
    <body><%
        String operation = request.getParameter("operation");
        String name = request.getParameter("name");
        String amount = request.getParameter("amount");
        if (operation != null && name != null) {
            if (operation.equals("add")) {
                cart.addItem(new Item(name, Integer.parseInt(amount)));
            } else if (operation.equals("remove")) {
                cart.removeItem(name);
            }
        }
        if (null == name) name = "";
        if (null == amount) amount = "";%>
        <h2>You have the following items in your cart:</h2>
        <table><%
        for (Item i : cart.getItems()) {%>
            <tr>
                <td align="right"><%=i.getAmount()%></td>
                <td>x</td>
                <td><b><%=i.getName()%></b></td>
            </tr><%
        }%>
        </table>
        <form method="post" action="<%=request.getContextPath()%>/cart.jsp">
            <h2>Item to add or remove:</h2>
            <div>
                <input type="text" name="amount" value="<%=amount%>"/> x
                <input type="text" name="name" value="<%=name%>"/>
            </div>
            <div><em>(amount x name)</em></div>
            <div>
                <input type="submit" name="operation" value="add"/>
                <input type="submit" name="operation" value="remove"/>
            </div>
        </form>
    </body>
</html>

