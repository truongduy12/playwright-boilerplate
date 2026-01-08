# Hướng dẫn Setup Jenkins Local cho Playwright Java (macOS)

Tài liệu này hướng dẫn bạn cách cài đặt Jenkins trực tiếp trên macOS (Native) và cấu hình để chạy test Playwright cũng như hiển thị báo cáo Allure.
Yêu cầu: source hiện tại đã run test được bằng mvn test.

---

## 1. Cài đặt Jenkins
Cách dễ nhất để cài đặt Jenkins trên macOS là sử dụng [Homebrew](https://brew.sh/).

1.  **Cài đặt Jenkins LTS:**
    ```bash
    brew install jenkins-lts
    ```
2.  **Khởi chạy Jenkins:**
    ```bash
    brew services start jenkins-lts
    ```
3.  **Truy cập Jenkins:**
    Mở trình duyệt và truy cập: `http://localhost:8080`
4.  **Mở khóa Jenkins:**
    Lấy mật khẩu admin bằng lệnh:
    ```bash
    cat /usr/local/var/jenkins/home/secrets/initialAdminPassword
    ```
    (Hoặc `/Users/<User>/.jenkins/secrets/initialAdminPassword`)

---

## 2. Cài đặt Plugins cần thiết
Sau khi setup cơ bản, hãy vào **Manage Jenkins** > **Plugins** > **Available Plugins** và cài đặt các plugin sau:
*   **Allure Jenkins Plugin**: Để hiển thị báo cáo Allure.
*   **JUnit Plugin**: Để hiển thị báo cáo test tiêu chuẩn (thường đã có sẵn).
*   **Pipeline**: Để chạy project theo script `Jenkinsfile`.

---

## 3. Cấu hình Công cụ (Global Tool Configuration)
Vào **Manage Jenkins** > **Tools**:

### JDK (Java Development Kit)
*   Thêm JDK 17 (Cần thiết cho Playwright và dự án này).
*   *Lưu ý:* Nếu bạn đã cài JDK 17 trên máy, chỉ cần trỏ đường dẫn `JAVA_HOME`.

echo $JAVA_HOME

### Maven
*   Thêm Maven (ví dụ: đặt tên là `Maven 3.9`).
*   Chọn "Install automatically" hoặc trỏ tới thư mục Maven đã cài trên máy.

mvn -version


### Allure Commandline
Đây là công cụ cần thiết để Jenkins có thể biên dịch các file kết quả (`target/allure-results`) thành giao diện báo cáo HTML thân thiện.

*   Nhấn **Add Allure Commandline**.
*   **Name**: Nhập chính xác là `allure-commandline` (Tên này sẽ được Plugin Allure sử dụng để tìm công cụ).
*   Chọn **Install automatically**.
*   **Install from Maven Central**: Chọn phiên bản mới nhất (ví dụ: 2.25.0 hoặc mới hơn).
*   Jenkins sẽ tự động tải và cài đặt công cụ này khi Pipeline chạy lần đầu tiên.



## 4. Tạo Job Pipeline
1.  Tại trang chủ Jenkins, chọn **New Item**.
2.  Nhập tên (ví dụ: `Playwright-Java-Boilerplate`), chọn **Pipeline** và nhấn **OK**.
3.  Trong phần **Pipeline**:
    *   **Definition**: Chọn *Pipeline script from SCM*.
    *   **SCM**: Chọn *Git*.
    *   **Repository URL**: Đường dẫn tới repo của bạn (hoặc đường dẫn local).
    *   **Script Path**: Đảm bảo là `Jenkinsfile`.
    * **Phải commit changes thì mới có tác dụng khi build**
---

## 5. Lưu ý quan trọng cho Playwright (Native macOS)
Dự án đã có sẵn `Jenkinsfile`. Khi chạy lần đầu, stage `Install Dependencies & Browsers` sẽ tự động tải các trình duyệt cần thiết của Playwright về máy local.

Nếu xảy ra lỗi thiếu thư viện hệ thống (thường hiếm gặp trên macOS), bạn có thể cần chạy lệnh sau thủ công một lần:
```bash
npx playwright install-deps
```

---

## 6. Xem Báo cáo
*   **JUnit Report**: Hiển thị ngay trong dashboard của từng Build.
*   **Allure Report**: Sau khi build thành công, bạn sẽ thấy biểu tượng **Allure Report** ở menu bên trái của Build đó. Nhấn vào để xem báo cáo chi tiết với các bước (steps) và screenshot (nếu có).

---

## 7. Giải quyết lỗi thường gặp (Troubleshooting)

### Lỗi: fatal: couldn't find remote ref refs/heads/master
Lỗi này xảy ra khi Jenkins mặc định tìm nhánh `master`, nhưng dự án của bạn sử dụng nhánh `main`.

**Cách sửa:**
1.  Vào cấu hình Job của bạn (**Configure**).
2.  Tìm phần **Branch Specifier (blank for 'any')**.
3.  Đổi `*/master` thành `*/main`.
4.  Lưu và chạy lại Build (**Build Now**).

### Lỗi: Checkout of Git remote ... aborted because it references a local directory
Jenkins coi việc checkout từ thư mục local là không an toàn (Insecure) theo mặc định khi dùng plugin Git mới.

**Cách sửa (Dành cho Jenkins cài bằng Homebrew - macOS):**
1.  Mở file cấu hình dịch vụ của Jenkins:
    ```bash
    nano /opt/homebrew/opt/jenkins-lts/homebrew.mxcl.jenkins-lts.plist
    ```
    *(Nếu dùng máy Intel: `/usr/local/opt/jenkins-lts/homebrew.mxcl.jenkins-lts.plist`)*
2.  Tìm phần `<key>ProgramArguments</key>` và thêm dòng sau vào ngay sau `<string>java</string>`:
    ```xml
    <string>-Dhudson.plugins.git.GitSCM.ALLOW_LOCAL_CHECKOUT=true</string>
    ```
3.  Lưu file (`Ctrl+O`, `Enter`, `Ctrl+X`).
4.  Khởi động lại dịch vụ:
    ```bash
    brew services restart jenkins-lts
    ```

**Cách 2 (Dùng Script Console - Thử lại):**
Đảm bảo bạn dán đúng lệnh này và nhấn **Run**:
```groovy
hudson.plugins.git.GitSCM.ALLOW_LOCAL_CHECKOUT = true
```
*(Nếu vẫn không được, hãy dùng Cách 1 ở trên).*

### Lỗi: mvn: command not found
Jenkins không tìm thấy lệnh `mvn` vì biến môi trường PATH chưa được thiết lập.

**Cách sửa:**
Sử dụng block `tools` trong `Jenkinsfile` để Jenkins tự động nạp đường dẫn:

1. Đảm bảo trong **Manage Jenkins > Tools**, bạn đã đặt tên cho Maven là `Maven 3` và JDK là `jdk-17`.
2. `Jenkinsfile` hiện tại đã được cập nhật để sử dụng các tên này.

```groovy
tools {
    maven 'Maven 3'
    jdk 'jdk-17'
}
```

