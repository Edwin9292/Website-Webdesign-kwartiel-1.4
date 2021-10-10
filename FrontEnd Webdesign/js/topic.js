let result = new URLSearchParams(window.location.search);
const currentTopicId = result.get("id");
const topicContainer = document.getElementById("topic-container");
const replyPopup = document.getElementById("create-reply-popup");

getTopicById(currentTopicId);

const topicAuthor = document.getElementById("topic-author");
const topicPublished = document.getElementById("topic-published");
const topicText = document.getElementById("topic-text");
const replyContainer = document.getElementById("replies-container");

async function getTopicById(id){
  try{
    const response = await fetch("http://localhost:8080/topics/"+id);

    if (!response.ok) {
      response.text().then(text => {
        alert("Failed to find the topic: " + JSON.parse(text).message);
      });
    }
    else{
      const responseJson = await response.json();
      console.log(responseJson);
      const accountID = responseJson["accountID"];
      if(accountID != null){
        console.log(accountID);
        const author = await getUserAccount(accountID);
        if(author !== undefined){
          responseJson.author = author["username"];
          console.log(responseJson);
        }
      }
      await processTopic(responseJson);
    }
  }
  catch (e){
    alert(e.message);
    alert("Error: Could not load the topic!");
  }
}

async function getUserAccount(id){
  try{
    const response = await fetch("http://localhost:8080/accounts/" +id);
    if(!response.ok){
      response.text().then(text => {
        alert("failed to load this user: " + JSON.parse(text).message);
        return undefined;
      })
    }
    else{
      const responseJson = await response.json();
      console.log(responseJson);
      return responseJson;
    }
  }
  catch(e){
    alert("There was a problem with searching your account, please try again");
  }
}

async function processTopic(topic){
  topicAuthor.innerText = "Author: " + topic["author"];

  const date = new Date(topic["createdOn"]);
  topicPublished.innerText = "published: " + date.toLocaleDateString() + " " + date.toLocaleTimeString();
  topicText
  topicText.innerText = topic["topicText"];

  if(topic["replyPosts"].length > 0){
    replyContainer.innerHTML="";

    for(const reply of topic["replyPosts"]){
      await addReplyToPage(reply);
    }
  }
}

async function addReplyToPage(reply){
  const outerDiv = document.createElement('div');
  outerDiv.style.borderRadius = "3px";
  outerDiv.style.overflow = "hidden";
  outerDiv.classList.add("reply");

  const headerDiv = document.createElement("div");
  headerDiv.classList.add("section-header-blue");

  const authorP = document.createElement("p");
  const author = await getUserAccount(reply["accountID"]);
  authorP.innerText = "Author: " + author["username"];

  const publishedP = document.createElement("p");
  const date = new Date(reply["createdOn"]);
  publishedP.innerText = "published: " + date.toLocaleDateString() + " " + date.toLocaleTimeString();

  const replyText = document.createElement('p');
  console.log(reply)
  replyText.innerText = reply["text"];

  headerDiv.appendChild(authorP);
  headerDiv.appendChild(publishedP);
  outerDiv.appendChild(headerDiv);
  outerDiv.appendChild(replyText);

  replyContainer.appendChild(outerDiv);
}

function showReplyPopup(){
  event.preventDefault();
  topicContainer.style.display = "none";
  replyPopup.style.display = "block";
}

function hideReplyPopup(){
  event.preventDefault();
  topicContainer.style.display = "block";
  replyPopup.style.display = "none";
}


document.getElementById("create-reply-form")
  .addEventListener('submit', async (event) =>{
    event.preventDefault();
    const formData = new FormData(event.target);
    if(validateFormData(formData)){
      const user = await getUserAccountByUsername(formData.get("username"));
      if(user == undefined){
        return;
      }
      else{
        formData.append("accountID", user["id"]);
        const reply = JSON.stringify(Object.fromEntries(formData.entries()))
        console.log(reply);
        await postNewReply(currentTopicId, reply);
      }
    }
  })

function validateFormData(formData){
  const replyText = formData.get("text");
  if(replyText == "" || replyText == undefined){
    alert("Error: Every reply needs to have some text!");
    return false;
  }

  const username = formData.get("username");
  if(username == "" || username == undefined){
    alert("Error: please enter your username in order to create a topic");
    return false;
  }
  return true;
}

async function postNewReply(topicID, reply){
  try {
    const response = await fetch("http://localhost:8080/topics/" + topicID + "/posts", {
      method: 'post',
      headers: {
        'Content-Type': 'Application/json'
      },
      body: reply
    });
    if (!response.ok) {
      return response.text().then(text => {
        alert("Failed to create the reply: " + JSON.parse(text).message);
      });
    }
    else{
      alert("Reply successfully created!");
      document.getElementById("create-reply-form").reset();
      replyPopup.style.display = "none";
      topicContainer.style.display = "block";
      await getTopicById(topicID);
    }
  }
  catch(e){
    alert(e.message);
    //alert("Failed to create a new topic");
  }
}

async function getUserAccountByUsername(username){
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
