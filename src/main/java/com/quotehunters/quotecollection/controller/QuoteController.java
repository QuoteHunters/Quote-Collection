package com.quotehunters.quotecollection.controller;

import com.quotehunters.quotecollection.model.dto.QuoteDTO;
import com.quotehunters.quotecollection.model.service.QuoteService;
import com.quotehunters.quotecollection.view.ResultView;

import java.util.List;

public class QuoteController {

    private QuoteService quoteService = new QuoteService();
    private ResultView resultView = new ResultView();

    public void selectAllQuotes() {

        List<QuoteDTO> quoteList = quoteService.selectAllQuotes();

        if (quoteList.isEmpty()) {
            resultView.printMessage("등록된 명언이 없습니다.");
        } else {
            resultView.printQuoteList(quoteList);
        }
    }

    public void selectTodayQuote() {

        QuoteDTO quote = quoteService.selectTodayQuote();

        if (quote == null) {
            resultView.printMessage("등록된 명언이 없습니다.");
        } else {
            resultView.printTodayQuote(quote);
        }
    }

    // 키워드 검색 결과의 존재 여부에 따라 목록 또는 안내 메시지를 출력한다.
    public void searchQuotesByKeyword(String keyword) {

        try {
            List<QuoteDTO> quoteList =
                    quoteService.searchQuotesByKeyword(keyword);

            if (quoteList.isEmpty()) {
                resultView.printMessage(
                        "'" + keyword + "'(으)로 조회된 명언이 없습니다."
                );
            } else {
                resultView.printSearchedQuoteList(quoteList);
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            resultView.printMessage("명언 검색 중 오류가 발생했습니다.");
        }
    }
}