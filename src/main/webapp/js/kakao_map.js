// 마커를 클릭했을 때 해당 장소의 상세정보를 보여줄 커스텀오버레이입니다
var placeOverlay = new kakao.maps.CustomOverlay({zIndex:1}), 
	contentNode = document.createElement('div'), // 커스텀 오버레이의 컨텐츠 엘리먼트 입니다 
	markers = []; // 마커를 담을 배열입니다

var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	mapOption = {
		center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
		level: 7 // 지도의 확대 레벨
	};

// 지도를 생성합니다    
var map = new kakao.maps.Map(mapContainer, mapOption);

// 장소 검색 객체를 생성합니다
var ps = new kakao.maps.services.Places(map);

// 주소-좌표 변환 객체를 생성합니다
var geocoder = new kakao.maps.services.Geocoder();

//시도,구군을 담는 곳
let setAddr = new Set();

window.onload = () =>{
	searchAddrFromCoords(map.getCenter(), displayCenterInfo);
	searchPlaces();
	getAddressJson();
}


kakao.maps.event.addListener(map, 'idle', function() {

	//장소 검색
	searchPlaces();
	getAddressJson();

	//중심 좌표나 확대 수준이 변경됐을 때 지도 중심 좌표에 대한 주소 정보를 표시하도록 이벤트를 등록합니다
	searchAddrFromCoords(map.getCenter(), displayCenterInfo);

});

function searchAddrFromCoords(coords, callback) {
	// 좌표로 행정동 주소 정보를 요청합니다

	//좌표->주소변경
	geocoder.coord2RegionCode(coords.getLng(), coords.getLat(), callback);
}

// 카테고리 검색을 요청하는 함수입니다
function searchPlaces() {

	ps.keywordSearch('구청', placesSearchCB, { category_group_code: 'PO3', useMapBounds: true })
	ps.keywordSearch('시청', placesSearchCB, { category_group_code: 'PO3', useMapBounds: true })
	ps.keywordSearch('군청', placesSearchCB, { category_group_code: 'PO3', useMapBounds: true });

}


// 키워드 검색 완료 시 호출되는 콜백함수 입니다
function placesSearchCB(data, status, pagination) {
	if (status === kakao.maps.services.Status.OK) {
		//console.log(data);
		//장소검색 완료 후 데이터 중복제거
		addrToSet(data);
		/*for (var i = 0; i < data.length; i++) {
			displayMarker(data[i]);
		}*/
	}
}


function addrToSet(data) {
	//console.log(data);
	for (let i = 0; i < data.length; i++) {
		let fullAddress = data[i].address_name;
		//console.log(fullAddress);
		let addrArr = fullAddress.split(" ");
		//console.log(addrArr);
		let jsonObjAddr = new Object();
		jsonObjAddr.sido = addrArr[0];
		if(addrArr[1] == '창원시'){
			switch(addrArr[2]){
				case '마산합포구':
				case '마산회원구':
					jsonObjAddr.gugun = '창원시 마산합포회원구';
					break;
				case '진해구':
					jsonObjAddr.gugun = '창원시 진해구';
					break;
				default:
					jsonObjAddr.gugun = '창원시 의창성산구';
			}
		}else{		
			jsonObjAddr.gugun = addrArr[1];
		}
		//jsonObjAddr을 json으로 변환
		let stringAddr = JSON.stringify(jsonObjAddr);
		//console.log(stringAddr);
		setAddr.add(stringAddr);
	}
}


function getAddressJson() {
	//console.log(setAddr);
	let addrJsonArray = new Array();
	setAddr.forEach((addr) => {
		addrJsonArray.push(addr);
	})
	//console.log(addrJsonArray)
	setAddr.clear();
	let result = "[" + addrJsonArray.toString() + "]";
	//console.log('결과'+result);	

	if (result != '[]') {
		//ajax로 데이터 송부
		getTotalCounts(result);
	}else{
		let onLand = '[{"sido":"서울","gugun":"성북구"},{"sido":"서울","gugun":"성동구"},'
			+'{"sido":"서울","gugun":"은평구"},{"sido":"서울","gugun":"서대문구"},'
			+'{"sido":"서울","gugun":"용산구"},{"sido":"서울","gugun":"광진구"},'
			+'{"sido":"서울","gugun":"종로구"},{"sido":"서울","gugun":"중구"},{"sido":"서울","gugun":"동대문구"}]';
		getTotalCounts(onLand);
		//console.log("랜딩에 출력");
	}

}

