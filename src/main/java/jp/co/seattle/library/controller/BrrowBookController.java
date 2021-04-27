package jp.co.seattle.library.controller;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.seattle.library.service.BooksService;

@Controller //APIの入り口

public class BrrowBookController {

    final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

    @Autowired
    private BooksService booksService;

    /**
     * 対象書籍を借りる
     *
     * @param locale ロケール情報
     * @param bookId 書籍ID
     * @param model モデル情報
     * @return 遷移先画面名
     */
    @Transactional
    @RequestMapping(value = "/rentBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String rentBook(
            Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        logger.info("Welcome delete! The client locale is {}.", locale);

        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        //貸し出し中か確認する
        if (booksService.isBorrowing(bookId)) {
            model.addAttribute("borrowingMessage", "この本は貸し出し中です");
            return "details";
        }

        //borrowテーブルに追加する
        booksService.rentBook(bookId);

        model.addAttribute("bookList", booksService.getBookList());
        return "details";

    }

    /**
     * 対象書籍を返却する
     *
     * @param locale ロケール情報
     * @param bookId 書籍ID
     * @param model モデル情報
     * @return 遷移先画面名
     */
    @Transactional
    @RequestMapping(value = "/returnBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String returnBook(
            Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        logger.info("Welcome delete! The client locale is {}.", locale);

        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));

        //貸し出し中か確認する
        if (!(booksService.isBorrowing(bookId))) {
            model.addAttribute("borrowingMessage", "この本は返却済です");
            return "details";
        }

        ////borrowテーブルから削除
        booksService.returnBook(bookId);

        return "details";

    }

}