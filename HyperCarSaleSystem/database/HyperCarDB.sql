-- =======================================================
-- SCRIPT KHỞI TẠO CƠ SỞ DỮ LIỆU HYPERCAR SALE SYSTEM (PRJ301)
-- HỆ QUẢN TRỊ CSDL: MICROSOFT SQL SERVER
-- (ĐÃ CHUẨN HOÁ RÀNG BUỘC & QUAN HỆ ERD THEO THỰC TẾ)
-- =======================================================

USE master;
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = N'HyperCarDB')
BEGIN
    ALTER DATABASE HyperCarDB SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE HyperCarDB;
END
GO

CREATE DATABASE HyperCarDB;
GO

USE HyperCarDB;
GO

-- 1. BẢNG ROLES (Vai trò người dùng)
CREATE TABLE Roles (
    role_id INT PRIMARY KEY IDENTITY(1,1),
    role_name NVARCHAR(50) NOT NULL UNIQUE
);
GO

-- 2. BẢNG USERS (Người dùng / Khách hàng / Quản trị viên)
CREATE TABLE Users (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name NVARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address NVARCHAR(255),
    role_id INT NOT NULL,
    status INT DEFAULT 1, -- 1: Active, 0: Locked
    created_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Users_Roles FOREIGN KEY (role_id) REFERENCES Roles(role_id)
);
GO

-- 3. BẢNG BRANDS (Hãng sản xuất siêu xe)
CREATE TABLE Brands (
    brand_id INT PRIMARY KEY IDENTITY(1,1),
    brand_name NVARCHAR(100) NOT NULL UNIQUE,
    country NVARCHAR(50),
    logo_url NVARCHAR(500),
    description NVARCHAR(MAX)
);
GO

-- 4. BẢNG CATEGORIES (Phân loại dòng siêu xe)
CREATE TABLE Categories (
    category_id INT PRIMARY KEY IDENTITY(1,1),
    category_name NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(MAX)
);
GO

-- 5. BẢNG CARS (Danh mục siêu xe)
CREATE TABLE Cars (
    car_id INT PRIMARY KEY IDENTITY(1,1),
    model_name NVARCHAR(150) NOT NULL,
    brand_id INT NOT NULL,
    category_id INT NOT NULL,
    price DECIMAL(15, 2) NOT NULL,
    deposit_rate DECIMAL(5, 2) DEFAULT 10.0, -- Phần trăm đặt cọc (mặc định 10%)
    year INT NOT NULL,
    horsepower INT NOT NULL, -- Mã lực (HP)
    acceleration_0_100 DECIMAL(4, 2) NOT NULL, -- Tăng tốc 0-100 km/h (giây)
    top_speed INT NOT NULL, -- Tốc độ tối đa (km/h)
    stock_quantity INT DEFAULT 1,
    thumbnail_url NVARCHAR(500),
    color_options NVARCHAR(255), -- Các màu sơn tùy chọn
    engine_spec NVARCHAR(255), -- Loại động cơ
    description NVARCHAR(MAX),
    status INT DEFAULT 1, -- 1: Đang mở bán, 0: Ngừng kinh doanh
    created_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Cars_Brands FOREIGN KEY (brand_id) REFERENCES Brands(brand_id),
    CONSTRAINT FK_Cars_Categories FOREIGN KEY (category_id) REFERENCES Categories(category_id)
);
GO

-- 6. BẢNG CAR_IMAGES (Bộ sưu tập ảnh chi tiết siêu xe)
CREATE TABLE CarImages (
    image_id INT PRIMARY KEY IDENTITY(1,1),
    car_id INT NOT NULL,
    image_url NVARCHAR(500) NOT NULL,
    caption NVARCHAR(100),
    CONSTRAINT FK_CarImages_Cars FOREIGN KEY (car_id) REFERENCES Cars(car_id) ON DELETE CASCADE
);
GO

-- 7. BẢNG COUPONS (Mã ưu đãi / Chiết khấu VIP)
CREATE TABLE Coupons (
    coupon_code VARCHAR(50) PRIMARY KEY,
    discount_percent DECIMAL(5,2) DEFAULT 0,
    max_discount DECIMAL(15,2) DEFAULT 0,
    min_order_amount DECIMAL(15,2) DEFAULT 0,
    expiry_date DATETIME NOT NULL,
    is_active INT DEFAULT 1
);
GO

