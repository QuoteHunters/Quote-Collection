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
}