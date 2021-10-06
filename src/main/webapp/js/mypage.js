
$(document)
  .ready(function () {
	
	let ul = document.getElementById('tag-list');
    let tag = [];
    let counter = ul.childElementCount;

    // 태그 추가 함수
    function addTag(value) {
      tag[counter] = value; // 태그를 배열 안에 추가
      counter++; // counter 증가 삭제를 위한 del-btn 의 고유 id 부여
    }

	function getTag(){
		for(let i = 0; i<ul.childElementCount; i++){
			let text = ul.childNodes[i].childNodes[0].textContent;
			tag.push(text);
		}
			//console.log(tag);
	}

    // 최종적으로 서버에 넘길때 tag 안에 있는 값을 array type 으로 만들어서 넘긴다.
    function marginTag() {
      return Object.values(tag)
        .filter(function (word) {
          return word !== "";
        });
    }

    $("#tag")
      .on("keyup", function (e) { //input 에 focus 되있을 때 
        let self = $(this);
        //console.log("keypress");

        // 엔터 입력시 구동
        if (e.key === "Enter" || e.keyCode == 13) {

          let tagValue = self.val(); // 값 가져오기

          // 값이 없으면 동작 X
          if (tagValue !== "") {

            // 같은 태그가 있는지 검사. 있다면 해당값이 array 로 return 된다.
            let result = Object.values(tag)
              .filter(function (word) {
                return word === tagValue;
              });

            // 태그 중복 검사
            if (result.length == 0) {
              $("#tag-list")
                .append("<li class='tag-item'>" + tagValue + "<span class='del-btn' idx='" + counter + "'>x</span></li>");
              addTag(tagValue);
              self.val("");
            } else {
              alert("태그값이 중복됩니다.");
            }
          }
          e.preventDefault(); // SpaceBar 시 빈공간이 생기지 않도록 방지
			//console.log(marginTag());
			let note = marginTag().toString();
			//console.log(typeof(note));
			//console.log('note: '+note);
			mypageNote(note);
        }
      });

    // 삭제 버튼
    // 삭제 버튼은 비동기적 생성이므로 document 최초 생성시가 아닌 검색을 통해 이벤트를 구현시킨다.
    $(document)
      .on("click", ".del-btn", function (e) {
        let index = $(this)
          .attr("idx");
        tag[index] = "";
        $(this)
          .parent()
          .remove();
		//console.log(marginTag());
		let note = marginTag().toString();
		mypageNote(note);
      });

	let mypageNote = (note) => {	
		//console.log('mynote: '+note)
		$.ajax({
			url: './mypage/mypage_note.jsp',
			type: 'post',
			dataType: 'json',
			data: {
				'note' : note 
			},
			success: ( result ) => {
				//console.log(result);
				if(result.flag == 1){
					console.log('저장 성공');
				}else{
					console.log('저장 실패');
				}
			},
			error: () => {
				console.log('서버 에러');
				alert('죄송합니다. 서버 에러')
			}
			
		})
		
	}
	
		getTag();

  });
