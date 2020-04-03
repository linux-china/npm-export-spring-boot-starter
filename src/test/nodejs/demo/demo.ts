import userController from "@UserService/UserController"

userController.setBaseUrl("http://localhost:8080")

userController.findUserById(1).then(user => {
    console.log(user.nick)
});
