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

    // 국가별 인물 목록 출력
    public void selectPersonByCountry(List<PersonDTO> personList) {

        // 국가별 조회는 모두 같은 국가에 속한 인물들이므로 제목에 한 번만 출력
        String countryName = personList.get(0).getCountryName();
        System.out.println( "\n========== " + countryName + "의 인물 목록 ==========");

        for (PersonDTO person : personList) {
            System.out.print("인물 번호 : " + person.getPersonId());
            System.out.print(" | 인물 이름 : " + person.getPersonName());
            System.out.print(" | 시대 : " + person.getPeriodName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println("----------------------------------");
    }

    // 시대별 인물 목록
    public void selectPersonByPeriod(List<PersonDTO> personList) {

        // 시대별 조회 또한 모두 같은 시대에 속한 인물들이므로 제목에 한 번만 출력
        String periodName = personList.get(0).getPeriodName();
        System.out.println("\n========== " + periodName + "의 인물 목록 ==========");

        for (PersonDTO person : personList) {
            System.out.print("인물 번호 : " + person.getPersonId());
            System.out.print(" | 인물 이름 : " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 분야 : " + person.getFieldName());
            System.out.println();
        }

        System.out.println("----------------------------------");
    }

    // 분야별 인물 조회
    public void selectPersonByField(List<PersonDTO> personList) {

        // 분야별 조회 또한 모두 같은 시대에 속한 인물들이므로 제목에 한 번만 출력
        String fieldName = personList.get(0).getFieldName();

        System.out.println("\n========== " + fieldName + "분야의 인물 목록 ==========");

        for (PersonDTO person : personList) {
            System.out.print("인물 번호 : " + person.getPersonId());
            System.out.print(" | 인물 이름 : " + person.getPersonName());
            System.out.print(" | 국가 : " + person.getCountryName());
            System.out.print(" | 시대 : " + person.getPeriodName());
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
