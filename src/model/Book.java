package model; 

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

    // 제목 또는 저자로 검색하는 기능
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

    // CSV 로드
    //"a,b,c" → ["a","b","c"] 배열로 자름
    public static Book fromCsvString(String line) {   
        String[] parts = line.split(",");
        return new Book(parts[0], parts[1], parts[2], Boolean.parseBoolean(parts[3]));
    }
    

    //콘솔 UI 출력, 디버깅 시 가독성 용도의 오버라이딩
    @Override
    public String toString() {
        String availabilityStatus = isAvailable ? "대출가능" : "대출중";  
        return "[" + bookId + "] " + title + " - " + author
             + " (" + availabilityStatus + ")";
    }
}
