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
import jp.co.seattle.library.service.ThumbnailService;

/**
 * 削除コントローラー
 */
@Controller //APIの入り口
public class DeleteBookController {
    final static Logger logger = LoggerFactory.getLogger(DeleteBookController.class);

    @Autowired
    private BooksService booksService;

    private ThumbnailService thumbnailService;



    /**
     * 対象書籍を削除する
     *
     * @param locale ロケール情報
     * @param bookId 書籍ID
     * @param model モデル情報
     * @return 遷移先画面名
     */
    @Transactional
    @RequestMapping(value = "/deleteBook", method = RequestMethod.POST)
    public String deleteBook(
            Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        logger.info("Welcome delete! The client locale is {}.", locale);

        //貸し出し中でなければ書籍情報を削除
        if (booksService.isBorrowing(bookId)) {
            model.addAttribute("borrowingMessage", "この本は貸し出し中のため、削除できません。");
            model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
            return "details";
        }

        String thumbnailName = booksService.getBookInfo(bookId).getThumbnailName();

        booksService.deleteBook(bookId);

        //サムネイルをminioから削除
        if (thumbnailName != null) {
            thumbnailService.deleteUrl(thumbnailName);
        }

        model.addAttribute("bookList", booksService.getBookList());
        return "home";

    }

    /**
     * 選択した書籍を一括削除
     * @param locale
     * @param bookIds 削除したい書籍のリスト
     * @param model
     * @return ホーム画面に遷移
     */
    @Transactional
    @RequestMapping(value = "/deleteBooksBulk", method = RequestMethod.POST)
    public String deleteBooksBulk(
            Locale locale,
            @RequestParam("deleteBookList") Integer[] bookIds,
            Model model) {
        logger.info("Welcome deleteBulk! The client locale is {}.", locale);

        int deleteBooksCount = bookIds.length; //削除する書籍の冊数

        for (int bookId : bookIds) {
            if (!booksService.isBorrowing(bookId)) {
                booksService.deleteBook(bookId);

            }
            if (booksService.isBorrowing(bookId)) {
                deleteBooksCount -= 1;
                model.addAttribute("cannotDelete", "貸し出し中の書籍は削除されません");
            }
        }

        model.addAttribute("bookList", booksService.getBookList());
        model.addAttribute("deleteMessage", deleteBooksCount + "冊の書籍を削除しました");
        return "home";
    }

}
