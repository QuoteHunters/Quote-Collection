package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.FieldController;
import com.quotehunters.quotecollection.model.dto.FieldDTO;

import java.util.List;
import java.util.Scanner;

public class FieldView {
    private final FieldController fieldController = new FieldController();
    private final ScannerView scv = new ScannerView();
    private final Scanner sc = new Scanner(System.in);
    private final ResultView rv = new ResultView();

    public void fieldMainView() {
        System.out.println("1. 등록");
        System.out.println("2. 수정");
        System.out.println("3. 삭제");
        System.out.println("0. 메인 화면으로");

        while (true) {
            int num = scv.scannInt(sc, "선택");

            switch (num) {
                case 0: {
                    return;
                }
                case 1: {
                    insertField();
                    return;
                }
                case 2: {
                    updateField();
                    return;
                }
                case 3: {
                    System.out.println("삭제");
                    return;
                }
                default: {
                    rv.errorMessage("메뉴에 있는 번호를 선택해주세요.");
                }
            }
        }
    }

    public void allFields() {
        List<FieldDTO> fields = fieldController.allFields();

        System.out.println("----------------------------");
        if (fields.isEmpty()) {
            System.out.println("조회 결과 없음");
            return;
        }

        for (int i = 0; i < fields.size(); i++) {
            System.out.println((i + 1) + ". " + fields.get(i).getFieldName());
        }
        System.out.println("----------------------------");
    }

    public int selectFields() {
        List<FieldDTO> fields = fieldController.allFields();

        allFields();

        int choice = 0;

        while (true) {
            choice = scv.scannInt(sc, "선택 (0: 뒤로가기)");
            if (choice == 0) break;

            if (choice < 0 || fields.size() < choice) {
                rv.errorMessage("메뉴에 있는 번호를 선택해주세요");
                continue;
            }

            break;
        }

        if (choice == 0) return choice;

        return fields.get(choice - 1).getFieldId();
    }

    public void updateField() {
        selectLoop:
        while (true) {
            int id = selectFields();

            if (id == 0) return;

            modifyLoop:
            while (true) {
                String changeName = scv.scannString(sc, "변경할 분야명 입력 (0: 뒤로가기)");
                if (changeName.equals("0")) {
                    continue selectLoop;
                }

                if (changeName.length() > 10) {
                    rv.errorMessage("분야명은 10글자 이하로 입력해주세요.");
                    continue;
                }

                if (fieldController.existsField(id, changeName)) {
                    rv.errorMessage("중복되는 분야가 존재합니다.");
                    continue;
                }

                while (true) {
                    String check = scv.scannString(sc, "정말 수정하시겠습니까? 예 / 재수정 / 아니오");

                    if (check.equals("예")) {
                        int result = fieldController.updateField(id, changeName);
                        if (result > 0) {
                            rv.successMessage("수정이 완료되었습니다.");
                            return;
                        }

                        rv.errorMessage("수정에 실패하였습니다. 다시 시도해주세요");
                        return;
                    }

                    if (check.equals("재수정")) continue modifyLoop;

                    if (check.equals("아니오")) return;

                    rv.errorMessage("예, 재수정, 아니오 중 하나를 입력해주세요.");
                }
            }

        }
    }

    public void insertField() {
        System.out.println("---------- 분야 등록 ----------");

        insertLoop:
        while (true) {
            String name = scv.scannString(sc, "입력 (0: 뒤로가기)");

            if (name.equals("0")) return;

            if (name.length() > 10) {
                rv.errorMessage("분야명은 10글자 이하로 입력해주세요.");
                continue;
            }

            if (fieldController.existsField(name)) {
                rv.errorMessage("중복되는 분야가 존재합니다.");
                continue;
            }

            while (true) {
                String check = scv.scannString(sc, "등록 / 수정 / 취소");

                if (check.equals("수정")) continue insertLoop;
                if (check.equals("취소")) return;
                if (check.equals("등록")) {
                    int result = fieldController.insertField(name);

                    if (result > 0) {
                        rv.successMessage("등록이 완료되었습니다.");
                        return;
                    }

                    rv.errorMessage("등록에 실패하였습니다.");
                    return;
                }

                rv.errorMessage("등록, 수정, 취소 중 하나를 입력해주세요");
                continue;
            }
        }
    }
}
