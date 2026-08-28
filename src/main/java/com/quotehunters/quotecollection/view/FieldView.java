package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.FieldController;
import com.quotehunters.quotecollection.model.dao.FieldDTO;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class FieldView {
    private final FieldController fieldController = new FieldController();
    private final ScannerView scv = new ScannerView();
    private final Scanner sc = new Scanner(System.in);

    public void allFields() {
        List<FieldDTO> fields = fieldController.allFields();

        System.out.println("----------------------------");
        if (fields.isEmpty()) {
            System.out.println("조회 결과 없음");
            return;
        }

        for (int i = 1; i < fields.size(); i++) {
            System.out.println(i + ". " + fields.get(i).getFieldName());
        }
        System.out.println("----------------------------");
    }

    public int selectFields() {
        List<FieldDTO> fields = fieldController.allFields();
        int choice = 0;

        while (true) {
            choice = scv.scannInt(sc, "선택 (0: 뒤로가기)");
            if (choice == 0) break;

            if (fields.size() < choice) {
                System.out.println("메뉴에 있는 번호를 선택해주세요");
                continue;
            }

            break;
        }

        if (choice == 0) return choice;

        return fields.get(choice - 1).getFieldId();
    }

//    public void modifyField(int id) {
//        List<FieldDTO> fields = fieldController.allFields();
//
//        while (true) {
//
//
//            String changeName = scv.scannString(sc, "변경할 분야명 입력 (0: 뒤로가기)");
//            if (changeName.equals("0")) {
//                continue;
//            }
//
//
////            System.out.println(field.getFieldName() + " => " + changeName);
//            System.out.println("정말 수정하시겠습니까? 예 / 재수정 / 아니오");
//
//            return;
//        }
//    }
}
