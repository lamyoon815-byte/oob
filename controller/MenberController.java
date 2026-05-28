package controller;

import model.Loan;
import model.Member;
import model.LoanRepository;
import model.MemberRepository;
import view.MemberView;

import java.util.List;

public class MemberController {
    private final MemberRepository memberRepository; // 회원 데이터 저장소
    private final LoanRepository loanRepository; // 대출 데이터 저장소 
    private final MemberView memberView; // ui

    // MemberController 객체를 만들 때 호출되는 생성자 
    // 회원가입 버튼을 누르면 handleSignUp 메소드로 연결, 대출이력 조회 버튼을 누르면 handleHistorySearch 메소드로 연결
    public MemberController(MemberRepository memberRepository,
                            LoanRepository loanRepository,
                            MemberView memberView) {
        this.memberRepository = memberRepository;
        this.loanRepository = loanRepository;
        this.memberView = memberView;

        memberView.setOnSignUp(this::handleSignUp);
        memberView.setOnHistorySearch(this::handleHistorySearch);
    }

    private void handleSignUp(Member memberInfo) {
        if (memberRepository.existsById(memberInfo.getMemberId())) {
            memberView.showDuplicateIdMessage();
            return;
        }
        memberRepository.addMember(memberInfo); // 회원가입 버튼 이벤트 연결 
        memberView.showSignUpSuccess(); // 이력 조회 버튼 이벤트 연결 
    }

    private void handleHistorySearch(String memberId) {
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            memberView.showMemberLoans(memberId, null); // 회원 없으면 빈 이력 출력
            return;
        }
        List<Loan> loans = loanRepository.findByMemberId(memberId); // 대출 이력 조회
        memberView.showMemberLoans(memberId, loans); // 화면에 출력 
    }
}