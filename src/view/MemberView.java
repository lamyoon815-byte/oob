package view;

import model.Loan;
import model.Member;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class MemberView extends JPanel {
    private final JTextField nameField = new JTextField(10);
    private final JTextField phoneField = new JTextField(10);
    private final JTextField memberIdField = new JTextField(10);
    private final JPasswordField passwordField = new JPasswordField(10);

    private final JButton signUpButton = new JButton("회원가입");
    private final JButton historyButton = new JButton("대출이력 조회");
    private final JTextArea resultArea = new JTextArea();

    private Consumer<Member> onSignUp;
    private Consumer<String> onHistorySearch;

    public MemberView() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new GridLayout(3, 4, 8, 8));
        top.add(new JLabel("이름"));
        top.add(nameField);
        top.add(new JLabel("연락처"));
        top.add(phoneField);
        top.add(new JLabel("회원 ID"));
        top.add(memberIdField);
        top.add(new JLabel("비밀번호"));
        top.add(passwordField);
        top.add(signUpButton);
        top.add(historyButton);

        resultArea.setEditable(false);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        signUpButton.addActionListener(e -> {
            if (onSignUp != null) onSignUp.accept(getSignUpInfo());
        });

        historyButton.addActionListener(e -> {
            if (onHistorySearch != null) onHistorySearch.accept(memberIdField.getText().trim());
        });
    }

    public void setOnSignUp(Consumer<Member> onSignUp) {
        this.onSignUp = onSignUp;
    }

    public void setOnHistorySearch(Consumer<String> onHistorySearch) {
        this.onHistorySearch = onHistorySearch;
    }

    public Member getSignUpInfo() {
        return new Member(
                memberIdField.getText().trim(),
                new String(passwordField.getPassword()),
                nameField.getText().trim(),
                phoneField.getText().trim()
        );
    }

    public void showSignUpSuccess() {
        JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다.");
    }

    public void showDuplicateIdMessage() {
        JOptionPane.showMessageDialog(this, "이미 사용 중인 회원 ID입니다.");
    }

    public void showMemberInfo(Member member) {
        resultArea.setText("[회원 정보]\n" + member + "\n");
    }

    public void showMemberList(List<Member> members) {
        resultArea.setText("[회원 목록]\n");
        if (members == null || members.isEmpty()) {
            resultArea.append("- 회원이 없습니다.\n");
            return;
        }
        for (Member m : members) resultArea.append("- " + m + "\n");
    }

    public void showMemberLoans(String memberId, List<Loan> loans) {
        resultArea.setText("[" + memberId + "님의 대출 이력]\n");
        if (loans == null || loans.isEmpty()) {
            resultArea.append("- 대출 이력이 없습니다.\n");
            return;
        }
        for (Loan loan : loans) resultArea.append("- " + loan + "\n");
    }
}
