// const userController = require("./UserController");
const userController = require("@UserService/UserController").setBaseUrl("http://localhost:8080");

(async () => {
    let nick = await userController.findNickById(1);
    console.log(nick);
})()