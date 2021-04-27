package jp.co.seattle.library.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
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

/**
 * Handles requests for the application home page.
 */
@Controller //APIの入り口
public class BulkRegistrationController {
    final static Logger logger = LoggerFactory.getLogger(AddBooksController.class);

    @Autowired
    private BooksService booksService;

    @RequestMapping(value = "/bulkRegistration", method = RequestMethod.GET) //value＝actionで指定したパラメータ
    public String bulkRegistration(Locale locale) {
        return "bulkRegistration";
    }

    /**
     * 一括登録
     * @param locale ロケール情報
     * @param file csvファイル
     * @param model モデル
     * @return 一括登録画面
     */
    @Transactional
    @RequestMapping(value = "/bulkRegistrationBook", method = RequestMethod.POST, produces = "text/plain;charset=utf-8")
    public String bulkRegistrationBook(Locale locale,
            @RequestParam("csvFile") MultipartFile file,
            Model model) {
        logger.info("Welcome insertBooksBulk.java! The client locale is {}.", locale);


        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));) {
            List<BookDetailsInfo> bookList = new ArrayList<BookDetailsInfo>();
            String errorMessage = "";
            int rowCount = 1; //csvファイルの行番号            
            boolean flag = false; //エラー判定用フラグ
            String line;

            // 一行ずつ読み出してList<BookDetailsInfo>型のbookListに格納
            while ((line = br.readLine()) != null) {
                String[] bookData = line.split(",", 0);

                BookDetailsInfo bookInfo = new BookDetailsInfo();

                //必須項目、bookData[0]-[3]に値が入っているか
                if (bookData[0].isEmpty() || bookData[1].isEmpty() || bookData[2].isEmpty() || bookData[3].isEmpty()) {
                    errorMessage += rowCount + "行目で必要な情報がありません。\n";
                    flag = true;
                }

                //出版日とISBNのバリデーションチェック
                if (bookData[3] != null) {
                    try {
                        // 日付チェック
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                        sdf.setLenient(false);
                        sdf.parse(bookData[3]);

                    } catch (ParseException ex) {
                        errorMessage += rowCount + "行目の出版日はYYYYMMDDの形式で入力してください。\n";
                        flag = true;
                    }

                }
                if (bookData[4] != null && !(bookData[4].isEmpty())
                        && !(bookData[4].matches("([0-9]{10}|[0-9]{13})?"))) {
                    errorMessage += rowCount + "行目のISBNは10桁もしくは13桁の数字で入力してください。\n";
                    flag = true;
                }

                //各行のデータを,毎に区切り書籍Dtoに格納
                //bookData[0]=タイトル、bookData[1]=著者名、bookData[2]=出版社、
                //bookData[3]=出版日、bookData[4]=ISBN、bookData[5]=説明 を格納
                bookInfo.setTitle(bookData[0]);
                bookInfo.setAuthor(bookData[1]);
                bookInfo.setPublisher(bookData[2]);
                bookInfo.setPublishDate(bookData[3]);
                bookInfo.setIsbn(bookData[4]);
                bookInfo.setDescription(bookData[5]);

                bookList.add(bookInfo);
                rowCount++;

            }
            //エラーがあった場合の処理
            if (flag) {
                model.addAttribute("errorMessage", errorMessage);
                return "bulkRegistration";
            }


            // 書籍情報を新規登録する
            for (BookDetailsInfo book : bookList) {
                booksService.registBook(book);
            }

            model.addAttribute("complete", "登録完了");
            return "bulkRegistration";


        } catch (IOException e) {
            model.addAttribute("errorMessage", "CSVファイル読み込みでエラーが発生しました。");
            return "bulkRegistration";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "CSVファイル読み込みでエラーが発生しました。");
            return "bulkRegistration";
        }
    }
}