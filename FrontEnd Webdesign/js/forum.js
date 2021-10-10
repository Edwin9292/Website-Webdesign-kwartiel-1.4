loadTopics();

let headerSection;
let topicsSection;

async function loadTopics(){
  try{
    const response = await fetch("http://localhost:8080/topics");
    const responseJson = await response.json();
    if(responseJson.length > 0){
      clearTopicList();
      for(const topic of responseJson){
        addTopicToForum(topic);
      }
    }
    else{
      document.getElementById("topic-loading").innerHTML = "No topics have been found";
    }
  }
  catch{
    document.getElementById("topic-loading").innerHTML = "No connection to the server";
  }
}

function clearTopicList(){
  document.getElementById("topic-container").innerHTML = '';
}

document.getElementById("search-topic-form")
  .addEventListener("submit", async function onSubmit(event){
    event.preventDefault();
    let formData = new FormData(event.target);
    let titleToSearch = formData.get("topicTitle");
    if(titleToSearch != "" && titleToSearch != undefined){
      const response = await searchTopics(titleToSearch, event);

      clearTopicList();

      if(response.length > 0){
        for(const topic of response){
          addTopicToForum(topic);
        }
      }
      else{
        const topicContainer = document.getElementById("topic-container");

        const forumSection = document.createElement('section');
        forumSection.classList.add("topic", "hover-blue");

        const titleParagraph = document.createElement('p');
        titleParagraph.innerHTML = 'No topics with title "<b>' + titleToSearch + '</b>" have been found.';

        forumSection.appendChild(titleParagraph);
        topicContainer.appendChild(forumSection);
      }
    }
  });

async function searchTopics(topicTitle){
  try{
    const response = await fetch("http://localhost:8080/topics?topicTitle="+topicTitle);
    const responseJson = await response.json();
    return responseJson;
  }
  catch{
    document.getElementById("topic-loading").innerHTML = "No connection to the server";
  }
}

function addTopicToForum(topic){
  const topicContainer = document.getElementById("topic-container");

  const forumSection = document.createElement('section');
  forumSection.classList.add("topic", "hover-blue");

  const titleParagraph = document.createElement('p');
  titleParagraph.innerHTML = topic["topicTitle"];

  forumSection.addEventListener('click' ,()=>{
    location.href="topic.html?id="+topic["id"]
  });

  forumSection.appendChild(titleParagraph);
  topicContainer.appendChild(forumSection);
}

function closeCreateTopic(){
  event.preventDefault()
  document.getElementById("create-topic-popup").style.display="none";
  headerSection.style.display = "block";
  topicsSection.style.display = "block";
}

function moveElements(elements) {
  let id = null;
  let offSet = 0;
  let currentElement;
  let callbackElements = [];
  if(elements.length > 0){
    currentElement = elements.shift();
  }
  let direction = 'X';
  clearInterval(id);
  id = setInterval(frame, 2);

  function frame() {
    if ((direction === 'X' && offSet >= screen.width) ||(direction === 'Y' &&offSet >= screen.height)) {
      clearInterval(id);
      callbackElements.push(currentElement);
      if(elements.length > 0){
        offSet = 0;
        currentElement = elements.shift();
        if(direction === 'X'){
          direction = 'Y';
        }
        else{
          direction = 'X';
        }
        id = setInterval(frame, 1);
      }
      else{
        hideAndResetElements(callbackElements);
        displayCreateTopic();
      }
      }
    else {
      if(direction ==='X'){
        offSet += 10;
      }
      else{
        offSet += 5;
      }
      currentElement.style.transform = "translate"+ direction+"(-"+offSet+"px)";
    }
  }
}

function hideAndResetElements(elements){
  for(const element of elements){
    element.style.display = "none";
    element.style.transform = "translate(0)";
  }
}

function displayCreateTopic(){
  document.getElementById("create-topic-popup").style.display="block";
}

function openCreateTopic(){
  event.preventDefault()
  headerSection = document.getElementById("header");
  topicsSection = document.getElementById("topics");
  moveElements([headerSection, topicsSection]);
}

document.getElementById("create-topic-form")
  .addEventListener('submit', async (event) =>{
    event.preventDefault();
    const formData = new FormData(event.target);
    if(validateFormData(formData)){
      const user = await getUserAccount(formData.get("username"));
      if(user == undefined){
        return;
      }
      else{
        formData.append("accountID", user["id"]);
        const topic = JSON.stringify(Object.fromEntries(formData.entries()))
        console.log(topic);
        await postNewTopic(topic);
      }
    }
  })

async function getUserAccount(username){
    try{
      const response = await fetch("http://localhost:8080/accounts/search?username=" +username);
      if(!response.ok){
        response.text().then(text => {
          alert("failed to load this user: " + JSON.parse(text).message);
          return undefined;
        })
      }
      else{
        return await response.json();
      }
    }
    catch(e){
      alert("There was a problem with searching your account, please try again");
    }
}

function validateFormData(formData){
  const titleField = formData.get("topicTitle")
  if(titleField == "" || titleField == undefined){
    alert("Error: Every topic needs to have a title!");
    return false;
  }

  const topicText = formData.get("topicText");
  if(topicText == "" || topicText == undefined){
    alert("Error: Every topic needs to have some text!");
    return false;
  }

  const username = formData.get("username");
  if(username == "" || username == undefined){
    alert("Error: please enter your username in order to create a topic");
    return false;
  }
  return true;
}

async function postNewTopic(topic){
  try {
    const response = await fetch("http://localhost:8080/topics", {
      method: 'post',
      headers: {
        'Content-Type': 'Application/json'
      },
      body: topic
    });
    if (!response.ok) {
      return response.text().then(text => {
        alert("Failed to create the topic: " + JSON.parse(text).message);
      });
    }
    else{
      alert("Topic successfully created!");
      document.getElementById("create-topic-form").reset();
      document.getElementById("create-topic-popup").style.display = "none";
      headerSection.style.display = "block";
      topicsSection.style.display = "block";
      await loadTopics();
    }
  }
  catch(e){
    alert("Failed to create a new topic");
  }
}
