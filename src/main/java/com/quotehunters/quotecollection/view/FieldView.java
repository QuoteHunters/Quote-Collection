package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.controller.FieldController;
import com.quotehunters.quotecollection.model.dto.FieldDTO;

import java.util.List;
import java.util.Scanner;

public class FieldView {
    private static final String HEADER = "=".repeat(10);
    private static final String LINE = "-".repeat(30);

    private final FieldController fieldController = new FieldController();
    private final ResultView rv = new ResultView();

//    public void fieldMainView() {
//        while (true) {
//            System.out.println("1. 조회");
//            System.out.println("2. 등록");
//            System.out.println("3. 수정");
//            System.out.println("4. 삭제");
//            System.out.println("0. 메인 화면으로");
//
//            int num = scv.scannInt(sc, "선택");
//
//            switch (num) {
//                case 0: {
//                    return;
//                }
//                case 1: {
//                    allFields();
//                    break;
//                }
//                case 2: {
//                    insertField();
//                    break;
//                }
//                case 3: {
//                    updateField(scv, sc);
//                    break;
//                }
//                case 4: {
//                    deleteField(scv, sc);
//                    break;
//                }
//                default: {
//                    rv.errorMessage("메뉴에 있는 번호를 선택해주세요.");
//                    break;
//                }
//            }
//        }
//    }

    public void allFields() {
        List<FieldDTO> fields = fieldController.allFields();

        printHeader("분야 조회");
        printFields(fields);
    }

    private void printHeader(String title) {
        System.out.println(HEADER + " " + title + " " + HEADER);
    }

    private void printFields(List<FieldDTO> fields) {
        System.out.println(LINE);
        if (fields.isEmpty()) {
            System.out.println("조회된 분야가 없습니다.");
            System.out.println(LINE);
            return;
        }

        for (int i = 0; i < fields.size(); i++) {
            System.out.println((i + 1) + ". " + fields.get(i).getFieldName());
        }
        System.out.println(LINE);
    }

    public int selectFields(ScannerView scannerView, Scanner scanner) {
        List<FieldDTO> fields;

        while (true) {
            try {
                fields = fieldController.allFields();
                break;
            } catch (RuntimeException e) {
                rv.errorMessage("분야 목록 조회 중 오류가 발생했습니다.");
                if (!askRetry(scannerView, scanner, "뒤로가기")) return 0;
            }
        }

        printFields(fields);

        if (fields.isEmpty()) return 0;

        int choice = 0;

        while (true) {
            choice = scannerView.scannInt(scanner, "선택 (0: 뒤로가기)");
            if (choice == 0) break;

            if (choice < 0 || fields.size() < choice) {
                rv.errorMessage("리스트에 존재하는 번호를 입력해주세요.");
                continue;
            }

            break;
        }

        if (choice == 0) return choice;

        return fields.get(choice - 1).getFieldId();
    }

    public boolean updateField(ScannerView scannerView, Scanner scanner) {
        printHeader("분야 수정");

        selectLoop:
        while (true) {
            int id = selectFields(scannerView, scanner);

            if (id == 0) return false;

            modifyLoop:
            while (true) {
                String changeName = scannerView.scannString(scanner, "변경할 분야명 입력 (0: 뒤로가기)");
                if (changeName.equals("0")) {
                    continue selectLoop;
                }

                if (changeName.length() > 10) {
                    rv.errorMessage("분야명은 10글자 이하로 입력해주세요.");
                    continue;
                }

                while (true) {
                    try {
                        if (fieldController.existsField(id, changeName)) {
                            rv.errorMessage("이미 등록된 분야명입니다.");
                            continue modifyLoop;
                        }
                        break;
                    } catch (RuntimeException e) {
                        rv.errorMessage("분야명 중복 확인 중 오류가 발생했습니다.");
                        if (!askRetry(scannerView, scanner, "취소")) return true;
                    }
                }

                while (true) {
                    String check = scannerView.scannString(scanner,
                            "[" + changeName + "](으)로 수정하시겠습니까? (완료 / 재수정 / 취소)");

                    if (check.equals("완료")) {
                        try {
                            int result = fieldController.updateField(id, changeName);
                            if (result > 0) {
                                rv.successMessage("분야가 수정되었습니다.");
                                return false;
                            }

                            rv.errorMessage("분야 수정에 실패했습니다.");
                        } catch (RuntimeException e) {
                            rv.errorMessage("분야 수정 중 오류가 발생했습니다.");
                        }
                        continue;
                    }

                    if (check.equals("재수정")) continue modifyLoop;

                    if (check.equals("취소")) return true;

                    rv.errorMessage("완료, 재수정, 취소 중 하나를 입력해주세요.");
                }
            }
        }
    }

