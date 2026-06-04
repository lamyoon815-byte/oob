package controller;

import model.Book;
import model.Loan;
import model.Member;
import model.BookRepository;
import model.LoanRepository;
import model.MemberRepository;
import view.LoanView;

// 도서 대출 요청을 처리하는 컨트롤러 (회원/도서 존재 여부, 중복 대출 여부를 검증한 후 대출 처리)
public class LoanController {
    private final BookRepository bookRepository; // 도서 데이터 저장소
    private final LoanRepository loanRepository; // 대출 데이터 저장소
    private final MemberRepository memberRepository; // 회원 데이터 저장소
    private final LoanView loanView; // ui

    // LoanController 객체를 만들 때 호출되는 생성자 (대출출 버튼 클릭 이벤트 발생시 handleLoan 메소드 연결)
    public LoanController(BookRepository bookRepository, 
                          LoanRepository loanRepository,
                          MemberRepository memberRepository,
                          LoanView loanView) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
        this.memberRepository = memberRepository;
        this.loanView = loanView;

        loanView.setOnLoan(this::handleLoan); // 대출 버튼 이벤트 연결
    }

    private synchronized void handleLoan(String memberId, String bookId) {
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            loanView.showLoanFail("존재하지 않는 회원입니다."); // 회원 존재 확인 후 메세지 출력 
            return; 
        }

        Book book = bookRepository.findById(bookId);
        if (book == null) {
            loanView.showLoanFail("존재하지 않는 도서입니다."); // 도서 존재 확인 후 없으면 메세지 출력 
            return;
        }

        if (loanRepository.isBookOnLoan(bookId)) {
            loanView.showDuplicateLoanMessage(); 
            return;
        }

        if (!book.isAvailable()) {
            loanView.showLoanFail("대출 불가능한 도서입니다."); // 이미 대출 중이면 메세지 출력 (중복 대출을 방지)
            return;
        }

        book.setAvailable(false); // 도서 상태를 대출중으로 변경 
        Loan newLoan = loanRepository.createLoan(memberId, bookId); // 대출 기록 생성
        loanView.showLoanSuccess(newLoan); // 대출 성공 화면 출력 
    }
}