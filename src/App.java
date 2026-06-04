import controller.*;
import model.*;
import view.*;

public class App {
    public static void main(String[] args) {
        // 1. 모델 (데이터 저장소) 생성
        BookRepository bookRepo = new BookRepository();
        LoanRepository loanRepo = new LoanRepository();
        MemberRepository memberRepo = new MemberRepository();

        // 2. 뷰 (메인 GUI 프레임) 생성
        MainFrame mainFrame = new MainFrame();

        // 3. 컨트롤러 생성 및 뷰-모델 연결
        // MainFrame 안에 있는 각각의 뷰(탭)를 꺼내서 컨트롤러에 연결해 줍니다.
        new SearchController(bookRepo, mainFrame.getSearchView());
        new LoanController(bookRepo, loanRepo, memberRepo, mainFrame.getLoanView());
        new ReturnController(bookRepo, loanRepo, mainFrame.getReturnView());
        new MemberController(memberRepo, loanRepo, mainFrame.getMemberView());

        // 4. 화면 띄우기!
        mainFrame.setVisible(true);
    }
}