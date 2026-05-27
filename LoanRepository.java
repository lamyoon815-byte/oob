import java.io.*;  //파일 읽기/쓰기, 콘솔 입력 등 컴퓨터와 데이터를 주고받는(Input/Output) 도구들을 통째로 가져옴
import java.util.ArrayList;  //여러 개의 데이터를 순서대로 담을 수 있는 '목록(List)'이라는 표준 규격(인터페이스)을 가져옵니다.
import java.util.List; //데이터를 원하는 만큼 늘렸다 줄였다 하며 자유롭게 저장할 수 있는 '가변형 배열 상자'를 가져옴. (위 List의 대표적인 실체)
import java.util.stream.Collectors; //데이터를 필터링하거나 변환하는 '스트림(Stream)' 작업의 결과를 다시 리스트나 셋(Set) 등으로 예쁘게 포장해 주는 수집기.

public class LoanRepository {                               //Loan 객체의 저장소, 대출 생성, 중복 대출 방지, 연체 조회
    private List<Loan> loanList = new ArrayList<>(); //모든 대출 기록을 담는 리스트(반납 완료된 대출도 그대로 보관 — 이력 추적용).
    private static final String FILE_PATH = "loans.csv";     // CSV 파일 경로 상수  
    private int loanCounter = 1;     
    // 새 대출 ID(L001, L002, ...)를 만들 때 사용하는 일련번호.
    // 1부터 시작해 createLoan 호출 시마다 1씩 늘어난다.
    // 'int' 는 정수형. 기본값은 0이지만 여기서는 1로 명시 초기화.                      

    // 새 대출 생성
    public Loan createLoan(String memberId, String bookId) { 
        String loanId = "L" + String.format("%03d", loanCounter++); //Loan의 생성자 #1을 호출해 새 대출, 객체 만듦
        Loan newLoan = new Loan(loanId, memberId, bookId);   
        loanList.add(newLoan);  //만든 객체를 내부 리스트에 추가
        return newLoan;
    }

    // 대출번호로 조회
    public Loan findById(String loanId) {
        return loanList.stream()
                .filter(loan -> loan.getLoanId().equals(loanId)) //대출번호 일치하는 것만 통과.
                .findFirst()  //첫 번째 결과를 Optional로 받음.
                .orElse(null);  //없으면 null.
    }

    // 해당 도서가 현재 대출 중인지 (중복 대출 방지용)
    public boolean isBookOnLoan(String bookId) {   
        // anyMatch(조건) : 리스트 중 "단 하나라도 조건을 만족"하면 true, 그렇지 않으면 false.          
        return loanList.stream()
                .anyMatch(loan -> loan.getBookId().equals(bookId) && loan.isActive());
                //조건: 책 id가 같다, 현재 대출 중이다, 둘 모두 true여야 전체 true
    }

    // 특정 회원의 대출 이력
    public List<Loan> findByMemberId(String memberId) {
        return loanList.stream()
                .filter(loan -> loan.getMemberId().equals(memberId)) //회원 ID 일치하는 것만 통과.
                .collect(Collectors.toList()); //다시 리스트로 모아 돌려준다.
    }

    // 연체 도서 목록
    public List<Loan> findOverdueLoans() {                   
        return loanList.stream()
                .filter(Loan::isOverdue)  // 연체된 것만 통과. 로직은 Loan안에 있
                .collect(Collectors.toList());  // 리스트로 모음.
    }

    //대출 목록을 csv 파일에 저
    public void saveToFile() throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Loan loan : loanList) {
                bufferedWriter.write(loan.toCsvString());  // 한 줄로 변환해 파일에 기록.
                bufferedWriter.newLine();
            }
        }
    }

    //대출 목록 복
    public void loadFromFile() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;  // 파일 없으면 메소드 탈출.

        loanList.clear();  // 기존 리스트 비우기(중복 적재 방지).

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {  // 파일 끝까지 한 줄씩.
                if (!line.trim().isEmpty()) {   // 빈 줄은 건너뜀.
                    loanList.add(Loan.fromCsvString(line));    // 한 줄 → Loan 객체 복원해 리스트 추가.
                }
            }
        }
        loanCounter = loanList.size() + 1;
        //카운터 동기화  (//   loanList.size() == 5 이므로 +1 해서 6을 다음 번호로 세팅.)
    }
}
