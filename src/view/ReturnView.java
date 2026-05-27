package view;

import model.Loan;

import javax.swing.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.function.Consumer;

public class ReturnView extends JPanel {
    private final JTextField loanIdField = new JTextField(12);
    private final JButton returnButton = new JButton("반납");
    private final JTextArea resultArea = new JTextArea();

    private Consumer<String> onReturn;
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ReturnView() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.add(new JLabel("대출번호:"));
        top.add(loanIdField);
        top.add(returnButton);

        resultArea.setEditable(false);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        returnButton.addActionListener(e -> {
            if (onReturn != null) onReturn.accept(getLoanIdInput());
        });
    }

    public void setOnReturn(Consumer<String> onReturn) {
        this.onReturn = onReturn;
    }

    public String getLoanIdInput() {
        return loanIdField.getText().trim();
    }

    public void showReturnSuccess(Loan loan, long overdueDaysAtReturn) {
        resultArea.setText("");
        resultArea.append("반납 완료\n");
        resultArea.append("- 대출번호: " + loan.getLoanId() + "\n");
        resultArea.append("- 도서ID: " + loan.getBookId() + "\n");
        resultArea.append("- 반납일: " + loan.getReturnDate().format(F) + "\n");
        resultArea.append("- 연체일수: " + overdueDaysAtReturn + "일\n");
    }

    public void showReturnFail(String message) {
        JOptionPane.showMessageDialog(this, "반납 실패: " + message);
    }
}
