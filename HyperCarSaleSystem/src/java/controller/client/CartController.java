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
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }

        if ("add".equalsIgnoreCase(action)) {
            try {
                int carId = Integer.parseInt(request.getParameter("carId"));
                int quantity = 1;
                if (request.getParameter("quantity") != null) {
                    quantity = Integer.parseInt(request.getParameter("quantity"));
                }
                String color = request.getParameter("color");
                String options = request.getParameter("customOptions");

                Car car = carDAO.getCarById(carId);
                if (car != null && car.getStockQuantity() >= quantity) {
                    cart.addItem(car, quantity, color, options);
                    session.setAttribute("toastMessage", "Đã thêm " + car.getModelName() + " vào danh sách đặt cọc!");
                } else {
                    session.setAttribute("errorMessage", "Số lượng xe trong showroom không đủ!");
                }
            } catch (NumberFormatException ignored) {}

        } else if ("update".equalsIgnoreCase(action)) {
            try {
                int carId = Integer.parseInt(request.getParameter("carId"));
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                cart.updateQuantity(carId, quantity);
            } catch (NumberFormatException ignored) {}

        } else if ("remove".equalsIgnoreCase(action)) {
            try {
                int carId = Integer.parseInt(request.getParameter("carId"));
                cart.removeItem(carId);
                session.setAttribute("toastMessage", "Đã xóa siêu xe khỏi danh sách đặt cọc.");
            } catch (NumberFormatException ignored) {}

        } else if ("clear".equalsIgnoreCase(action)) {
            cart.clear();
            session.setAttribute("toastMessage", "Đã dọn sạch danh sách đặt cọc.");
        }

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/cart");
        }
    }
}
