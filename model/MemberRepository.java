package model; 

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository {                             
    private List<Member> memberList = new ArrayList<>();     //회원 목록 (List<Member>)
    private static final String FILE_PATH = "members.csv";   //파일 경로 상수 (static final)

    // ID로 회원 조회
    public Member findById(String memberId) {  // findById. ID로 회원 찾기
        return memberList.stream()  //Stream 사용: filter로 ID 일치 회원만 추
                .filter(member -> member.getMemberId().equals(memberId))
                .findFirst() //findFirst()로 첫 번째 결과, orElse(null)로 없으면 null.
                .orElse(null); 
    }

    // 중복 ID 체크
    public boolean existsById(String memberId) {     //existsById. 해당 ID의 회원이 이미 존재하는가?
        return findById(memberId) != null;   //findById의 결과가 null이 아니면 존재함을 의미.
    }

    // 회원 등록
    public void addMember(Member member) {
        memberList.add(member);  //리스트에 추가
    }

    //saveToFile. csv파일로 저장
    public void saveToFile() throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (Member member : memberList) {   //각 Member 객체의 toCsvString()을 호출해 한 줄씩 기록.
                bufferedWriter.write(member.toCsvString());
                bufferedWriter.newLine();
            }
        }
    }

    //loadFromFile. CSV 파일에서 회원 목록 복원
    public void loadFromFile() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) return; // 파일 없으면 종료

        memberList.clear();  //기존에 있던 회원 리스트 비움
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {  // 한 줄씩 읽어 Member.fromCsvString()으로 객체 복원
                if (!line.trim().isEmpty()) {  //빈 줄이 아닌 경우 다음 줄 실행
                    memberList.add(Member.fromCsvString(line));  //읽어온 문자열을 Member 객체로 변환/ 회원 리스트에 추가
                }
            }
        }
    }
}
