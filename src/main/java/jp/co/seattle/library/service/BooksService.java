package jp.co.seattle.library.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jp.co.seattle.library.dto.BookDetailsInfo;
import jp.co.seattle.library.dto.BookInfo;
import jp.co.seattle.library.rowMapper.BookDetailsInfoRowMapper;
import jp.co.seattle.library.rowMapper.BookInfoRowMapper;

/**
 * 書籍サービス
 * 
 *  booksテーブルに関する処理を実装する
 */
@Service
public class BooksService {
    final static Logger logger = LoggerFactory.getLogger(BooksService.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 書籍リストを取得する
     *
     * @return 書籍リスト
     */
    public List<BookInfo> getBookList() {

        //取得したい情報を取得するようにSQLを修正
        List<BookInfo> getedBookList = jdbcTemplate.query(
                "select id, title, author, publisher, publish_date, thumbnail_url from books order by title asc",
                new BookInfoRowMapper());

        return getedBookList;
    }

    /**
     * 書籍IDに紐づく書籍詳細情報を取得する
     *
     * @param bookId 書籍ID
     * @return 書籍情報
     */
    public BookDetailsInfo getBookInfo(int bookId) {

        // JSPに渡すデータを設定する
        String sql = "SELECT * FROM books where id ="
                + bookId;

        BookDetailsInfo bookDetailsInfo = jdbcTemplate.queryForObject(sql, new BookDetailsInfoRowMapper());

        return bookDetailsInfo;
    }


    /**
     * 書籍を登録する
     *
     * @param bookInfo 書籍情報
     */
    public void registBook(BookDetailsInfo bookInfo) {

        String sql = "INSERT INTO books (title,description,author,publisher,publish_date,thumbnail_name,thumbnail_url,isbn,reg_date,upd_date) VALUES ('"
                + bookInfo.getTitle() + "','" + bookInfo.getDescription() + "','" + bookInfo.getAuthor() + "','"
                + bookInfo.getPublisher() + "','"
                + bookInfo.getPublishDate() + "','"
                + bookInfo.getThumbnailName() + "','"
                + bookInfo.getThumbnailUrl() + "','"
                + bookInfo.getIsbn() + "',"
                + "sysdate(),"
                + "sysdate())";

        jdbcTemplate.update(sql);
    }


    /**
     * 書籍を削除する
     *
     * @param bookId 書籍ID
     */
    public void deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id='" + bookId + "'";
        jdbcTemplate.update(sql);
    }


    /**
     * 追加した書籍のIDを取得
     * @return booksテーブルのIDの最大値
     */
    public int getBookId() {
        String sql = "SELECT MAX(ID) FROM books";

        int id = jdbcTemplate.queryForObject(sql, Integer.class);
        return id;
    }

    /**
     * 書籍を更新する
     *
     * @param bookInfo 更新したい書籍情報
     */
    public void updateBook(BookDetailsInfo bookInfo) {
        String sql = "UPDATE books SET title = '"
                + bookInfo.getTitle() + "', author = '"
                + bookInfo.getAuthor() + "', publisher = '"
                + bookInfo.getPublisher() + "', publish_date = '"
                + bookInfo.getPublishDate() + "', isbn = '"
                + bookInfo.getIsbn() + "', description = '"
                + bookInfo.getDescription() + "', thumbnail_url = '"
                + bookInfo.getThumbnailUrl() + "', thumbnail_name = '"
                + bookInfo.getThumbnailName() + "', upd_date = sysdate() WHERE id = " + bookInfo.getBookId();

        jdbcTemplate.update(sql);
    }

    /**
     * 書籍が貸し出し中の場合true
     * @param bookId
     * @return true or false
     */
    public boolean isBorrowing(int bookId) {
        String sql = "select book_id from borrow where book_id = " + bookId;
        try {
            jdbcTemplate.queryForObject(sql, Integer.class);
            return true;
        } catch (IncorrectResultSizeDataAccessException e) {
            return false;
        }
    }

    /**
     * 書籍の貸し出し処理
     * @param bookId
     * 
     */
    public void rentBook(int bookId) {
        String sql = "insert into borrow (book_id) values (" + bookId + ")";
        jdbcTemplate.update(sql);
    }

    /**
     * 書籍の返却処理
     * @param bookId
     * 
     */
    public void returnBook(int bookId) {
        String sql = "delete from borrow where book_id =" + bookId;
        jdbcTemplate.update(sql);
    }

}