//ajax로 시, 구군 주소 코드 데이터를 보내고 totalCount받아옴
function getTotalCounts(list) {
	$.ajax({
		url:'./lostAnimals/getTotalCount.jsp',
		type: 'post',
		async: false,
		data: {
			'list': list,
		},
		dataType: 'json',
		success: function(response) {
			//console.log(response.datas);
			//console.log(response.datas.length);
			if (response != null) {
				for (let i in response.datas) {
					let sido = response.datas[i].sido;
					let gugun = response.datas[i].gugun;
					let totalCount = response.datas[i].totalCounts;
					let x = response.datas[i].x;
					let y = response.datas[i].y;
					//console.log(sido, gugun, totalCount, x, y);

					makeContent(sido, gugun, totalCount);
					overlayMarkers(x, y);

				}
			}
		},
		error:function(request, status, error){
			alert("에러-> 콘솔 확인");
			console.log("code = " + request.status + " message = " + request.responseText + " error = " + error); // 실패 시 처리
		}
	});
}

// api 에서 페이지별 정보 받아오기 위한 변수
let totalRecord = 0;
let currentPage = 1;

//let sido = '서울';
//let gugun = '용산구';

// 동물 리스트에서 뽑은 jsonData 담을 임시 변수
let currentAnimalList = {};

// 페이지 클릭했을때 끌고 들어올 전역변수
let sidoForPage = '';
let gugunForPage = '';

//지도 좌측상단에 지도 중심좌표에 대한 주소정보를 표출하는 함수입니다(혜진님)
function displayCenterInfo(result, status) {
    if (status === kakao.maps.services.Status.OK) {
        const infoDiv = document.getElementById('centerAddr');
		// 지도 이동시 currentPage 를 다시 1로 리셋
        currentPage = 1;
        sidoForPage = result[1].region_1depth_name;
        gugunForPage = result[1].region_2depth_name;
        //console.log(sidoForPage);
        //console.log(gugunForPage);
        // 행정동의 region_type 값은 'H' 이므로
        if (result[1].region_type === 'H') {
            infoDiv.innerHTML = result[1].address_name;
            getAnimalList(currentPage, sidoForPage, gugunForPage);
        }
    }
}


/* 마커 표시 함수 */

let content = '';

// 커스텀 오버레이에 표시할 내용입니다     
// HTML 문자열 또는 Dom Element 입니다 
function makeContent(sido, gugun, totalCount) {

	content = '<div class="marker_outer" onclick="clickMarker(\''+sido + '\',\'' + gugun + '\')">'
		+ '<div class="marker_inner">'
		+ '<span class="marker_place">' + gugun + '</span>'
		+ '<span class="marker_totalCounts">' + totalCount+ '</span>'
		+ '</div>';
}
//클릭한 지역에 새로운 리스트 불러오기
function clickMarker(sido, gugun) {
    //console.log('click!!!');
    //console.log(sido,"/", gugun);
    // 마커 클릭시 전역변수 초기화
    currentPage = 1;
    sidoForPage = sido;
    gugunForPage = gugun;
    getAnimalList(currentPage, sidoForPage, gugunForPage);
}


function overlayMarkers(x, y) {

	// 커스텀 오버레이가 표시될 위치입니다 
	let position = new kakao.maps.LatLng(x, y);

	// 커스텀 오버레이를 생성합니다
	let customOverlay = new kakao.maps.CustomOverlay({
		position: position,
		content: content,
		xAnchor: 0.5,
		yAnchor: 0.5
	});

	// 커스텀 오버레이를 지도에 표시합니다
	customOverlay.setMap(map);

}


/*
*
* lost animals page
* 
*/

// api 에서 동물 리스트 받아오는 메소드
function getAnimalList(page, sido, gugun) {
    // 리스트 불러오기 전 기존 내용 한번 비워주기
    //console.log(page, "/", sido, "/", gugun);
    $('#animalList').empty();
    $.ajax({
        type: "get",
        url: "./lostAnimals/getAnimalList.jsp",
        data: {
            'pageNo': page,
            'sido': sido,
            'gugun': gugun
        },
        dataType: 'json',
        success: function(jsonArray) {
            // 분리 후 반복
            jsonArray.forEach(function(json, index, array) {
                if (index == array.length - 1) {
                    // array 의 마지막 : totalCount
                    totalRecord = array[index].totalCount;
                    //console.log('totalCount : ', totalRecord);
                    getPaging(currentPage, totalRecord);
                    return;
                }
                // 매 jsonObject 마다 list 그려주기
                lenderList(index, json);
            });
			mouseOverEvent();
        },
        error: function() {
            //console.log('getAnimalList.jsp error');
        }
    });
}