    public boolean insertField(ScannerView scannerView, Scanner scanner) {
        printHeader("분야 등록");

        insertLoop:
        while (true) {
            String name = scannerView.scannString(scanner, "등록할 분야명 입력 (0: 뒤로가기)");

            if (name.equals("0")) return false;

            if (name.length() > 10) {
                rv.errorMessage("분야명은 10글자 이하로 입력해주세요.");
                continue;
            }

            while (true) {
                try {
                    if (fieldController.existsField(name)) {
                        rv.errorMessage("이미 등록된 분야명입니다.");
                        continue insertLoop;
                    }
                    break;
                } catch (RuntimeException e) {
                    rv.errorMessage("분야명 중복 확인 중 오류가 발생했습니다.");
                    if (!askRetry(scannerView, scanner, "취소")) return true;
                }
            }

            while (true) {
                String check = scannerView.scannString(scanner,
                        "[" + name + "](으)로 등록하시겠습니까? (등록 / 수정 / 취소)");

                if (check.equals("수정")) continue insertLoop;
                if (check.equals("취소")) return true;
                if (check.equals("등록")) {
                    try {
                        int result = fieldController.insertField(name);

                        if (result > 0) {
                            rv.successMessage("분야가 등록되었습니다.");
                            return false;
                        }

                        rv.errorMessage("분야 등록에 실패했습니다.");
                    } catch (RuntimeException e) {
                        rv.errorMessage("분야 등록 중 오류가 발생했습니다.");
                    }
                    continue;
                }

                rv.errorMessage("등록, 수정, 취소 중 하나를 입력해주세요.");
            }
        }
    }

    public boolean deleteField(ScannerView scannerView, Scanner scanner) {
        printHeader("분야 삭제");

        deleteLoop:
        while (true) {
            int id = selectFields(scannerView, scanner);
            if (id == 0) return false;

            while (true) {
                String str = scannerView.scannString(scanner,
                        "선택한 분야를 삭제하시겠습니까? (완료 / 재선택 / 취소)");
                if (str.equals("취소")) return true;
                if (str.equals("재선택")) continue deleteLoop;
                if (str.equals("완료")) {
                    try {
                        int result = fieldController.deleteField(id);
                        if (result > 0) {
                            rv.successMessage("분야가 삭제되었습니다.");
                            return false;
                        }

                        rv.errorMessage("분야 삭제에 실패했습니다.");
                    } catch (RuntimeException e) {
                        rv.errorMessage("분야 삭제 중 오류가 발생했습니다.");
                    }
                    continue;
                }

                rv.errorMessage("완료, 재선택, 취소 중 하나를 입력해주세요.");
            }
        }
    }

    private boolean askRetry(ScannerView scannerView, Scanner scanner, String cancelText) {
        while (true) {
            String choice = scannerView.scannString(scanner, "재시도 / " + cancelText);
            if (choice.equals("재시도")) return true;
            if (choice.equals(cancelText)) return false;
            rv.errorMessage("재시도, " + cancelText + " 중 하나를 입력해주세요.");
        }
    }
}
