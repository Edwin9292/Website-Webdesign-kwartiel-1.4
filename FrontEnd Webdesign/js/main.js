
getNewestTopics(5);
getNewestMembers(5);

async function getNewestTopics(amount){
  try{
    const response = await fetch("http://localhost:8080/topics/newest?amount="+amount);
    const responseJson = await response.json();
    processResponse("topic", responseJson);
  }
  catch(e){
    document.getElementById("new-topics-loading").innerHTML = "No connection to the server";
  }
}

async function getNewestMembers(amount){
  try{
    const response = await fetch("http://localhost:8080/accounts/newest?amount="+amount);
    const responseJson = await response.json();
    processResponse("member", responseJson);
  }
  catch(e){
    document.getElementById("new-members-loading").innerHTML = "No connection to the server";
  }
}

function processResponse(type, response){
  const placeholder = document.getElementById("new-" + type + "s-loading");
  if(response != undefined && response.length > 0){

    document.getElementById("newest-" + type + "s").removeChild(placeholder);

    if(type === "topic"){
      for(let topic of response){
        createHomepageTopic(topic);
      }
    }
    else if(type === "member"){
      for(let member of response){
        createHomepageMember(member);
      }
    }
  }
  else{
    placeholder.innerText = "no "+ type + "s have been found";
  }
}


// <article className="newest topic hover-blue">
//   Topic title
// </article>

function createHomepageTopic(topicJson) {
  const article = document.createElement('article');
  article.classList.add("newest", "topic", "hover-blue");
  article.innerText = topicJson['topicTitle'];

  article.addEventListener("click", () =>{
    location.href = "topic.html?id="+topicJson["id"];
  });

  document.getElementById("newest-topics").appendChild(article);
}


function createHomepageMember(memberJson){
  const article = document.createElement('article');
  article.classList.add("newest", "member");
  article.innerText = memberJson['username'];

  document.getElementById("newest-members").appendChild(article);
}