// list 그려주는 메서드 
function lenderList(index, data) {
	const json = JSON.stringify(data);
	//console.log('lenderList json : ',data);
	//console.log('lenderList json.stringify : ',json);

	let list = '<li class="animal" onclick="openModal( ' + index + ' )">'
	//let list = '<li class="animal">'
		+ '<div class="list_content">'
		+ '<div class="popfile_box">'
		+'<img class="popfile" src="' + data.popfile + '"/>'
		+ '<p><sapn class="processState">' + data.processState + '</sapn></p>'
		+'</div>'
		+ '<div class="txt_box">'
		+ '<p><span class="kindCd">종류</span><span>'+data.kindCd + '&nbsp;</span><span class="sexCd">성별</span><span>' + data.sexCd + '</span></p>'
		+ '<p><span class="careNm">보호소</span><span>' + data.careNm + '</span>(<span class="orgNm">' + data.orgNm + '</span>)</p>'
		+ '<p><span class="happenPlace">발견장소</span><span>' + data.happenPlace + '</sapn></p>'
		+ '<p><span class="happenDt">발견일자</span><span>' + data.happenDt + '</sapn></p>'
		+ '<p><span class="specialMark">특징</span><span>' + data.specialMark + '</sapn></p>'
		+ '</div>'
		+ '</div>'
		+ '</li>';
	//console.log('1: lenderList');
	currentAnimalList[index] = data;
	$('#animalList').append(list);
	
}
	
	
/* 마우스 오버 이벤트 시작 */
function mouseOverEvent(){
	
	let animalItem = document.getElementsByClassName("animal");
	
	for(let i = 0; i<animalItem.length; i++){
			
			let place = animalItem.item(i).childNodes.item(0).childNodes.item(1).childNodes.item(2).childNodes.item(1).innerText

			animalItem.item(i).addEventListener("mouseenter", ()=>{		
				shelterClear();
				//console.log(place);
				// 주소로 좌표를 검색합니다
				geocoder.addressSearch(place, function(result, status) {
			
			    // 정상적으로 검색이 완료됐으면 
			     if (status === kakao.maps.services.Status.OK) {
			
			        var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
					
					var content = '<div class="shelter"></div>';
			        // 커스텀 오버레이를 생성합니다
					customOverlay = new kakao.maps.CustomOverlay({
					    position: coords,
					    content: content,
					    xAnchor: 0.5,
					    yAnchor: 0.5
					});
					
					// 커스텀 오버레이를 지도에 표시합니다
						customOverlay.setMap(map);		
						//console.log(result[0].y, result[0].x)
			    } 
					
			});
				
		}); //end of mouseenter Event
		
		animalItem.item(i).addEventListener("mouseleave", ()=>{
			//console.log(customOverlay);
			customOverlay.setMap(null);
			
		});
		
	} //end of for
		
}

function shelterClear(){
	
	let shelter = document.getElementsByClassName('shelter');
	
		for(let i = 0; i<shelter.length; i++){
			
			let parent = shelter.item(i).parentNode
			//console.log(parent)
			parent.removeChild(shelter.item(i));
		
		}
}

	
// 페이징 그려주는 메서드 
function getPaging(currentPage, totalRecord) {
	$.ajax({
		url: './lostAnimals/pagination.jsp',
		type: 'get',
		data: {
			'cpage': currentPage,
			'totalRecord': totalRecord
		},
		success: function(data) {
			//console.log('getPaging page:',data);
			$('#page_box').empty();
			$('#page_box').append(data);
			clickPage();
		},
		error: function() {
			console.log('pagination.jsp error');
		}
	});
}

// 페이징 클릭 메서드
function clickPage() {
	$('#page_box li').on('click', function(event) {
		currentPage = $(event.target).html();
		if (currentPage == '▶' || currentPage == '◀') {
			currentPage = $(event.target).val();
		}
		getPaging(currentPage, totalRecord);
		getAnimalList(currentPage, sidoForPage, gugunForPage);
	});
};

// 모달창 메서드
function openModal(index) {
	//console.log('click!! : ', index);
	//console.log(currentAnimalList[index]);
	const data = getDday(currentAnimalList[index]);
	lenderModal(currentAnimalList[index]);
	$('#modal').css('display', 'flex');
}

function closeModal() {
	$('#modal .close-area').on('click', function() {
		//console.log('4: modalClose');
		$('#modal').css('display', 'none');
	});
}

function getToday(date){
    var year = date.getFullYear();
    var month = (1 + date.getMonth());
    month = month >= 10 ? month : '0' + month;
    var day = date.getDate();
    day = day >= 10 ? day : '0' + day; 
    return  year + month + day;
}

