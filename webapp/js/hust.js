function setmain_from_inmain(){
	$(".main").html($(".inmain").html());
	$(".inmain").remove();
}


function setSummerNote(){
	$('#summernote').summernote({
		  toolbar: [
			  ['style', ['style']],
			  ['font', ['bold', 'underline', 'italic', 'strikethrough', 'clear']],
			  ['fontname', ['fontname']],
			  ['fontsize', ['fontsize']],
			  ['color', ['color']],
			  ['para', ['ul', 'ol', 'paragraph', 'height']],
			  ['table', ['table']],
			  ['insert', ['link', 'picture', 'video']],
			  ['view', ['fullscreen', 'undo', 'redo', 'codeview', 'help']]
			],
			lang: "ko-KR"
		});
	  $('#summernote').summernote({
		  lineHeights: ['0.2', '0.3', '0.4', '0.5', '0.6', '0.8', '1.0', '1.2', '1.4', '1.5', '2.0', '3.0']
		});
}