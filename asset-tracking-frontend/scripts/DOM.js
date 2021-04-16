// for handling input label animation
function handleInputLabelTransition(){
    let text = document.getElementsByClassName("effect-17");
		
    for(let i=0;i<text.length;i++){
    text[i].addEventListener("blur",function(){
    if(text[i].value != ""){
    text[i].classList.add("has-content");
          }else{
            text[i].classList.remove("has-content");
          }
        })
      }
}

// for showing notifications
function showPopupNotification(msg="Logged In"){

  if(msg=="Logged In" && localStorage.getItem("initial")=="false") return;

  localStorage.setItem("initial","false");

	let notification = document.getElementsByClassName('notification')[0];
  
  if(msg=="Logged In" || msg=="Logged Out")
	notification.innerHTML = `
  <i class="far fa-check-circle"></i>${msg}
  `;

  else notification.innerHTML = `<i class="fas fa-exclamation-circle"></i>${msg}`;

    notification.className = "notification notification-show";

    setTimeout(()=>{
      notification.className = "notification";
    },4000)
}


handleInputLabelTransition();


let clearInputs = document.querySelectorAll(".fa-times");

for(let i=0;i<clearInputs.length;i++){
  clearInputs[i].addEventListener('click',()=>{
    clearInputs[i].previousElementSibling.value="";
    clearInputs[i].previousElementSibling.classList.remove("has-content");
  })
}

let drawer = document.querySelector(".drawer");
let mainMap = document.querySelector('#map');
let Form = document.querySelector('.drawer form');

// to slide up the input drawer
function openDrawer(){
  drawer.style.animation = "slideUpDrawer 0.8s ease";
  mainMap.style.animation = "slideUpMap 0.8s ease";

  drawer.style.animationFillMode = "forwards";
  mainMap.style.animationFillMode = "forwards";

  Form.style.display = 'flex';
  formTogglerUp.style.display = 'none';
  formTogglerDown.style.display = 'block';
  formTogglerDown.style.marginTop = '1rem';
}

// to slide down the input drawer
function closeDrawer(){
  drawer.style.animation = "slideDownDrawer 0.8s ease";
  mainMap.style.animation = "slideDownMap 0.8s ease";

  drawer.style.animationFillMode = "forwards";
  mainMap.style.animationFillMode = "forwards";

  Form.style.display = 'none';
  formTogglerDown.style.marginTop  = 0;
  formTogglerDown.style.display = 'none';
  formTogglerUp.style.display = 'block';
}


if(window.innerWidth<570){
  let searchBtn = document.querySelector('#searchBtn');
  let resetBtn = document.querySelector('#resetBtn');

  if(searchBtn)
  searchBtn.addEventListener('click',closeDrawer);

  if(resetBtn)
  resetBtn.addEventListener('click',closeDrawer);
}


let formTogglerUp = document.querySelector('.fa-chevron-up');
let formTogglerDown = document.querySelector('.fa-chevron-down');

if(formTogglerUp) formTogglerUp.addEventListener('click',openDrawer);

if(formTogglerDown) formTogglerDown.addEventListener('click',closeDrawer);

