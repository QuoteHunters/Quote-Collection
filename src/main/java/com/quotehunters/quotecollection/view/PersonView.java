package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.model.dto.PersonDTO;

import java.util.List;

public class PersonView {

    // 전체 인물 목록 출력
    // 인물 식별 번호는 출력하되 국가·시대·분야 ID는 사용자에게 노출하지 않음
    public void displayAllPerson(List<PersonDTO> personList) {

        System.out.println("\n========== 전체 인물 목록 ==========");

        for (PersonDTO person : personList) {
            System.out.println("----------------------------------");
            System.out.print("인물 번호 : " + person.getPersonId());
            System.out.print(" | 인물 이름 : " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 시대 : " + person.getPeriodName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println("----------------------------------");
    }

    // 성공·실패·안내 메세지 출력
    // View : 받은 문장을 보여주기만 하는 역할
    // Controller : 실행 결과에 따라 어떤 문장을 보여줄지 결정
    public void printMessage(String message) {
        System.out.println();
        System.out.println(message);
    }

}
