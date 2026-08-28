package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.PersonDTO;
import com.quotehunters.quotecollection.model.service.PersonService;
import com.quotehunters.quotecollection.view.PersonView;

import java.util.List;

public class PersonController {

    private PersonService personService =  new PersonService();
    private PersonView personView = new PersonView();

    // 전체 인물 조회
    public void selectAllPerson() {

        List<PersonDTO> personList = personService.selectAllPerson();

        // 조회 결과 유무에 따라 안내 메시지 또는 인물 목록 출력
        // 인물 리스트에 값이 존재하지 않을 때
        if (personList.isEmpty()) {
            personView.printMessage("등록된 인물이 없습니다.");
        } else {
            // View에 전체 인물의 정보를 전달
            personView.displayAllPerson(personList);
        }

    }

}
