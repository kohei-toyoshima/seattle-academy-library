package jp.co.seattle.library.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class SearchBookController {

    @Autowired
    private BooksService booksService;

    @RequestMapping(value = "/searchBook", method = RequestMethod.POST) //value＝actionで指定したパラメータ
    public String bulkRegistration(Locale locale,
            @RequestParam("search") String search,
            @RequestParam("pattern") String pattern,
            Model model) {

        if (pattern.equals("perfect")) {
            if (booksService.perfectMatchSearchBook(search).isEmpty()) {
                model.addAttribute("noBook", "検索結果と一致する書籍データがありません");
            } else {
                model.addAttribute("bookList", booksService.perfectMatchSearchBook(search));
            }

        }
        if (pattern.equals("part")) {
            if (booksService.partMatchSearchBook(search).isEmpty()) {
                model.addAttribute("noBook", "検索結果と一致する書籍データがありません");
            } else {
                model.addAttribute("bookList", booksService.partMatchSearchBook(search));
            }
        }

        return "home";
    }

}
