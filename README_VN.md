# Dự Án Mẫu Playwright Java (Playwright Java Boilerplate)

Dự án này là một khung mẫu toàn diện để kiểm thử các ứng dụng web sử dụng Playwright và Java. Nó hỗ trợ cả kiểm thử độc lập các URL công khai và kiểm thử tích hợp các ứng dụng cục bộ sử dụng Docker Compose.

> [!NOTE]
> 👉 **[Switch to English / Chuyển sang Tiếng Anh](README.md)**

## 1. Yêu Cầu

- **Java JDK 17+**
- **Maven** (hoặc dùng file `mvnw` có sẵn trong dự án)

---

## 2. Cấu Hình

Bạn có thể cấu hình việc chạy test thông qua file `src/test/resources/config.properties` hoặc sử dụng **Biến Môi Trường** (System Environment Variables).

Các tùy chọn chính:
- `base.url`: Địa chỉ URL cần test.
- `headless`: Chạy ẩn (`true`) hoặc hiện trình duyệt (`false`).
- `browser`: Trình duyệt `chromium`, `firefox`, hoặc `webkit`.

**Độ ưu tiên:** Biến môi trường > Thuộc tính hệ thống (-D...) > File cấu hình.

---

## 3. Cách Chạy (Hướng Dẫn)

### Chế Độ A: Đơn Lẻ (Test URL Công Khai)

Chạy test trực tiếp trên máy cá nhân của bạn để kiểm thử các trang web bên ngoài hoặc localhost.

```bash
# Chạy tất cả các test
./mvnw test

# Chạy một test cụ thể
./mvnw test -Dtest=ExampleTest

# Chạy với cấu hình tùy chỉnh (ví dụ: đổi URL và hiện trình duyệt)
./mvnw test -Dbase.url=https://google.com -Dheadless=false

### Chế Độ B: Chạy Bằng Docker

Chạy test trong Docker container để đảm bảo môi trường đồng nhất.

```bash
# Build image
docker build -t playwright-tests .

# Chạy container (mặc định chạy lệnh ./mvnw test)
docker run --rm --ipc=host playwright-tests
```

**Lưu ý:** Tham số `--ipc=host` được khuyến nghị để Playwright/Chrome chạy ổn định.

---

## 4. Xem Báo Cáo

Chạy lệnh sau để xem báo cáo Allure trên trình duyệt (yêu cầu máy đã cài Allure command line, hoặc cài qua brew/npm):

```bash
allure serve target/allure-results
```

---

## 5. Tích Hợp CI/CD (Jenkins)

Dự án đã bao gồm file `Jenkinsfile` để tích hợp dễ dàng.

### Yêu cầu trên Jenkins Agent:
1. **Java 17**: Phải được cài đặt và config (hoặc có sẵn trong PATH).
2. **Allure Plugin**: (Tùy chọn) Để xem báo cáo đẹp trực tiếp trên Jenkins.

### Các bước trong Pipeline:
1. **Initialize**: Cấp quyền chạy cho file `mvnw`.
2. **Install Browsers**: Tự động tải và cài đặt trình duyệt cần thiết cho Playwright.
3. **Run Tests**: Chạy toàn bộ test case.
4. **Publish Reports**: Xuất báo cáo JUnit XML và Allure.

---

## Cấu Trúc Dự Án

```text
playwright-java-boilerplate/
├── src/main/java       # Page Objects, Config, Utils (Mã nguồn chính)
├── src/test/java       # Tests, BaseTest (Mã nguồn test)
├── src/test/resources  # Config properties (Cấu hình)
├── Jenkinsfile         # Cấu hình CI/CD Pipeline
├── Dockerfile          # Định nghĩa môi trường chạy Test
└── pom.xml             # Quản lý thư viện phụ thuộc (Maven)
```

