package view;

import model.Loan;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.function.BiConsumer;

public class LoanView extends JPanel {
    private final JTextField memberIdField = new JTextField(12);
    private final JTextField bookIdField = new JTextField(12);
    private final JButton loanButton = new JButton("대출");
    private final JTextArea resultArea = new JTextArea();

    private BiConsumer<String, String> onLoan;
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LoanView() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.add(new JLabel("회원 ID:"));
        top.add(memberIdField);
        top.add(new JLabel("도서 ID:"));
        top.add(bookIdField);
        top.add(loanButton);

        resultArea.setEditable(false);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        loanButton.addActionListener(e -> {
            if (onLoan != null) onLoan.accept(getMemberIdInput(), getBookIdInput());
        });
    }

    public void setOnLoan(BiConsumer<String, String> onLoan) {
        this.onLoan = onLoan;
    }

    public String getMemberIdInput() {
        return memberIdField.getText().trim();
    }

    public String getBookIdInput() {
        return bookIdField.getText().trim();
    }

    public void showLoanSuccess(Loan loan) {
        resultArea.setText("");
        resultArea.append("대출 완료\n");
        resultArea.append("- 대출번호: " + loan.getLoanId() + "\n");
        resultArea.append("- 반납기한: " + loan.getDueDate().format(F) + "\n");
    }

    public void showLoanFail(String reason) {
        JOptionPane.showMessageDialog(this, "대출 실패: " + reason);
    }

    public void showDuplicateLoanMessage() {
        JOptionPane.showMessageDialog(this, "이미 대출 중인 도서입니다.");
    }
}
