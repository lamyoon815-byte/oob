public class Book {                                  //  클래스 Book
    private String bookId;                           //  클래스에서 지정한 변수: 책 아이디, 제목, 저자, 대출 가능 유무
    private String title;
    private String author;
    private boolean isAvailable;                     

    // 새 도서 생성용 생성자
    public Book(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isAvailable = true; //이용 가능 유무(새로 등록해서 무조건 이용 가능)
    }

    // CSV 불러오는 생성자
    public Book(String bookId, String title, String author, boolean isAvailable) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isAvailable = isAvailable; //파일에 있는 상태로 대출 가능 여부를 불러와야 함
    }

    // Getter (읽기 전용으로 가져오는 메소드)
    public String getBookId() { return bookId; }    
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; } //boolean은 get 대신 is를 사용하는 것이 표준 관례

    // Setter (대출 반납 시 대출 가능 상태를 바꾸는 기능.)
    public void setAvailable(boolean isAvailable) {  
        this.isAvailable = isAvailable;
    }

    // 제목 또는 저자로 검색하는 기능.
    public boolean matchesKeyword(String keyword) {   
        String lowerKeyword = keyword.toLowerCase();  // 입력 키워드를 소문자로
        return title.toLowerCase().contains(lowerKeyword)  //대소문자 구분 없이 검색하기 위해 양쪽 모두 toLowerCase() 적용.
            || author.toLowerCase().contains(lowerKeyword); // contains(lowerKeyword) : 문자열에 lowerKeyword가 포함돼 있으면 true
    }

    // CSV 저장용 
    //파일 저장시 사용. 필드는 콤마(,)로 이어붙임
    public String toCsvString() {                     
        return bookId + "," + title + "," + author + "," + isAvailable;
    }

    // CSV 로드용
    //   - static 키워드:
    //       객체를 만들지 않고도 "Book.fromCsvString(line)" 형태로 호출 가능.
    //       (객체가 아직 없을 때 객체를 만들어야 하므로 static이 필수)
    //   - split(",") : "a,b,c" → ["a","b","c"] 배열로 자른다.
    //   - Boolean.parseBoolean("true") : 문자열을 boolean으로 변환.
    public static Book fromCsvString(String line) {   
        String[] parts = line.split(",");
        return new Book(parts[0], parts[1], parts[2], Boolean.parseBoolean(parts[3]));
        // ↑ 4-매개변수 생성자(생성자 #2)를 호출 → isAvailable 값까지 복원
    } // <-- 중괄호 오타 수정 완료

    //   - @Override : 부모 클래스(Object)의 toString을 "재정의"한다는 표시.
    //   - System.out.println(book) 호출 시 이 메소드가 자동으로 실행됨.
    //   - 삼항연산자 "조건 ? A : B" : 조건이 true면 A, 아니면 B.
    @Override
    public String toString() {
        String availabilityStatus = isAvailable ? "대출가능" : "대출중";  
        return "[" + bookId + "] " + title + " - " + author
             + " (" + availabilityStatus + ")";
    }
} // <-- 클래스의 끝
