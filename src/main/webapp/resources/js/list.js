/**
 * 
 */

$(function() {
	var vals = [];
		//チェックボックスにイベントが発生した場合
		$(".checkbox").on('change', function() {
		//チェックボックスがチェックされたら配列に追加
		if (this.checked) {
			vals.push($(this).val());
		}
		//チェックボックスのチェックが外されたら配列から削除
		if (!this.checked) {
			vals.splice(vals.indexOf($(this).val()),1);
		}
	});
	
	//全選択ボタンが押下された場合の処理
	$('.btn_all_select').on('click', function(){
		//チェックボックスにチェックが入っていたら、チェックを外し、配列から削除
		if($('[name="check"]').prop('checked')){
			$('.checkbox').prop('checked', false);
  			$('input[name="check"]:not(:checked)').each(function() {
      			vals.splice(vals.indexOf($(this).val()),1); 
 		 	});

		//チェックボックスにチェックが入っていなかったら、チェックを入れ配列に追加
		}else {
			$('.checkbox').prop('checked', true);	
  			$('input[name="check"]:checked').each(function() {
      			vals.push($(this).val()); 
 		 	});
		}
	});

	//削除ボタンを押したら、削除する書籍のリストをinputタグのvalueに設定
	$(".btn_deleteBook").on('click', function() {
		if(vals.length != 0){
			
			//最終確認を行うダイアログ
			if(window.confirm('本当に削除しますか？')){
				$("#delete_books").val(vals);
			}else {
				//キャンセルが押された場合
				return false;
			}
		}
		if(vals.length == 0){
			window.alert('削除対象を選んでください');
			return false;
		}
		
	});
}); 
