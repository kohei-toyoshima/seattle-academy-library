/**
 * 
 */

$(function() {
	//削除ボタンを押したら、削除する書籍のリストをinputタグのvalueに設定
	$(".btn_deleteBook").on('click', function() {
		if(!confirm("本当に削除しますか？")){
			return false;
		}
	});
}); 