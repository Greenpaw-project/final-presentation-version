// 네브 메뉴 마우스 이벤트
let depth1Arr = document.getElementsByClassName("depth1");
for (let i = 0; i < depth1Arr.length; i++) {
  depth1Arr[i].addEventListener("mouseover", function () {
    let depth2 = depth1Arr[i].children[1].classList.add('active');
  });
  depth1Arr[i].addEventListener("mouseleave", function () {
    let depth2 = depth1Arr[i].children[1].classList.remove('active');
  });
}
let logInStatus = document.getElementById("login_status"); //바뀔 <a>태그
logInStatus.innerHTML = "log in";

logInStatus.onclick = () => {
  location.href = getContextPath()+"/sign_in.jsp";
};

function getContextPath() {
	let hostIndex = location.href.indexOf( location.host ) + location.host.length;
	return location.href.substring( hostIndex, location.href.indexOf('/', hostIndex + 1) );
};