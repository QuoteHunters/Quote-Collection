package com.quotehunters.quotecollection.view;

import com.quotehunters.quotecollection.model.dto.QuoteDTO;

import java.util.List;

public class ResultView {

    public void printQuoteList(List<QuoteDTO> quoteList) {

        System.out.println();
        System.out.println("========== 명언 목록 ==========");

        for (QuoteDTO quote : quoteList) {
            System.out.println("명언 : " + quote.getQuoteContent());
            System.out.println("인물 : " + quote.getPersonName());
            System.out.println("주제 : " + quote.getThemeName());
            System.out.println("------------------------------");
        }
    }

    public void printMessage(String message) {
        System.out.println(">> " + message);
    }

    public void printTodayQuote(QuoteDTO quote) {

        System.out.println();
        System.out.println("========== 오늘의 명언 ==========");
        System.out.println("명언 : " + quote.getQuoteContent());
        System.out.println("인물 : " + quote.getPersonName());
        System.out.println("주제 : " + quote.getThemeName());
        System.out.println("================================");
    }

    // 키워드로 검색된 명언 목록을 선택 가능한 번호와 함께 출력한다.
    public void printSearchedQuoteList(List<QuoteDTO> quoteList) {

        System.out.println();
        System.out.println("========== 검색 결과 ==========");

        for (QuoteDTO quote : quoteList) {
            System.out.println("번호 : " + quote.getQuoteId());
            System.out.println("명언 : " + quote.getQuoteContent());
            System.out.println("인물 : " + quote.getPersonName());
            System.out.println("주제 : " + quote.getThemeName());
            System.out.println("------------------------------");
        }
    }
}