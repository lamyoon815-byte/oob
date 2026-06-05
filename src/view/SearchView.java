package view;

import model.Book;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

public class SearchView extends JPanel {
    private final JTextField keywordField = new JTextField(20);
    private final JButton searchButton = new JButton("검색");
    private final JTextArea resultArea = new JTextArea();
    private Consumer<String> onSearch;

    public SearchView() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel();
        top.add(new JLabel("검색어(제목/저자): "));
        top.add(keywordField);
        top.add(searchButton);

        resultArea.setEditable(false);
        resultArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resultArea.setMargin(new Insets(10, 10, 10, 10));

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);

        searchButton.addActionListener(e -> {
            if (onSearch != null) onSearch.accept(keywordField.getText().trim());
        });
    }

    public void setOnSearch(Consumer<String> onSearch) {
        this.onSearch = onSearch;
    }

    public String getKeyword() {
        return keywordField.getText().trim();
    }

    // 수정된 부분: 검색 결과가 0개일 때도 UI 틀을 유지합니다.
    public void showSearchResult(List<Book> books) {
        StringBuilder sb = new StringBuilder();
        int bookCount = (books == null) ? 0 : books.size(); // 검색된 책의 권수 확인

        // 검색 결과 유무와 상관없이 항상 고정으로 출력되는 상단 틀
        sb.append("========================================\n");
        sb.append("  📚 도서 검색 결과\n");
        sb.append("========================================\n");
        sb.append("총 ").append(bookCount).append("권의 도서가 있습니다.\n\n");

        if (bookCount == 0) {
            // 결과가 없을 때 출력되는 메시지
            sb.append(" • 입력하신 검색어와 일치하는 도서가 없습니다.\n");
        } else {
            // 결과가 있을 때 출력되는 도서 리스트
            for (Book b : books) {
                sb.append(" • ").append(b.toString()).append("\n"); 
            }
        }
        
        resultArea.setText(sb.toString());
        SwingUtilities.invokeLater(() -> resultArea.setCaretPosition(0));
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}