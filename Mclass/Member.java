public class Member { 
    private String memberId;   // 회원 ID (로그인용)
    private String password;  // 비밀번호
    private String name;  // 이름
    private String phoneNumber;  // 연락처

    public Member(String memberId, String password, String name, String phoneNumber) {   // 생성자. 회원 가입 시 모든 정보를 한 번에 받아 객체 생성.
        this.memberId = memberId; 
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // Getter private 필드를 외부에서 읽기 위한 접근자
    public String getMemberId() { return memberId; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }

    // 비밀번호 검증
    public boolean checkPassword(String inputPassword) {  // ✅ 매개변수: 축약 없이 명확
        return this.password.equals(inputPassword);
    }

    public String toCsvString() {   // [toCsvString] 회원 정보를 CSV 한 줄로 변환 (저장용)
        return memberId + "," + password + "," + name + "," + phoneNumber;
    }

    // fromCsvString. CSV 한 줄> Member 객체 (로드용)
    public static Member fromCsvString(String line) {
        String[] parts = line.split(","); // 콤마로 잘라 배열로 만
        return new Member(parts[0], parts[1], parts[2], parts[3]);
    }

     // toString. 출력 형식 (비밀번호는 보안상 노출하지 않음)
    @Override
    public String toString() {
        return "[" + memberId + "] " + name + " (연락처: " + phoneNumber + ")";
    }
}
