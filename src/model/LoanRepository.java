package model; 

import java.io.*; 
import java.util.ArrayList;  
import java.util.List; 
import java.util.stream.Collectors;

public class LoanRepository {                     //Loan 객체의 저장소, 대출 생성, 중복 대출 방지, 연체 조회
    private List<Loan> loanList = new ArrayList<>(); // 전체 대출 기 아카이빙 리스트
    private static final String FILE_PATH = "loans.csv";     // CSV 파일 경로 상수  
    private int loanCounter = 1;     // 대출 ID 고유성 보장을 위한 내부 인덱스 변수                   

    //새 대출번호 자동 생성 후 장부에 등록
    //번호 중복 방지로 대출하자마자 번호에 +1
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

    // 해당 도서가 현재 대출 중인지 확인 (중복 대출 방지용)
    public boolean isBookOnLoan(String bookId) {   
        //리스트 중 하나라도 조건을 만족하면 true, 그렇지 않으면 false.          
        return loanList.stream()
                .anyMatch(loan -> loan.getBookId().equals(bookId) && loan.isActive());
                //책 id가 같다, 현재 대출 중이다, 둘 모두 true여야 전체 true
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

    //대출 목록을 csv 파일에 저장
    public void saveToFile() throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Loan loan : loanList) {
                bufferedWriter.write(loan.toCsvString());  // 한 줄로 변환해 파일에 기록.
                bufferedWriter.newLine();
            }
        }
    }

    //대출 기록 불러오기, 다음 대출 번호 맞추
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
        //다음 신규 번호가 기존 번호와 겹치지 않도록 +1
    }
}