-- 8. BẢNG ORDERS (Đơn hàng / Hợp đồng đặt cọc)
-- QUAN HỆ VỚI COUPONS: 0..1 (coupon_code có thể NULL)
CREATE TABLE Orders (
    order_id INT PRIMARY KEY IDENTITY(1,1),
    order_code VARCHAR(50) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    total_amount DECIMAL(15,2) NOT NULL,
    deposit_amount DECIMAL(15,2) NOT NULL,
    coupon_code VARCHAR(50) NULL, -- Cho phép NULL nếu khách không áp mã
    discount_amount DECIMAL(15,2) DEFAULT 0,
    status NVARCHAR(50) DEFAULT N'PENDING', -- PENDING, CONFIRMED, PROCESSING, COMPLETED, CANCELLED
    payment_method NVARCHAR(50) DEFAULT N'BANK_TRANSFER', -- BANK_TRANSFER, CRYPTO_USDT, SHOWROOM_DIRECT
    delivery_address NVARCHAR(255),
    phone VARCHAR(20),
    note NVARCHAR(MAX),
    order_date DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Orders_Users FOREIGN KEY (user_id) REFERENCES Users(user_id),
    CONSTRAINT FK_Orders_Coupons FOREIGN KEY (coupon_code) REFERENCES Coupons(coupon_code)
);
GO

-- 9. BẢNG ORDER_DETAILS (Chi tiết đơn hàng / Siêu xe đặt cọc)
-- QUAN HỆ VỚI ORDERS: 1..N (Mỗi Order tạo thành công bắt buộc có ít nhất 1 detail)
CREATE TABLE OrderDetails (
    detail_id INT PRIMARY KEY IDENTITY(1,1),
    order_id INT NOT NULL,
    car_id INT NOT NULL,
    quantity INT DEFAULT 1 CHECK (quantity >= 1),
    unit_price DECIMAL(15,2) NOT NULL,
    selected_color NVARCHAR(50),
    custom_options NVARCHAR(255),
    CONSTRAINT FK_OrderDetails_Orders FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    CONSTRAINT FK_OrderDetails_Cars FOREIGN KEY (car_id) REFERENCES Cars(car_id)
);
GO

-- 10. BẢNG TEST_DRIVE_BOOKINGS (Đặt lịch trải nghiệm & lái thử Track VIP)
CREATE TABLE TestDriveBookings (
    booking_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    car_id INT NOT NULL,
    booking_date DATE NOT NULL,
    time_slot VARCHAR(50) NOT NULL,
    location_track NVARCHAR(200) NOT NULL,
    driver_license_number VARCHAR(50) NOT NULL,
    note NVARCHAR(MAX),
    status NVARCHAR(50) DEFAULT N'PENDING',
    created_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_TestDrive_Users FOREIGN KEY (user_id) REFERENCES Users(user_id),
    CONSTRAINT FK_TestDrive_Cars FOREIGN KEY (car_id) REFERENCES Cars(car_id)
);
GO

-- 11. BẢNG CAR_REVIEWS (Đánh giá & Bình luận của Khách hàng VIP)
-- RÀNG BUỘC: UNIQUE(user_id, car_id) - Mỗi khách hàng chỉ đánh giá 1 lần trên 1 mẫu xe
CREATE TABLE CarReviews (
    review_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    car_id INT NOT NULL,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comment NVARCHAR(MAX),
    created_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Reviews_Users FOREIGN KEY (user_id) REFERENCES Users(user_id),
    CONSTRAINT FK_Reviews_Cars FOREIGN KEY (car_id) REFERENCES Cars(car_id),
    CONSTRAINT UQ_User_Car_Review UNIQUE (user_id, car_id)
);
GO

-- 12. BẢNG ACTIVITY_LOGS (Ghi vết hoạt động hệ thống / Audit)
CREATE TABLE ActivityLogs (
    log_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NULL,
    action NVARCHAR(100) NOT NULL,
    details NVARCHAR(MAX),
    created_at DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Logs_Users FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE SET NULL
);
GO

-- =======================================================
-- DỮ LIỆU MẪU (SEED DATA ĐẲNG CẤP) - ẢNH LOCAL
-- =======================================================

-- Roles
INSERT INTO Roles (role_name) VALUES 
('ADMIN'),
('CUSTOMER'),
('STAFF');
GO

