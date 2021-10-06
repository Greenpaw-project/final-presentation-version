//로그인시
let btnSignIn = document.getElementById("btnSignIn");
btnSignIn.onclick = () => {
  alert("이미 로그인 하셨습니다.");
  btnSignIn.href = "./main.jsp";
};