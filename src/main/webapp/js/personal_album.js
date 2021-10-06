let items = document.querySelectorAll(".album_content ");
let container = document.querySelector(".album_wrap");
items.forEach(item => {
  item.addEventListener("mouseover", () => {
    container.style.background = "#ffffffde";
  });
  item.addEventListener("mouseleave", () => {
    container.style.background = "#e7e7e7e6";
  });
});