-- Users (Mật khẩu mặc định: 123456 -> Mã hóa jBCrypt)
-- Hash của '123456': $2a$10$w8L0QyS29pQdZ4lHqjUgeOB1H7vS0Xo6WdUkpC22xI4qR3eNq8v1i
INSERT INTO Users (username, password_hash, full_name, email, phone, address, role_id, status) VALUES
('admin', '$2a$10$w8L0QyS29pQdZ4lHqjUgeOB1H7vS0Xo6WdUkpC22xI4qR3eNq8v1i', N'Giám Đốc Quản Trị Hệ Thống', 'admin@hypercars.vn', '0988888888', N'Tòa Landmark 81, TP. Hồ Chí Minh', 1, 1),
('johnwick', '$2a$10$w8L0QyS29pQdZ4lHqjUgeOB1H7vS0Xo6WdUkpC22xI4qR3eNq8v1i', N'John Wick', 'john.wick@continental.com', '0912345678', N'Continental Hotel, New York / Q1, TP. HCM', 2, 1),
('tonystark', '$2a$10$w8L0QyS29pQdZ4lHqjUgeOB1H7vS0Xo6WdUkpC22xI4qR3eNq8v1i', N'Tony Stark', 'tony@starkindustries.com', '0999999999', N'Stark Tower, Manhattan / Ba Đình, Hà Nội', 2, 1),
('staff01', '$2a$10$w8L0QyS29pQdZ4lHqjUgeOB1H7vS0Xo6WdUkpC22xI4qR3eNq8v1i', N'Chuyên Viên Tư Vấn VIP 01', 'staff@hypercars.vn', '0901234567', N'Showroom HyperCar Sala, TP. Thủ Đức', 3, 1);
GO

-- Brands (ẢNH LOCAL)
INSERT INTO Brands (brand_name, country, logo_url, description) VALUES
('Bugatti', N'Pháp', 'assets/images/brands/bugatti.jpg', N'Hãng siêu xe đỉnh cao với các dòng xe nhanh nhất thế giới mang tính nghệ thuật và tốc độ tuyệt hảo.'),
('Ferrari', N'Ý', 'assets/images/brands/ferrari.jpg', N'Biểu tượng huyền thoại của làng đua xe Công thức 1 và siêu xe thể thao nước Ý.'),
('Lamborghini', N'Ý', 'assets/images/brands/lamborghini.jpg', N'Thiết kế góc cạnh, động cơ gầm rú đầy mãnh lực và cá tính vị lai.'),
('Koenigsegg', N'Thụy Điển', 'assets/images/brands/koenigsegg.jpg', N'Đỉnh cao cơ khí chính xác và công nghệ Megacar không giới hạn.'),
('McLaren', N'Anh Quốc', 'assets/images/brands/mclaren.jpg', N'Siêu xe công nghệ carbon thừa hưởng trực tiếp từ đường đua F1.'),
('Pagani', N'Ý', 'assets/images/brands/pagani.jpg', N'Kiệt tác thủ công Carbo-Titanium độc bản của Horacio Pagani.'),
('Porsche', N'Đức', 'assets/images/brands/porsche.jpg', N'Chuẩn mực kỹ thuật ô tô thể thao Đức, tin cậy và hoàn hảo trên mọi cung đường.'),
('Rimac', N'Croatia', 'assets/images/brands/rimac.jpg', N'Kỷ nguyên siêu xe thuần điện với gia tốc xé toạc mọi giới hạn vật lý.');
GO

-- Categories
INSERT INTO Categories (category_name, description) VALUES
('Hypercar', N'Dòng siêu xe thương mại đỉnh cao với công suất từ 800 - 1500 HP và tốc độ trên 350 km/h.'),
('Megacar', N'Siêu xe đạt tỷ lệ công suất 1 Megawatt (1341+ HP) với tỷ lệ trọng lượng/công suất 1:1.'),
('Track-Focused', N'Dòng xe phát triển tối ưu khí động học và trọng lượng nhẹ chuyên biệt cho đường đua.'),
('Grand Tourer', N'Siêu xe thể thao sang trọng, tiện nghi đỉnh cao phục vụ hành trình xuyên lục địa.'),
('Open-Top Spider', N'Phiên bản mui trần quyến rũ mang lại cảm giác lái thăng hoa cùng âm thanh động cơ thuần khiết.');
GO

