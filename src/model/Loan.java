package model; 

import java.time.LocalDate; // 날짜 클래스
import java.time.format.DateTimeFormatter; // 날짜 ↔ 문자열 변환 도구
import java.time.temporal.ChronoUnit; // 날짜 간격 계산 (일/월/년)


//대출에 필요한 정보
public class Loan {                                       
    private String loanId;   //대출 고유번호 (예: "L001")
    private String memberId;  //빌린 사람의 회원 ID
    private String bookId;  //빌려간 책 ID
    private LocalDate loanDate;  //대출일
    private LocalDate returnDate;  //반납일 (null이면 미반납)


    //대출의 기본 규칙
    private static final int LOAN_PERIOD_DAYS = 14;  //대출 기간: 14일. 바꿀 수 없는 고정 상수
    private static final DateTimeFormatter DATE_FORMATTER
            = DateTimeFormatter.ofPattern("yyyy-MM-dd");  //날짜 표시 형식
 
    // 새 대출 생성
    public Loan(String loanId, String memberId, String bookId) {
        this.loanId = loanId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.loanDate = LocalDate.now();   //시스템의 오늘 날짜
        this.returnDate = null;    //아직 반납 전
    }

    // CSV 로드용
    public Loan(String loanId, String memberId, String bookId,
                LocalDate loanDate, LocalDate returnDate) {
        this.loanId = loanId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
    }

    // Getter  private 필드 접근자
    public String getLoanId() { return loanId; }
    public String getMemberId() { return memberId; }
    public String getBookId() { return bookId; }
    public LocalDate getLoanDate() { return loanDate; }
    public LocalDate getReturnDate() { return returnDate; }

   

    //반납 기한일 계산 (대출일 + 14일)
    public LocalDate getDueDate() {                         
        return loanDate.plusDays(LOAN_PERIOD_DAYS);         
    }

    // 현재 대출 상태 (null이면 아직 안 돌려준 상태)
    public boolean isActive() {                             
        return returnDate == null;
    }

    //연체 여부 판별
    public boolean isOverdue() {                           
        if (!isActive()) return false;   // 이미 반납했으면 연체 아님
        return LocalDate.now().isAfter(getDueDate());  // 오늘 > 기한일 이면 연체
    }

    // 연체 일수 계산 (연체가 아니면 0)
    public long calculateOverdueDays() {                    
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(getDueDate(), LocalDate.now());   // ChronoUnit.DAYS.between(a, b) : a부터 b까지의 일수 차이.
    }

    //반납 처리
    public void processReturn() {                          
        this.returnDate = LocalDate.now();
         // 이 메소드를 호출한 시점부터 isActive()가 false가 되고, isOverdue()도 false를 반환하게 된다.
    }

    //CSV 변환
    public String toCsvString() {
        String returnDateString = (returnDate == null) ? "null" : returnDate.format(DATE_FORMATTER);        
        return loanId + "," + memberId + "," + bookId + "," + loanDate.format(DATE_FORMATTER) + "," + returnDateString;
    }

    //CSV 한 줄을 Loan 객체로 복원 및 호출
    public static Loan fromCsvString(String line) {
        String[] parts = line.split(",");
        LocalDate parsedLoanDate = LocalDate.parse(parts[3], DATE_FORMATTER);
        LocalDate parsedReturnDate = parts[4].equals("null") ? null : LocalDate.parse(parts[4], DATE_FORMATTER);
        // 저장 시 "null"이라 적어뒀던 경우 다시 null로 복원
        
        return new Loan(parts[0], parts[1], parts[2], parsedLoanDate, parsedReturnDate);
    }

    @Override
    public String toString() {
        String loanStatus;                                 
        if (isActive()) {
            loanStatus = isOverdue()
                    ? "⚠ 연체 " + calculateOverdueDays() + "일"
                    : "대출중";
        } else {
            loanStatus = "반납완료(" + returnDate.format(DATE_FORMATTER) + ")";
        }
        return "[" + loanId + "] 도서:" + bookId + " 회원:" + memberId
             + " 대출일:" + loanDate.format(DATE_FORMATTER)
             + " 반납기한:" + getDueDate().format(DATE_FORMATTER)
             + " [" + loanStatus + "]";
    }
}
