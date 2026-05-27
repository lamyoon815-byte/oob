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

    public void showSearchResult(List<Book> books) {
        resultArea.setText("");
        if (books == null || books.isEmpty()) {
            resultArea.append("검색 결과가 없습니다.\n");
            return;
        }
        for (Book b : books) resultArea.append("- " + b + "\n");
    }

    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}
