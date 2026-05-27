package view;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final SearchView searchView;
    private final LoanView loanView;
    private final ReturnView returnView;
    private final MemberView memberView;

    public MainFrame() {
        setTitle("도서관 관리 시스템");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        searchView = new SearchView();
        loanView = new LoanView();
        returnView = new ReturnView();
        memberView = new MemberView();

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("도서 검색", searchView);
        tabs.addTab("대출", loanView);
        tabs.addTab("반납", returnView);
        tabs.addTab("회원", memberView);

        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
    }

    public SearchView getSearchView() { return searchView; }
    public LoanView getLoanView() { return loanView; }
    public ReturnView getReturnView() { return returnView; }
    public MemberView getMemberView() { return memberView; }
}
