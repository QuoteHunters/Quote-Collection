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
}