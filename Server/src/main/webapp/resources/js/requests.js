const apiUrl = "http://localhost:8080/api"
const authUrl = "http://localhost:8080/auth"
const groupsUrl = apiUrl + "/groups"
const feedsUrl = apiUrl + "/feeds"

const URL = {

    auth: {
        login: authUrl + "/login",
        register: authUrl + "/register"
    },

    groups: {
        get: groupsUrl,

        add(groupName) {
            return groupsUrl + "/" + groupName
        },

        delete(groupName = null) {
            if (groupName == null) {
                return groupsUrl
            }

            return groupsUrl + "/" + groupName
        }
    },

    feeds: {
        add: feedsUrl,
        messages: feedsUrl + "/messages"
    }
}

function headers(authorize = true) {
    if (!authorize) {
        return {
            "Accept": "application/json",
            "Content-Type": "application/json"
        }
    } else {
        const token = $.cookie("token")

        return {
            "Accept": "application/json",
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        }
    }
}

const requests = {

    auth: {

        login(username, password, callback) {
            const data = {
                'username': username,
                'password': password
            }

            $.ajax({
                url: URL.auth.login,
                headers: headers(false),
                method: 'POST',
                data: JSON.stringify(data),
                dataType: 'json',

                success: result => {
                    $.cookie('token', result.token, {'expires' : 1})
                    callback()
                },

                statusCode: {
                    401: _ => {
                        alert("Bad credentials")
                    }
                }

            })
        },

        register(username, password, callback) {
            const data = {
                'username': username,
                'password': password
            }

            $.ajax({
                url: URL.auth.register,
                headers: headers(false),
                method: 'POST',
                data: JSON.stringify(data),
                dataType: 'json',

                success: result => {
                    if (result.success) {
                        callback()
                    } else {
                        alert(result.message)
                    }
                }

            })
        },
    },

    groups: {

        get(callback) {
            $.ajax({
                url: URL.groups.get,
                headers: headers(),
                method: "GET",
                dataType: 'json',

                success: result => {
                    callback(result)
                }
            })
        },

        add(groupName, callback) {
            $.ajax({
                url: URL.groups.add(groupName),
                headers: headers(),
                method: "PUT",

                statusCode: {
                    200: _ => {
                        callback()
                    }
                }
            })
        },

        delete(groupName, callback) {
            $.ajax({
                url: URL.groups.delete(groupName),
                headers: headers(),
                method: "DELETE",

                statusCode: {
                    200: _ => {
                        callback()
                    }
                }
            })
        },

        deleteAll(callback) {
            $.ajax({
                url: URL.groups.delete(),
                headers: headers(),
                method: "DELETE",

                statusCode: {
                    200: _ => {
                        callback()
                    }
                }
            })
        }
    },

    feeds: {

        add(url, groupName, callback) {
            const data = {
                "feedName": url,
                "groupName": groupName
            }

            $.ajax({
                url: URL.feeds.add,
                headers: headers(),
                method: 'PUT',
                data: JSON.stringify(data),
                dataType: 'json',

                statusCode: {
                    200: _ => {
                        callback()
                    }
                }

            })
        },

        messages(url, callback) {
            const data = {
                "url": url
            }

            $.ajax({
                url: URL.feeds.messages,
                headers: headers(),
                method: 'POST',
                data: JSON.stringify(data),
                dataType: 'json',

                success: result => {
                    callback(result)
                },
            })
        }
    }
}

