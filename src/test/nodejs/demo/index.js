const userService = require("./UserController");

(async () => {
    let nick = await userService.findNickById(1);
    console.log(nick);
})()