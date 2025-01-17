package jp.co.seattle.library.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import org.springframework.web.multipart.MultipartFile;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.service.BooksService;
import jp.co.seattle.library.service.ThumbnailService;

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口

public class EditBookController {
    final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

    @Autowired
    private BooksService booksService;

    @Autowired
    private ThumbnailService thumbnailService;

    @RequestMapping(value = "/editBook", method = RequestMethod.POST) //value＝actionで指定したパラメータ
    //RequestParamでname属性を取得
    public String detailsBook(Locale locale,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        // デバッグ用ログ
        logger.info("Welcome detailsControler.java! The client locale is {}.", locale);

        model.addAttribute("bookInfo", booksService.getBookInfo(bookId));
        return "editBook";
    }

    /**
     * 書籍情報を更新する
     * @param locale ロケール情報
     * @param title 書籍名
     * @param author 著者名
     * @param publisher 出版社
     * @param file サムネイルファイル
     * @param publishDate 出版日
     * @param isbn ISBN
     * @param description 書籍説明
     * @param model モデル
     * @return 遷移先画面
     */
    @Transactional
    @RequestMapping(value = "/updateBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String updateBook(Locale locale,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("publisher") String publisher,
            @RequestParam("thumbnail") MultipartFile file,
            @RequestParam("publish_date") String publishDate,
            @RequestParam("isbn") String isbn,
            @RequestParam("description") String description,
            @RequestParam("bookId") Integer bookId,
            Model model) {
        logger.info("Welcome insertBooks.java! The client locale is {}.", locale);

        // パラメータで受け取った書籍情報をDtoに格納する。
        BookDetailsInfo bookInfo = new BookDetailsInfo();
        bookInfo.setTitle(title);
        bookInfo.setAuthor(author);
        bookInfo.setPublisher(publisher);
        bookInfo.setPublishDate(publishDate);
        bookInfo.setIsbn(isbn);
        bookInfo.setDescription(description);
        bookInfo.setBookId(bookId);

        //出版日のバリデーションチェック
        try {
            if (!(publishDate.matches("^[0-9]+$"))) {
                model.addAttribute("notDateError", "出版日はYYYYMMDDの形式で入力してください");
                model.addAttribute("bookInfo", booksService.getBookInfo(bookId));
                return "editBook";
            }

            // 日付チェック
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            sdf.setLenient(false);
            sdf.parse(publishDate);
            bookInfo.setPublishDate(publishDate);

        } catch (ParseException ex) {
            model.addAttribute("notDateError", "出版日はYYYYMMDDの形式で入力してください");
            model.addAttribute("bookInfo", booksService.getBookInfo(bookId));
            return "editBook";
        }

        //ISBNのバリデーションチェック
        if (!(bookInfo.getIsbn().matches("([0-9]{10}|[0-9]{13})?"))) {
            model.addAttribute("notIsbnError", "ISBNは10桁もしくは13桁の数字で入力してください");
            model.addAttribute("bookInfo", booksService.getBookInfo(bookId));
            return "editBook";
        }


        if (!file.isEmpty()) {
            try {
                // クライアントのファイルシステムにある元のファイル名を設定する
                String thumbnail = file.getOriginalFilename();
                // サムネイル画像をアップロード
                String fileName = thumbnailService.uploadThumbnail(thumbnail, file);
                // URLを取得
                String thumbnailUrl = thumbnailService.getURL(fileName);

                bookInfo.setThumbnailName(fileName);
                bookInfo.setThumbnailUrl(thumbnailUrl);

            } catch (Exception e) {

                // 異常終了時の処理
                logger.error("サムネイルアップロードでエラー発生", e);
                model.addAttribute("bookInfo", bookInfo);
                return "editBook";
            }

        } else {
            bookInfo.setThumbnailUrl(booksService.getBookInfo(bookId).getThumbnailUrl());
            bookInfo.setThumbnailName(booksService.getBookInfo(bookId).getThumbnailName());
        }

        // 書籍情報を新規登録する
        booksService.updateBook(bookInfo);

        //古いサムネイルをminioから削除
        thumbnailService.deleteUrl(booksService.getBookInfo(bookId).getThumbnailName());

        // 編集した書籍の詳細情報を表示するように実装
        //  詳細画面に遷移する
        model.addAttribute("bookDetailsInfo", booksService.getBookInfo(bookId));
        return "details";
    }

}