-- Cars (ẢNH LOCAL)
INSERT INTO Cars (model_name, brand_id, category_id, price, deposit_rate, year, horsepower, acceleration_0_100, top_speed, stock_quantity, thumbnail_url, color_options, engine_spec, description, status) VALUES
(N'Bugatti Chiron Pur Sport', 1, 1, 3800000.00, 10.0, 2024, 1500, 2.3, 350, 2, 'assets/images/cars/bugatti-chiron.jpg', N'Jaune Molsheim & Carbon, French Racing Blue, Nocturne Black', N'8.0L Quad-Turbo W16 (1500 HP / 1600 Nm)', N'Phiên bản tập trung vào độ linh hoạt và vào cua siêu chuẩn xác với cánh gió sợi carbon cố định 1.9m.', 1),
(N'Ferrari SF90 Stradale Assetto Fiorano', 2, 1, 650000.00, 10.0, 2024, 1000, 2.5, 340, 3, 'assets/images/cars/ferrari-sf90.jpg', N'Rosso Corsa, Giallo Modena, Bianco Avus, Blu Pozzi', N'4.0L Twin-Turbo V8 + 3 Motor Điện Plug-in Hybrid', N'Siêu phẩm hybrid sạc ngoài mạnh nhất của Ferrari với hệ dẫn động 4 bánh toàn thời gian.', 1),
(N'Lamborghini Revuelto V12 HPEV', 3, 1, 608000.00, 10.0, 2025, 1015, 2.5, 350, 4, 'assets/images/cars/lamborghini-revuelto.jpg', N'Arancio Apodis, Verde Shock, Grigio Acheso, Blu Astraeus', N'6.5L Hút khí tự nhiên V12 + 3 Motor Điện (1015 HP)', N'Kỷ nguyên mới của siêu bò xứ Sant''Agata Bolognese với khung gầm Monofuselage carbon nguyên khối.', 1),
(N'Koenigsegg Jesko Absolut', 4, 2, 3400000.00, 15.0, 2024, 1600, 2.5, 500, 1, 'assets/images/cars/koenigsegg-jesko.jpg', N'Tang Orange, Crystal White, Imperial Blue, Raw Carbon', N'5.0L Twin-Turbo V8 Flat-plane (1600 HP E85)', N'Siêu phẩm được mệnh danh là mẫu xe nhanh nhất trong lịch sử loài người với hệ số cản khí động học chỉ 0.278 Cd.', 1),
(N'McLaren Senna LM Edition', 5, 3, 1500000.00, 10.0, 2023, 800, 2.8, 335, 1, 'assets/images/cars/mclaren-senna.jpg', N'McLaren Orange, Pure White, Volcano Red, Onyx Black', N'4.0L Twin-Turbo V8 (M840TR)', N'Được đặt theo tên huyền thoại Ayrton Senna, mang triết lý khí động học thuần chất đường đua F1.', 1),
(N'Pagani Huayra Roadster BC', 6, 5, 3500000.00, 20.0, 2024, 802, 2.8, 380, 1, 'assets/images/cars/pagani-huayra.jpg', N'Carbon Grigio, Blu Danubio, Rosso Dubai', N'6.0L Mercedes-AMG Twin-Turbo V12 (802 HP / 1050 Nm)', N'Tác phẩm nghệ thuật điêu khắc giới hạn 40 chiếc toàn cầu làm từ hợp chất Carbo-Triax HP62.', 1),
(N'Rimac Nevera Pure Electric', 8, 2, 2200000.00, 10.0, 2024, 1914, 1.81, 412, 2, 'assets/images/cars/rimac-nevera.jpg', N'Signature Blue, Lunar White, Stellar Black', N'4 Motor Điện Độc Lập All-Wheel Torque Vectoring (1914 HP)', N'Kỷ lục gia tăng tốc thế giới 0-100 km/h chỉ 1.81 giây, đỉnh cao công nghệ tương lai.', 1),
(N'Porsche 918 Spyder Weissach', 7, 5, 1800000.00, 10.0, 2023, 887, 2.6, 345, 2, 'assets/images/cars/porsche-918.jpg', N'Liquid Metal Silver, Acid Green, Guard Red', N'4.6L V8 Tự Nhiên + 2 Motor Điện (887 HP)', N'Huyền thoại bộ ba Holy Trinity với gói nâng cấp giảm trọng lượng sợi carbon Weissach Package.', 1);
GO

-- Car Images (ẢNH LOCAL)
INSERT INTO CarImages (car_id, image_url, caption) VALUES
(1, 'assets/images/cars/bugatti-chiron.jpg', N'Ngoại thất góc nghiêng cánh gió W16'),
(1, 'assets/images/cars/bugatti-chiron-interior.jpg', N'Khoang lái bọc da Alcantara chế tác thủ công'),
(2, 'assets/images/cars/ferrari-sf90.jpg', N'Ngoại thất đỏ Rosso Corsa huyền thoại'),
(3, 'assets/images/cars/lamborghini-revuelto.jpg', N'Đầu xe chữ Y vị lai Lamborghini Revuelto'),
(4, 'assets/images/cars/koenigsegg-jesko.jpg', N'Đuôi xe Koenigsegg Jesko Absolut khí động học');
GO

