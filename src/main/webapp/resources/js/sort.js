/**
 * 
 */
$(function(){
	//セレクトタグに変更が変わった時のソート
	$('.sort_item').on('change', function() {
		var vals = [];
		var result = $('option:selected').val();
		var sort = $('input:radio[name="sort"]:checked').val();
		
		$(".books").each(function(i) {
    		vals[i] = {
      		title: $(this).find(".book_title").text(),
			author: $(this).find(".book_author").text(),
			publisher: $(this).find(".book_publisher").text(),
			publishDate: $(this).find(".book_publish_date").text(),
      		source: $(this).html()
    		};
			
  		});

		//タイトルの昇順、降順
		if(result == "title"){
			if(sort == "asc"){
				vals.sort(function(a,b){
   					if (a.title > b.title){
       					return 1;
    				}
					if (a.title < b.title){ 
       					return -1;
    				}else{
       					return 0;
    				}
				}); 
			}
			
			if(sort == "desc"){
				vals.sort(function(a,b){
   					if (a.title < b.title){
       					return 1;
    				}
					if (a.title > b.title){ 
       					return -1;
    				}else{
       					return 0;
    				}
				});
			}
			
		}
		
		//著者名の昇順降順
		if(result == "author"){
			if(sort == "asc"){
				vals.sort(function(a,b){
   					if (a.author > b.author){
       					return 1;
    				}
					if (a.author < b.author){ 
       					return -1;
    				}else{
       					return 0;
    				}
				}); 
			}
			
			if(sort == "desc"){
				vals.sort(function(a,b){
   					if (a.author < b.author){
       					return 1;
    				}
					if (a.author > b.author){ 
       					return -1;
    				}else{
       					return 0;
    				}
				});
			}
			
		}
		
		//出版社の昇順降順
		if(result == "publisher"){
			if(sort == "asc"){
				vals.sort(function(a,b){
   					if (a.publisher > b.publisher){
       					return 1;
    				}
					if (a.publisher < b.publisher){ 
       					return -1;
    				}else{
       					return 0;
    				}
				}); 
			}
			
			if(sort == "desc"){
				vals.sort(function(a,b){
   					if (a.publisher < b.publisher){
       					return 1;
    				}
					if (a.publisher > b.publisher){ 
       					return -1;
    				}else{
       					return 0;
    				}
				});
			}
			
		}
		
		//出版日の昇順降順
		if(result == "publishDate"){
			if(sort == "asc"){
				vals.sort(function(a,b){
   					if (a.publishDate > b.publishDate){
       					return 1;
    				}
					if (a.publishDate < b.publishDate){ 
       					return -1;
    				}else{
       					return 0;
    				}
				}); 
			}
			
			if(sort == "desc"){
				vals.sort(function(a,b){
   					if (a.publishDate < b.publishDate){
       					return 1;
    				}
					if (a.publishDate > b.publishDate){ 
       					return -1;
    				}else{
       					return 0;
    				}
				}); 
			}
			
		}
		
		for (var j = 0; j < vals.length; j++) {
    		$(".books").eq(j).html(vals[j].source);
		}
	
	});
	
	
	$('.order').on('change', function() {
		var vals = [];
		var sort = $('input:radio[name="sort"]:checked').val();
		
		$(".books").each(function(i) {
    		vals[i] = {
      		title: $(this).find(".book_title").text(),
			author: $(this).find(".book_author").text(),
			publisher: $(this).find(".book_publisher").text(),
			publishDate: $(this).find(".book_publish_date").text(),
      		source: $(this).html()
    		};	
  		});
		
		//タイトルの昇順、降順
		if(sort == "asc"){
			vals.sort(function(a,b){
   				if (a.title > b.title){
       				return 1;
    			}
				if (a.title < b.title){ 
       				return -1;
    			}else{
       				return 0;
    			}
			}); 
		}	
		if(sort == "desc"){
			vals.sort(function(a,b){
   				if (a.title < b.title){
       				return 1;
    			}
				if (a.title > b.title){ 
       				return -1;
    			}else{
       				return 0;
    			}
			});
		}
		
		//著者名の昇順、降順
		if(sort == "asc"){
				vals.sort(function(a,b){
   				if (a.author > b.author){
       				return 1;
    			}
				if (a.author < b.author){ 
       				return -1;
    			}else{
       				return 0;
    			}
			}); 
		}
		if(sort == "desc"){
			vals.sort(function(a,b){
   				if (a.author < b.author){
       				return 1;
    			}
				if (a.author > b.author){ 
       				return -1;
    			}else{
       				return 0;
    			}
			});
		}
		
		//出版社の昇順、降順
		if(sort == "asc"){
			vals.sort(function(a,b){
   				if (a.publisher > b.publisher){
       				return 1;
    			}
				if (a.publisher < b.publisher){ 
       				return -1;
    			}else{
       				return 0;
    			}
			}); 
		}
		if(sort == "desc"){
			vals.sort(function(a,b){
   				if (a.publisher < b.publisher){
					return 1;
   				}
				if (a.publisher > b.publisher){ 
       				return -1;
    			}else{
       				return 0;
    			}
			});
		}
		
		//出版日の昇順、降順
		if(sort == "asc"){
			vals.sort(function(a,b){
   				if (a.publishDate > b.publishDate){
      				return 1;
   				}
				if (a.publishDate < b.publishDate){ 
       				return -1;
    			}else{
       				return 0;
    			}
			}); 
		}
		if(sort == "desc"){
			vals.sort(function(a,b){
   				if (a.publishDate < b.publishDate){
       				return 1;
    			}
				if (a.publishDate > b.publishDate){ 
       				return -1;
    			}else{
       				return 0;
    			}
			}); 
		}
		
		for (var j = 0; j < vals.length; j++) {
    		$(".books").eq(j).html(vals[j].source);
		}
		
			
	});
	
});