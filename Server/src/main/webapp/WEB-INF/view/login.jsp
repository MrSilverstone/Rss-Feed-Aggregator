<!DOCTYPE html>
<html lang="en-US">
<head>
    <meta charset="utf-8">
    <title>RSS Feed Aggregator - Login</title>

    <script src="https://unpkg.com/vue/dist/vue.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
    <script src=" https://cdnjs.cloudflare.com/ajax/libs/jquery-cookie/1.4.1/jquery.cookie.min.js"></script>
    <script src="${pageContext.request.contextPath}/app-resources/js/models.js"></script>
    <script src="${pageContext.request.contextPath}/app-resources/js/requests.js"></script>
    <script src="${pageContext.request.contextPath}/app-resources/js/login.js"></script>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/app-resources/css/style.css"/>
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:300,400,500,700" type="text/css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
    <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
    <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
</head>
<body class="blueBG">


<!-- Square card -->
<style>
    .demo-card-square.mdl-card {
        width: 320px;
        height: 300px;
        position: fixed;
        top: 50%;
        left: 50%;
        /* bring your own prefixes */
        transform: translate(-50%, -50%);
    }
</style>

<div id="loginProcess">
<div class="demo-card-square mdl-card mdl-shadow--2dp .centered" id="loginDiv" v-if="isLogingIn">
    <div class="mdl-card__title mdl-card--expand">
        <h2 class="mdl-card__title-text">RSS-Feed-Aggregator - Log in</h2>
    </div>
    <div class="mdl-card__supporting-text">
        <form action="#">
            <div class="mdl-textfield mdl-js-textfield">
                <input class="mdl-textfield__input" type="text" id="loginTF">
                <label class="mdl-textfield__label" for="loginTF">username</label>
            </div>
        </form>
        <form action="#">
            <div class="mdl-textfield mdl-js-textfield">
                <input class="mdl-textfield__input" type="text" id="loginPWD">
                <label class="mdl-textfield__label" for="loginPWD">password</label>
            </div>
        </form>
    </div>
    <div class="mdl-card__actions mdl-card--border">
        <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect"  v-on:click="login">
            Login
        </a>
        <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect"  v-on:click="toggleAction">
            Register
        </a>
    </div>
</div>
<div class="demo-card-square mdl-card mdl-shadow--2dp .centered" v-else>
    <div class="mdl-card__title mdl-card--expand">
        <h2 class="mdl-card__title-text">RSS-Feed-Aggregator - Registration</h2>
    </div>
    <div class="mdl-card__supporting-text">
        <form action="#">
            <div class="mdl-textfield mdl-js-textfield">
                <input class="mdl-textfield__input" type="text" id="registerTF">
                <label class="mdl-textfield__label" for="registerTF">username</label>
            </div>
        </form>
        <form action="#">
            <div class="mdl-textfield mdl-js-textfield">
                <input class="mdl-textfield__input" type="text" id="registerPWD">
                <label class="mdl-textfield__label" for="registerPWD">password</label>
            </div>
        </form>
    </div>
    <div class="mdl-card__actions mdl-card--border">
        <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect"  v-on:click="register">
            Register
        </a>
        <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect"  v-on:click="toggleAction">
            Login
        </a>
    </div>
</div>
</div>
</body>
</html>
