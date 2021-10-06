/*
*
* manager 회원 관리  
*
*/

function selectAll(selectAll)  {
  const checkboxes 
       = document.getElementsByName('selectcheck');
  
  checkboxes.forEach((checkbox) => {
    checkbox.checked = selectAll.checked;
  })
}
function changeClick(){
	console.log("변경 클릭 ");
	var nicknamelist = [];
	$('input:checked[name="selectcheck"]').each(function(i){
		nicknamelist.push($(this).val());
	})
	var nicknames = JSON.stringify(nicknamelist);
	var status = $("select[name=statusOption]").val();
	console.log("status : "+status);
	
	if (nicknamelist.length === 0) {
		alert('변경할 항목을 선택해 주세요.');
		return;
	}
	
	$.ajax({
		data: {
			nicknames : nicknames,
			status : status
		},
		type: "post",
		url: "./manager_modify_ok.jsp",
		dataType: "json",
		success: function(result) {
			console.log('changeClick 정상 실행!');
			const flag = result.flag;
			if (flag == 1) {
				alert('변경되었습니다.');
				location.replace('./manager_mboard.jsp');
				
			} else if (flag == 0) {
				alert('변경에 실패했습니다');
			}
		},
		error: function() {
			console.log("status : " , request.status);
			console.log("message : " , request.responseText);
			console.log("error : " , error);
			alert('시스템 오류');
		}
	});
}
