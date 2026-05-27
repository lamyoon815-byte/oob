import java.io.*;  // 파일 입출력 클래스들 (BufferedReader 등)
import java.util.ArrayList;  // 가변 길이 배열
import java.util.List;   // 리스트 인터페이스
import java.util.stream.Collectors;   // Stream 결과를 다시 List로 모으는 도구

public class BookRepository {                              
    private List<Book> bookList = new ArrayList<>();       // 변수 bookList
    private static final String FILE_PATH = "books.csv";   // FILE_PATH는 static final 상수 (변경되면 안 되는 파일 경로)

    // 키워드로 검색
    public List<Book> searchByKeyword(String keyword) {    
        return bookList.stream()  //bookList.stream() : 리스트를 "흐름(스트림)"으로 변환
                .filter(book -> book.matchesKeyword(keyword))  //.filter(조건): 조건이 true인 원소만 통과시킴
                .collect(Collectors.toList());  //.collect(toList): 다시 List로 모음
    }

    // ID로 도서 조회
    public Book findById(String bookId) {                  
        return bookList.stream()
                .filter(book -> book.getBookId().equals(bookId))
                .findFirst()  //.findFirst(): 조건 만족하는 "첫 번째" 원소를 Optional로 반환.
                .orElse(null);  //.orElse(null): 못 찾았으면 null을 반환.
    }

    // 도서 추가
    public void addBook(Book book) {                       
        bookList.add(book);
    }

    //전체 목록 반환
    //bookList를 그대로 반환하지 않고 "복사본"을 만들어 반환.
    public List<Book> findAll() {
        return new ArrayList<>(bookList);
    }

    //도서 목록을 CSV 파일로 저장
    public void saveToFile() throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Book book : bookList) {              // 향상된 for문: 리스트 순회
                bufferedWriter.write(book.toCsvString());  // 한 줄 쓰기
                bufferedWriter.newLine();  // 줄바꿈
            }
        } //bufferedWriter.close() 자동 호출
    }

    // CSV 파일에서 도서 목록을 읽어 메모리로 복원
    public void loadFromFile() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;  // 파일이 없으면 작업 중단

        bookList.clear();  // 기존 데이터 비우기
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {  // 빈 줄은 건너뛰기
                    bookList.add(Book.fromCsvString(line));
                     //static 메소드: 객체 없이 클래스명으로 호출 가능
                }
            }
        }
    }
}
