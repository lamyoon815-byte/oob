package controller;

import model.Book;
import model.Loan;
import model.BookRepository;
import model.LoanRepository;
import view.ReturnView;

// 도서 반납 요청을 처리하는 컨트롤러 (대출 번호로 대출 기록을 찾아 반납 처리하고 연체일수를 계산해 화면에 전달)
public class ReturnController {
    private final BookRepository bookRepository; // 도서 데이터 저장소 
    private final LoanRepository loanRepository; // 대출 데이터 저장소 
    private final ReturnView returnView; //ui

    // ReturnController 객체를 만들 때 호출되는 생성자 
    public ReturnController(BookRepository bookRepository,
                            LoanRepository loanRepository,
                            ReturnView returnView) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.returnView = returnView; 

        returnView.setOnReturn(this::handleReturn); // 반납 버튼 이벤트 연결 
    }

    private void handleReturn(String loanId) {
        Loan loan = loanRepository.findById(loanId);
        if (loan == null) {
            returnView.showReturnFail("존재하지 않는 대출 번호입니다."); // 대출 번호로 대출 기록 조회 후 기록 없으면 실패 메시지
            return;
        }

        if (!loan.isActive()) {
            returnView.showReturnFail("이미 반납된 도서입니다."); // 이미 반납된 도서인지 확인 후 반납된 경우 실패 메시지
            return;
        }

        long overdueDays = loan.calculateOverdueDays(); // 반납 처리 전에 연체일수 미리 계산


        loan.processReturn(); // 반납 처리 (returnDate를 오늘 날짜로 설정)

        Book book = bookRepository.findById(loan.getBookId());
        if (book != null) {
            book.setAvailable(true); // 도서 상태를 대출 가능으로 변경 
        }

        returnView.showReturnSuccess(loan, overdueDays); // 결과 화면 출력 
    }
}