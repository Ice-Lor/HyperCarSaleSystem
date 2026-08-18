package controller.client;

import dal.CarDAO;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Car;
import model.Cart;

public class CartController extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/client/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            int carId = Integer.parseInt(request.getParameter("carId"));
            int quantity = 1;
            try {
                quantity = Math.max(1, Integer.parseInt(request.getParameter("quantity")));
            } catch (Exception ignored) {}
            String color = request.getParameter("color");
            String customOptions = request.getParameter("customOptions");

            Car car = carDAO.getCarById(carId);
            if (car != null) {
                cart.addItem(car, quantity, color, customOptions);
                session.setAttribute("toastMessage", "Đã thêm " + car.getModelName() + " vào giỏ xe VIP!");
            }
        } else if ("update".equals(action)) {
            int carId = Integer.parseInt(request.getParameter("carId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            cart.updateQuantity(carId, quantity);
        } else if ("remove".equals(action)) {
            int carId = Integer.parseInt(request.getParameter("carId"));
            cart.removeItem(carId);
            session.setAttribute("toastMessage", "Đã xóa siêu xe khỏi giỏ hàng.");
        } else if ("clear".equals(action)) {
            cart.clear();
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
