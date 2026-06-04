package controller;

import model.Book;
import model.BookRepository;
import view.SearchView;

import java.util.List;

// 도서 검색 요청 처리 
public class SearchController {
    private final BookRepository bookRepository; // 도서 데이터 저장소
    private final SearchView searchView; // ui


    // SearchController 만들 때 호출되는 생성자
    // bookRepository와 searchView를 받아 필드에 저장, 검색 버튼이 눌렸을 때 handleSearch 매소드를 View에 등록
    public SearchController(BookRepository bookRepository, SearchView searchView) {
        this.bookRepository = bookRepository; 
        this.searchView = searchView; 

        searchView.setOnSearch(this::handleSearch); // 검색 버튼 클릭 이벤트 연결 
    }


    // 검색시 호출되는 매소드 
    private void handleSearch(String keyword) {
        List<Book> results;
        if (keyword == null || keyword.trim().isEmpty()) {
            results = bookRepository.findAll(); // 검색어 없으면 전체 반환 
        } else {
            results = bookRepository.searchByKeyword(keyword.trim()); // 키워드 검색
        }
        searchView.showSearchResult(results); // 검색 결과 화면에 출력

    }

    // 도서 id로 도서를 조회하여 반환, 없으면 null 반환

    public Book findBookById(String bookId) {
        return bookRepository.findById(bookId);
    }
}