-- Coupons
INSERT INTO Coupons (coupon_code, discount_percent, max_discount, min_order_amount, expiry_date, is_active) VALUES
('VIP50K', 2.0, 50000.00, 500000.00, '2027-12-31 23:59:59', 1),
('HYPER2026', 3.0, 100000.00, 1000000.00, '2026-12-31 23:59:59', 1),
('SUPERWELCOME', 1.5, 30000.00, 300000.00, '2027-06-30 23:59:59', 1);
GO

-- Orders Demo (Đơn 1 có coupon, Đơn 2 không có coupon)
INSERT INTO Orders (order_code, user_id, total_amount, deposit_amount, coupon_code, discount_amount, status, payment_method, delivery_address, phone, note, order_date) VALUES
('ORD-2026-0001', 2, 3800000.00, 380000.00, 'VIP50K', 50000.00, N'CONFIRMED', N'BANK_TRANSFER', N'Biệt thự số 10 Thảo Điền, TP. Thủ Đức', '0912345678', N'Giao xe kèm đầy đủ bộ phụ kiện và đồng hồ Bugatti đi kèm.', DATEADD(day, -5, GETDATE())),
('ORD-2026-0002', 3, 608000.00, 60800.00, NULL, 0, N'PENDING', N'CRYPTO_USDT', N'Penthouse Landmark 81, TP. HCM', '0999999999', N'Yêu cầu giao xe bằng xe thùng kín chuyên dụng VIP.', DATEADD(day, -1, GETDATE()));
GO

-- Order Details Demo
INSERT INTO OrderDetails (order_id, car_id, quantity, unit_price, selected_color, custom_options) VALUES
(1, 1, 1, 3800000.00, N'Jaune Molsheim & Carbon', N'Cánh gió carbon full, mâm hợp kim Magie siêu nhẹ'),
(2, 3, 1, 608000.00, N'Arancio Apodis', N'Ống xả titan Akrapovic, nội thất chỉ thêu tương phản cam');
GO

-- Test Drive Bookings Demo
INSERT INTO TestDriveBookings (user_id, car_id, booking_date, time_slot, location_track, driver_license_number, note, status, created_at) VALUES
(2, 4, DATEADD(day, 3, CAST(GETDATE() AS DATE)), '09:00 - 11:00', N'Đường đua Sepang International Circuit (Chuyến bay VIP)', 'B2-99887766', N'Khách hàng có chứng chỉ FIA Super License quốc tế.', N'CONFIRMED', GETDATE()),
(3, 7, DATEADD(day, 5, CAST(GETDATE() AS DATE)), '14:00 - 16:00', N'Đường thử cao tốc F1 Hanoi Circuit', 'B2-11223344', N'Trải nghiệm gia tốc Nevera ở chế độ Launch Control.', N'PENDING', GETDATE());
GO

-- Car Reviews Demo (Mỗi user đánh giá 1 xe đúng 1 lần)
INSERT INTO CarReviews (user_id, car_id, rating, comment, created_at) VALUES
(2, 1, 5, N'Đỉnh cao của công nghệ động cơ W16. Cảm giác tăng tốc êm ái nhưng dính chặt lưng vào ghế. Đẳng cấp tuyệt đối!', DATEADD(day, -2, GETDATE())),
(3, 2, 5, N'Hệ thống hybrid của SF90 quá thông minh, vừa có thể chạy tĩnh lặng trong phố, vừa gầm rú mãnh liệt khi chuyển sang chế độ Qualify.', DATEADD(day, -1, GETDATE())),
(2, 4, 5, N'Cảm giác nhìn Jesko ngoài đời như phi thuyền không gian. Không có đối thủ!', GETDATE());
GO

-- Activity Logs Demo
INSERT INTO ActivityLogs (user_id, action, details, created_at) VALUES
(1, N'SYSTEM_INIT', N'Hệ thống HyperCar Sale System khởi tạo cơ sở dữ liệu thành công.', GETDATE()),
(2, N'PLACE_ORDER', N'Khách hàng John Wick đặt cọc thành công đơn hàng ORD-2026-0001 (Bugatti Chiron).', DATEADD(day, -5, GETDATE())),
(3, N'BOOK_TESTDRIVE', N'Khách hàng Tony Stark đặt lịch lái thử siêu xe Rimac Nevera.', DATEADD(day, -1, GETDATE()));
GO
