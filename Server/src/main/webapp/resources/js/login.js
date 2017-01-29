$(document).ready( _ => {
    var loginData = new Vue({
        el: "#loginProcess",

        data: {
            isLogingIn: true
        },

        methods: {
            login: function() {
                const loginTF = $("#loginTF").val()
                const loginPWD = $("#loginPWD").val()

                if (loginTF == "" || loginPWD == "") {
                    alert("One or more field is empty")
                    return ;
                }

                requests.auth.login(loginTF, loginPWD, _ => {
                 $( location ).attr("href", host);
                })
            },

            register: function() {
                const registrationTF = $("#registerTF").val()
                const registrationPWD = $("#registerPWD").val()

                if (registrationTF == "" || registrationPWD == "") {
                    alert("One or more field is empty")
                    return ;
                }

                requests.auth.register(registrationTF, registrationPWD, _ => {
                    $( location ).attr("href", host);
                    alert("Account created")
                })
            },

            toggleAction: function() {
                this.isLogingIn = !this.isLogingIn
            }
        }
    })
})





   
