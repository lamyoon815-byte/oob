package model; 

public class Member { 
    private String memberId;   // 회원 ID (로그인용)
    private String password;  // 비밀번호
    private String name;  // 이름
    private String phoneNumber;  // 연락처

    //회원 가입 시 모든 정보를 한 번에 받아 객체 생성.
    public Member(String memberId, String password, String name, String phoneNumber) {   
        this.memberId = memberId; 
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // 읽기 전용 getter
    public String getMemberId() { return memberId; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }

    // 비밀번호 검증
    public boolean checkPassword(String inputPassword) {  
        return this.password.equals(inputPassword);
    }

    //회원 정보를 CSV 한 줄로 변환 (저장용)
    public String toCsvString() {   
        return memberId + "," + password + "," + name + "," + phoneNumber;
    }

    //파일에서 읽어온 텍스트를 콤마 단위로 분해해 Member 객체로 복원
    public static Member fromCsvString(String line) {
        String[] parts = line.split(","); 
        return new Member(parts[0], parts[1], parts[2], parts[3]);
    }

     //출력 형식 (비밀번호 노출 X) 
    @Override
    public String toString() {
        return "[" + memberId + "] " + name + " (연락처: " + phoneNumber + ")";
    }
}
