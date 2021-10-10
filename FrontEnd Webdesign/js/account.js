document.getElementById("create-account-form").addEventListener('submit', async (event) =>{
  event.preventDefault();

  const formData = new FormData(event.currentTarget);
  console.log(JSON.stringify(Object.fromEntries(formData.entries())))
  //form is correct
  if(validateCreateAccountForm(formData, event)){
    await postNewAccount(JSON.stringify(Object.fromEntries(formData.entries())));
  }
  });

function validateCreateAccountForm(formData, event){
  const firstNameInput = formData.get("firstName");
  const lastNameInput = formData.get("lastName");
  const ageInput = formData.get("age");
  const usernameInput = formData.get("username");
  if(firstNameInput == "" || firstNameInput == undefined){
    alert("Firstname can not be empty!");
    return false;
  }
  if(lastNameInput == "" || lastNameInput == undefined){
    alert("Lastname can not be empty!")
    return false;
  }
  console.log(isNaN(ageInput));
  if(isNaN(ageInput) || ageInput > 100 || ageInput < 0){
    alert("You have entered an invalid age!")
    return false;
  }
  if(usernameInput == "" || usernameInput == undefined){
    alert("You have to enter a username in order to create an account!");
    return false;
  }
  return true;
}

async function postNewAccount(account){

  try{
    const response = await fetch("http://localhost:8080/accounts", {
      method: 'post',
      headers: {
        'Content-Type': 'Application/json'
      },
      body: account
    });

   // checkForError(response);
    if (!response.ok) {
      return response.text().then(text => {
        alert("Failed to create an account: " + JSON.parse(text).message);
      });
    }
    else{
      alert("Account successfully created!");
      document.getElementById("create-account-form").reset();
    }
    await response.json();
  }
  catch(e){
    alert(e.message);
  }
}

document.getElementById("search-account-form").addEventListener('submit', async (event) =>{
  event.preventDefault();

  const formData = new FormData(event.currentTarget);
  const userToSearch = formData.get("username");

  if(userToSearch !== "" && userToSearch !== undefined){
    const response = await searchUser(userToSearch, event);

    if(response != null){
      const idField = document.getElementById("user-id");
      const firstNameField = document.getElementById("edit-fname");
      const lastNameField = document.getElementById("edit-lname");
      const ageField = document.getElementById("edit-age");
      const usernameField = document.getElementById("edit-username");
      const investorTypeField = document.getElementById("edit-investor-type");

      idField.value = response["id"];
      firstNameField.value = response["firstName"];
      lastNameField.value = response["lastName"];
      ageField.value = response["age"];
      usernameField.value = response["username"];
      investorTypeField.value = response["investorType"];

      firstNameField.disabled = false;
      lastNameField.disabled = false;
      ageField.disabled = false;
      usernameField.disabled = false;
      investorTypeField.disabled = false;

      event.target.reset();
    }

  }

});

async function searchUser(username){
  try{
    const response = await fetch("http://localhost:8080/accounts/search?username="+username);

    if (!response.ok) {
      response.text().then(text => {
        alert("Failed to find the account: " + JSON.parse(text).message);
        return null;
      });
    }
    else{
      const responseJson = await response.json();
      return responseJson;
    }
  }
  catch{
    alert("Error: No connection to the database!");
  }
}

document.getElementById("edit-account-form").addEventListener('submit', async (event) =>{
  event.preventDefault();

  const formData = new FormData(event.currentTarget);
  const buttonClicked = event.target.value;
  const userId = formData.get("id");
  const username = formData.get("username");

  if(buttonClicked === "Edit"){
    const updatedAccount = JSON.stringify(Object.fromEntries(formData.entries()))
    await editAccount(userId, updatedAccount);
  }
  else if(buttonClicked === "Delete") {
    await deleteAccount(userId, username);
  }
});

async function editAccount(accountId, updatedAccount){
  try{
    console.log(accountId);
    console.log(updatedAccount);
    const response = await fetch("http://localhost:8080/accounts/" + accountId, {
      method: 'put',
      headers: {
        'Content-Type': 'Application/json'
      },
      body: updatedAccount
    });

    if(!response.ok) {
      response.text().then(text => {
        alert (JSON.parse(text).message);
      })
    }
    else {
      alert('The account with username: ' + updatedAccount["username"] + ' has successfully been updated!');
      clearEditAccountForm();
    }
  }
  catch(e){
    alert("No connection to the database");
  }

}

async function deleteAccount(accountId, username){
  try{
    const response = await fetch("http://localhost:8080/accounts/" + accountId, {
      method: 'delete'
    });

    if(!response.ok) {
      response.text().then(text => {
        alert (JSON.parse(text).message);
      })
    }
    else {
      alert('The account with username: ' + username + ' has successfully been deleted!');
      clearEditAccountForm();
    }
  }
  catch (e){
    alert("No connection to the database");
  }
}

function clearEditAccountForm(){
  const form = document.getElementById("edit-account-form");
  form.reset();
  const elements = form.querySelectorAll("input[type=text], select");
  for(const element of elements){
    element.disabled = true;
  }
}