// 현재 날짜를 yyyyMMdd 포맷으로 출력
var today = getToday(new Date());
//console.log("날짜 "+today); //20210915 String
today *= 1;    // 이제 Number

//디데이 구하는 함수
function getDday(data){
    //console.log(data);
	if(data.processState == '보호중'){
	    var dday = today-data.noticeEdt;
	    if(dday>=0){
	        return "D+"+dday+"일"
	    }
	    return "D"+dday+"일";
	    //console.log("날짜 : ",dday);
	}else{
		return '';
	}
}

//모달 렌더링
function lenderModal(data) {
	let html = '<div class="modal-window">'
		+ '<div class="modal_title">'
		+ '<h2>동물 상세 정보</h2>'
		+ '<div class="close-area"></div>'
		+ '</div>'
		+ '<table class="table">'
		+ '<thead class="table-head">'
		+ '<tr>'
		+ '<td colspan="4">동물사진</td>'
		+ '</tr>'
		+ '</thead>'
		+ '<tbody>'
		+ '<tr>'
		+ '<td colspan="4"><img src="' + data.filename + '"/></td>'
		+ '</tr>'
		+ '</tbody>'
		+ '</table>'
		+ '<table class="table">'
		+ '<thead>'
		+ '<tr>'
		+ '<td colspan="4">'
		+ '<div class="two-title">'
		+ '<span>유기동물 정보</span> <span>' + data.processState + '</span>'
		+ '</div>'
		+ '</td>'
		+ '</tr>'
		+ '</thead>'
		+ '<tbody>'
		+ '<tr>'
		+ '<td class="row-title">종류</td>'
		+ '<td colspan="3">' + data.kindCd + '</td>'
		+ '</tr>'
		+ '<tr>'
		+ '<td class="row-title">구조일</td>'
		+ '<td>' + data.happenDt + '</td>'
		+ '<td class="row-title">구조장소</td>'
		+ '<td>' + data.happenPlace + '</td>'
		+ '</tr>'
		+ '<tr>'
		+ '<td class="row-title">색상</td>'
		+ '<td>' + data.colorCd + '</td>'
		+ '<td class="row-title">성별</td>'
		+ '<td>' + data.sexCd + '</td>'
		+ '</tr>'
		+ '<tr>'
		+ '<td class="row-title">나이</td>'
		+ '<td>' + data.age + '</td>'
		+ '<td class="row-title">무게</td>'
		+ '<td>' + data.weight + '</td>'
		+ '</tr>'
		+ '<tr>'
		+ '<td class="row-title">특징</td>'
		+ '<td colspan="3">' + data.specialMark + '</td>'
		+ '</tr>'
		+ '</tbody>'
		+ '</table>'
		+ '<table class="table">'
		+ '<thead>'
		+ '<tr>'
		+ '<td colspan="4">'
		+ '<div class="two-title">'
		+ '<span class="row-title">공고정보</span><span>'+getDday(data)+'</span>'
		+ '</div>'
		+ '</td>'
		+ '</tr>'
		+ '</thead>'
		+ '<tbody>'
		+ '<tr>'
		+ '<td class="row-title">공고번호</td>'
		+ '<td colspan="3">' + data.noticeNo + '</td>'
		+ '</tr>'
		+ '<tr>'
		+ '<td class="row-title">공고기간</td>'
		+ '<td colspan="3">' + data.noticeSdt + '부터 ' + data.noticeEdt + '까지</td>'
		+ '</tr>'
		+ '</tbody>'
		+ '</table>'
		+ '<table class="table">'
		+ '<thead>'
		+ '<tr>'
		+ '<td colspan="4">보호소 정보</td>'
		+ '</tr>'
		+ '</thead>'
		+ '<tbody>'
		+ '<tr>'
		+ '<td class="row-title">주소</td>'
		+ '<td colspan="3">' + data.careAddr + '</td>'
		+ '</tr>'
		+ '<tr>'
		+ '<td class="row-title">담당자</td>'
		+ '<td>' + data.chargeNm + '</td>'
		+ '<td class="row-title">연락처</td>'
		+ '<td>' + data.officetel + '</td>'
		+ '</tr>'
		+ '<tr>'
		+ '<td class="row-title">보호소명</td>'
		+ '<td>' + data.careNm + '</td>'
		+ '<td class="row-title">보호소<br>&nbsp;연락처</td>'
		+ '<td>' + data.careTel + '</td>'
		+ '</tr>'
		+ '</tbody>'
		+ '</table>'
		+ '</div>';
	//console.log('3: lenderModal');
	$('#modal').empty();
	$('#modal').append(html);
	closeModal();
}

